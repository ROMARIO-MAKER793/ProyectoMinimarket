import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // La URL del backend Spring Boot (AuthRestController)
  private authUrl = 'http://localhost:8080/api/auth/login';

  //inyectamos el HttpClient que configuramos en el paso 1
  constructor(private http: HttpClient) { }

  login(credenciales: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      //vital para la seuridad stateless conn cookies
      withCredentials: true 
    };

    //hacemos el POST enviando el JSON con las credenciales
    return this.http.post<any>(this.authUrl, credenciales, httpOptions);
  }
}