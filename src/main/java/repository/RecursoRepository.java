package repository;

import model.Recurso;

import java.util.List;

public interface RecursoRepository {
    void guardar(Recurso recurso);
    List<Recurso> listar();
    Recurso buscarPorId(int id);
    void actualizar(Recurso recurso);
    void eliminar(Recurso recurso);
}
