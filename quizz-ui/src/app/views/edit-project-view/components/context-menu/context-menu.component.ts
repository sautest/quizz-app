import {Component, EventEmitter, HostBinding, Input, OnInit, Output} from "@angular/core";
import {ContextMenuEvent, ContextMenuItem} from "../graph/graph.component";
import {MenuItem} from "primeng/api";

@Component({
  selector: "app-context-menu",
  templateUrl: "./context-menu.component.html",
  styleUrl: "./context-menu.component.scss"
})
export class ContextMenuComponent implements OnInit {
  @Input() location!: d3.ClientPointEvent;
  @Input() items!: ContextMenuItem[];
  @Output() onItemSelected: EventEmitter<any> = new EventEmitter<any>();

  menuItems: MenuItem[] = [];

  @HostBinding("style.left") left = "0px";
  @HostBinding("style.top") top = "0px";

  ngOnInit(): void {
    this.menuItems = this.items.map(item => ({label: item.label, icon: item.icon, command: () => this.onItemClick(item.event)}));
  }

  ngOnChanges() {
    this.left = `${this.location.clientX}px`;
    this.top = `${this.location.clientY}px`;
  }

  onItemClick(event: ContextMenuEvent) {
    this.onItemSelected.emit(event);
  }
}
