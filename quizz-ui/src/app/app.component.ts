import { Component } from "@angular/core";
import { HealthService } from "./shared/services/health.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent {
  title = "quizz-app";

  constructor(private service: HealthService) {}

  onClick() {
    this.service.health().subscribe((message) => console.log(message));
  }
}
