import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRejectCsrCheckComponent } from './admin-reject-csr-check.component';

describe('AdminRejectCsrCheckComponent', () => {
  let component: AdminRejectCsrCheckComponent;
  let fixture: ComponentFixture<AdminRejectCsrCheckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminRejectCsrCheckComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRejectCsrCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
