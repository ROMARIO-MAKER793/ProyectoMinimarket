import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../shared/services/auth';
// Importamos SweetAlert2
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  credenciales = { username: '', password: '' };
  isLoading = false; // Para deshabilitar el botón mientras carga

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    // Validar campos vacíos primero (igual que tu script original)
    if (!this.credenciales.username || !this.credenciales.password) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos requeridos',
        text: !this.credenciales.username && !this.credenciales.password ? 'Ingrese su usuario y contraseña' :
              !this.credenciales.username ? 'Ingrese su usuario' : 'Ingrese su contraseña',
        confirmButtonColor: '#4F46E5',
        customClass: { popup: 'rounded-2xl', confirmButton: 'rounded-xl px-6 py-2 font-semibold' }
      });
      return;
    }

    this.isLoading = true;

    this.authService.login(this.credenciales).subscribe({
      next: (response) => {
        // Mostrar alerta de éxito
        Swal.fire({
          icon: 'success',
          title: `¡Bienvenido, ${response.usuario}!`,
          text: 'Has iniciado sesión correctamente.',
          timer: 1500,
          showConfirmButton: false,
          customClass: { popup: 'rounded-2xl' }
        }).then(() => {
          this.isLoading = false;
          this.router.navigate(['/admin/productos']);
        });
      },
      error: (error) => {
        this.isLoading = false;
        // Mostrar alerta de error
        Swal.fire({
          icon: 'error',
          title: 'Acceso Denegado',
          text: 'Usuario o contraseña incorrectos',
          confirmButtonColor: '#4F46E5',
          customClass: { popup: 'rounded-2xl', confirmButton: 'rounded-xl px-6 py-2 font-semibold' }
        });
      }
    });
  }
}