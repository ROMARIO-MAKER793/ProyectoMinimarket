import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { ProductoService } from '../../shared/services/producto';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [FormsModule, CurrencyPipe],
  templateUrl: './productos.html',
  styleUrl: './productos.css',
})
export class ProductosComponent implements OnInit {

  // Variables para la vista
  terminoBusqueda: string = '';
  mostrarModal: boolean = false;
  esAdmin: boolean = true; 

  // Arrays vacíos listos para recibir los datos de tu API REST
  productos: any[] = [];
  categorias: any[] = [];
  marcas: any[] = [];

  // Objeto para manejar el formulario (Crear/Editar)
  productoSeleccionado: any = {};
  archivoSeleccionado: File | null = null;

  // ¡AQUÍ ES DONDE USAMOS EL SERVICIO! Esto le quitará lo transparente al import.
  constructor(private productoService: ProductoService) {}

  // Este método se dispara automáticamente al entrar a la pantalla
  ngOnInit(): void {
    this.cargarProductos();
  }

  // --- MÉTODOS DE LA VISTA ---

  cargarProductos(): void {
    // Llamamos a Spring Boot para traer la lista
    this.productoService.listarTodos().subscribe({
      next: (datos) => {
        this.productos = datos;
        console.log('Productos cargados con éxito:', this.productos);
      },
      error: (error) => {
        console.error('Error al cargar los productos:', error);
      }
    });
  }

  filtrarTabla() {
    console.log('Buscando:', this.terminoBusqueda);
    // Pendiente: Lógica de filtro local
  }

  abrirModal(producto?: any) {
    this.archivoSeleccionado = null; 
    if (producto) {
      this.productoSeleccionado = { 
        ...producto, 
        categoriaId: producto.categoria?.id, 
        marcaId: producto.marca?.id 
      };
    } else {
      this.productoSeleccionado = {};
    }
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.productoSeleccionado = {};
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.archivoSeleccionado = file;
    }
  }

  guardarProducto() {
    console.log('Guardando producto...', this.productoSeleccionado);
    // Pendiente: Llamar al POST del servicio
  }

  eliminarProducto(id: number) {
    if (confirm('¿Estás seguro de eliminar este producto?')) {
      console.log('Eliminando producto con ID:', id);
      // Pendiente: Llamar al DELETE del servicio
    }
  }
}