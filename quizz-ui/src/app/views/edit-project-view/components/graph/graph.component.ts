import {Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewChild} from "@angular/core";
import {Graphviz, graphviz} from "d3-graphviz";
import * as d3 from "d3";
import {Quiz} from "../../../../shared/models/quiz.interface";
import {Survey} from "../../../../shared/models/survey.interface";
import {QuizService} from "../../../../shared/services/quizz/quiz.service";
import {Question} from "../../../../shared/models/question.interface";
import {ActivatedRoute} from "@angular/router";
import {SurveyService} from "../../../../shared/services/survey/survey.service";

export type Selector = d3.Selection<d3.BaseType, undefined, null, undefined>;

export enum GraphViewStatus {
  DEFAULT = "DEFAULT",
  SHOW_MENU = "SHOW_MENU",
  PENDING_SELECTION = "PENDING_SELECTION"
}

export enum ContextMenuEvent {
  EDIT_QUESTION = "EDIT_QUESTION",
  DELETE_QUESTION = "DELETE_QUESTION",
  MOVE_QUESTION = "MOVE_QUESTION",
  QUESTION_LOGIC = "QUESTION_LOGIC"
}

export interface ContextMenuItem {
  label: string;
  icon: string;
  event: ContextMenuEvent;
}

@Component({
  selector: "app-graph",
  templateUrl: "./graph.component.html",
  styleUrl: "./graph.component.scss"
})
export class GraphComponent {
  @ViewChild("graph") graph!: ElementRef<HTMLElement>;
  @Input() project!: Quiz | Survey;
  @Output() onQuestionEdit: EventEmitter<any> = new EventEmitter<any>();
  @Output() onQuestionDelete: EventEmitter<any> = new EventEmitter<any>();
  @Output() onQuestionMove: EventEmitter<any> = new EventEmitter<any>();
  @Output() onQuestionLogic: EventEmitter<any> = new EventEmitter<any>();
  private graphvizElement!: Graphviz<d3.BaseType, any, d3.BaseType, any>;

  isVerticalAlignment = false;

  contextMenu = {
    items: <Array<ContextMenuItem>>[],
    location: <d3.ClientPointEvent>{clientX: 0, clientY: 0}
  };

  ConnectionsViewStatus = GraphViewStatus;
  viewStatus: GraphViewStatus = GraphViewStatus.DEFAULT;
  selectedNode: d3.BaseType = null;

  get isPendingSelection(): boolean {
    return this.viewStatus === GraphViewStatus.PENDING_SELECTION;
  }

  constructor(private quizService: QuizService, private surveyService: SurveyService, private route: ActivatedRoute) {}

  renderGraph(project: Quiz | Survey, isQuiz: boolean) {
    if (isQuiz) {
      this.quizService.generateGraph(project, this.isVerticalAlignment).subscribe(res => {
        this.graphvizElement = graphviz(this.graph.nativeElement);

        graphviz(this.graph.nativeElement)
          .renderDot(res.graph)
          .transition(() => <any>d3.transition().duration(250))
          .on("end", () => this.postprocessGraph());
      });
    } else {
      this.surveyService.generateGraph(project, this.isVerticalAlignment).subscribe(res => {
        this.graphvizElement = graphviz(this.graph.nativeElement);

        graphviz(this.graph.nativeElement)
          .renderDot(res.graph)
          .transition(() => <any>d3.transition().duration(250))
          .on("end", () => this.postprocessGraph());
      });
    }
  }

  private postprocessGraph(): void {
    this.centerGraph();
    this.initListeners();

    let dropShadowsTargetClasses = ["node"];
    d3.select(this.graph.nativeElement)
      .selectAll(dropShadowsTargetClasses.join(","))
      .style("filter", "drop-shadow(0 5px 1px hsl(0, 0%, 0%, 0.5))");
  }

  private initListeners(): void {
    this.initListenersNodes();
  }

  private initListenersNodes() {
    let me = this;
    d3.select(this.graph.nativeElement)
      .selectAll(".node")
      .each(function () {
        let nodeSelector: Selector = d3.select(this);
        let nodeContextMenu = nodeSelector
          .selectAll("text")
          .nodes()
          .find((x: any) => x.textContent == "⋮")!;
        nodeSelector
          .on("mouseover", function (event) {
            me.onNodeMouseOver(nodeSelector, nodeContextMenu, event);
          })
          .on("mouseout", function (event) {
            me.onNodeMouseOut(nodeSelector, nodeContextMenu, event);
          })
          .on("click", function (event) {
            me.onNodeClick(nodeSelector, nodeContextMenu, event);
            event.stopPropagation();
          });
      });
  }

