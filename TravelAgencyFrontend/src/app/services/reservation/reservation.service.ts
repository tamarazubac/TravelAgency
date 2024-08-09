import { environment } from 'src/app/common/env/env';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reservation } from 'src/app/models/reservation';


@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<Reservation[]> {
    return this.httpClient.get<Reservation[]>(environment.apiHost + 'reservations')
  }

   create(newReservation: Reservation): Observable<Reservation> {
     return this.httpClient.post<Reservation>(`${environment.apiHost}reservations`, newReservation);
  }

  getReservationsByArrangementId(arrangementId: number): Observable<Reservation[]> {
    return this.httpClient.get<Reservation[]>(`${environment.apiHost}reservations/arrangement/${arrangementId}`);
  }

  getReservationsByUserId(userId: number): Observable<Reservation[]> {
    return this.httpClient.get<Reservation[]>(`${environment.apiHost}reservations/user/${userId}`);
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.apiHost}reservations/${id}`);
  }

  update(id: number, reservationDTO: Reservation): Observable<Reservation> {
    return this.httpClient.put<Reservation>(`${environment.apiHost}reservations/${id}`, reservationDTO);
  }





}
