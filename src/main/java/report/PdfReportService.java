package report;


import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

//esta es la central para generar los reportes PDF del sistema.
//se le aplica un formato bonito


public class PdfReportService {
    private static final Font Fuente_Sistema = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.DARK_GRAY);
    private static final Font Fuente_Titulo = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
    private static final Font Fuente_Metadata = new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK);
    private static final Font FUENTE_ENCABEZADO_TABLA = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font FUENTE_CELDA = new Font(Font.HELVETICA, 9, Font.NORMAL);
    private static final Color COLOR_ENCABEZADO_TABLA = new Color(180, 0, 0);
    private static final Color COLOR_CELDA_OCUPADA = new Color(255, 245, 180);


    //generar reporte
    public void generarReporteTabla(String rutaSalida, ReportHeader header, ReportTable tabla)
        throws ReportException{
        //generarReporte(rutaSalida, header, tabla, null);
    }

    public void ReporteGrafico(String rutaSalida, ReportHeader header, ReportTable tabla, byte[] imagenGraficoPng)
        throws ReportException{

        if(rutaSalida == null || rutaSalida.isBlank()){
            throw new ReportException("La ruta de salida del reporte no puede estar vacía..");
        }
        if(header == null){
            throw new ReportException("El reporte requiere un encabezado..");
        }
        if(tabla == null){
            throw new ReportException("El reporte requiere una tabla con datos..");
        }

        Document documento = new Document(PageSize.A4, 36,36,54,54);
        try (FileOutputStream salida = new FileOutputStream(rutaSalida)) {
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setPageEvent(new //PiePaginaConNumero());

            //documento.open();

           // agregarEncabezado(documento, header);
            //agregarTabla(documento, tabla);

           // if (imagenGraficoPng != null && imagenGraficoPng.length > 0) {
                //agregarGrafico(documento, imagenGraficoPng);
            //}

           // documento.close();

       // } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new ReportException("No fue posible generar el reporte PDF: " + e.getMessage(), e);
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    private void agregarEncabezado(Document documento, ReportHeader header) throws DocumentException {
        Paragraph nombreSistema = new Paragraph(header.getNombreSistema(), Fuente_Sistema);
        nombreSistema.setAlignment(Element.ALIGN_LEFT);
        documento.add(nombreSistema);

        Paragraph titulo = new Paragraph(header.getTitulo(), Fuente_Titulo);
        titulo.setSpacingBefore(4f);
        titulo.setSpacingAfter(8f);
        documento.add(titulo);

        Paragraph metadata = new Paragraph();
        metadata.setFont(Fuente_Metadata);
        metadata.add("Generado: " + header.getFechaGeneracionFormateada() + "\n");
        metadata.add("Usuario: " + header.getUsuarioGenera() + "\n");
        if (header.tieneFiltros()) {
            metadata.add("Filtros aplicados: " + header.getFiltrosAplicados() + "\n");
        }
        metadata.setSpacingAfter(14f);
        documento.add(metadata);
    }

    }


