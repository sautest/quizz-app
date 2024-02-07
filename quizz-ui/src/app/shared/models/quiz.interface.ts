import {Question} from "./question.interface";
import {Settings} from "./settings.interface";

export interface Quiz {
  id: number;
  title: string;
  questions: Question[];
  settings: Settings;
}
