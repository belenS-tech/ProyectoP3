package controller.login;

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

    private void abrirVentanaPrincipal(Usuario usuario) {
        javax.swing.JOptionPane.showMessageDialog(null,
                "Bienvenido, " + usuario.getId() + " (" + usuario.getRol() + ")");
    }
}