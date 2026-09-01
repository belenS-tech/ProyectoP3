package controller;
/*
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;

import report.PdfReportService;
import report.ReportException;
import report.ReportHeader;
import report.ReportTable;
import repository.ReservaRepository;
import service.EstadisticasActividadesService;
import util.SessionManager;

import javax.swing.filechooser.FileSystemView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EstadisticasActividadesController implements Initializable {

    @FXML private DatePicker fechaDesdePicker;
    @FXML private DatePicker fechaHastaPicker;
    @FXML private Button btnCalcular;
    @FXML private Button btnImprimir;
    @FXML private Label lblMensaje;
    @FXML private TableView<FilaEstadistica> tablaEstadisticas;
    @FXML private TableColumn<FilaEstadistica, String> colSemana;
    @FXML private TableColumn<FilaEstadistica, Number> colCantidad;
    @FXML private BarChart<String, Number> graficoBarras;
    @FXML private CategoryAxis ejeX;
    @FXML private NumberAxis ejeY;

    private EstadisticasActividadesService service;
    private final PdfReportService pdfReportService = new PdfReportService();

    private LinkedHashMap<String, Integer> ultimoResultado;
    private LocalDate ultimoDesde;
    private LocalDate ultimoHasta;

    /** Inyección de dependencias reales. Llamar justo después de cargar el FXML.
    public void configurarDependencias(ReservaRepository reservaRepo) {
        this.service = new EstadisticasActividadesService(reservaRepo);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSemana.setCellValueFactory(new PropertyValueFactory<>("semana"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        graficoBarras.setLegendVisible(false);
        graficoBarras.setAnimated(false);
    }

    @FXML
    private void onCalcular() {
        lblMensaje.setText("");
        LocalDate desde = fechaDesdePicker.getValue();
        LocalDate hasta = fechaHastaPicker.getValue();

        try {
            LinkedHashMap<String, Integer> resultado = service.calcular(desde, hasta);
            this.ultimoResultado = resultado;
            this.ultimoDesde = desde;
            this.ultimoHasta = hasta;

            List<FilaEstadistica> filas = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : resultado.entrySet()) {
                filas.add(new FilaEstadistica(entry.getKey(), entry.getValue()));
            }
            tablaEstadisticas.setItems(FXCollections.observableArrayList(filas));

            actualizarGrafico(resultado);

            if (resultado.isEmpty()) {
                lblMensaje.setText("No se encontraron actividades en el período seleccionado.");
            }
            btnImprimir.setDisable(false);

        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
            btnImprimir.setDisable(true);
        }
    }

    private void actualizarGrafico(LinkedHashMap<String, Integer> resultado) {
        graficoBarras.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Actividades");
        for (Map.Entry<String, Integer> entry : resultado.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        graficoBarras.getData().add(serie);
    }

    @FXML
    private void onImprimir() {
        if (ultimoResultado == null) {
            lblMensaje.setText("Primero debe calcular las estadísticas.");
            return;
        }

        try {
            ReportTable tabla = new ReportTable(List.of("Semana", "Cantidad de actividades"));
            for (Map.Entry<String, Integer> entry : ultimoResultado.entrySet()) {
                tabla.agregarFila(entry.getKey(), String.valueOf(entry.getValue()));
            }

            String filtros = "Desde: " + ultimoDesde + " | Hasta: " + ultimoHasta;
            ReportHeader header = new ReportHeader(
                    "Estadísticas de Actividades",
                    SessionManager.getInstancia().getUsuarioActual().getId(),
                    filtros
            );

            byte[] imagenGrafico = capturarGraficoComoPng();

            String carpetaDescargas = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            String ruta = carpetaDescargas + "/estadisticas_actividades_" + ultimoDesde + "_" + ultimoHasta + ".pdf";

            pdfReportService.generarReporteConGrafico(ruta, header, tabla, imagenGrafico);
            lblMensaje.setStyle("-fx-text-fill: #007a2f;");
            lblMensaje.setText("Reporte generado en: " + ruta);

        } catch (ReportException e) {
            lblMensaje.setStyle("-fx-text-fill: #b40000;");
            lblMensaje.setText("Error al generar el PDF: " + e.getMessage());
        }
    }

    private byte[] capturarGraficoComoPng() throws ReportException {
        WritableImage imagenFx = graficoBarras.snapshot(new SnapshotParameters(), null);
        return PdfReportService.convertirImagenJavaFxAPng(imagenFx);
    }

    /** Fila auxiliar solo para mostrar la tabla en pantalla.
    public static class FilaEstadistica {
        private final String semana;
        private final int cantidad;

        public FilaEstadistica(String semana, int cantidad) {
            this.semana = semana;
            this.cantidad = cantidad;
        }

        public String getSemana() { return semana; }
        public int getCantidad() { return cantidad; }
    }
}

*/