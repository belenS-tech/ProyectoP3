package controller.login;

import service.login.AuthService;
import service.login.UsuarioRepository;
import service.login.UsuarioXmlRepository;

import javax.swing.*;

/**
 * Clase de arranque para probar el módulo de login (Integrante 1 - Juan).
 * No es el punto de entrada oficial del proyecto; el arranque definitivo
 * se define en integración con el equipo.
 */
public class MainLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioRepository usuarioRepository = new UsuarioXmlRepository();
            AuthService authService = new AuthService(usuarioRepository);

            LoginView loginView = new LoginView();
            LoginController loginController =
                    new LoginController(loginView, authService, usuarioRepository);

            loginView.setVisible(true);
        });
    }
}