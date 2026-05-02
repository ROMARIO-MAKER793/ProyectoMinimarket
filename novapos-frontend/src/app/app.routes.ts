import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login'; // <-- Apunta a login.ts
import { ProductosComponent } from './admin/productos/productos'; // O productos.component

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  
  { path: 'admin/productos', component: ProductosComponent },
  
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];