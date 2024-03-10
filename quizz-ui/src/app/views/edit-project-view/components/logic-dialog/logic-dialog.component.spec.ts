import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogicDialogComponent } from './logic-dialog.component';

describe('LogicDialogComponent', () => {
  let component: LogicDialogComponent;
  let fixture: ComponentFixture<LogicDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LogicDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LogicDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
