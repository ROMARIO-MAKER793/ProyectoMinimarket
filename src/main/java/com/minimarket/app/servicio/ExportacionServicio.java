package com.minimarket.app.servicio;

import com.minimarket.app.entidad.Venta;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportacionServicio {

    // ==========================================
    // EXPORTAR A EXCEL (Mejorado)
    // ==========================================
    public void exportarVentasExcel(List<Venta> ventas, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Historial de Ventas");

        // 1. Estilo para el Título Principal
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFont(titleFont);

        // 2. Estilo para la Cabecera de la tabla
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        // 3. Estilos para los datos (Bordes y formatos)
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.cloneStyleFrom(dataStyle);
        DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("\"S/ \"#,##0.00"));

        // --- CONSTRUCCIÓN DEL DOCUMENTO ---

        // Fila de Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE VENTAS - BODEGAS FAMILIA S.A.C.");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // Combinar celdas para el título

        // Fila de Cabecera (Fila 2, dejamos la 1 en blanco)
        Row headerRow = sheet.createRow(2);
        String[] columnas = {"ID Venta", "Fecha y Hora", "N° Boleta", "Cajero", "Total"};
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar datos
        int rowIdx = 3;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowIdx++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(venta.getId());
            cell0.setCellStyle(dataStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(venta.getFecha() != null ? venta.getFecha().format(formatter) : "N/A");
            cell1.setCellStyle(dataStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(venta.getNumeroBoleta() != null ? venta.getNumeroBoleta() : "S/N");
            cell2.setCellStyle(dataStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(venta.getUsuario() != null ? venta.getUsuario().getUsername() : "N/A");
            cell3.setCellStyle(dataStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(venta.getTotal());
            cell4.setCellStyle(currencyStyle); // Aplicar formato de moneda
        }

        // Autoajustar columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // ==========================================
    // EXPORTAR A PDF (Mejorado)
    // ==========================================
    public void exportarVentasPdf(List<Venta> ventas, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Fuentes
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(0, 51, 102)); // Azul oscuro
        Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(0, 51, 102));

        // Título y Fecha de Generación
        Paragraph title = new Paragraph("BODEGAS FAMILIA S.A.C.", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Paragraph subtitle = new Paragraph("Reporte de Historial de Ventas\nGenerado el: " + dtf.format(LocalDateTime.now()), fontSubtitle);
        subtitle.setAlignment(Paragraph.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        // Tabla PDF
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2.5f, 2f, 2.5f, 2f});

        // Cabeceras de la tabla
        String[] columnas = {"ID", "Fecha", "N° Boleta", "Cajero", "Total (S/)"};
        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, fontHeader));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(new Color(0, 102, 204)); // Fondo azul
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Llenar datos y sumar total
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        double sumaTotal = 0;

        for (Venta venta : ventas) {
            PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(venta.getId()), fontData));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            table.addCell(new PdfPCell(new Phrase(venta.getFecha() != null ? venta.getFecha().format(formatter) : "N/A", fontData)));
            table.addCell(new PdfPCell(new Phrase(venta.getNumeroBoleta() != null ? venta.getNumeroBoleta() : "S/N", fontData)));
            table.addCell(new PdfPCell(new Phrase(venta.getUsuario() != null ? venta.getUsuario().getUsername() : "N/A", fontData)));
            
            PdfPCell cellTotal = new PdfPCell(new Phrase(String.format("%.2f", venta.getTotal()), fontData));
            cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cellTotal);

            sumaTotal += venta.getTotal();
        }

        // Fila de Total General
        PdfPCell cellSumaLabel = new PdfPCell(new Phrase("TOTAL GENERAL:", fontTotal));
        cellSumaLabel.setColspan(4);
        cellSumaLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSumaLabel.setBackgroundColor(new Color(240, 240, 240));
        cellSumaLabel.setPadding(8);
        table.addCell(cellSumaLabel);

        PdfPCell cellSumaValue = new PdfPCell(new Phrase(String.format("S/ %.2f", sumaTotal), fontTotal));
        cellSumaValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSumaValue.setBackgroundColor(new Color(240, 240, 240));
        cellSumaValue.setPadding(8);
        table.addCell(cellSumaValue);

        document.add(table);
        document.close();
    }
}