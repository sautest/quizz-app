import {Component, Input} from "@angular/core";
import {Quiz} from "../../../../shared/models/quiz.interface";
import {Survey} from "../../../../shared/models/survey.interface";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../../../shared/services/quizz/quiz.service";
import {SurveyService} from "../../../../shared/services/survey/survey.service";
import {ToastrNotificationService} from "../../../../shared/services/toastr/toastr-notification.service";
import {getFromLocalStorage} from "../../../../shared/app-util";

@Component({
  selector: "app-theme-editor",
  templateUrl: "./theme-editor.component.html",
  styleUrl: "./theme-editor.component.scss"
})
export class ThemeEditorComponent {
  @Input() project!: Quiz | Survey;

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private notificationService: ToastrNotificationService
  ) {}

  uploadedFiles: any[] = [];

  onUpload(event: any) {
    const file = event.files[0];

    const fileReader = new FileReader();
    fileReader.onload = () => {
      const base64String = fileReader.result as string;
      this.project.theme.bgImage = base64String;
    };
    fileReader.readAsDataURL(file);
  }

  removeBgImage() {
    this.project.theme.bgImage = null;
  }

  onSaveTheme(): void {
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
