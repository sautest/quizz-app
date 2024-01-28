import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {MenuItem} from "primeng/api";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {setUserContext} from "../../shared/store/user/user.actions";
import {Store} from "@ngrx/store";

@Component({
  selector: "app-user-view",
  templateUrl: "./user-view.component.html",
  styleUrl: "./user-view.component.scss"
})
export class UserViewComponent {
  constructor(private store: Store, private router: Router, private notificationService: ToastrNotificationService) {}
  items: MenuItem[] | undefined;

  home: MenuItem | undefined;

  ngOnInit() {
    this.items = [{label: "Computer"}, {label: "Notebook"}, {label: "Accessories"}, {label: "Backpacks"}, {label: "Item"}];

    this.home = {icon: "pi pi-home", routerLink: "/"};
  }

  onLogOutBtnClick() {
    this.router.navigate(["/"]);
    this.notificationService.success("Logged Out!");
    this.store.dispatch(setUserContext({userContext: {id: null, token: null}}));
  }
}
