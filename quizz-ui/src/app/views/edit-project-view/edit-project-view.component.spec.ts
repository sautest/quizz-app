import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProjectViewComponent } from './edit-project-view.component';

describe('EditProjectViewComponent', () => {
  let component: EditProjectViewComponent;
  let fixture: ComponentFixture<EditProjectViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditProjectViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditProjectViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
