package service.categorias;

import model.categorias.CategoriaRecurso;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaService {

    private static final String PREFIJO = "CAT-";
    private static final int LARGO_NUMERO = 6;

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaRecurso> listarTodos() {
        return categoriaRepository.listarTodos();
    }

    /** Busca por coincidencia parcial en la descripción. */
    public List<CategoriaRecurso> buscar(String descripcion) {
        return categoriaRepository.listarTodos().stream()
                .filter(c -> descripcion == null || descripcion.isBlank()
                        || c.getDescripcion().toLowerCase()
                        .contains(descripcion.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    public CategoriaRecurso buscarPorId(String id) {
        return categoriaRepository.buscarPorId(id);
    }

    /** Registra una categoría nueva con ID autogenerado. */
    public void incluir(String descripcion) {
        validarDescripcion(descripcion);

        if (existeDescripcion(descripcion, null)) {
            throw new IllegalArgumentException("Ya existe una categoría con esa descripción.");
        }

        CategoriaRecurso categoria = new CategoriaRecurso(generarId(), descripcion.trim());
        categoriaRepository.guardar(categoria);
    }

    /** Modifica la descripción. El ID no se puede cambiar. */
    public void modificar(String id, String descripcion) {
        validarDescripcion(descripcion);

        CategoriaRecurso categoria = categoriaRepository.buscarPorId(id);
        if (categoria == null) {
            throw new IllegalArgumentException("No existe una categoría con ese ID.");
        }

        if (existeDescripcion(descripcion, id)) {
            throw new IllegalArgumentException("Ya existe otra categoría con esa descripción.");
        }

        categoria.setDescripcion(descripcion.trim());
        categoriaRepository.guardar(categoria);
    }

    public void eliminar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        if (categoriaRepository.buscarPorId(id) == null) {
            throw new IllegalArgumentException("No existe una categoría con ese ID.");
        }

        // Pendiente: impedir la eliminación si la categoría tiene recursos
        // asociados (depende del módulo de recursos, Integrante 2).

        categoriaRepository.eliminar(id);
    }

    /** Genera el siguiente ID consecutivo con formato CAT-000001 */
    private String generarId() {
        int mayor = 0;
        for (CategoriaRecurso c : categoriaRepository.listarTodos()) {
            String id = c.getId();
            if (id != null && id.startsWith(PREFIJO)) {
                try {
                    int numero = Integer.parseInt(id.substring(PREFIJO.length()));
                    if (numero > mayor) {
                        mayor = numero;
                    }
                } catch (NumberFormatException ignorado) {
                    // Un ID con formato distinto no afecta la numeración
                }
            }
        }
        return PREFIJO + String.format("%0" + LARGO_NUMERO + "d", mayor + 1);
    }

    /** Verifica descripciones repetidas, ignorando la categoría que se está modificando */
    private boolean existeDescripcion(String descripcion, String idExcluido) {
        return categoriaRepository.listarTodos().stream()
                .filter(c -> idExcluido == null || !c.getId().equals(idExcluido))
                .anyMatch(c -> c.getDescripcion().equalsIgnoreCase(descripcion.trim()));
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
    }
}