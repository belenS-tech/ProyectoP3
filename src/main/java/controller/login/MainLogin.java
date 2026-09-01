package controller.login;

import service.login.AuthService;
import service.login.UsuarioRepository;
import service.login.UsuarioXmlRepository;

import javax.swing.*;

/**
 Clase de arranque para probar el módulo de login (Juan).
 */

public class MainLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioRepository usuarioRepository = new UsuarioXmlRepository();
            AuthService authService = new AuthService(usuarioRepository);

            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, authService);

            loginView.setVisible(true);
        });
    }
}