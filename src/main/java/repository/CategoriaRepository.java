package repository;

import model.categorias.CategoriaRecurso;
import java.util.List;



public interface CategoriaRepository {
    List<CategoriaRecurso> listarTodas();
}