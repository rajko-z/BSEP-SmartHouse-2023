import { TestBed } from '@angular/core/testing';

import { CSRRequestService } from './csrrequest.service';

describe('CSRRequestService', () => {
  let service: CSRRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CSRRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
