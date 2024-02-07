import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InitProjectDialogComponent } from './init-project-dialog.component';

describe('InitProjectDialogComponent', () => {
  let component: InitProjectDialogComponent;
  let fixture: ComponentFixture<InitProjectDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InitProjectDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InitProjectDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
