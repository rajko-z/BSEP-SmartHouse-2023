import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLogPageComponent } from './admin-log-page.component';

describe('AdminLogPageComponent', () => {
  let component: AdminLogPageComponent;
  let fixture: ComponentFixture<AdminLogPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminLogPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminLogPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
