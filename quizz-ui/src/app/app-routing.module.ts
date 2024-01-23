import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {GuestViewComponent} from "./views/guest-view/guest-view.component";

const routes: Routes = [
  {path: "", redirectTo: "/home", pathMatch: "full"},
  {path: "home", component: GuestViewComponent},
  {path: "sign-in", component: GuestViewComponent},
  {path: "sign-up", component: GuestViewComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
