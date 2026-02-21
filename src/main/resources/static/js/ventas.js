let carrito = [];

// Configuración de un Toast(la noti en la esquina) para agregar productos sin interrumpir
const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 1500,
    timerProgressBar: true
});

function filtrarProductos() {
    let input = document.getElementById('buscarProducto').value.toLowerCase();
    let cards = document.querySelectorAll('.producto-card');
    cards.forEach(card => {
        let nombre = card.querySelector('.producto-nombre').textContent.toLowerCase();
        card.style.display = nombre.includes(input) ? '' : 'none';
    });
}

function agregarAlCarrito(elemento) {
    let id = parseInt(elemento.getAttribute('data-id'));
    let nombre = elemento.getAttribute('data-nombre');
    let precio = parseFloat(elemento.getAttribute('data-precio'));
    let stock = parseInt(elemento.getAttribute('data-stock'));

    let itemExistente = carrito.find(item => item.idProducto === id);

    if (itemExistente) {
        if (itemExistente.cantidad < stock) {
            itemExistente.cantidad++;
            itemExistente.subtotal = itemExistente.cantidad * itemExistente.precioUnitario;
            Toast.fire({ icon: 'success', title: '+1 agregado' });
        } else {
            Swal.fire({ icon: 'warning', title: 'Stock Límite', text: `Solo existen ${stock} unidades de ${nombre} en inventario.`});
        }
    } else {
        if (stock > 0) {
            carrito.push({
                idProducto: id,
                nombreProducto: nombre,
                cantidad: 1,
                precioUnitario: precio,
                subtotal: precio
            });
            Toast.fire({ icon: 'success', title: `${nombre} agregado` });
        } else {
            Swal.fire({ icon: 'error', title: 'Agotado', text: `El producto ${nombre} no tiene stock.`});
        }
    }
    actualizarUI();
}

function cambiarCantidad(id, delta) {
    let item = carrito.find(i => i.idProducto === id);
    if(item) {
        let cardDOM = document.querySelector(`.producto-card[data-id='${id}']`);
        let stockMaximo = parseInt(cardDOM.getAttribute('data-stock'));

        let nuevaCant = item.cantidad + delta;
        if(nuevaCant > 0 && nuevaCant <= stockMaximo) {
            item.cantidad = nuevaCant;
            item.subtotal = item.cantidad * item.precioUnitario;
        } else if (nuevaCant === 0) {
            eliminarItem(id);
            return;
        } else if (nuevaCant > stockMaximo) {
            Swal.fire({ icon: 'warning', title: 'Stock Límite', text: 'No puedes agregar más unidades de las disponibles.'});
        }
    }
    actualizarUI();
}

function eliminarItem(id) {
    carrito = carrito.filter(item => item.idProducto !== id);
    actualizarUI();
}

// Nueva función con confirmación para vaciar todo el carrito
function vaciarCarritoConfirmacion() {
    if(carrito.length === 0) return;
    
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Se eliminarán todos los productos del ticket.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, vaciar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            carrito = [];
            actualizarUI();
            Toast.fire({ icon: 'info', title: 'Carrito vaciado' });
        }
    });
}

function actualizarUI() {
    let lista = document.getElementById('listaCarrito');
    let totalDOM = document.getElementById('totalVenta');
    let vacioDOM = document.getElementById('carritoVacio');
    let btnCobrar = document.getElementById('btnCobrar');

    lista.innerHTML = '';
    let granTotal = 0.0;

    if(carrito.length === 0) {
        vacioDOM.style.display = 'block';
        btnCobrar.disabled = true;
    } else {
        vacioDOM.style.display = 'none';
        btnCobrar.disabled = false;

        carrito.forEach(item => {
            granTotal += item.subtotal;
            lista.innerHTML += `
                <li class="bg-white p-3 rounded-lg shadow-sm border border-gray-100 flex justify-between items-center">
                    <div class="flex-grow">
                        <h4 class="font-bold text-gray-800 text-sm leading-tight">${item.nombreProducto}</h4>
                        <p class="text-xs text-gray-500 mt-1">S/ ${item.precioUnitario.toFixed(2)} c/u</p>
                    </div>
                    <div class="flex items-center gap-3">
                        <div class="flex items-center bg-gray-100 rounded-lg">
                            <button onclick="cambiarCantidad(${item.idProducto}, -1)" class="px-2 py-1 text-gray-600 hover:text-red-500 font-bold">-</button>
                            <span class="px-2 font-semibold text-sm w-6 text-center">${item.cantidad}</span>
                            <button onclick="cambiarCantidad(${item.idProducto}, 1)" class="px-2 py-1 text-gray-600 hover:text-green-500 font-bold">+</button>
                        </div>
                        <span class="font-black text-green-600 w-16 text-right">S/ ${item.subtotal.toFixed(2)}</span>
                        <button onclick="eliminarItem(${item.idProducto})" class="text-red-400 hover:text-red-600">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
                        </button>
                    </div>
                </li>
            `;
        });
    }
    totalDOM.innerText = granTotal.toFixed(2);
}