  private onNodeMouseOut(nodeSelector: Selector, menu: d3.BaseType, event: any): void {
    if (event.target == menu) {
      nodeSelector.style("cursor", "pointer");
    }
  }

  private onNodeMouseOver(nodeSelector: Selector, menu: d3.BaseType, event: any): void {
    let node: d3.BaseType = nodeSelector.node();
    nodeSelector.style("cursor", "default");

    if (this.isPendingSelection) {
      nodeSelector.style("cursor", "alias");
    } else if (event.target == menu) {
      nodeSelector.style("cursor", "pointer");
    }
  }

  private onNodeClick(nodeSelector: Selector, menu: d3.BaseType, event: any): void {
    let node: d3.BaseType = nodeSelector.node();
    if (this.isPendingSelection) {
      const sourceId: number | undefined = GraphComponent.getBaselineUuid(this.selectedNode);
      const targetId: number | undefined = GraphComponent.getBaselineUuid(node);

      this.onQuestionMove.emit({
        source: this.project.questions.findIndex(q => q.id == sourceId),
        target: this.project.questions.findIndex(q => q.id == targetId)
      });

      this.viewStatus = GraphViewStatus.DEFAULT;
    }
    if (event.target == menu) {
      this.onNodeContextMenuIconClick(node, event);
    }
  }

  onNodeContextMenuIconClick(node: d3.BaseType, location: d3.ClientPointEvent) {
    this.selectedNode = node;
    this.contextMenu.items = [
      this.getEditQuestionItem(),
      this.getDeleteQuestionItem(),
      this.getMoveQuestionItem(),
      this.getLogicQuestionItem()
    ];
    this.contextMenu.location = {clientX: location.clientX, clientY: location.clientY};
    this.viewStatus = GraphViewStatus.SHOW_MENU;
  }

  onNodeContextMenuItemSelected(event: ContextMenuEvent) {
    const nodeId: number | undefined = GraphComponent.getBaselineUuid(this.selectedNode);
    const question: Question | undefined = this.project.questions.find(q => q.id == nodeId);

    switch (event) {
      case ContextMenuEvent.EDIT_QUESTION:
        this.onQuestionEdit.emit(question);
        break;
      case ContextMenuEvent.DELETE_QUESTION:
        this.onQuestionDelete.emit(question);
        break;
      case ContextMenuEvent.MOVE_QUESTION:
        this.viewStatus = GraphViewStatus.PENDING_SELECTION;
        break;
      case ContextMenuEvent.QUESTION_LOGIC:
        this.onQuestionLogic.emit(question);
        break;
    }
  }

  @HostListener("document:click", ["$event"])
  onDocumentClick(_event: any): void {
    if (!this.isPendingSelection) {
      this.contextMenu.items = [];
      this.selectedNode = null;
      this.viewStatus = GraphViewStatus.DEFAULT;
    }
  }

  centerGraph() {
    this.graphvizElement.resetZoom();
  }

  onAlignmentChange() {
    this.renderGraph(this.project, this.route.snapshot.params["type"] === "quiz");
  }

  private getEditQuestionItem(): ContextMenuItem {
    return {
      label: "Edit",
      icon: "pi pi-file-edit",
      event: ContextMenuEvent.EDIT_QUESTION
    };
  }

  private getDeleteQuestionItem(): ContextMenuItem {
    return {
      label: "Delete",
      icon: "pi pi-times",
      event: ContextMenuEvent.DELETE_QUESTION
    };
  }

  private getMoveQuestionItem(): ContextMenuItem {
    return {
      label: "Move",
      icon: "pi pi-arrows-alt",
      event: ContextMenuEvent.MOVE_QUESTION
    };
  }

  private getLogicQuestionItem(): ContextMenuItem {
    return {
      label: "Logic",
      icon: "pi pi-sitemap",
      event: ContextMenuEvent.QUESTION_LOGIC
    };
  }

  static getBaselineUuid(node: d3.BaseType): number | undefined {
    let id: any = null;
    d3.select(node).each(function () {
      id = this;
    });
    if (id) {
      return id.getAttribute("id");
    }
    return undefined;
  }
}
