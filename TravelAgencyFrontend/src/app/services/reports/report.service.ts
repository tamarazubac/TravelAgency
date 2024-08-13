import { formatDate } from '@angular/common';
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

  downloadReportDestination(): Observable<Blob> {
    return this.http.get(environment.apiHost + 'reports/generateCombinedReport', {
      responseType: 'blob' //blob - because of pdf
    });
  }

  downloadBalanceReport(startDate: Date, endDate: Date): Observable<Blob> {

    const formattedStartDate = formatDate(startDate, 'yyyy-MM-dd', 'en');
    const formattedEndDate = formatDate(endDate, 'yyyy-MM-dd', 'en');


    const url = `${environment.apiHost}reports/generateBalanceReport?startDate=${encodeURIComponent(formattedStartDate)}&endDate=${encodeURIComponent(formattedEndDate)}`;

    return this.http.get(url, { responseType: 'blob' });
  }
}
