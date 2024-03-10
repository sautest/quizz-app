import {Component, OnInit} from "@angular/core";
import {UserService} from "../../shared/services/user/user.service";
import {User} from "../../shared/models/user.interface";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";

@Component({
  selector: "app-profile-view",
  templateUrl: "./profile-view.component.html",
  styleUrl: "./profile-view.component.scss"
})
export class ProfileViewComponent implements OnInit {
  username: string = "";
  email: string = "";
  dateJoined: string = "";

  constructor(private userService: UserService, private notificationService: ToastrNotificationService) {}

  ngOnInit(): void {
    this.userService.getUser().subscribe((res: User) => {
      console.log(res);
      this.username = res.username;
      this.email = res.email;
      this.dateJoined = res.dateJoined;
    });
  }

  onSave(): void {
    this.userService.edit(this.email, this.username).subscribe(res => {
      this.notificationService.success("Profile updated!");
    });
  }
}
