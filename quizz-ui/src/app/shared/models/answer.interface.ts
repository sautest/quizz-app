export interface Answer {
  id?: number;
  questionId: number | undefined;
  selectedOptionIds: (number | undefined)[];
  participantName?: string | undefined;
  participantAge?: number | undefined;
}
