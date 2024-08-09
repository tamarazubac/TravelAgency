import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/common/env/env';
import { User } from 'src/app/models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) {
  }

  getAll(): Observable<User[]> {
    return this.httpClient.get<User[]>(environment.apiHost + 'users')
  }

  getById(userId: number): Observable<User> {
    return this.httpClient.get<User>(`${environment.apiHost}users/id/${userId}`);
  }

  getByUsername(username: string): Observable<User> {
    return this.httpClient.get<User>(`${environment.apiHost}users/username/${username}`);
  }

  addRoleToUser(userId: number, roleName: string): Observable<User> {
    return this.httpClient.patch<User>(`${environment.apiHost}users/${userId}/role/${roleName}`, {});
  }

  addRolesToUser(username: string, roles: string[]): Observable<void> {
    return this.httpClient.post<void>(`${environment.apiHost}users/${username}/roles`, roles);
  }


  removeRoleFromUser(userId: number, roleName: string): Observable<User> {
    return this.httpClient.patch<User>(`${environment.apiHost}users/remove/${userId}/role/${roleName}`, {});
  }

  deleteUserByUsername(username: string): Observable<void> {
    return this.httpClient.delete<void>(`${environment.apiHost}users/username/${username}`);
  }

  deleteUserById(userId: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.apiHost}users/${userId}`);
  }

  getUsersByRole(roleName: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`${environment.apiHost}users/role/${roleName}`);
  }


  create(newUser: User): Observable<User> {
    return this.httpClient.post<User>(`${environment.apiHost}users`, newUser);
  }


}
