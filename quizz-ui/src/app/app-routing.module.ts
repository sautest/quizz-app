import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {GuestViewComponent} from "./views/guest-view/guest-view.component";
import {UserViewComponent} from "./views/user-view/user-view.component";
import {ProjectActionViewComponent} from "./views/project-action-view/project-action-view.component";

const routes: Routes = [
  {path: "", redirectTo: "/home", pathMatch: "full"},
  {path: "home", component: GuestViewComponent, data: {showPublicProjects: true, showSignUpDialog: false, showSignInDialog: false}},
  {path: "sign-in", component: GuestViewComponent, data: {showSignUpDialog: false, showSignInDialog: true}},
  {path: "sign-up", component: GuestViewComponent, data: {showSignUpDialog: true, showSignInDialog: false}},
  {path: "users", component: UserViewComponent, data: {showUsersList: true}},
  {path: "dashboard/:id", component: UserViewComponent, data: {showDashboard: true}},
  {path: "create", component: UserViewComponent, data: {showNewProjectDialog: true}},
  {path: "create/:type/:id", component: UserViewComponent, data: {showEditProjectView: true}},
  {path: "statistics/:type/:id", component: UserViewComponent, data: {showStatisticsView: true}},
  {path: "profile/:id", component: UserViewComponent, data: {showUserProfile: true}},
  {path: "preview/:type/:id", component: ProjectActionViewComponent},
  {path: ":type/:id", component: ProjectActionViewComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
