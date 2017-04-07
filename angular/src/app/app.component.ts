import { SidenavSharedServiceService } from './sidenav/shared/sidenav-shared-service.service';
import { Component } from '@angular/core';

@Component({
  selector: 'qs-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {

  constructor(private sidenav: SidenavSharedServiceService) {
  }

  closeSideNav(): void {
    this.sidenav.closeSideNav();
  }

  openSideNav(): void {
    this.sidenav.openSideNav();
  }

}
