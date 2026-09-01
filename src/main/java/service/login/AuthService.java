package service.login;

import model.login.Usuario;

public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario iniciarSesion(String id, String clave) {
        if (id == null || id.isBlank() || clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("El ID y la clave no pueden estar vacíos.");
        }

        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("No existe un usuario con ese ID.");
        }

        if (!usuario.getClave().equals(clave)) {
            throw new IllegalArgumentException("La clave es incorrecta.");
        }

        SessionManager.getInstancia().iniciarSesion(usuario);
        return usuario;
    }

    public void cambiarClave(String claveActual, String claveNueva, String confirmacion) {
        Usuario usuario = SessionManager.getInstancia().getUsuarioActual();
        if (usuario == null) {
            throw new IllegalStateException("No hay una sesión activa.");
        }

        if (!usuario.getClave().equals(claveActual)) {
            throw new IllegalArgumentException("La clave actual no coincide.");
        }

        if (claveNueva == null || claveNueva.isBlank()) {
            throw new IllegalArgumentException("La clave nueva no puede estar vacía.");
        }

        if (!claveNueva.equals(confirmacion)) {
            throw new IllegalArgumentException("Las claves nuevas no coinciden.");
        }

        usuario.setClave(claveNueva);
        usuarioRepository.guardar(usuario);
    }
}
