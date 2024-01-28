import {Component, Input, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../../../../shared/services/user/user.service";
import {ToastrNotificationService} from "../../../../shared/services/toastr/toastr-notification.service";
import {Store} from "@ngrx/store";
import {setUserContext} from "../../../../shared/store/user/user.actions";

@Component({
  selector: "app-auth",
  templateUrl: "./auth.component.html",
  styleUrl: "./auth.component.scss"
})
export class AuthComponent {
  constructor(
    private store: Store,
    private router: Router,
    private userService: UserService,
    private notificationService: ToastrNotificationService
  ) {}

  @Input() showSignUpDialog: boolean = false;
  @Input() showSignInDialog: boolean = false;

  email: string = "";
  username: string = "";
  password: string = "";

  get showEmailInput(): boolean {
    return this.showSignUpDialog;
  }
  get showUsernameInput(): boolean {
    return this.showSignUpDialog || this.showSignInDialog;
  }
  get showPasswordInput(): boolean {
    return this.showSignUpDialog || this.showSignInDialog;
  }

  get isFormFilled(): boolean {
    return (
      (this.showSignUpDialog && !!this.email.trim() && !!this.username.trim() && !!this.password.trim()) ||
      (this.showSignInDialog && !!this.username.trim() && !!this.password.trim())
    );
  }

  onToggleDialog(): void {
    if (this.showSignUpDialog) {
      this.router.navigate(["/sign-in"]);
    } else {
      this.router.navigate(["/sign-up"]);
    }
  }

  onSignUpBtnClick(): void {
    this.userService.create(this.email, this.username, this.password).subscribe(res => {
      if (res) {
        this.notificationService.success("Account Created!");
        this.router.navigate(["/sign-in"]);
      }
    });
  }

  onSignInBtnClick(): void {
    this.userService.login(this.username, this.password).subscribe(res => {
      if (res?.response.severity === "SUCCESS") {
        this.notificationService.success("Logged In!");
        this.router.navigate(["/dashboard"]);
        this.store.dispatch(setUserContext({userContext: {id: res.id, token: res.token}}));
      }
    });
  }
}
