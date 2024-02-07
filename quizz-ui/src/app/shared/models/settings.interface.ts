export interface Settings {
  id?: number;
  enableTimeLimit: boolean;
  min?: number;
  hours?: number;
  questionsPerPage: number;
  enableAnswerNotInOrder: boolean;
  enableAskForBasicUserInfo: boolean;
  enableProgressBar: boolean;
  enableRandomizeQuestions: boolean;
  enablePublic: boolean;
  enableShowAnswersAtTheEnd: boolean;
}
