import {Component, Input} from "@angular/core";
import {Router} from "@angular/router";

@Component({
  selector: "app-auth",
  templateUrl: "./auth.component.html",
  styleUrl: "./auth.component.scss"
})
export class AuthComponent {
  constructor(private router: Router) {}

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
      this.showSignUpDialog = false;
      this.showSignInDialog = true;
    } else {
      this.router.navigate(["/sign-up"]);
      this.showSignUpDialog = true;
      this.showSignInDialog = false;
    }
  }

  onSubmitBtnClick(): void {}
}
