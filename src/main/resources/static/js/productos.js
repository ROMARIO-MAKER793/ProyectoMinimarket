
console.log("productos.js cargado");

function openModal() {
    const modal = document.getElementById('productModal');
    modal.classList.remove('hidden');
    modal.classList.add('flex');
}

function closeModal() {
    const modal = document.getElementById('productModal');
    modal.classList.add('hidden');
    modal.classList.remove('flex');
}

function prepareCreate() {
    document.getElementById('modalTitle').innerText = 'Nuevo Producto';
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';

    const imagenPreview = document.getElementById('imagenPreview'); 
    if (imagenPreview) {
        imagenPreview.src = '';
        imagenPreview.classList.add('hidden');
    }

    openModal();
}

function prepareEdit(id, nombre, precio, stockActual, categoriaId, marcaId, descripcion, imagenUrl) {
    document.getElementById('modalTitle').innerText = 'Editar Producto';
    document.getElementById('productId').value = id;
    document.getElementById('nombre').value = nombre;
    document.getElementById('precio').value = precio;
    document.getElementById('stockActual').value = stockActual;
    document.getElementById('categoria').value = categoriaId;
    document.getElementById('marca').value = marcaId;
    document.getElementById('descripcion').value = descripcion;

    const imagenPreview = document.getElementById("imagenPreview");
    if (imagenUrl && imagenPreview) {
        imagenPreview.src = imagenUrl;
        imagenPreview.classList.remove('hidden');
    } else if(imagenPreview) {
        imagenPreview.classList.add('hidden');
    }

    openModal();
}

function confirmDelete(id) {
    Swal.fire({
        title: '¿Eliminar producto?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '/admin/productos/eliminar/' + id;
        }
    });
}

const productForm = document.getElementById("productForm");
if (productForm) {
    productForm.addEventListener("submit", function(e) {
        let nombre = document.getElementById("nombre").value.trim();
        let precio = parseFloat(document.getElementById("precio").value);
        let stock = parseInt(document.getElementById("stockActual").value);
        let categoria = document.getElementById("categoria").value;
        let marca = document.getElementById("marca").value;

        if (!nombre) { e.preventDefault(); Swal.fire("El nombre es obligatorio"); return; }
        if (isNaN(precio) || precio <= 0) { e.preventDefault(); Swal.fire("El precio debe ser mayor a 0"); return; }
        if (isNaN(stock) || stock < 20) { e.preventDefault(); Swal.fire("El stock mínimo debe ser 20"); return; }
        if (!categoria) { e.preventDefault(); Swal.fire("Debe seleccionar una categoría"); return; }
        if (!marca) { e.preventDefault(); Swal.fire("Debe seleccionar una marca"); return; }
    });
}
