import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnonymousHomePageComponent } from './anonymous-home-page.component';

describe('AnonymousHomePageComponent', () => {
  let component: AnonymousHomePageComponent;
  let fixture: ComponentFixture<AnonymousHomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnonymousHomePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnonymousHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
