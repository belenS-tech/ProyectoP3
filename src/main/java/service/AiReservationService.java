package service;

import com.google.genai.Client;
import com.google.genai.gaos.models.interactions.CreateModelInteraction;
import com.google.genai.gaos.models.interactions.Interaction;
import com.google.genai.gaos.models.interactions.InteractionsInput;
import com.google.genai.gaos.models.interactions.Model;
import com.google.genai.gaos.models.operations.CreateInteractionRequestBody;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.AiReservationException;


import java.time.LocalDate;

public class AiReservationService {

    private final Gson gson = new Gson();

    public AiReservationResponse interpretarSolicitud(String solicitud) throws AiReservationException {

        if (solicitud == null || solicitud.trim().isEmpty()) {
            throw new AiReservationException("La solicitud no puede estar vacía.");
        }

        try {

            // 1. Enviar solicitud a la IA
            String respuestaIA = consultarIA(solicitud);

            // 2. Verificar que la IA haya respondido
            if (respuestaIA.trim().isEmpty()) {
                throw new AiReservationException("La IA no devolvió una respuesta.");
            }

            // 3. Convertir respuesta a nuestro objeto
            return convertirRespuesta(respuestaIA);

        } catch (AiReservationException e) {
            throw e;

        } catch (Exception e) {
            throw new AiReservationException("Ocurrió un error al procesar la solicitud.", e);
        }
    }


    private String consultarIA(String solicitud) throws AiReservationException {
        try {
            Client client = new Client();

            CreateModelInteraction peticion = CreateModelInteraction.builder().model(Model.of("gemini-3.7-flash"))
                    .input(InteractionsInput.of(construirPrompt(solicitud))).build();

            Interaction respuesta = client.interactions.create(CreateInteractionRequestBody.of(peticion)).interaction()
                    .get();

            String texto = respuesta.outputText().orElse("");

            if (texto.isEmpty()) {
                throw new AiReservationException("La IA no devolvió ningún resultado.");
            }

            return texto;
        } catch (AiReservationException e){
            throw e;

        } catch (Exception e) {
            throw new AiReservationException("No se pudo conectar con la IA.", e);
        }
    }

    private String construirPrompt(String solicitud) {

        LocalDate hoy = LocalDate.now();

        return String.format("""
            Eres un asistente para un sistema de reservas.

            La fecha de hoy es: %s

            Analiza la solicitud del usuario y extrae:

            - actividad
            - fecha
            - horaInicio
            - horaFin
            - categorias

            Las categorías permitidas son:
            SALA
            PROYECTOR
            COMPUTADORA
            PIZARRA

            Devuelve únicamente un JSON con este formato:

            {
              "actividad": "string",
              "fecha": "YYYY-MM-DD",
              "horaInicio": "HH:mm",
              "horaFin": "HH:mm",
              "categorias": ["CATEGORIA"]
            }

            Para expresiones como "mañana", "pasado mañana",
            "el próximo martes", etc., utiliza la fecha de hoy
            como referencia.

            No agregues explicaciones ni texto fuera del JSON.

            Solicitud del usuario: %s
            """, hoy, solicitud);
    }


    private AiReservationResponse convertirRespuesta(String respuestaIA) throws AiReservationException {
        try {
            AiReservationResponse respuesta = gson.fromJson(respuestaIA, AiReservationResponse.class);

            if (respuesta == null) {
                throw new AiReservationException("La respuesta de la IA no) tiene el formato esperado.");
            }
            validarRespuesta(respuesta);
            return respuesta;
        } catch(JsonSyntaxException e) {
            throw new AiReservationException("La respuesta de la IA tiene un formato inválido.", e);

        } catch (AiReservationException e) {
                    throw e;

        } catch (Exception e) {
            throw new AiReservationException("Ocurrió un error inesperado al convertir la respuesta de la IA.", e);
        }
    }

    private void validarRespuesta(AiReservationResponse respuesta) throws AiReservationException {
        if (respuesta.getActividad() == null || respuesta.getActividad().trim().isEmpty()) {
            throw new AiReservationException("La actividad no puede estar vacía.");
        }

        if (respuesta.getFecha() == null) {
            throw new AiReservationException("La fecha no pudo ser interpretada.");
        }
        if (respuesta.getHoraInicio() == null || respuesta.getHoraFin() == null) {
            throw new AiReservationException("La hora no pudo ser interpretada.");
        }

        if(!respuesta.getHoraInicio().isBefore(respuesta.getHoraFin())) {
            throw new AiReservationException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        if(respuesta.getCategorias() == null || respuesta.getCategorias().isEmpty()) {
            throw new AiReservationException("Debe especificar al menos una categoría de recurso.");
        }
    }
}