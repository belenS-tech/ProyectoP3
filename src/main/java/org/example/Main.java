package org.example;

import report.ReportHeader;
import report.ReportTable;
import report.PdfReportService;

public class Main {
    public static void main(String[] args) {
        try {
            ReportHeader header = new ReportHeader(
                    "Reporte de Prueba",
                    "admin",
                    "Semana: 01/01/2025 - 07/01/2025"
            );

            ReportTable tabla = new ReportTable(java.util.List.of("Hora", "Lunes", "Martes", "Miércoles"));
            tabla.agregarFila("08:00", "Matemáticas", "", "Física");
            tabla.agregarFila("09:00", "", "Historia", "");
            tabla.agregarFila("10:00", "Química", "Química", "Matemáticas");

            PdfReportService service = new PdfReportService();
            service.generarReporteTabla("reporte_prueba.pdf", header, tabla);
            service.generarReporteMatriz("reporte_matriz.pdf", header, tabla);

            System.out.println("PDFs generados correctamente.");
            System.out.println("Archivos: reporte_prueba.pdf, reporte_matriz.pdf");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
