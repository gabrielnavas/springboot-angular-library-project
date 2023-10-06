import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublisherCompanyReadComponent } from './publisher-company-read.component';

describe('PublisherCompanyReadComponent', () => {
  let component: PublisherCompanyReadComponent;
  let fixture: ComponentFixture<PublisherCompanyReadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PublisherCompanyReadComponent]
    });
    fixture = TestBed.createComponent(PublisherCompanyReadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
