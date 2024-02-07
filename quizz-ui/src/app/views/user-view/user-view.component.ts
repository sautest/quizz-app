import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {MenuItem} from "primeng/api";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {getFromLocalStorage} from "../../shared/app-util";

@Component({
  selector: "app-user-view",
  templateUrl: "./user-view.component.html",
  styleUrl: "./user-view.component.scss"
})
export class UserViewComponent implements OnInit {
  items: MenuItem[] | undefined;
  home: MenuItem | undefined;
  showDashboard: boolean = false;
  showNewProjectDialog: boolean = false;
  showEditProjectView: boolean = false;
  showUserProfile: boolean = false;
  constructor(private router: Router, private notificationService: ToastrNotificationService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.items = [{label: "Computer"}, {label: "Notebook"}];
    this.home = {icon: "pi pi-home", routerLink: "/"};

    this.route.data.subscribe(data => {
      this.showDashboard = data["showDashboard"];
      this.showNewProjectDialog = data["showNewProjectDialog"];
      this.showEditProjectView = data["showEditProjectView"];
      this.showUserProfile = data["showUserProfile"];
    });
  }

  onDashboardBtnClick() {
    this.router.navigate([`/dashboard/${getFromLocalStorage("id")}`]);
  }

  onNewProjectBtnClick() {
    this.router.navigate(["/create"]);
  }

  onUserProfileBtnClick() {
    this.router.navigate(["/profile"]);
  }

  onLogOutBtnClick() {
    this.router.navigate(["/"]);
    this.notificationService.success("Logged Out!");
    localStorage.setItem("token", "");
    localStorage.setItem("id", "");
  }
}
