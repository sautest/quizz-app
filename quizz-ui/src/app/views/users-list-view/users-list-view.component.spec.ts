import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersListViewComponent } from './users-list-view.component';

describe('UsersListViewComponent', () => {
  let component: UsersListViewComponent;
  let fixture: ComponentFixture<UsersListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsersListViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsersListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
