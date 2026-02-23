// dashboard.js
console.log("dashboard.js cargado");

// =============================
// FUNCIONES DE GRAFICOS
// =============================

function renderVentasMesChart() {
    const canvas = document.getElementById('salesChart');
    if (!canvas) return;

    const ventas = window.ventasData || [];
    if (!Array.isArray(ventas) || ventas.length === 0) return;

    new Chart(canvas.getContext('2d'), {
        type: 'line',
        data: {
            labels: ventas.map(v => "Mes " + v.mes),
            datasets: [{
                label: 'Ventas por Mes',
                data: ventas.map(v => parseFloat(v.total.toFixed(2))),
                borderWidth: 2,
                fill: false,
                borderColor: 'rgba(34,197,94,0.8)',
                backgroundColor: 'rgba(34,197,94,0.4)'
            }]
        },
        options: { responsive: true }
    });
}

function renderProductosMasVendidosChart() {
    const canvas = document.getElementById('productChart');
    if (!canvas) return;

    const productos = window.productosData || [];
    if (!Array.isArray(productos) || productos.length === 0) return;

    new Chart(canvas.getContext('2d'), {
        type: 'bar',
        data: {
            labels: productos.map(p => p.nombre),
            datasets: [{
                label: 'Productos más vendidos',
                data: productos.map(p => p.cantidad),
                backgroundColor: 'rgba(37,99,235,0.7)',
                borderWidth: 1
            }]
        },
        options: { responsive: true }
    });
}

function renderDistribucionCategoriasChart() {
    const canvas = document.getElementById('categoryChart');
    if (!canvas) return;

    const categorias = window.categoriasData || [];
    if (!Array.isArray(categorias) || categorias.length === 0) return;

    new Chart(canvas.getContext('2d'), {
        type: 'pie',
        data: {
            labels: categorias.map(c => c.nombre),
            datasets: [{
                label: 'Distribución por Categoría',
                data: categorias.map(c => c.cantidad),
                backgroundColor: [
                    'rgba(59,130,246,0.7)',
                    'rgba(16,185,129,0.7)',
                    'rgba(245,158,11,0.7)',
                    'rgba(239,68,68,0.7)',
                    'rgba(139,92,246,0.7)'
                ]
            }]
        },
        options: { responsive: true }
    });
}

function renderVentasDelMesChart() {
    const canvas = document.getElementById('ventasMesChart');
    if (!canvas) return;

    const ventasMes = window.ventasMesData || [];
    if (!Array.isArray(ventasMes) || ventasMes.length === 0) return;

    const maxValor = Math.max(...ventasMes.map(v => v.totalVendido));

    new Chart(canvas.getContext('2d'), {
        type: 'bar',
        data: {
            labels: ventasMes.map(v => v.producto),
            datasets: [{
                label: 'Total Vendido S/',
                data: ventasMes.map(v => v.totalVendido),
                backgroundColor: 'rgba(34,197,94,0.7)',
                borderRadius: 8,
                barThickness: 40
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: {
                    beginAtZero: true,
                    suggestedMax: maxValor + (maxValor * 0.1),
                    ticks: { stepSize: Math.ceil(maxValor / 4) },
                    grid: { color: "rgba(0,0,0,0.05)" }
                },
                x: { grid: { display: false } }
            }
        }
    });
}

// =============================
// INICIALIZADOR
// =============================

document.addEventListener("DOMContentLoaded", function () {
    renderVentasMesChart();
    renderProductosMasVendidosChart();
    renderDistribucionCategoriasChart();
    renderVentasDelMesChart();
});