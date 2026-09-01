package controller.principal;

import controller.login.CambiarClaveController;
import controller.login.CambiarClaveView;
import controller.login.LoginController;
import controller.login.LoginView;
import service.login.AuthService;
import service.login.SessionManager;

/**
 * Controlador de la ventana principal (NO es un punto de entrada, no tiene main()).
 * Maneja las acciones de sesión: cambiar clave, cerrar sesión y salir.
 *JUAN
 */
public class MainController {

    private final MainView vista;
    private final AuthService authService;

    public MainController(MainView vista, AuthService authService) {
        this.vista = vista;
        this.authService = authService;
        registrarEventos();
    }

    private void registrarEventos() {
        vista.getItemCambiarClave().addActionListener(e -> abrirCambiarClave());
        vista.getItemCerrarSesion().addActionListener(e -> cerrarSesion());
        vista.getItemSalir().addActionListener(e -> salir());
    }

    private void abrirCambiarClave() {
        CambiarClaveView vistaClave = new CambiarClaveView();
        new CambiarClaveController(vistaClave, authService);
        vistaClave.setVisible(true);
    }

    private void cerrarSesion() {
        if (!vista.confirmar("¿Desea cerrar la sesión?")) {
            return;
        }

        SessionManager.getInstancia().cerrarSesion();
        vista.dispose();

        LoginView loginView = new LoginView();
        new LoginController(loginView, authService);
        loginView.setVisible(true);
    }

    private void salir() {
        if (vista.confirmar("¿Está seguro que desea salir del sistema?")) {
            System.exit(0);
        }
    }
}