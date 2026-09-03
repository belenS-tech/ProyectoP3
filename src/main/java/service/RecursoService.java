package service;


import model.Recurso;
import repository.RecursoRepository;

public class RecursoService {
    private final RecursoRepository repository;

    public RecursoService(RecursoRepository repository) {
        this.repository = repository;
    }

    public void verificarId(Recurso recurso) {
        if(recurso.getId() == null || recurso.getId().isEmpty()) {
            throw new IllegalArgumentException("El ID del recurso no puede ser nulo o vacío.");
        }

        if(repository.buscarPorId(Integer.parseInt(recurso.getId())) != null) {
            throw new IllegalArgumentException("El ID del recurso ya existe.");
        }
    }

    public void verificarCategoria(Recurso recurso) {
        if(recurso.getCategoria() == null || recurso.getCategoria().isEmpty()) {
            throw new IllegalArgumentException("La categoría del recurso no puede ser nula o vacía.");
        }
    }

    public void verificarDescripcion(Recurso recurso) {
        if(recurso.getDescripcion() == null || recurso.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La descripción del recurso no puede ser nula o vacía.");
        }
    }

    public void consultar(Recurso recurso) {
        verificarId(recurso);
        verificarCategoria(recurso);
        verificarDescripcion(recurso);
    }

    public void registrar(Recurso recurso) {
        consultar(recurso);
        repository.guardar(recurso);
    }

    public void actualizar(Recurso recurso) {
        consultar(recurso);
        repository.actualizar(recurso);
    }

    public void eliminar(Recurso recurso) {
        consultar(recurso);
        repository.eliminar(recurso);
    }
}
