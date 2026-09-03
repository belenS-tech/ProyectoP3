package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.EstadoReserva;
import model.Reserva;
import repository.RecursoXmlRepository;
import repository.ReservaRepository;
import repository.ReservaXmlRepository;
import service.DisponibilidadService;
import service.ReservaService;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtActividad;

    @FXML
    private TextField txtFecha;

    @FXML
    private TextField txtHoraInicio;

    @FXML
    private TextField txtHoraFin;

    @FXML
    private TextField txtFuncionarioId;

    @FXML
    private ComboBox<String> cmbEstado;

    private final ReservaService reservaService;

    public ReservaController() {
        ReservaRepository reservaRepository = new ReservaXmlRepository();
        DisponibilidadService disponibilidadService =
                new DisponibilidadService(new RecursoXmlRepository(), reservaRepository);
        this.reservaService = new ReservaService(disponibilidadService, reservaRepository);
    }

    @FXML
    private void initialize() {
        cmbEstado.getItems().setAll("ACTIVA", "CANCELADA", "FINALIZADA");
    }

    @FXML
    private void agregarReserva() {
        try {
            Reserva reserva = construirReserva();
            reservaService.registrar(reserva);
            System.out.println("Reserva registrada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar la reserva: " + e.getMessage());
        }
    }

    @FXML
    private void consultarReserva() {
        try {
            Reserva reserva = reservaService.buscarPorId(txtId.getText());
            if (reserva != null) {
                System.out.println("Reserva consultada correctamente.");
            } else {
                System.out.println("No se encontró la reserva.");
            }
        } catch (Exception e) {
            System.out.println("Error al consultar la reserva: " + e.getMessage());
        }
    }

    @FXML
    private void modificarReserva() {
        try {
            Reserva reserva = construirReserva();
            reservaService.actualizar(reserva);
            System.out.println("Reserva modificada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al modificar la reserva: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarReserva() {
        try {
            Reserva reserva = construirReserva();
            reservaService.eliminar(reserva);
            System.out.println("Reserva eliminada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar la reserva: " + e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        txtActividad.clear();
        txtFecha.clear();
        txtHoraInicio.clear();
        txtHoraFin.clear();
        txtFuncionarioId.clear();
        cmbEstado.getSelectionModel().clearSelection();
    }

    private Reserva construirReserva() {
        String id = txtId.getText();
        String actividad = txtActividad.getText();
        LocalDate fecha = LocalDate.parse(txtFecha.getText());
        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
        LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());
        String funcionarioId = txtFuncionarioId.getText();
        EstadoReserva estado = EstadoReserva.valueOf(cmbEstado.getValue());

        return new Reserva(id, actividad, fecha, horaInicio, horaFin, funcionarioId, estado);
    }
}