import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectActionViewComponent } from './project-action-view.component';

describe('ProjectActionViewComponent', () => {
  let component: ProjectActionViewComponent;
  let fixture: ComponentFixture<ProjectActionViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProjectActionViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProjectActionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
