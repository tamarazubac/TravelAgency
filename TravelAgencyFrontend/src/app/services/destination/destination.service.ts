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

  // uploadImage(id: number, file: File): Observable<void> {
  //   const formData = new FormData();
  //   formData.append('file', file);

  //   return this.httpClient.post<void>(`${environment.apiHost}/destinations/${id}/upload-image`, formData);
  // }

  // getImages(id: number): Observable<string[]> {
  //   return this.httpClient.get<string[]>(`${environment.apiHost}/destinations/${id}/images`);
  // }

  uploadImage(destinationId: number, file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);
    return this.httpClient.post<void>(`${environment.apiHost}destinations/${destinationId}/upload-image`, formData);
  }

  getImages(destinationId: number): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiHost}destinations/${destinationId}/images`);
  }


}
