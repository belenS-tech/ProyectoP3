package controller.login;

import controller.principal.MainController;
import controller.principal.MainView;
import model.login.Usuario;
import service.login.AuthService;

public class LoginController {

    private final LoginView vista;
    private final AuthService authService;

    public LoginController(LoginView vista, AuthService authService) {
        this.vista = vista;
        this.authService = authService;
        registrarEventos();
    }

    private void registrarEventos() {
        vista.getBotonIngresar().addActionListener(e -> intentarIngresar());
        vista.getBotonCancelar().addActionListener(e -> System.exit(0));
        vista.getBotonCambiar().addActionListener(e -> abrirCambiarClave());
    }

    private void intentarIngresar() {
        try {
            String id = vista.getIdIngresado();
            String clave = vista.getClaveIngresada();

            Usuario usuario = authService.iniciarSesion(id, clave);

            vista.dispose();
            abrirVentanaPrincipal(usuario);

        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void abrirCambiarClave() {
        String id = vista.getIdIngresado();

        try {
            authService.verificarExiste(id);
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
            return;
        }

        CambiarClaveView vistaClave = new CambiarClaveView(id);
        new CambiarClaveController(vistaClave, authService, id);
        vistaClave.setVisible(true);

        vista.limpiarCampos();
    }

    private void abrirVentanaPrincipal(Usuario usuario) {
        MainView mainView = new MainView(usuario);
        new MainController(mainView, authService);
        mainView.setVisible(true);
    }
}