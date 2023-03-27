import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCertificatesPageComponent } from './admin-certificates-page.component';

describe('AdminCertificatesPageComponent', () => {
  let component: AdminCertificatesPageComponent;
  let fixture: ComponentFixture<AdminCertificatesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminCertificatesPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCertificatesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
