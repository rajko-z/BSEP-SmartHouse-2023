import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCSRRequestsPageComponent } from './admin-csrrequests-page.component';

describe('AdminCSRRequestsPageComponent', () => {
  let component: AdminCSRRequestsPageComponent;
  let fixture: ComponentFixture<AdminCSRRequestsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminCSRRequestsPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCSRRequestsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
