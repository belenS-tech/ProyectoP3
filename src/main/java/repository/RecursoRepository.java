package repository;

import model.Recurso;

import java.util.List;

public interface RecursoRepository {
    void guardar(Recurso recurso);
    List<Recurso> listar();
    Recurso buscarPorId(String id);
    void actualizar(Recurso recurso);
    void eliminar(Recurso recurso);
}
/*
import model.Recurso;
import java.util.List;

/**
 * Contrato del repositorio de recursos. La implementación real con
 * XML la construye el Integrante 2 (RecursoXmlRepository). Mientras
 * tanto, el Integrante 3 puede trabajar contra una implementación
 * falsa (fake) que cumpla esta misma interfaz.

public interface RecursoRepository {
    List<Recurso> buscarPorCategoria(String categoriaId);
    List<Recurso> listarTodos();
}
*/
