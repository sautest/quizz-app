import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {QuestionService} from "../../../../shared/services/question/question.service";
import {getFromLocalStorage} from "../../../../shared/app-util";
import {Quiz} from "../../../../shared/models/quiz.interface";
import {Survey} from "../../../../shared/models/survey.interface";

@Component({
  selector: "app-question-bank-dialog",
  templateUrl: "./question-bank-dialog.component.html",
  styleUrl: "./question-bank-dialog.component.scss"
})
export class QuestionBankDialogComponent implements OnInit {
  sourceProducts: any[] = [];
  targetProducts: any[] = [];

  @Output() onClose: EventEmitter<void> = new EventEmitter();
  @Input() project!: Quiz | Survey;

  constructor(private questionService: QuestionService) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.questionService.get(+getFromLocalStorage("id")).subscribe(res => {
        console.log(res);

        for (let q of res) {
          if (q.quizzes.find((quiz: Quiz) => quiz.id === this.project.id)) {
            this.targetProducts.push(q);
          } else {
            this.sourceProducts.push(q);
          }
        }

        console.log(this.targetProducts, this.sourceProducts);
      });
    }, 200);

    console.log("herro");
  }

  onCloseBtnClick() {
    this.onClose.emit();
  }

  onDelete() {
    console.log(this.targetProducts, this.sourceProducts);
  }

  onSaveBtnClick() {}
}
