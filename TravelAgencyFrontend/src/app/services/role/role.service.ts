import { Role } from './../../models/role';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/common/env/env';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private httpClient: HttpClient) {
   }


   getAll(): Observable<Role[]> {
    return this.httpClient.get<Role[]>(environment.apiHost + 'roles')
  }
}
