/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.manager;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import ui.utilities.ConfigReader;

/**
 * Cliente REST utilizando Jersey para interactuar con el servicio ManagerFacadeREST.
 * Esta clase implementa la interfaz {@link IManager} y proporciona métodos para realizar operaciones 
 * relacionadas con los gerentes, como obtener, crear, actualizar, y eliminar gerentes a través de solicitudes REST.
 * 
 * <p>USO:</p>
 * <pre>
 *        ManagerRESTClient client = new ManagerRESTClient();
 *        Object response = client.XXX(...);
 *        // realizar operaciones con la respuesta
 *        client.close();
 * </pre>
 * 
 * <p>La clase utiliza la configuración proporcionada por {@link ConfigReader} para definir la URI base 
 * del servicio y utiliza el cliente Jersey para hacer solicitudes HTTP.</p>
 * 
 * @author Ander
 */
public class ManagerRESTClient implements IManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ConfigReader.getBaseUri();

    /**
     * Constructor que inicializa el cliente Jersey y define el endpoint base para las solicitudes.
     */
    public ManagerRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("manager");
    }

    /**
     * Obtiene un gerentes de acuerdo a las credenciales proporcionadas (email y contraseña).
     * 
     * @param <T> El tipo de respuesta esperado.
     * @param responseType El tipo genérico de la respuesta.
     * @param email El correo electrónico del gerente.
     * @param password La contraseña del gerente.
     * @return El objeto con los datos del gerente.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public <T> T getManager(GenericType<T> responseType, String email, String password) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}/{1}", new Object[]{email, password}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Actualiza los datos de un gerente.
     * 
     * @param requestEntity El objeto con los datos del gerente a actualizar.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public void updateManager(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Crea un nuevo gerente.
     * 
     * @param requestEntity El objeto con los datos del nuevo gerente.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public void createManager(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Obtiene todos los gerentes.
     * 
     * @param <T> El tipo de respuesta esperado.
     * @param responseType El tipo genérico de la respuesta.
     * @return Una lista de todos los gerentes.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public <T> T getManagers(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request().get(responseType);
    }

    /**
     * Obtiene un gerente basado en su correo electrónico.
     * 
     * @param <T> El tipo de respuesta esperado.
     * @param responseType El tipo genérico de la respuesta.
     * @param email El correo electrónico del gerente.
     * @return El gerente correspondiente al correo electrónico.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public <T>T getManagerByEmail(GenericType<T> responseType, String email) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}", new Object[]{email}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Restablece la contraseña de un gerente.
     * 
     * @param requestEntity El objeto con los datos del gerente cuya contraseña se va a restablecer.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public void resetPassword(Object requestEntity) throws WebApplicationException {
        webTarget.path("reset").request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Realiza el inicio de sesión de un gerente.
     * 
     * @param requestEntity El objeto con los datos de inicio de sesión (correo y contraseña).
     * @param <T> El tipo de respuesta esperado.
     * @param responseType El tipo genérico de la respuesta.
     * @return El gerente autenticado.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public <T> T signIn(Object requestEntity, GenericType<T> responseType) throws WebApplicationException {
        return webTarget.path("signin").request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    /**
     * Realiza el registro de un nuevo gerente.
     * 
     * @param requestEntity El objeto con los datos del gerente a registrar.
     * @throws WebApplicationException Si ocurre un error en la solicitud HTTP.
     */
    @Override
    public void signUp(Object requestEntity) throws WebApplicationException {
        webTarget.path("signup").request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Cierra la conexión del cliente Jersey.
     */
    @Override
    public void close() {
        client.close();
    }
}
