package controller;

import model.EstadoReserva;
import model.Reserva;
import repository.ReservaXmlRepository;
import service.ReservaService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaController extends JPanel {

    private final ReservaService reservaService;

    private JTextField txtId;
    private JTextField txtActividad;
    private JTextField txtFecha;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JTextField txtFuncionarioId;
    private JComboBox<EstadoReserva> cmbEstado;
    private JButton btnAgregar;
    private JButton btnConsultar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public ReservaController() {
        this.reservaService = new ReservaService(new ReservaXmlRepository());
        construirPantalla();
        cargarTabla();
        registrarEventos();
    }

    private void construirPantalla() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gestión de Reservas");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(construirFormulario(), BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(construirTabla(), BorderLayout.CENTER);
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Reserva"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Actividad:"), gbc);
        txtActividad = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtActividad, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        txtFecha = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Hora inicio (HH:mm):"), gbc);
        txtHoraInicio = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(txtHoraInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Hora fin (HH:mm):"), gbc);
        txtHoraFin = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(txtHoraFin, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Funcionario ID:"), gbc);
        txtFuncionarioId = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(txtFuncionarioId, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Estado:"), gbc);
        cmbEstado = new JComboBox<>(EstadoReserva.values());
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(cmbEstado, gbc);

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

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(botones, gbc);

        return panel;
    }

    private JScrollPane construirTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Actividad", "Fecha", "Inicio", "Fin", "Funcionario", "Estado"}, 0) {
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
        btnAgregar.addActionListener(e -> agregarReserva());
        btnConsultar.addActionListener(e -> consultarReserva());
        btnModificar.addActionListener(e -> modificarReserva());
        btnEliminar.addActionListener(e -> eliminarReserva());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        tabla.getSelectionModel().addListSelectionListener(this::alSeleccionarFila);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Reserva> reservas = reservaService.listarTodos();
        for (Reserva reserva : reservas) {
            modeloTabla.addRow(new Object[]{
                    reserva.getId(),
                    reserva.getActividad(),
                    reserva.getFecha(),
                    reserva.getHoraInicio(),
                    reserva.getHoraFin(),
                    reserva.getFuncionarioId(),
                    reserva.getEstado()
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
        txtActividad.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtHoraInicio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtHoraFin.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        txtFuncionarioId.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
        cmbEstado.setSelectedItem(EstadoReserva.valueOf(String.valueOf(modeloTabla.getValueAt(fila, 6))));
    }

    private Reserva construirReserva() {
        return new Reserva(
                txtId.getText(),
                txtActividad.getText(),
                LocalDate.parse(txtFecha.getText()),
                LocalTime.parse(txtHoraInicio.getText()),
                LocalTime.parse(txtHoraFin.getText()),
                txtFuncionarioId.getText(),
                (EstadoReserva) cmbEstado.getSelectedItem()
        );
    }

    private void agregarReserva() {
        try {
            reservaService.registrar(construirReserva());
            cargarTabla();
            limpiarCampos();
            mostrarMensaje("Reserva registrada correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void consultarReserva() {
        try {
            Reserva reserva = reservaService.buscarPorId(txtId.getText());
            if (reserva == null) {
                mostrarMensaje("No se encontró la reserva.");
                return;
            }
            mostrarEnFormulario(reserva);
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void modificarReserva() {
        try {
            reservaService.actualizar(construirReserva());
            cargarTabla();
            mostrarMensaje("Reserva modificada correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarReserva() {
        try {
            reservaService.eliminar(construirReserva());
            cargarTabla();
            limpiarCampos();
            mostrarMensaje("Reserva eliminada correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarEnFormulario(Reserva reserva) {
        txtId.setText(reserva.getId());
        txtActividad.setText(reserva.getActividad());
        txtFecha.setText(reserva.getFecha().toString());
        txtHoraInicio.setText(reserva.getHoraInicio().toString());
        txtHoraFin.setText(reserva.getHoraFin().toString());
        txtFuncionarioId.setText(reserva.getFuncionarioId());
        cmbEstado.setSelectedItem(reserva.getEstado());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtActividad.setText("");
        txtFecha.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        txtFuncionarioId.setText("");
        cmbEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
