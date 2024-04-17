import {Component, ElementRef, OnInit, Renderer2, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {AnswerService} from "../../shared/services/answer/answer.service";
import {Survey} from "../../shared/models/survey.interface";
import {Quiz} from "../../shared/models/quiz.interface";
import {LegendPosition, ScaleType} from "@swimlane/ngx-charts";
import {getDate} from "../../shared/app-util";
import jsPDF, * as jspdf from "jspdf";
import html2canvas from "html2canvas";

export enum ChartType {
  PIE_CHART,
  PIE_GRID_CHART,
  VERTICAL_BAR_CHART,
  HORIZONTAL_BAR_CHART
}

export enum ExportType {
  CHARTS,
  RESPONSES,
  LEADERBOARD
}

@Component({
  selector: "app-statistics-view",
  templateUrl: "./statistics-view.component.html",
  styleUrl: "./statistics-view.component.scss"
})
export class StatisticsViewComponent implements OnInit {
  @ViewChild("charts") charts!: ElementRef;
  @ViewChild("responses") responses!: ElementRef;
  @ViewChild("leaderboard") leaderboard!: ElementRef;

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

  colorSchemeH = "nightLights";
  colorSchemeV = "nightLights";
  schemeType: ScaleType = ScaleType.Ordinal;

  chartType = ChartType;
  exportType = ExportType;

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
    private answerService: AnswerService,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.getQuizzQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        if (Array.isArray(res)) {
          this.project = res[0];

          this.project.questions.forEach(q => q.options.forEach(opt => (opt.checked = false)));

          for (let question of this.project.questions) {
            let temp: any[] = [];
            question.options.forEach(opt => temp.push({name: opt.text, value: 0}));
            this.data.push(temp);
          }

          for (let [index, question] of this.project.questions.entries()) {
            let answer: any[] = [];

            this.answerService.get(question.id ?? 0).subscribe(res => {
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
                });
              }, 200);
            });
          }

          setTimeout(() => {
            this.loadingFinished = true;
          }, 1000);
        }
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

        for (let [index, question] of this.project.questions.entries()) {
          let answer: any[] = [];

          this.answerService.get(question.id ?? 0).subscribe(res => {
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
      if (row.selectedOptions.length > 0 && row.selectedOptions.every((opt: any) => opt.correct)) {
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

  downloadResultsPdf(exportType: ExportType) {
    let exportElement;
    if (exportType === ExportType.CHARTS) {
      exportElement = this.charts.nativeElement;
    } else if (exportType === ExportType.RESPONSES) {
      exportElement = this.responses.nativeElement;
    } else {
      exportElement = this.leaderboard.nativeElement;
    }

    const pdf = new jsPDF();
    const pageHeight = 800;

    const childElements = Array.from(exportElement.children);

    const captureAndSavePage = (index: number, remainingHeight: number, isFirstPage: boolean) => {
      if (index >= childElements.length) {
        pdf.save(`project_results_${Date.now()}.pdf`);
        return;
      }

      const childElement = childElements[index] as HTMLElement;

      const elementHeight = childElement.scrollHeight;

      const windowHeight = Math.min(remainingHeight, elementHeight);

      html2canvas(childElement, {
        windowHeight: windowHeight,
        scale: 1,
        logging: false
      }).then(canvas => {
        const imgData = canvas.toDataURL("image/jpeg");
        const imgWidth = 202;
        const imgHeight = (canvas.height * imgWidth) / canvas.width;
        let imgMargin = 4;
        if (isFirstPage) {
          let headerText;
          if (exportType === ExportType.CHARTS) {
            headerText = `${this.project.title} Charts`;
          } else if (exportType === ExportType.RESPONSES) {
            headerText = `${this.project.title} Responses`;
          } else {
            headerText = `${this.project.title} Leaderboard`;
          }

          const textWidth = pdf.getTextDimensions(headerText).w;

          const xPosition = (pdf.internal.pageSize.width - textWidth) / 2;

          pdf.text(headerText, xPosition, 10);
          imgMargin = 20;
        }

        pdf.addImage(imgData, "jpeg", 4, imgMargin, imgWidth, imgHeight);

        const nextRemainingHeight = remainingHeight - windowHeight;

        if (index < childElements.length - 1 && nextRemainingHeight >= elementHeight) {
          captureAndSavePage(index + 1, nextRemainingHeight, false);
        } else {
          pdf.addPage();
          captureAndSavePage(index + 1, pageHeight, false);
        }
      });
    };

    captureAndSavePage(0, pageHeight, true);
  }
}
