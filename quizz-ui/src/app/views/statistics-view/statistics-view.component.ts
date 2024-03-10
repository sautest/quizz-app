import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {AnswerService} from "../../shared/services/answer/answer.service";
import {Survey} from "../../shared/models/survey.interface";
import {Quiz} from "../../shared/models/quiz.interface";
import {LegendPosition, ScaleType} from "@swimlane/ngx-charts";
import {getDate} from "../../shared/app-util";

export enum ChartType {
  PIE_CHART,
  PIE_GRID_CHART,
  VERTICAL_BAR_CHART,
  HORIZONTAL_BAR_CHART
}

@Component({
  selector: "app-statistics-view",
  templateUrl: "./statistics-view.component.html",
  styleUrl: "./statistics-view.component.scss"
})
export class StatisticsViewComponent implements OnInit {
  project!: Quiz | Survey;
  data: any[][] = [];
  individualData: Map<string, any[]> = new Map();
  view: [number, number] = [600, 400];
  loadingFinished: boolean = false;

  // options
  showXAxis: boolean = true;
  showYAxis: boolean = true;
  gradient: boolean = true;
  showLegend: boolean = false;
  legendPosition: LegendPosition = LegendPosition.Below;
  showXAxisLabel: boolean = true;
  yAxisLabel: string = "Option";
  showYAxisLabel: boolean = true;
  xAxisLabel = "response";
  showLabels: boolean = true;
  isDoughnut: boolean = false;

  colorSchemeH = "ocean";
  colorSchemeV = "ocean";
  schemeType: ScaleType = ScaleType.Ordinal;

  chartType = ChartType;
  value: ChartType = ChartType.PIE_CHART;
  chartOptions: any[] = [
    {icon: "fa-solid fa-chart-pie", value: ChartType.PIE_CHART},
    {icon: "fa-solid fa-circle-half-stroke", value: ChartType.PIE_GRID_CHART},
    {icon: "fa-solid fa-chart-bar", value: ChartType.HORIZONTAL_BAR_CHART},
    {icon: "fa-solid fa-chart-column", value: ChartType.VERTICAL_BAR_CHART}
  ];

  showList: any;
  listOptions: any[] = [{icon: "fa-solid fa-table", value: "table"}];

  participant: string[] = [];

  searchValue: string | undefined;

  get indData(): Map<string, any[]> {
    if (this.searchValue?.trim() !== "") {
      return new Map(Array.from(this.individualData.entries()).filter(([key, value]) => key.startsWith(this.searchValue ?? "")));
    } else {
      return this.individualData;
    }
  }

  get showLeaderboard(): boolean {
    return this.route.snapshot.params["type"] === "quiz";
  }

  get date(): string {
    return getDate();
  }
  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private answerService: AnswerService
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.getQuizzQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        this.project = res[0];

        this.project.questions.forEach(q => q.options.forEach(opt => (opt.checked = false)));

        for (let question of this.project.questions) {
          let temp: any[] = [];
          question.options.forEach(opt => temp.push({name: opt.text, value: 0}));
          this.data.push(temp);
        }
        console.log(this.data);

        for (let [index, question] of this.project.questions.entries()) {
          let answer: any[] = [];

          this.answerService.get(question.id ?? 0).subscribe(res => {
            console.log(res);
            answer = res;

            if (index === 0) {
              answer.forEach(a => this.participant.push(a.participantName));

              for (let p of this.participant) {
                this.individualData.set(p, []);
              }
            }

            answer.forEach(a =>
              a.selectedOptions.forEach((opt: any) =>
                this.data[index].forEach((x, i) => (opt.text === x.name ? this.data[index][i].value++ : opt))
              )
            );

            setTimeout(() => {
              answer.forEach(a => {
                if (this.individualData.get(a.participantName)) {
                  const dataArray = this.individualData.get(a.participantName) ?? [];
                  let questiontext = this.project.questions[index].text;
                  let questionScore = this.project.questions[index].score;
                  a.text = questiontext;
                  a.maxScore = questionScore;
                  dataArray.push(a);
                  this.individualData.set(a.participantName, dataArray);
                }

                console.log(this.individualData);
              });
            }, 200);
          });
        }

        setTimeout(() => {
          this.loadingFinished = true;
        }, 1000);
      });
    } else {
      this.surveyService.getSurveyQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        this.project = res[0];

        this.project.questions.forEach(q => q.options.forEach(opt => (opt.checked = false)));

        for (let question of this.project.questions) {
          let temp: any[] = [];
          question.options.forEach(opt => temp.push({name: opt.text, value: 0}));
          this.data.push(temp);
        }
        console.log(this.data);

        for (let [index, question] of this.project.questions.entries()) {
          let answer: any[] = [];

          this.answerService.get(question.id ?? 0).subscribe(res => {
            console.log(res);
            answer = res;

            if (index === 0) {
              answer.forEach(a => this.participant.push(a.participantName));

              for (let p of this.participant) {
                this.individualData.set(p, []);
              }
            }

            answer.forEach(a =>
              a.selectedOptions.forEach((opt: any) =>
                this.data[index].forEach((x, i) => (opt.text === x.name ? this.data[index][i].value++ : opt))
              )
            );

            setTimeout(() => {
              answer.forEach(a => {
                if (this.individualData.get(a.participantName)) {
                  const dataArray = this.individualData.get(a.participantName) ?? [];
                  let questiontext = this.project.questions[index].text;
                  let questionScore = this.project.questions[index].score;
                  a.text = questiontext;
                  a.maxScore = questionScore;
                  dataArray.push(a);
                  this.individualData.set(a.participantName, dataArray);
                }

                console.log(this.individualData);
              });
            }, 200);
          });
        }

        setTimeout(() => {
          this.loadingFinished = true;
        }, 1000);
      });
    }
  }

  onSelect(data: any): void {
    console.log(this.data[0]);
    console.log(this.participant);
    console.log(this.individualData);
    console.log("leader", this.getLeaderboardList());

    console.log("Item clicked", JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: any): void {
    console.log("Activate", JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: any): void {
    console.log("Deactivate", JSON.parse(JSON.stringify(data)));
  }

  countAnswerPercentage(array: any[], value: number) {
    let total = array.reduce((acc, curr) => acc + curr.value, 0);
    let percentage = Math.round((value / total) * 100);
    return percentage + "%";
  }

  calcUserScore(array: any[]) {
    let totalScore: number = 0;

    for (let row of array) {
      if (row.selectedOptions.every((opt: any) => opt.correct)) {
        totalScore = totalScore + row.maxScore;
      }
    }

    return totalScore;
  }

  getLeaderboardList(): any[] {
    let leaderboardList: any[] = [];

    for (let [key, value] of this.indData) {
      leaderboardList.push({name: key, score: this.calcUserScore(value)});
    }

    return leaderboardList.sort((a, b) => b.score - a.score);
  }
}
