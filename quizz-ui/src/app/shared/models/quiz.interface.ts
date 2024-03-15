import {Question} from "./question.interface";
import {Settings} from "./settings.interface";
import {Theme} from "./theme.interface";

export interface Quiz {
  id: number;
  uuid: string;
  title: string;
  responses: number;
  status: ProjectStatus;
  questions: Question[];
  settings: Settings;
  theme: Theme;
  logo: string;
}

export enum ProjectStatus {
  IN_DESIGN = "IN_DESIGN",
  OPEN = "OPEN",
  CLOSED = "CLOSED"
}
