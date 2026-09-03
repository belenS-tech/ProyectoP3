package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Recurso;
import repository.CategoriaRecurso;
import repository.RecursoRepository;
import repository.RecursoXmlRepository;
import service.RecursoService;

public class RecursoController {

    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private TextField txtDescripcion;

    private final RecursoService recursoService;

    public RecursoController() {
        RecursoRepository repository = new RecursoXmlRepository();
        recursoService = new RecursoService(repository);
    }

    @FXML
    private void agregarRecurso() {

        try {

            String id = txtId.getText();
            String categoria = cmbCategoria.getValue();
            String descripcion = txtDescripcion.getText();

            Recurso recurso = new Recurso(id, CategoriaRecurso.valueOf(categoria), descripcion);

            recursoService.registrar(recurso);


            System.out.println("Recurso registrado correctamente.");

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void consultarRecurso() {
        try {
            recursoService.consultar(new Recurso(txtId.getText(), CategoriaRecurso.valueOf(cmbCategoria.getValue()), txtDescripcion.getText()));
            System.out.println("Recurso consultado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al consultar los recursos: " + e.getMessage());
        }
    }

    @FXML
    private void modificarRecurso() {
        try {
            recursoService.actualizar(new Recurso(txtId.getText(), CategoriaRecurso.valueOf(cmbCategoria.getValue()), txtDescripcion.getText()));
            System.out.println("Recurso modificado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al modificar los recursos: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarRecurso() {
        try {
            recursoService.eliminar(new Recurso(txtId.getText(), CategoriaRecurso.valueOf(cmbCategoria.getValue()), txtDescripcion.getText()));
            System.out.println("Recurso eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar los recursos: " + e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtDescripcion.clear();
    }
}