function procesarVenta() {
    if(carrito.length === 0) return;

    let total = document.getElementById('totalVenta').innerText;

    // Modal de confirmación de SweetAlert2
    Swal.fire({
        title: 'Confirmar Venta',
        html: `El total a cobrar es de <b>S/ ${total}</b>`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Cobrar',
        cancelButtonText: 'Cancelar',
        reverseButtons: true
    }).then(async (result) => {
        if (result.isConfirmed) {
            
            // Mostrar estado de carga
            Swal.fire({
                title: 'Procesando...',
                allowOutsideClick: false,
                didOpen: () => { Swal.showLoading(); }
            });

            let usuarioId = document.getElementById('usuarioIdInput').value;
            let csrfToken = document.getElementById('csrfToken').value;
            let csrfHeader = document.getElementById('csrfHeader').value;

            let ventaDTO = {
                usuarioId: parseInt(usuarioId),
                total: parseFloat(total),
                detalles: carrito
            };

            try {
                let response = await fetch('/admin/ventas/crear', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(ventaDTO)
                });

                let data = await response.json();

				if(response.ok && !data.error) {
                    // Cambiamos el SweetAlert para preguntar si desea imprimir el ticket
                    Swal.fire({
                        icon: 'success',
                        title: '¡Venta Procesada!',
                        text: 'Comprobante: ' + data.numeroBoleta,
                        showCancelButton: true,
                        confirmButtonText: '<i class="fa fa-print"></i> Imprimir Ticket',
                        cancelButtonText: 'Cerrar y Continuar',
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#6c757d',
                    }).then((result) => {
                        if (result.isConfirmed) {
                            imprimirTicket(data.numeroBoleta, total);
                        } else {
                            window.location.reload(); 
                        }
                    });
                } else {
                    Swal.fire('Error', 'No se pudo procesar la venta: ' + (data.error || 'Desconocido'), 'error');
                }
            } catch (error) {
                console.error('Error:', error);
                Swal.fire('Error de conexión', 'No se pudo contactar al servidor.', 'error');
            }
        }
    });
}

//FUNCIONALIDAD DE IMPRESIÓN DE TICKET
function imprimirTicket(numeroBoleta, total) {
    let fecha = new Date().toLocaleString('es-PE');
    let cajeroId = document.getElementById('usuarioIdInput').value;//obtenemos nombre de cajero en lugar de id
    
    // Plantilla HTML estructurada para impresora térmica de 80mm
    let ticketHTML = `
        <!DOCTYPE html>
        <html>
        <head>
            <title>Ticket de Venta</title>
            <style>
                body { 
                    font-family: 'Courier New', Courier, monospace; 
                    font-size: 12px; 
                    width: 80mm; 
                    margin: 0 auto; 
                    padding: 10px; 
                    color: #000;
                }
                h2, p { margin: 2px 0; text-align: center; }
                h2 { font-size: 16px; font-weight: bold; margin-bottom: 5px; }
                table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 10px; }
                th, td { text-align: left; padding: 4px 0; font-size: 12px; border-bottom: 1px dotted #ccc;}
                .right { text-align: right; }
                .center { text-align: center; }
                .divider { border-top: 1px dashed #000; margin: 10px 0; }
                .total-row { font-weight: bold; font-size: 14px; }
                .total-row td { border-bottom: none; padding-top: 10px; }
                @media print {
                    @page { margin: 0; }
                    body { margin: 0.5cm; }
                }
            </style>
        </head>
        <body>
            <h2>BODEGAS FAMILIA S.A.C.</h2>
            <p>RUC: 20123456789</p>
            <p>Av. Principal 123, San Juan de Lurigancho</p>
            <div class="divider"></div>
            <p style="text-align: left;"><strong>TICKET BOLETA:</strong> ${numeroBoleta}</p>
            <p style="text-align: left;"><strong>FECHA:</strong> ${fecha}</p>
            <p style="text-align: left; text-transform: uppercase;"><strong>CAJERO:</strong> ${cajeroNombre}</p>
            <div class="divider"></div>
            
            <table>
                <thead>
                    <tr>
                        <th class="center" style="width: 15%;">Cant</th>
                        <th style="width: 55%;">Descripción</th>
                        <th class="right" style="width: 30%;">Importe</th>
                    </tr>
                </thead>
                <tbody>
                    ${carrito.map(item => `
                        <tr>
                            <td class="center">${item.cantidad}</td>
                            <td>${item.nombreProducto}</td>
                            <td class="right">S/ ${item.subtotal.toFixed(2)}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
            
            <table>
                <tr class="total-row">
                    <td>TOTAL A PAGAR:</td>
                    <td class="right">S/ ${total}</td>
                </tr>
            </table>
            
            <div class="divider"></div>
            <p>¡Gracias por su preferencia!</p>
            <p>Conserve este ticket en caso de cambios o devoluciones.</p>
        </body>
        </html>
    `;

    //Abrimos una ventana oculta/emergente para renderizar el ticket
    let ventanaEmergente = window.open('', 'Imprimir Ticket', 'width=400,height=600');
    ventanaEmergente.document.write(ticketHTML);
    ventanaEmergente.document.close();
    
    //Enfocamos la ventana y disparamos la impresión
    ventanaEmergente.focus();
    setTimeout(() => {
        ventanaEmergente.print();
        ventanaEmergente.close();
        //Una vez que se cierra el diálogo de impresión, recargamos la página principal
        window.location.reload();
    }, 500);
}