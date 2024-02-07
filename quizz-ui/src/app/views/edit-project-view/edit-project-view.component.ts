import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {QuestionService} from "../../shared/services/question/question.service";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {Question} from "../../shared/models/question.interface";
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";
import {QuestionType} from "./components/question-dialog/question-dialog.component";
import {Quiz} from "../../shared/models/quiz.interface";
import {getFromLocalStorage} from "../../shared/app-util";
import {ConfirmationService, MessageService} from "primeng/api";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {Survey} from "../../shared/models/survey.interface";

@Component({
  selector: "app-edit-project-view",
  templateUrl: "./edit-project-view.component.html",
  styleUrl: "./edit-project-view.component.scss",
  providers: [ConfirmationService, MessageService]
})
export class EditProjectViewComponent implements OnInit {
  stateOptions: any[] = [
    {label: "List", value: "list"},
    {label: "Graph", value: "graph"}
  ];

  value: string = "list";

  showQuestionDialog: boolean = false;
  project!: Quiz | Survey;
  editableQuestion!: Question | null;
  readonly QUESTION_TYPE = QuestionType;

  get projectTitle() {
    return this.route.snapshot.params["type"] === "quiz" ? `Quiz: ${this.project.title}` : `Survey: ${this.project.title}`;
  }

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private questionService: QuestionService,
    private notificationService: ToastrNotificationService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.fetchQuestions();
  }

  fetchQuestions(): void {
    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.getQuizzQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        this.project = res[0];
      });
    } else {
      this.surveyService.getSurveyQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        this.project = res[0];
      });
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.project.questions, event.previousIndex, event.currentIndex);

    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
    } else {
      this.surveyService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
    }
  }

  onAddQuestionClick(): void {
    this.editableQuestion = null;
    this.showQuestionDialog = true;
  }

  onShowEditDialog(question: Question): void {
    this.editableQuestion = question;
    this.showQuestionDialog = true;
  }

  onCloseQuestionClick(): void {
    this.fetchQuestions();
    this.showQuestionDialog = false;
  }

  onDeleteConfirmation(event: Event, question: Question) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: "Do you want to delete this Question?",
      header: "Delete Confirmation",
      icon: "pi pi-info-circle",
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.onQuestionDelete(question);
      },
      reject: () => {
        this.confirmationService.close();
      }
    });
  }

  onQuestionDelete(question: Question) {
    if (question.id) {
      this.questionService.delete(question.id, getFromLocalStorage("token")).subscribe(_res => {
        this.notificationService.success("Question Deleted!");
        this.fetchQuestions();
      });
    }
  }
}
