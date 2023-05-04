import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUserListPageComponent } from './admin-user-list-page.component';

describe('AdminUserListPageComponent', () => {
  let component: AdminUserListPageComponent;
  let fixture: ComponentFixture<AdminUserListPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminUserListPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminUserListPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
