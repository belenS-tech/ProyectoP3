package controller;
/*
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import repository.ReservaRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EstadisticasController implements Initializable {

    @FXML private TabPane tabPane;
    @FXML private Tab tabEstadisticasRecursos;
    @FXML private Tab tabEstadisticasActividades;

    private EstadisticasActividadesController controladorActividades;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getStyleClass().add("estadisticas-tab-pane");
    }

    /** Llamar después de cargar este FXML, con las dependencias reales.
    public void configurarDependencias(ReservaRepository reservaRepo,
                                       Parent contenidoEstadisticasRecursos) throws IOException {

        FXMLLoader loaderActividades = new FXMLLoader(
                getClass().getResource("/view/EstadisticasActividadesView.fxml"));
        Parent vistaActividades = loaderActividades.load();
        controladorActividades = loaderActividades.getController();
        controladorActividades.configurarDependencias(reservaRepo);
        tabEstadisticasActividades.setContent(vistaActividades);

        tabEstadisticasRecursos.setContent(contenidoEstadisticasRecursos);
    }
}
*/