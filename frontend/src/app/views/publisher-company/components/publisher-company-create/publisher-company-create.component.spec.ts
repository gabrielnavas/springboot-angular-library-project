import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublisherCompanyCreateComponent } from './publisher-company-create.component';

describe('PublisherCompanyCreateComponent', () => {
  let component: PublisherCompanyCreateComponent;
  let fixture: ComponentFixture<PublisherCompanyCreateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PublisherCompanyCreateComponent]
    });
    fixture = TestBed.createComponent(PublisherCompanyCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
