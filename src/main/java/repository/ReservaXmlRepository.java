package repository;

import model.DetalleReserva;
import model.EstadoReserva;
import model.Recurso;
import model.Reserva;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservaXmlRepository implements ReservaRepository {

    private final String archivo = "src/main/resources/data/reservas.xml";
    private final RecursoRepository recursoRepository;

    public ReservaXmlRepository() {
        this(new RecursoXmlRepository());
    }

    public ReservaXmlRepository(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Override
    public void guardar(Reserva reserva){
        try {
            File file = new File(archivo);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file);

            Element raiz = document.getDocumentElement(); //raiz -> <reservas>

            Element elementoReserva = document.createElement("reserva");

            Element elementoId = document.createElement("id");
            elementoId.setTextContent(String.valueOf(reserva.getId()));
            elementoReserva.appendChild(elementoId);

            Element elementoActividad = document.createElement("actividad");
            elementoActividad.setTextContent(reserva.getActividad());
            elementoReserva.appendChild(elementoActividad);

            Element elementoFecha = document.createElement("fecha");
            elementoFecha.setTextContent(reserva.getFecha().toString());
            elementoReserva.appendChild(elementoFecha);

            Element elementoHoraInicio = document.createElement("horaInicio");
            elementoHoraInicio.setTextContent(reserva.getHoraInicio().toString());
            elementoReserva.appendChild(elementoHoraInicio);

            Element elementoHoraFin = document.createElement("horaFin");
            elementoHoraFin.setTextContent(reserva.getHoraFin().toString());
            elementoReserva.appendChild(elementoHoraFin);

            Element elementoFuncionarioId = document.createElement("funcionarioId");
            elementoFuncionarioId.setTextContent(String.valueOf(reserva.getFuncionarioId()));
            elementoReserva.appendChild(elementoFuncionarioId);

            Element elementoEstado = document.createElement("estado");
            elementoEstado.setTextContent(reserva.getEstado().name());
            elementoReserva.appendChild(elementoEstado);

            Element elementoDetalles = document.createElement("detalles");

            for (DetalleReserva detalle : reserva.getDetalles()) {

                Element elementoDetalle = document.createElement("detalle");

                Element elementoRecursoId = document.createElement("recursoId");
                elementoRecursoId.setTextContent(detalle.getRecurso().getId());

                Element elementoCategoria = document.createElement("categoria");
                elementoCategoria.setTextContent(
                        String.valueOf(detalle.getCategoria().getId())
                );

                elementoDetalle.appendChild(elementoRecursoId);
                elementoDetalle.appendChild(elementoCategoria);

                elementoDetalles.appendChild(elementoDetalle);
            }

            elementoReserva.appendChild(elementoDetalles);

            raiz.appendChild(elementoReserva);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(archivo));
            transformer.transform(source, result);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List <Reserva> listar() {
        List<Reserva> reservas = new ArrayList<>();
        try {
            File file = new File(archivo);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(archivo);

            NodeList listaReservas = document.getElementsByTagName("reserva");
            for (int i = 0; i < listaReservas.getLength(); i++) {
                Element elementoReserva = (Element) listaReservas.item(i);
                String id = elementoReserva.getElementsByTagName("id").item(0).getTextContent();
                String actividad = elementoReserva.getElementsByTagName("actividad").item(0).getTextContent();
                String fechaStr = elementoReserva.getElementsByTagName("fecha").item(0).getTextContent();
                String horaInicioStr = elementoReserva.getElementsByTagName("horaInicio").item(0).getTextContent();
                String horaFinStr = elementoReserva.getElementsByTagName("horaFin").item(0).getTextContent();
                LocalTime horaInicio = LocalTime.parse(horaInicioStr);
                LocalTime horaFin = LocalTime.parse(horaFinStr);
                String funcionarioId = elementoReserva.getElementsByTagName("funcionarioId").item(0).getTextContent();
                String estadoStr = elementoReserva.getElementsByTagName("estado").item(0).getTextContent();

                LocalDate fecha = LocalDate.parse(fechaStr);
                EstadoReserva estado = EstadoReserva.valueOf(estadoStr);

                Reserva reserva = new Reserva(id, actividad, fecha, horaInicio, horaFin, funcionarioId, estado);
                reservas.add(reserva);
                Element elementoDetalles =
                        (Element) elementoReserva.getElementsByTagName("detalles").item(0);

                NodeList listaDetalles =
                        elementoDetalles.getElementsByTagName("detalle");

                for (int j = 0; j < listaDetalles.getLength(); j++) {

                    Element elementoDetalle = (Element) listaDetalles.item(j);

                    String recursoId =
                            elementoDetalle.getElementsByTagName("recursoId")
                                    .item(0)
                                    .getTextContent();

                    String categoriaTexto =
                            elementoDetalle.getElementsByTagName("categoria")
                                    .item(0)
                                    .getTextContent();

                    Recurso recurso = recursoRepository.buscarPorId(recursoId);
                    if(recurso != null) {
                    CategoriaRecurso categoria =
                            CategoriaRecurso.valueOf(categoriaTexto);

                        DetalleReserva detalle =
                                new DetalleReserva(categoria, recurso);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return reservas;
    }


    public Reserva buscarPorId(String id) {
        List<Reserva> reservas = listar();
        for (Reserva reserva : reservas) {
            if (Objects.equals(reserva.getId(), id)) {
                return reserva;
            }
        }
        return null;
    }

    public void actualizar(Reserva reserva) {
        try {
            File file = new File(archivo);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(archivo);

            NodeList listaReservas = document.getElementsByTagName("reserva");
            for (int i = 0; i < listaReservas.getLength(); i++) {
                Element elementoReserva = (Element) listaReservas.item(i);
                String idReserva = elementoReserva.getElementsByTagName("id").item(0).getTextContent();
                if (Objects.equals(idReserva, reserva.getId())) {
                    elementoReserva.getElementsByTagName("actividad").item(0).setTextContent(reserva.getActividad());
                    elementoReserva.getElementsByTagName("fecha").item(0).setTextContent(reserva.getFecha().toString());
                    elementoReserva.getElementsByTagName("horaInicio").item(0).setTextContent(reserva.getHoraInicio().toString());
                    elementoReserva.getElementsByTagName("horaFin").item(0).setTextContent(reserva.getHoraFin().toString());
                    elementoReserva.getElementsByTagName("funcionarioId").item(0).setTextContent(String.valueOf(reserva.getFuncionarioId()));
                    elementoReserva.getElementsByTagName("estado").item(0).setTextContent(reserva.getEstado().name());
                    break;
                }
                NodeList detallesExistentes =
                        elementoReserva.getElementsByTagName("detalles");

                if (detallesExistentes.getLength() > 0) {
                    elementoReserva.removeChild(detallesExistentes.item(0));
                }

                Element elementoDetalles = document.createElement("detalles");

                for (DetalleReserva detalle : reserva.getDetalles()) {

                    Element elementoDetalle = document.createElement("detalle");

                    Element elementoRecursoId = document.createElement("recursoId");
                    elementoRecursoId.setTextContent(detalle.getRecurso().getId());

                    Element elementoCategoria = document.createElement("categoria");
                    elementoCategoria.setTextContent(
                            String.valueOf(detalle.getCategoria().getId())
                    );

                    elementoDetalle.appendChild(elementoRecursoId);
                    elementoDetalle.appendChild(elementoCategoria);

                    elementoDetalles.appendChild(elementoDetalle);
                }

                elementoReserva.appendChild(elementoDetalles);
            }




            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(archivo));
            transformer.transform(source, result);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(Reserva reserva) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(archivo);

            NodeList listaReservas = document.getElementsByTagName("reserva");
                for (int i = 0; i < listaReservas.getLength(); i++) {
                    Element elementoReserva = (Element) listaReservas.item(i);
                    String idReserva = elementoReserva.getElementsByTagName("id").item(0).getTextContent();
                    if (Objects.equals(idReserva, reserva.getId())) {
                        elementoReserva.getParentNode().removeChild(elementoReserva);
                        break;
                    }
                }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(archivo));
            transformer.transform(source, result);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
