import {Component, OnInit} from "@angular/core";
import {ProjectStatus, Quiz} from "../../shared/models/quiz.interface";
import {Survey} from "../../shared/models/survey.interface";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {Subscription, interval} from "rxjs";
import {PaginatorState} from "primeng/paginator";
import {QuestionType} from "../edit-project-view/components/question-dialog/question-dialog.component";
import {getFromLocalStorage, shuffleArray} from "../../shared/app-util";
import {LogicActionType, LogicConditionType, Question} from "../../shared/models/question.interface";
import {Answer} from "../../shared/models/answer.interface";
import {AnswerService} from "../../shared/services/answer/answer.service";

@Component({
  selector: "app-project-action-view",
  templateUrl: "./project-action-view.component.html",
  styleUrl: "./project-action-view.component.scss"
})
export class ProjectActionViewComponent implements OnInit {
  project!: Quiz | Survey;

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private answerService: AnswerService
  ) {}

  readonly QUESTION_TYPE = QuestionType;

  name: string = "";
  age!: number;

  answers: Answer[] = [];

  showUserInfoForm: boolean = true;
  showProjectQuestions: boolean = false;
  showEndResults: boolean = false;
  showDetailedAnswers: boolean = false;

  currentPage: number = 0;
  counter = 60;
  elapsedTime = 0;
  displayDuration: String = "";

  timerSubscription!: Subscription;

  get progressBar(): number {
    return Math.round((this.currentPage / this.project.questions.length) * 100);
  }

  get questionsAmount(): number {
    return this.project.questions.length;
  }
  get questionsPerPage(): number {
    return this.project.settings.questionsPerPage;
  }

  get showProgressBar(): boolean {
    return this.project.settings.enableProgressBar;
  }

  get showTimer(): boolean {
    return this.project.settings.enableTimeLimit;
  }

  get show(): boolean {
    return this.project.settings.enableShowAnswersAtTheEnd;
  }

  get disableBackBtn(): boolean {
    return !this.project.settings.enableRandomizeQuestions;
  }

  get showParticipantForm(): boolean {
    return this.project.settings.enableAskForBasicUserInfo && this.showUserInfoForm;
  }

  get showQuestions(): boolean {
    return this.showProjectQuestions;
  }

  get showAnswersInEnding(): boolean {
    return this.project.settings.enableShowAnswersAtTheEnd && this.showEndResults;
  }

  get showMessageInEnding(): boolean {
    return !this.project.settings.enableShowAnswersAtTheEnd && this.showEndResults;
  }

  get endingGreeting(): string {
    return this.route.snapshot.params["type"] === "quiz" ? "Thanks for taking the quiz!" : "Thanks for taking the survey!";
  }

  get getFormattedTime(): string {
    const hours = Math.floor(this.counter / 3600);
    const minutes = Math.floor((this.counter % 3600) / 60);
    const seconds = Math.floor(this.counter % 60);

    return `${hours.toString().padStart(2, "0")}:${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
  }

  ngOnInit(): void {
    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.getQuizzQuestions(this.route.snapshot.params["id"], !window.location.href.includes("preview")).subscribe(res => {
        if (
          (res[0].status === ProjectStatus.CLOSED || res[0].status === ProjectStatus.IN_DESIGN) &&
          !window.location.href.includes("preview")
        ) {
          window.location.href = "http://localhost:4200/home";
        }
        this.project = res[0];

        this.project.questions.forEach(q => q.options.forEach(opt => (opt.checked = false)));

        if (this.project.settings.enableRandomizeQuestions) {
          this.project.questions = shuffleArray(this.project.questions);
        }

        if (!this.project.settings.enableAskForBasicUserInfo) {
          this.showUserInfoForm = false;
          this.showProjectQuestions = true;
        }

        this.counter = (this.project.settings.hours ?? 0) * 3600 + (this.project.settings.min ?? 0) * 60;

        this.timerSubscription = interval(1000).subscribe(() => {
          this.counter--;
          this.elapsedTime++;
          if (this.counter === 0) {
            this.showResults();
          }
        });

        this.answerService.get(this.project.questions[0].id ?? 0).subscribe(res => {});
      });
    } else {
      this.surveyService.getSurveyQuestions(this.route.snapshot.params["id"], !window.location.href.includes("preview")).subscribe(res => {
        if (
          (res[0].status === ProjectStatus.CLOSED || res[0].status === ProjectStatus.IN_DESIGN) &&
          !window.location.href.includes("preview")
        ) {
          window.location.href = "http://localhost:4200/home";
        }

        this.project = res[0];
        this.project.questions.forEach(q => q.options.forEach(opt => (opt.checked = false)));

        if (this.project.settings.enableRandomizeQuestions) {
          this.project.questions = shuffleArray(this.project.questions);
        }

        if (!this.project.settings.enableAskForBasicUserInfo) {
          this.showUserInfoForm = false;
          this.showProjectQuestions = true;
        }

        this.counter = (this.project.settings.hours ?? 0) * 3600 + (this.project.settings.min ?? 0) * 60;

        this.timerSubscription = interval(1000).subscribe(() => {
          this.counter--;
          this.elapsedTime++;

          if (this.counter === 0) {
            this.showResults();
          }
        });

        this.answerService.get(this.project.questions[0].id ?? 0).subscribe(res => {});
      });
    }
  }

  onSaveUserInfo() {
    this.showUserInfoForm = false;
    this.showProjectQuestions = true;
  }

  onNextQuestion() {
    let breakCycle = false;
    let defaultIncrement = true;

    if (this.project.questions[this.currentPage].logic && !this.project.settings.enableRandomizeQuestions) {
      this.project.questions[this.currentPage].logic?.forEach(logic => {
        if (!breakCycle) {
          if (logic.conditionType === LogicConditionType.ALWAYS) {
            defaultIncrement = false;

            if (logic.actionType === LogicActionType.END_QUESTIONING) {
              this.currentPage = this.questionsAmount;
              breakCycle = true;
            } else {
              this.currentPage = this.project.questions.findIndex(q => q.id == logic.actionOptionId);
              breakCycle = true;
            }
          } else {
            let option = this.project.questions[this.currentPage].options.find(opt => opt.id == logic.conditionOptionId);
            console.log(this.project.questions[this.currentPage].options);

            if (
              (this.project.questions[this.currentPage].type === QuestionType.MULTI_CHOICE &&
                option?.checked &&
                this.project.questions[this.currentPage].options.filter(opt => opt.checked).length === 1) ||
              (this.project.questions[this.currentPage].type === QuestionType.SINGLE_CHOICE &&
                option?.text === this.project.questions[this.currentPage].selected)
            ) {
              defaultIncrement = false;
              if (logic.actionType === LogicActionType.END_QUESTIONING) {
                this.currentPage = this.questionsAmount;
                breakCycle = true;
              } else {
                this.currentPage = this.project.questions.findIndex(q => q.id == logic.actionOptionId);
                breakCycle = true;
                console.log(this.currentPage);
              }
            }
          }
        }
      });
    }

    if (defaultIncrement) this.currentPage++;

    if (this.currentPage >= this.questionsAmount) {
      this.showResults();
    }
  }

  showResults(): void {
    this.counter = this.elapsedTime;
    this.displayDuration = this.getFormattedTime;
    this.counter = 9999;
    this.showProjectQuestions = false;
    if (!window.location.href.includes("preview")) {
      this.saveAnswers();
    }

    this.showEndResults = true;
  }

  isNextQuestionDisabled(): boolean {
    return (
      this.project.questions[this.currentPage].options.every(opt => !opt.checked) && !this.project.questions[this.currentPage].selected
    );
  }

  onBackToPreviousQuestion() {
    this.currentPage--;
  }

  onPageChange(value: PaginatorState) {
    this.currentPage = value.page ?? 0;
  }

  calculateScore(): string {
    let score: number = 0;

    this.project.questions.forEach(q => {
      if (q.type === this.QUESTION_TYPE.MULTI_CHOICE) {
        if (q.options.every(opt => opt.checked === opt.correct)) {
          score = <number>q.score + score;
        }
      } else {
        if (q.options.find(opt => opt.text === q.selected)?.correct) {
          score = <number>q.score + score;
        }
      }
    });

    return `${score}/${this.calculateMaxScore()}(${Math.floor((score / this.calculateMaxScore()) * 100)}%)`;
  }

  calculateMaxScore(): number {
    let score: number = 0;

    this.project.questions.forEach(q => {
      score = <number>q.score + score;
    });

    return score;
  }

  onReviewAnswers(): void {
    this.showEndResults = false;
    this.showDetailedAnswers = true;

    this.answerService.get(this.project.questions[0].id ?? 0).subscribe(res => {});
  }

  isQuestionCorrectlyAnswered(question: Question): boolean {
    if (question.type === QuestionType.MULTI_CHOICE) {
      return question.options.every(opt => opt.correct === opt.checked);
    } else {
      return !!question.options.find(q => q.text === question.selected)?.correct;
    }
  }

  getQuestionScore(question: Question): string {
    return this.isQuestionCorrectlyAnswered(question) ? `${question.score}/${question.score} ` : `0/${question.score}`;
  }

  saveAnswers() {
    let answers: Answer[] = [];

    if (this.name === "") {
      this.name = `anon${Math.floor(Math.random() * 10000) + 1}`;
    }

    answers = this.project.questions.map(q => ({
      questionId: q.id,
      selectedOptionIds:
        q.type === QuestionType.MULTI_CHOICE
          ? q.options.filter(opt => opt.checked).map(opt => opt.id)
          : [q.options.find(opt => opt.text === q.selected)?.id],
      participantName: this.name,
      participantAge: this.age ?? 0
    }));

    this.answerService.create(answers).subscribe(res => {});

    this.project.responses = this.project.responses + 1;

    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
    } else {
      this.surveyService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
    }
  }
}
