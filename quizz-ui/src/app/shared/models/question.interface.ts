import {QuestionType} from "../../views/edit-project-view/components/question-dialog/question-dialog.component";
import {Quiz} from "./quiz.interface";

export interface Question {
  id?: number;
  type: QuestionType;
  text: string;
  options: QuestionOption[];
  quizzes: Quiz[];
}

export interface QuestionOption {
  text: string;
  correct: boolean;
}
