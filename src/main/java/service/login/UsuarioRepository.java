package service.login;

import model.login.Usuario;

import java.util.List;

public interface UsuarioRepository {
    List<Usuario> listarTodos();
    Usuario buscarPorId(String id);
    void guardar(Usuario usuario);
    void eliminar(String id);
}