import {Component, OnInit} from "@angular/core";
import {UserService} from "../../shared/services/user/user.service";
import {User} from "../../shared/models/user.interface";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {getFromLocalStorage} from "../../shared/app-util";

@Component({
  selector: "app-profile-view",
  templateUrl: "./profile-view.component.html",
  styleUrl: "./profile-view.component.scss"
})
export class ProfileViewComponent implements OnInit {
  username: string = "";
  email: string = "";
  dateJoined: string = "";
  blocked: boolean = false;

  showForm: boolean = false;

  constructor(private userService: UserService, private notificationService: ToastrNotificationService) {}

  ngOnInit(): void {
    this.userService.getUser().subscribe((res: User) => {
      this.username = res.username;
      this.email = res.email;
      this.dateJoined = res.dateJoined;
      this.blocked = res.blocked;

      if (res?.id) {
        this.showForm = true;
      }
    });
  }

  onSave(): void {
    this.userService.edit(getFromLocalStorage("id"), this.email, this.username, this.blocked).subscribe(res => {
      this.notificationService.success("Profile updated!");
    });
  }
}
