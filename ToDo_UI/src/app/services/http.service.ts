import { Injectable } from '@angular/core';
import {Todo} from "../models/todo";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  todoApi = "/api/tasks";

  constructor(private httpClient: HttpClient) {}

  createTask(task : Todo) {
    return this.httpClient.post<Todo>(this.todoApi, task);
  }

  getTask()  {
    return this.httpClient.get<Todo[]>(this.todoApi).pipe(
      map((data) => {
        return data;
      })
    );
  }
  deleteTask(id: number) {
    const deleteUrl = `${this.todoApi}/${id}`;
    return this.httpClient.delete<Todo>(deleteUrl);
  }

  updateTask(task: Todo) {
    const updateUrl = `${this.todoApi}/${task.id}`;
    return this.httpClient.put<Todo>(updateUrl, task);
  }

  updateToDoStatus(id: number, completedStatus: boolean) {
    const patchUrl = `${this.todoApi}/${id}`;
    return this.httpClient.patch<Todo>(patchUrl, {
      completed: completedStatus
    });
  }

}

