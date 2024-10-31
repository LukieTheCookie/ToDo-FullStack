import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {TaskComponent} from './components/task/task.component';
import {AnaylticsComponent} from './components/anayltics/anayltics.component';

const routes: Routes = [
  { path: '', component: TaskComponent },
  { path: 'analytics', component: AnaylticsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
