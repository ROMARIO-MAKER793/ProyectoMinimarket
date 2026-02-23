
// FUNCIONES DE GRAFICOS

console.log("reportes.js cargado");

function renderVentasMesChart() {

    const canvas = document.getElementById("ventasMesChartReportes");
    if (!canvas) return;

    const ventas = window.ventasData || [];
    if (!Array.isArray(ventas) || ventas.length === 0) return;

    const labels = ventas.map(v => v.producto);
    const totales = ventas.map(v => v.totalVendido);
    const maxValor = Math.max(...totales);

    new Chart(canvas.getContext("2d"), {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Total Vendido S/",
                data: totales,
                backgroundColor: "rgba(34,197,94,0.7)",
                borderRadius: 8,
                barThickness: 40
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { grid: { display: false } },
                y: {
                    beginAtZero: true,
                    suggestedMax: maxValor + (maxValor * 0.1),
                    ticks: {
                        stepSize: Math.ceil(maxValor / 4)
                    },
                    grid: { color: "rgba(0,0,0,0.05)" }
                }
            }
        }
    });
}

function renderVentasPorDiaChart() {

    const canvas = document.getElementById("ventasDiaChart");
    if (!canvas) return;

    const ventasPorDia = window.ventasPorDiaData || [];
    if (!Array.isArray(ventasPorDia) || ventasPorDia.length === 0) return;

    const labels = ventasPorDia.map(v => "Día " + v[0]);
    const totales = ventasPorDia.map(v => v[1]);

    new Chart(canvas.getContext("2d"), {
        type: "line",
        data: {
            labels: labels,
            datasets: [{
                label: "Ventas por Día S/",
                data: totales,
                fill: true,
                backgroundColor: "rgba(37,99,235,1)",
                tension: 0.4,
                borderWidth: 3,
                pointRadius: 4,
                pointHoverRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { grid: { display: false } },
                y: {
                    beginAtZero: true,
                    grid: { color: "rgba(0,0,0,0.05)" }
                }
            }
        }
    });
}

function renderTopProductosChart() {

    const canvas = document.getElementById("topProductosChart");
    if (!canvas) return;

    const topProductos = window.topProductosData || [];
    if (!Array.isArray(topProductos) || topProductos.length === 0) return;

    const labels = topProductos.map(p => p[0]);
    const cantidades = topProductos.map(p => p[1]);

    new Chart(canvas.getContext("2d"), {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Cantidad Vendida",
                data: cantidades,
                backgroundColor: [
                    "rgba(59, 130, 246, 0.8)",
                    "rgba(16, 185, 129, 0.8)",
                    "rgba(245, 158, 11, 0.8)",
                    "rgba(239, 68, 68, 0.8)",
                    "rgba(139, 92, 246, 0.8)"
                ],
                borderWidth: 1
            }]
        },
        options: {
            indexAxis: 'y',
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { beginAtZero: true }
            }
        }
    });
}



// INICIALIZADOR GENERAL


document.addEventListener("DOMContentLoaded", function () {

    renderVentasMesChart();
    renderVentasPorDiaChart();
    renderTopProductosChart();

});