import {NgModule} from "@angular/core";
import {BrowserModule, provideClientHydration} from "@angular/platform-browser";
import {AppRoutingModule} from "./app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppComponent} from "./app.component";
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {RadioButtonModule} from "primeng/radiobutton";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {SidebarModule} from "primeng/sidebar";
import {ToastModule} from "primeng/toast";
import {DialogModule} from "primeng/dialog";
import {AvatarModule} from "primeng/avatar";
import {AvatarGroupModule} from "primeng/avatargroup";
import {SplitButtonModule} from "primeng/splitbutton";
import {BreadcrumbModule} from "primeng/breadcrumb";
import {ProgressBarModule} from "primeng/progressbar";
import {AngularEditorModule} from "@kolkov/angular-editor";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {FormsModule} from "@angular/forms";
import {CheckboxModule} from "primeng/checkbox";
import {GuestViewComponent} from "./views/guest-view/guest-view.component";
import {AuthComponent} from "./views/guest-view/components/auth/auth.component";
import {DividerModule} from "primeng/divider";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {DropdownModule} from "primeng/dropdown";
import {FileUploadModule} from "primeng/fileupload";
import {TableModule} from "primeng/table";
import {TooltipModule} from "primeng/tooltip";
import {TabViewModule} from "primeng/tabview";
import {InputSwitchModule} from "primeng/inputswitch";
import {HttpClientModule} from "@angular/common/http";
import {DashboardViewComponent} from "./views/dashboard-view/dashboard-view.component";
import {UserViewComponent} from "./views/user-view/user-view.component";
import {ToastrModule} from "ngx-toastr";
import {MenuModule} from "primeng/menu";
import {StoreModule} from "@ngrx/store";
import {DataViewModule} from "primeng/dataview";
import {userReducer} from "./shared/store/user/user.reducer";
import {SidebarComponent} from "./shared/components/sidebar/sidebar.component";
import {NewProjectViewComponent} from "./views/new-project-view/new-project-view.component";
import {InitProjectDialogComponent} from "./views/new-project-view/components/init-project-dialog/init-project-dialog.component";
import {EditProjectViewComponent} from "./views/edit-project-view/edit-project-view.component";
import {QuestionDialogComponent} from "./views/edit-project-view/components/question-dialog/question-dialog.component";
import {SelectButtonModule} from "primeng/selectbutton";
import {SettingsComponent} from "./views/edit-project-view/components/settings/settings.component";
import {GraphComponent} from "./views/edit-project-view/components/graph/graph.component";
import {LogicDialogComponent} from "./views/edit-project-view/components/logic-dialog/logic-dialog.component";
import {ContextMenuComponent} from "./views/edit-project-view/components/context-menu/context-menu.component";
import {ProjectActionViewComponent} from "./views/project-action-view/project-action-view.component";
import {StatisticsViewComponent} from "./views/statistics-view/statistics-view.component";
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {ColorPickerModule} from "primeng/colorpicker";
import {ProfileViewComponent} from "./views/profile-view/profile-view.component";
import {QuestionBankDialogComponent} from "./views/edit-project-view/components/question-bank-dialog/question-bank-dialog.component";
import {PickListModule} from "primeng/picklist";
import {ThemeEditorComponent} from "./views/edit-project-view/components/theme-editor/theme-editor.component";
import {ToolbarModule} from "primeng/toolbar";
import {UsersListViewComponent} from "./views/users-list-view/users-list-view.component";
import {MenubarModule} from "primeng/menubar";

@NgModule({
  declarations: [
    AppComponent,
    GuestViewComponent,
    AuthComponent,
    DashboardViewComponent,
    UserViewComponent,
    SidebarComponent,
    NewProjectViewComponent,
    InitProjectDialogComponent,
    EditProjectViewComponent,
    QuestionDialogComponent,
    SettingsComponent,
    GraphComponent,
    LogicDialogComponent,
    ContextMenuComponent,
    ProjectActionViewComponent,
    StatisticsViewComponent,
    ProfileViewComponent,
    QuestionBankDialogComponent,
    ThemeEditorComponent,
    UsersListViewComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    CardModule,
    ButtonModule,
    SidebarModule,
    DialogModule,
    AvatarModule,
    AvatarGroupModule,
    InputTextModule,
    PaginatorModule,
    ConfirmDialogModule,
    ToolbarModule,
    ProgressBarModule,
    MenubarModule,
    SelectButtonModule,
    DividerModule,
    FileUploadModule,
    DataViewModule,
    MenuModule,
    PickListModule,
    ColorPickerModule,
    TooltipModule,
    RadioButtonModule,
    CheckboxModule,
    AngularEditorModule,
    TabViewModule,
    InputSwitchModule,
    DragDropModule,
    DropdownModule,
    BreadcrumbModule,
    ToastModule,
    TableModule,
    FormsModule,
    SplitButtonModule,
    NgxChartsModule,
    ToastrModule.forRoot({
      closeButton: true,
      timeOut: 5000,
      preventDuplicates: false,
      maxOpened: 0
    }),
    StoreModule.forRoot({user: userReducer})
  ],
  providers: [provideClientHydration()],
  bootstrap: [AppComponent]
})
export class AppModule {}
