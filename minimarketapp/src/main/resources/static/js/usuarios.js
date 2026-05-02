// usuarios.js
console.log("usuarios.js cargado");

// =============================
// PREPARAR CREAR USUARIOS
// =============================
function prepareCreateUser() {
    document.getElementById('modalTitleUser').innerText = 'Nuevo Usuario';
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('userModal').classList.remove('hidden');
}

// =============================
// PREPARAR EDITAR
// =============================
function prepareEditUser(id, usuario, habilitado, rolId) {
    document.getElementById('modalTitleUser').innerText = 'Editar Usuario';
    document.getElementById('userId').value = id;
    document.getElementById('usuario').value = usuario;
    document.getElementById('habilitado').value = habilitado;
    document.getElementById('roles').value = rolId; // asigna el select al rol actual
    document.getElementById('contrasena').value = ''; // vacío, solo placeholder
    document.getElementById('confirmarContrasena').value = ''; // también vacío
    document.getElementById('userModal').classList.remove('hidden');
}

// =============================
// CERRAR MODAL
// =============================
function closeUserModal() {
    document.getElementById('userModal').classList.add('hidden');
}

// =============================
// CONFIRMAR ELIMINAR USUARIO
// =============================
function confirmDeleteUser(id) {
    Swal.fire({
        title: '¿Desea eliminar este usuario?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '/admin/usuarios/eliminar/' + id;
        }
    });
}

// =============================
// VALIDAR FORMULARIO CON SWEETALERT2
// =============================
document.addEventListener('DOMContentLoaded', function () {

    const userForm = document.getElementById('userForm');
    if (!userForm) return;

    userForm.addEventListener('submit', function (e) {
        let usuario = document.getElementById('usuario').value.trim();
        let pass = document.getElementById('contrasena').value;
        let confirmPass = document.getElementById('confirmarContrasena').value;
        let rol = document.getElementById('roles').value;

        if (!usuario) {
            e.preventDefault();
            Swal.fire('Error', 'El nombre de usuario es obligatorio', 'error');
            return;
        }

        if (!rol) {
            e.preventDefault();
            Swal.fire('Error', 'Seleccione un rol', 'error');
            return;
        }

        if (!pass) {
            e.preventDefault();
            Swal.fire('Error', 'La contraseña es obligatoria', 'error');
            return;
        }

        if (!confirmPass) {
            e.preventDefault();
            Swal.fire('Error', 'Debe confirmar la contraseña', 'error');
            return;
        }

        if (pass !== confirmPass) {
            e.preventDefault();
            Swal.fire('Error', 'Las contraseñas no coinciden', 'error');
            return;
        }
    });

});