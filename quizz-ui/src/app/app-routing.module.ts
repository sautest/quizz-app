import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {GuestViewComponent} from "./views/guest-view/guest-view.component";
import {UserViewComponent} from "./views/user-view/user-view.component";

const routes: Routes = [
  {path: "", redirectTo: "/home", pathMatch: "full"},
  {path: "home", component: GuestViewComponent, data: {showSignUpDialog: false, showSignInDialog: false}},
  {path: "sign-in", component: GuestViewComponent, data: {showSignUpDialog: false, showSignInDialog: true}},
  {path: "sign-up", component: GuestViewComponent, data: {showSignUpDialog: true, showSignInDialog: false}},
  {path: "dashboard/:id", component: UserViewComponent, data: {showDashboard: true}},
  {path: "create", component: UserViewComponent, data: {showNewProjectDialog: true}},
  {path: "create/:type/:id", component: UserViewComponent, data: {showEditProjectView: true}},
  {path: "profile", component: UserViewComponent, data: {showUserProfile: true}}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
