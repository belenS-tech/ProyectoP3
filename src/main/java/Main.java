import javax.swing.*;
import controller.login.LoginController;
import controller.login.LoginView;
import service.login.AuthService;
import service.login.UsuarioRepository;
import service.login.UsuarioXmlRepository;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioRepository usuarioRepository = new UsuarioXmlRepository();
            AuthService authService = new AuthService(usuarioRepository);
            LoginView loginView = new LoginView();
            new LoginController(loginView, authService, usuarioRepository);
            loginView.setVisible(true);
        });
    }
}
