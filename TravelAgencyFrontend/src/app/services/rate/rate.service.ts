import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/common/env/env';
import { Rate } from 'src/app/models/rate';

@Injectable({
  providedIn: 'root'
})
export class RateService {

  constructor(private httpClient: HttpClient) {
  }

  getRatesByArrangementId(arrangementId: number): Observable<Rate[]> {
    const url = `${environment.apiHost}rates/arrangement/${arrangementId}`;
    return this.httpClient.get<Rate[]>(url);
  }

  create(newRate: Rate): Observable<Rate> {
    const url = `${environment.apiHost}rates`;
    return this.httpClient.post<Rate>(url, newRate);
  }





}
