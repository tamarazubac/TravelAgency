import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Arrangement } from 'src/app/models/arrangement';
import { environment } from 'src/app/common/env/env';

@Injectable({
  providedIn: 'root'
})
export class ArrangementService {

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<Arrangement[]> {
   return this.httpClient.get<Arrangement[]>(environment.apiHost + 'arrangements')
 }

  create(newArrangement: Arrangement): Observable<Arrangement> {
    return this.httpClient.post<Arrangement>(`${environment.apiHost}arrangements`, newArrangement);
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.apiHost}arrangements/${id}`);
  }

  update(id: number, arrangement: Arrangement): Observable<Arrangement> {
    return this.httpClient.put<Arrangement>(`${environment.apiHost}arrangements/${id}`, arrangement);
  }

  findByDestinationAndDates(dateFrom: Date, dateTo: Date, destinationId: number): Observable<Arrangement[]> {
    const formattedDateFrom = dateFrom.toISOString().split('T')[0];
    const formattedDateTo = dateTo.toISOString().split('T')[0];
    return this.httpClient.get<Arrangement[]>(`${environment.apiHost}arrangements/dates/destination/${formattedDateFrom}/${formattedDateTo}/${destinationId}`);
  }

  findByDates(dateFrom: Date, dateTo: Date): Observable<Arrangement[]> {
    const formattedDateFrom = dateFrom.toISOString().split('T')[0];
    const formattedDateTo = dateTo.toISOString().split('T')[0];
    return this.httpClient.get<Arrangement[]>(`${environment.apiHost}arrangements/dates/${formattedDateFrom}/${formattedDateTo}`);
  }

  findByDestination(destinationId: number): Observable<Arrangement[]> {
    return this.httpClient.get<Arrangement[]>(`${environment.apiHost}arrangements/destination/${destinationId}`);
  }

  findById(id: number): Observable<Arrangement> {
    return this.httpClient.get<Arrangement>(`${environment.apiHost}arrangements/id/${id}`);
  }
}
