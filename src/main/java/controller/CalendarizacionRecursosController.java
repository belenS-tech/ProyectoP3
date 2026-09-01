package controller;
/*
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import model.CategoriaRecurso;
import model.FilaCalendarizacion;
import model.Recurso;
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

import javax.swing.filechooser.FileSystemView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CalendarizacionRecursosController implements Initializable {

    @FXML private DatePicker fechaPicker;
    @FXML private ComboBox<CategoriaRecurso> categoriaCombo;
    @FXML private Button btnCargar;
    @FXML private Button btnImprimir;
    @FXML private Label lblMensaje;
    @FXML private TableView<FilaCalendarizacion> tablaMatriz;
    @FXML private TableColumn<FilaCalendarizacion, String> colHora;

    private CalendarizacionRecursosService service;
    private CategoriaRepository categoriaRepo;
    private final PdfReportService pdfReportService = new PdfReportService();

    private ResultadoCalendarizacion ultimoResultado;
    private LocalDate ultimaFecha;
    private CategoriaRecurso ultimaCategoria;

    /** Inyección de dependencias reales. Llamar justo después de cargar el FXML.
    public void configurarDependencias(RecursoRepository recursoRepo,
                                       ReservaRepository reservaRepo,
                                       CategoriaRepository categoriaRepo) {
        this.service = new CalendarizacionRecursosService(recursoRepo, reservaRepo);
        this.categoriaRepo = categoriaRepo;
        cargarCategorias();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaTexto"));
        fechaPicker.setValue(LocalDate.now());

        categoriaCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(CategoriaRecurso categoria) {
                return categoria == null ? "" : categoria.getDescripcion();
            }
            @Override
            public CategoriaRecurso fromString(String string) {
                return null;
            }
        });
    }

    private void cargarCategorias() {
        List<CategoriaRecurso> categorias = categoriaRepo.listarTodas();
        categoriaCombo.getItems().setAll(categorias);
    }

    @FXML
    private void onCargar() {
        lblMensaje.setText("");
        LocalDate fecha = fechaPicker.getValue();
        CategoriaRecurso categoria = categoriaCombo.getValue();

        if (fecha == null) {
            lblMensaje.setText("Debe seleccionar una fecha.");
            return;
        }
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
                tablaMatriz.getColumns().setAll(colHora);
                tablaMatriz.getItems().clear();
                btnImprimir.setDisable(true);
                return;
            }

            construirColumnasDinamicas(resultado.getRecursos());
            tablaMatriz.getItems().setAll(resultado.getFilas());
            btnImprimir.setDisable(false);

        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        }
    }

    private void construirColumnasDinamicas(List<Recurso> recursos) {
        tablaMatriz.getColumns().setAll(colHora);

        for (Recurso recurso : recursos) {
            TableColumn<FilaCalendarizacion, String> columna = new TableColumn<>(recurso.getDescripcion());
            columna.setCellValueFactory(datos ->
                    new SimpleStringProperty(datos.getValue().getValor(recurso.getId())));

            columna.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String texto, boolean vacio) {
                    super.updateItem(texto, vacio);
                    if (vacio || texto == null || texto.isBlank()) {
                        setText("");
                        setStyle("");
                    } else {
                        setText(texto);
                        setStyle("-fx-background-color: #fff5b4;");
                    }
                }
            });

            tablaMatriz.getColumns().add(columna);
        }
    }

    @FXML
    private void onImprimir() {
        if (ultimoResultado == null || ultimoResultado.estaVacio()) {
            lblMensaje.setText("Primero debe cargar una calendarización con datos.");
            return;
        }

        try {
            ReportTable tabla = new ReportTable(construirEncabezados(ultimoResultado.getRecursos()));
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

            String carpetaDescargas = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            String ruta = carpetaDescargas + "/calendarizacion_" + ultimaFecha + ".pdf";

            pdfReportService.generarReporteMatriz(ruta, header, tabla);
            lblMensaje.setStyle("-fx-text-fill: #007a2f;");
            lblMensaje.setText("Reporte generado en: " + ruta);

        } catch (ReportException e) {
            lblMensaje.setStyle("-fx-text-fill: #b40000;");
            lblMensaje.setText("Error al generar el PDF: " + e.getMessage());
        }
    }

    private List<String> construirEncabezados(List<Recurso> recursos) {
        List<String> encabezados = new ArrayList<>();
        encabezados.add("Hora");
        for (Recurso recurso : recursos) {
            encabezados.add(recurso.getDescripcion());
        }
        return encabezados;
    }
}
*/