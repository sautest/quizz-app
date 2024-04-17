import {Time} from "@angular/common";
import {Component, Input, OnInit} from "@angular/core";
import {Survey} from "../../../../shared/models/survey.interface";
import {Quiz} from "../../../../shared/models/quiz.interface";
import {Settings} from "../../../../shared/models/settings.interface";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../../../shared/services/quizz/quiz.service";
import {SurveyService} from "../../../../shared/services/survey/survey.service";
import {getFromLocalStorage} from "../../../../shared/app-util";
import {ToastrNotificationService} from "../../../../shared/services/toastr/toastr-notification.service";

@Component({
  selector: "app-settings",
  templateUrl: "./settings.component.html",
  styleUrl: "./settings.component.scss"
})
export class SettingsComponent {
  @Input() project!: Quiz | Survey;

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private notificationService: ToastrNotificationService
  ) {}

  get isQuiz(): boolean {
    return this.route.snapshot.params["type"] === "quiz";
  }

  onUpload(event: any) {
    const file = event.files[0];

    const fileReader = new FileReader();
    fileReader.onload = () => {
      const base64String = fileReader.result as string;
      this.project.settings.logo = base64String;
    };
    fileReader.readAsDataURL(file);
  }

  removeBgImage() {
    this.project.settings.logo = "";
  }

  onSaveSettings(): void {
    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {
        this.notificationService.success("Settings Updated!");
      });
    } else {
      this.surveyService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {
        this.notificationService.success("Settings Updated!");
      });
    }
  }
}
