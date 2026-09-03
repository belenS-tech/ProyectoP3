package service.login;

import model.login.Rol;
import model.login.Usuario;

public class SessionManager {

    private static SessionManager instancia;
    private Usuario usuarioActual;

    private SessionManager() {
    }

    public static SessionManager getInstancia() {
        if (instancia == null) {
            instancia = new SessionManager();
        }
        return instancia;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public boolean esAdministrador() {
        return haySesionActiva() && usuarioActual.getRol() == Rol.ADMINISTRADOR;
    }
}