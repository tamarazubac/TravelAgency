import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/common/env/env';

@Injectable({
  providedIn: 'root'
})
export class ReportService {


  constructor(private http: HttpClient) { }

  downloadUserReport(): Observable<Blob> {
    return this.http.get(environment.apiHost + 'reports/user', {
      responseType: 'blob' //blob - because of pdf
    });
  }

  downloadReport1(): Observable<Blob> {
    return this.http.get(environment.apiHost + 'reports/report1', {
      responseType: 'blob' //blob - because of pdf
    });
  }

  downloadReport2(): Observable<Blob> {
    return this.http.get(environment.apiHost + 'reports/report2', {
      responseType: 'blob' //blob - because of pdf
    });
  }
}
