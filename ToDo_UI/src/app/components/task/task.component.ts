import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Todo} from "../../models/todo";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-form',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.scss']
})
export class TaskComponent implements OnInit{
  todoForm!: FormGroup;
  tasks: Todo[] = []
  paginatedTasks: Todo[] = [];
  currentPage: number = 1;
  pageSize: number = 9;
  isEditMode: boolean = false;

  constructor(private formBuiilder: FormBuilder,
              private httpService: HttpService
  ){}

  ngOnInit() {
    this.todoForm = this.formBuiilder.group({
      id: [''],
      title: ['', Validators.required],
      description: ['', Validators.required],
      completed: [''],
      dueDate: ['']
    })
    this.getToDo();
  }

  getToDo() {
    this.httpService.getTask().subscribe(data => {
      this.tasks = data;
      this.updatePaginatedTasks();
    })
  }

  deleteTask(id: number){
    if (confirm("Are you sure you want to delete this Item?")){
      this.httpService.deleteTask(id).subscribe(data => {
        this.getToDo();
      });
    }
  }

  updateTask(task: Todo) {
    this.httpService.updateTask(task).subscribe(data => {
      this.getToDo();
      this.todoForm.reset();
    });
  }

  handleEdit(task: Todo) {
    this.isEditMode = !this.isEditMode;
    this.todoForm.setValue(task);
  }

  updateToDoStatus(id: number, completedStatus: boolean) {
    this.httpService.updateToDoStatus(id, completedStatus).subscribe((data) => {
      this.getToDo();
    });
  }

  onsubmit() {
    if(this.todoForm.invalid){
      return;
    }
    const formValue: Todo = this.todoForm.value;
    if(this.isEditMode) {
      this.updateTask(formValue);
      this.isEditMode = !this.isEditMode;
    } else {
      const todoRequest: Todo = {
        title: formValue.title,
        description: formValue.description,
        completed: false,
        dueDate: formValue.dueDate
      };
      this.httpService.createTask(todoRequest).subscribe((data) => {
        this.getToDo();
      });
    }
  }

  updatePaginatedTasks() {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = Math.min(startIndex + this.pageSize, this.tasks.length);
    this.paginatedTasks = this.tasks.slice(startIndex, endIndex);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePaginatedTasks();
  }

  getTotalPages(): number {
    return Math.ceil(this.tasks.length / this.pageSize);
  }
}
