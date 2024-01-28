import {NgModule} from "@angular/core";
import {BrowserModule, provideClientHydration} from "@angular/platform-browser";

import {AppRoutingModule} from "./app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

import {AppComponent} from "./app.component";
import {ButtonModule} from "primeng/button";
import {SidebarModule} from "primeng/sidebar";
import {ToastModule} from "primeng/toast";
import {DialogModule} from "primeng/dialog";
import {AvatarModule} from "primeng/avatar";
import {AvatarGroupModule} from "primeng/avatargroup";
import {BreadcrumbModule} from "primeng/breadcrumb";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {GuestViewComponent} from "./views/guest-view/guest-view.component";
import {AuthComponent} from "./views/guest-view/components/auth/auth.component";
import {HttpClientModule} from "@angular/common/http";
import {DashboardViewComponent} from "./views/dashboard-view/dashboard-view.component";
import {UserViewComponent} from "./views/user-view/user-view.component";
import {ToastrModule} from "ngx-toastr";
import {StoreModule} from "@ngrx/store";
import {userReducer} from "./shared/store/user/user.reducer";
import {SidebarComponent} from "./shared/components/sidebar/sidebar.component";

@NgModule({
  declarations: [AppComponent, GuestViewComponent, AuthComponent, DashboardViewComponent, UserViewComponent, SidebarComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ButtonModule,
    SidebarModule,
    DialogModule,
    AvatarModule,
    AvatarGroupModule,
    InputTextModule,
    BreadcrumbModule,
    ToastModule,
    FormsModule,
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
