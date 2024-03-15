import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {MenuItem} from "primeng/api";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {getFromLocalStorage} from "../../shared/app-util";
import {jwtDecode} from "jwt-decode";

@Component({
  selector: "app-user-view",
  templateUrl: "./user-view.component.html",
  styleUrl: "./user-view.component.scss"
})
export class UserViewComponent implements OnInit {
  items: MenuItem[] = [];
  showUsersList: boolean = false;
  showDashboard: boolean = false;
  showNewProjectDialog: boolean = false;
  showEditProjectView: boolean = false;
  showUserProfile: boolean = false;
  showStatisticsView: boolean = false;

  showAdminBtns: boolean = false;
  constructor(private router: Router, private notificationService: ToastrNotificationService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const decodedToken: any = jwtDecode(getFromLocalStorage("token"));

    if (decodedToken.roles.includes("ADMIN_ROLES")) {
      this.showAdminBtns = true;
      this.items.push({label: "Users", icon: "pi pi-users", command: _event => this.onUsersBtnClick()});
    } else {
      this.showAdminBtns = false;
    }

    this.items.push(
      {label: "Dashboard", icon: "pi pi-list", command: _event => this.onDashboardBtnClick()},
      {label: "New project", icon: "pi pi-plus", command: _event => this.onNewProjectBtnClick()},
      {label: "Profile", icon: "pi pi-user", command: _event => this.onUserProfileBtnClick()},
      {label: "Log out", icon: "pi pi-sign-out", command: _event => this.onLogOutBtnClick()}
    );

    this.route.data.subscribe(data => {
      this.showUsersList = data["showUsersList"];
      this.showDashboard = data["showDashboard"];
      this.showNewProjectDialog = data["showNewProjectDialog"];
      this.showEditProjectView = data["showEditProjectView"];
      this.showUserProfile = data["showUserProfile"];
      this.showStatisticsView = data["showStatisticsView"];
    });
  }

  onUsersBtnClick() {
    this.router.navigate([`/users`]);
  }

  onDashboardBtnClick() {
    this.router.navigate([`/dashboard/${getFromLocalStorage("id")}`]);
  }

  onNewProjectBtnClick() {
    this.router.navigate(["/create"]);
  }

  onUserProfileBtnClick() {
    this.router.navigate([`/profile/${getFromLocalStorage("id")}`]);
  }

  onLogOutBtnClick() {
    this.router.navigate(["/"]);
    this.notificationService.success("Logged Out!");
    localStorage.setItem("token", "");
    localStorage.setItem("id", "");
  }
}
