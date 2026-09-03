package service.categorias;

import model.categorias.CategoriaRecurso;

import java.util.List;

public interface CategoriaRepository {

    List<CategoriaRecurso> listarTodos();

    CategoriaRecurso buscarPorId(String id);

    void guardar(CategoriaRecurso categoria);

    void eliminar(String id);
}