import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminViewUserProfilePageComponent } from './admin-view-user-profile-page.component';

describe('AdminViewUserProfilePageComponent', () => {
  let component: AdminViewUserProfilePageComponent;
  let fixture: ComponentFixture<AdminViewUserProfilePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminViewUserProfilePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminViewUserProfilePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
