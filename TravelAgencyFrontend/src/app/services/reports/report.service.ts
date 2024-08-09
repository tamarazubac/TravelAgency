import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = 'http://localhost:8084/reports/user';

  constructor(private http: HttpClient) { }

  downloadUserReport(): Observable<Blob> {
    return this.http.get(this.apiUrl, {
      responseType: 'blob' //blob - because of pdf
    });
  }
}
