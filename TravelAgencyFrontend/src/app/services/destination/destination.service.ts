import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Destination } from 'src/app/models/destination';
import { environment } from 'src/app/common/env/env';

@Injectable({
  providedIn: 'root'
})
export class DestinationService {

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<Destination[]> {
   return this.httpClient.get<Destination[]>(environment.apiHost + 'destinations')
 }

  create(newDestination: Destination): Observable<Destination> {
    return this.httpClient.post<Destination>(`${environment.apiHost}destinations`, newDestination);
  }


}
