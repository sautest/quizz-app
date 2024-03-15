import {Component, OnInit} from "@angular/core";
import {UserService} from "../../shared/services/user/user.service";
import {User} from "../../shared/models/user.interface";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";

@Component({
  selector: "app-users-list-view",
  templateUrl: "./users-list-view.component.html",
  styleUrl: "./users-list-view.component.scss"
})
export class UsersListViewComponent implements OnInit {
  users: User[] = [];
  constructor(private userService: UserService, private notificationService: ToastrNotificationService) {}

  ngOnInit(): void {
    this.userService.getUsers().subscribe(res => {
      console.log(res);
      this.users = res.filter(user => user.roles === "USER_ROLES");
    });
  }

  onToggleBlock(id: number): void {
    const user: User | undefined = this.users.find(user => user.id === id);
    if (user) {
      user.blocked = !user.blocked;
      this.userService.edit(`${user.id}`, user.email, user.username, user.blocked).subscribe(res => {
        if (user.blocked) {
          this.notificationService.success("User Blocked!");
        } else {
          this.notificationService.success("User Unblocked!");
        }
      });
    }
  }
}
