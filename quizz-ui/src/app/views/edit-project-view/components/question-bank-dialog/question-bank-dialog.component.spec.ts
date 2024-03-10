import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionBankDialogComponent } from './question-bank-dialog.component';

describe('QuestionBankDialogComponent', () => {
  let component: QuestionBankDialogComponent;
  let fixture: ComponentFixture<QuestionBankDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [QuestionBankDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(QuestionBankDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
