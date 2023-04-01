import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCsrrequestPageComponent } from './admin-csrrequest-page.component';

describe('AdminCsrrequestPageComponent', () => {
  let component: AdminCsrrequestPageComponent;
  let fixture: ComponentFixture<AdminCsrrequestPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminCsrrequestPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCsrrequestPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
