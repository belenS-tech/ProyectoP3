package model.categorias;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "categoria")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriaRecurso {

    private String id;
    private String descripcion;

    public CategoriaRecurso() {
    }

    public CategoriaRecurso(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEmpty() {
        return (id == null || id.isBlank())
                || (descripcion == null || descripcion.isBlank());
    }

    // Usa el texto como id, no genera el formato CAT-000001
    /*public static CategoriaRecurso valueOf(String categoria) {
        return new CategoriaRecurso(categoria, categoria);
        return (id == null || id.isBlank()) && (descripcion == null || descripcion.isBlank());
    }*/

   public static CategoriaRecurso valueOf(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return null;
        }
        return new CategoriaRecurso(categoria.trim(), categoria.trim());
    }



    @Override
    public String toString() {
        return descripcion;
    }
}