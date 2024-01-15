import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class HealthService {
  constructor(private http: HttpClient) {}

  readonly URL = `http://localhost:8080`;

  public health(): Observable<any> {
    return this.http.get<any>(`${this.URL}/api/health`);
  }
}
