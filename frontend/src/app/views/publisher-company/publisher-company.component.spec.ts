import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublisherCompanyComponent } from './publisher-company.component';

describe('PublisherCompanyComponent', () => {
  let component: PublisherCompanyComponent;
  let fixture: ComponentFixture<PublisherCompanyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PublisherCompanyComponent]
    });
    fixture = TestBed.createComponent(PublisherCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
