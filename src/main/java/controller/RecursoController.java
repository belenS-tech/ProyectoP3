package controller;

import model.Recurso;
import model.categorias.CategoriaRecurso;
import repository.RecursoXmlRepository;
import service.RecursoService;
import service.categorias.CategoriaService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecursoController extends JPanel {

    private final CategoriaService categoriaService;
    private final RecursoService recursoService;

    private JTextField txtId;
    private JComboBox<CategoriaRecurso> cmbCategoria;
    private JTextField txtDescripcion;
    private JButton btnAgregar;
    private JButton btnConsultar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public RecursoController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
        this.recursoService = new RecursoService(new RecursoXmlRepository());
        construirPantalla();
        cargarCategorias();
        cargarTabla();
        registrarEventos();
    }

    private void construirPantalla() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gestión de Recursos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(construirFormulario(), BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(construirTabla(), BorderLayout.CENTER);
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recurso"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Categoría:"), gbc);
        cmbCategoria = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(cmbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Descripción:"), gbc);
        txtDescripcion = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtDescripcion, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnAgregar = new JButton("Agregar");
        btnConsultar = new JButton("Consultar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        botones.add(btnAgregar);
        botones.add(btnConsultar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(botones, gbc);

        return panel;
    }

    private JScrollPane construirTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Categoría", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado"));
        return scroll;
    }

    private void registrarEventos() {
        btnAgregar.addActionListener(e -> agregarRecurso());
        btnConsultar.addActionListener(e -> consultarRecurso());
        btnModificar.addActionListener(e -> modificarRecurso());
        btnEliminar.addActionListener(e -> eliminarRecurso());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        tabla.getSelectionModel().addListSelectionListener(this::alSeleccionarFila);
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();
        for (CategoriaRecurso categoria : categoriaService.listarTodos()) {
            cmbCategoria.addItem(categoria);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Recurso> recursos = recursoService.listarTodos();
        for (Recurso recurso : recursos) {
            modeloTabla.addRow(new Object[]{
                    recurso.getId(),
                    recurso.getCategoria() != null ? recurso.getCategoria().getId() : "",
                    recurso.getDescripcion()
            });
        }
    }

    private void alSeleccionarFila(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        seleccionarCategoria(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtDescripcion.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
    }

    private void seleccionarCategoria(String idCategoria) {
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            CategoriaRecurso categoria = cmbCategoria.getItemAt(i);
            if (categoria != null && idCategoria.equals(categoria.getId())) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private Recurso construirRecurso() {
        return new Recurso(txtId.getText(), (CategoriaRecurso) cmbCategoria.getSelectedItem(), txtDescripcion.getText());
    }

    private void agregarRecurso() {
        try {
            recursoService.registrar(construirRecurso());
            cargarTabla();
            limpiarCampos();
            mostrarMensaje("Recurso registrado correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void consultarRecurso() {
        try {
            Recurso recurso = recursoService.buscarPorId(txtId.getText());
            if (recurso == null) {
                mostrarMensaje("No se encontró el recurso.");
                return;
            }
            mostrarEnFormulario(recurso);
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void modificarRecurso() {
        try {
            recursoService.actualizar(construirRecurso());
            cargarTabla();
            mostrarMensaje("Recurso modificado correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarRecurso() {
        try {
            recursoService.eliminar(construirRecurso());
            cargarTabla();
            limpiarCampos();
            mostrarMensaje("Recurso eliminado correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarEnFormulario(Recurso recurso) {
        txtId.setText(recurso.getId());
        txtDescripcion.setText(recurso.getDescripcion());
        seleccionarCategoria(recurso.getCategoria() != null ? recurso.getCategoria().getId() : "");
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtDescripcion.setText("");
        cmbCategoria.setSelectedIndex(-1);
        tabla.clearSelection();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
