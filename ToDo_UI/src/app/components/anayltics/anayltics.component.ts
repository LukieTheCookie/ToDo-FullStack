import { Component } from '@angular/core';
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-anayltics',
  templateUrl: './anayltics.component.html',
  styleUrls: ['./anayltics.component.scss']
})
export class AnaylticsComponent {
  totalTasks: number = 0;
  completedTasks: number = 0;
  pendingTasks: number = 0;
  tasksDueToday: number = 0;
  overdueTasks: number = 0;

  constructor(private httpService: HttpService) { }

  ngOnInit(): void {
    this.fetchTasks();
  }

  fetchTasks(): void {
    this.httpService.getTask().subscribe(tasks => {
      this.totalTasks = tasks.length;
      this.completedTasks = tasks.filter(task => task.completed).length;
      this.pendingTasks = tasks.filter(task => !task.completed).length;
      this.tasksDueToday = tasks.filter(task => this.isDueToday(task.dueDate)).length;
      this.overdueTasks = tasks.filter(task => this.isOverdue(task.dueDate)).length;
    });
  }
  isDueToday(dueDate?: string): boolean {
    if (!dueDate) return false;
    const today = new Date();
    const due = new Date(dueDate);
    return (
      today.getFullYear() === due.getFullYear() &&
      today.getMonth() === due.getMonth() &&
      today.getDate() === due.getDate()
    );
  }

  isOverdue(dueDate?: string): boolean {
    if (!dueDate) return false;
    const today = new Date();
    const due = new Date(dueDate);
    return due < today;
  }
}
