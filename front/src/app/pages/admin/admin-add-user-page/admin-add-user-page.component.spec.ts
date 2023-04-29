import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAddUserPageComponent } from './admin-add-user-page.component';

describe('AdminAddUserPageComponent', () => {
  let component: AdminAddUserPageComponent;
  let fixture: ComponentFixture<AdminAddUserPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminAddUserPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAddUserPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
