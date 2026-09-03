package controller.Calendarizacion;
/*
import model.Recurso;
import model.CategoriaRecurso;
import model.calendarizacion.FilaCalendarizacion;
import report.PdfReportService;
import report.ReportException;
import report.ReportHeader;
import report.ReportTable;
import repository.CategoriaRepository;
import repository.RecursoRepository;
import repository.ReservaRepository;
import service.CalendarizacionRecursosService;
import service.ResultadoCalendarizacion;
import util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de Calendarización de Recursos. Es un JPanel (no un
 * JFrame) para poder insertarse dentro de la pestaña correspondiente
 * de la ventana principal (MainView), tal como exige el equipo.

public class CalendarizacionRecursosPanel extends JPanel {

    private final JSpinner fechaSpinner;
    private final JComboBox<CategoriaRecurso> categoriaCombo;
    private final JButton btnCargar;
    private final JButton btnImprimir;
    private final JLabel lblMensaje;
    private final JTable tablaMatriz;
    private final DefaultTableModel modeloTabla;

    private CalendarizacionRecursosService service;
    private CategoriaRepository categoriaRepo;
    private final PdfReportService pdfReportService = new PdfReportService();

    private ResultadoCalendarizacion ultimoResultado;
    private LocalDate ultimaFecha;
    private CategoriaRecurso ultimaCategoria;

    public CalendarizacionRecursosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Calendarización de Recursos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fechaSpinner = new JSpinner(new SpinnerDateModel());
        fechaSpinner.setEditor(new JSpinner.DateEditor(fechaSpinner, "dd/MM/yyyy"));
        categoriaCombo = new JComboBox<>();
        btnCargar = new JButton("Cargar");
        btnImprimir = new JButton("Imprimir");
        btnImprimir.setEnabled(false);

        panelFiltros.add(new JLabel("Fecha:"));
        panelFiltros.add(fechaSpinner);
        panelFiltros.add(new JLabel("Categoría:"));
        panelFiltros.add(categoriaCombo);
        panelFiltros.add(btnCargar);
        panelFiltros.add(btnImprimir);
        panelSuperior.add(panelFiltros, BorderLayout.CENTER);

        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(180, 0, 0));

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Hora");
        tablaMatriz = new JTable(modeloTabla);
        tablaMatriz.setRowHeight(28);

        add(panelSuperior, BorderLayout.NORTH);
        add(lblMensaje, BorderLayout.SOUTH);
        add(new JScrollPane(tablaMatriz), BorderLayout.CENTER);

        btnCargar.addActionListener(e -> onCargar());
        btnImprimir.addActionListener(e -> onImprimir());
    }

    /** Inyección de dependencias reales. Llamar antes de mostrar el panel.
    public void configurarDependencias(RecursoRepository recursoRepo,
                                       ReservaRepository reservaRepo,
                                       CategoriaRepository categoriaRepo) {
        this.service = new CalendarizacionRecursosService(recursoRepo, reservaRepo);
        this.categoriaRepo = categoriaRepo;
        cargarCategorias();
    }

    private void cargarCategorias() {
        categoriaCombo.removeAllItems();
        for (CategoriaRecurso categoria : categoriaRepo.listarTodas()) {
            categoriaCombo.addItem(categoria);
        }
    }

    private void onCargar() {
        lblMensaje.setText(" ");
        java.util.Date fechaUtil = (java.util.Date) fechaSpinner.getValue();
        LocalDate fecha = fechaUtil.toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        CategoriaRecurso categoria = (CategoriaRecurso) categoriaCombo.getSelectedItem();

        if (categoria == null) {
            lblMensaje.setText("Debe seleccionar una categoría.");
            return;
        }

        try {
            ResultadoCalendarizacion resultado = service.construirMatriz(fecha, categoria.getId());
            this.ultimoResultado = resultado;
            this.ultimaFecha = fecha;
            this.ultimaCategoria = categoria;

            if (resultado.estaVacio()) {
                lblMensaje.setText("La categoría seleccionada no tiene recursos registrados.");
                limpiarTabla();
                btnImprimir.setEnabled(false);
                return;
            }

            construirTabla(resultado);
            btnImprimir.setEnabled(true);

        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        }
    }

    private void construirTabla(ResultadoCalendarizacion resultado) {
        modeloTabla.setColumnCount(0);
        modeloTabla.addColumn("Hora");
        for (Recurso recurso : resultado.getRecursos()) {
            modeloTabla.addColumn(recurso.getDescripcion());
        }

        modeloTabla.setRowCount(0);
        for (FilaCalendarizacion fila : resultado.getFilas()) {
            List<Object> valores = new ArrayList<>();
            valores.add(fila.getHoraTexto());
            for (Recurso recurso : resultado.getRecursos()) {
                valores.add(fila.getValor(recurso.getId()));
            }
            modeloTabla.addRow(valores.toArray());
        }
    }

    private void limpiarTabla() {
        modeloTabla.setColumnCount(0);
        modeloTabla.addColumn("Hora");
        modeloTabla.setRowCount(0);
    }

    private void onImprimir() {
        if (ultimoResultado == null || ultimoResultado.estaVacio()) {
            lblMensaje.setText("Primero debe cargar una calendarización con datos.");
            return;
        }

        try {
            List<String> encabezados = new ArrayList<>();
            encabezados.add("Hora");
            for (Recurso recurso : ultimoResultado.getRecursos()) {
                encabezados.add(recurso.getDescripcion());
            }

            ReportTable tabla = new ReportTable(encabezados);
            for (FilaCalendarizacion fila : ultimoResultado.getFilas()) {
                List<String> valores = new ArrayList<>();
                valores.add(fila.getHoraTexto());
                for (Recurso recurso : ultimoResultado.getRecursos()) {
                    valores.add(fila.getValor(recurso.getId()));
                }
                tabla.agregarFila(valores);
            }

            String filtros = "Fecha: " + ultimaFecha + " | Categoría: " + ultimaCategoria.getDescripcion();
            ReportHeader header = new ReportHeader(
                    "Calendarización de Recursos",
                    SessionManager.getInstancia().getUsuarioActual().getId(),
                    filtros
            );

            String carpetaDescargas = javax.swing.filechooser.FileSystemView.getFileSystemView()
                    .getDefaultDirectory().getPath();
            String ruta = carpetaDescargas + "/calendarizacion_" + ultimaFecha + ".pdf";

            pdfReportService.generarReporteMatriz(ruta, header, tabla);
            lblMensaje.setForeground(new Color(0, 122, 47));
            lblMensaje.setText("Reporte generado en: " + ruta);

        } catch (ReportException e) {
            lblMensaje.setForeground(new Color(180, 0, 0));
            lblMensaje.setText("Error al generar el PDF: " + e.getMessage());
        }
    }
}
*/