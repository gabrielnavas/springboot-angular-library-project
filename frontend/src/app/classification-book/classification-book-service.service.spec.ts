import { TestBed } from '@angular/core/testing';

import { ClassificationBookServiceService } from './classification-book.service';

describe('ClassificationBookServiceService', () => {
  let service: ClassificationBookServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassificationBookServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
