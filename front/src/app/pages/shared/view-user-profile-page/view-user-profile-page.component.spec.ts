import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewUserProfilePageComponent } from './view-user-profile-page.component';

describe('ViewUserProfilePageComponent', () => {
  let component: ViewUserProfilePageComponent;
  let fixture: ComponentFixture<ViewUserProfilePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewUserProfilePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewUserProfilePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
