package view;

import model.Recurso;
import model.Reserva;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import repository.RecursoRepository;
import repository.RecursoXmlRepository;
import repository.ReservaRepository;
import report.PdfReportService;
import report.ReportException;
import report.ReportHeader;
import report.ReportTable;
import service.SessionManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasRecursosPanel extends JPanel {

    private final JButton btnCalcular;
    private final JButton btnImprimir;
    private final JLabel lblMensaje;
    private final JTable tablaEstadisticas;
    private final DefaultTableModel modeloTabla;
    private final DefaultCategoryDataset dataset;
    private final JFreeChart grafico;
    private final ChartPanel panelGrafico;

    private ReservaRepository reservaRepo;
    private final RecursoRepository recursoRepo = new RecursoXmlRepository();
    private final PdfReportService pdfReportService = new PdfReportService();

    private LinkedHashMap<String, Integer> ultimoResultado;

    public EstadisticasRecursosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Estadísticas de Recursos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnCalcular = new JButton("Calcular");
        btnImprimir = new JButton("Imprimir");
        btnImprimir.setEnabled(false);

        URL urlImprimir = getClass().getResource("/icons/imprimir.png");
        if (urlImprimir != null) {
            ImageIcon original = new ImageIcon(urlImprimir);
            Image img = original.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnImprimir.setIcon(new ImageIcon(img));
        }

        URL urlCalcular = getClass().getResource("/icons/cargar.png");
        if (urlCalcular != null) {
            ImageIcon original = new ImageIcon(urlCalcular);
            Image img = original.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnCalcular.setIcon(new ImageIcon(img));
        }

        panelBotones.add(btnCalcular);
        panelBotones.add(btnImprimir);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);

        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(180, 0, 0));

        modeloTabla = new DefaultTableModel(new Object[]{"Recurso", "Reservas"}, 0);
        tablaEstadisticas = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEstadisticas);
        scrollTabla.setPreferredSize(new Dimension(350, 160));

        dataset = new DefaultCategoryDataset();
        grafico = ChartFactory.createBarChart(
                "Reservas por recurso", "Recurso", "Cantidad",
                dataset, PlotOrientation.VERTICAL, false, true, false);
        org.jfree.chart.plot.CategoryPlot plot = grafico.getCategoryPlot();
        ((org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer())
                .setSeriesPaint(0, new Color(109, 40, 217));
        panelGrafico = new ChartPanel(grafico);
        panelGrafico.setPreferredSize(new Dimension(500, 320));

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.add(scrollTabla, BorderLayout.WEST);
        panelCentro.add(panelGrafico, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(lblMensaje, BorderLayout.SOUTH);
        add(panelCentro, BorderLayout.CENTER);

        btnCalcular.addActionListener(e -> onCalcular());
        btnImprimir.addActionListener(e -> onImprimir());
        util.Tema.aplicar(this);
    }

    public void configurarDependencias(ReservaRepository reservaRepo) {
        this.reservaRepo = reservaRepo;
    }

    private void onCalcular() {
        if (reservaRepo == null) {
            lblMensaje.setText("El servicio no está configurado.");
            return;
        }

        List<Recurso> recursos = recursoRepo.listarTodos();
        List<Reserva> reservas = reservaRepo.listar();

        LinkedHashMap<String, Integer> resultado = new LinkedHashMap<>();
        for (Recurso recurso : recursos) {
            long count = reservas.stream()
                    .flatMap(r -> r.getDetalles().stream())
                    .filter(d -> d.getRecurso() != null && d.getRecurso().getId().equals(recurso.getId()))
                    .count();
            resultado.put(recurso.getDescripcion(), (int) count);
        }

        this.ultimoResultado = resultado;
        modeloTabla.setRowCount(0);
        dataset.clear();

        for (Map.Entry<String, Integer> entry : resultado.entrySet()) {
            modeloTabla.addRow(new Object[]{entry.getKey(), entry.getValue()});
            dataset.addValue(entry.getValue(), "Reservas", entry.getKey());
        }

        if (resultado.isEmpty()) {
            lblMensaje.setText("No hay recursos registrados.");
        } else {
            lblMensaje.setText(" ");
        }
        btnImprimir.setEnabled(true);
    }

    private void onImprimir() {
        if (ultimoResultado == null) {
            lblMensaje.setText("Primero debe calcular las estadísticas.");
            return;
        }
        try {
            ReportTable tabla = new ReportTable(java.util.List.of("Recurso", "Cantidad de reservas"));
            for (Map.Entry<String, Integer> entry : ultimoResultado.entrySet()) {
                tabla.agregarFila(entry.getKey(), String.valueOf(entry.getValue()));
            }

            ReportHeader header = new ReportHeader(
                    "Estadísticas de Recursos",
                    SessionManager.getInstancia().getUsuarioActual().getId(),
                    null
            );

            byte[] imagenGrafico = capturarGraficoComoPng();
            String ruta = elegirRutaGuardado();
            if (ruta == null) {
                return;
            }

            pdfReportService.reporteGrafico(ruta, header, tabla, imagenGrafico);
            lblMensaje.setForeground(new Color(0, 122, 47));
            lblMensaje.setText("Reporte generado en: " + ruta);

        } catch (ReportException e) {
            lblMensaje.setForeground(new Color(180, 0, 0));
            lblMensaje.setText("Error al generar el PDF: " + e.getMessage());
        } catch (Exception e) {
            lblMensaje.setForeground(new Color(180, 0, 0));
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    private byte[] capturarGraficoComoPng() throws Exception {
        BufferedImage imagen = grafico.createBufferedImage(480, 320);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(imagen, "png", buffer);
        return buffer.toByteArray();
    }
    /** Abre un diálogo "Guardar como" y devuelve la ruta elegida, o null si el usuario cancela. */
    private String elegirRutaGuardado() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar reporte PDF");
        selector.setSelectedFile(new java.io.File("estadisticas_recursos.pdf"));
        selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));

        int resultado = selector.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        java.io.File archivo = selector.getSelectedFile();
        String ruta = archivo.getAbsolutePath();
        if (!ruta.toLowerCase().endsWith(".pdf")) {
            ruta += ".pdf";
        }
        return ruta;
    }
}