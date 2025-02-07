package businessLogic.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ui.utilities.ConfigReader;

/**
 * Jersey REST client generated for REST resource:ProviderEntityFacadeREST [providerentity]<br>
 * USAGE:
 * <pre>
 *        ProviderBean client = new ProviderBean();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author InigoFreire
 */
public class ProviderRESTClient implements IProviderManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ConfigReader.getBaseUri();
    private static final Logger logger = Logger.getLogger(ProviderRESTClient.class.getName());

    public ProviderRESTClient() {
        logger.info("Inicializando el cliente REST para proveedores...");
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("providerentity");
        logger.info("Cliente REST inicializado correctamente.");
    }

    public <T> T getAllProviders(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget.path("all");
        Response response = resource.request(MediaType.APPLICATION_XML).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(responseType);
        } else {
            String errorMessage = response.readEntity(String.class);
            throw new WebApplicationException("Error en la solicitud: " + errorMessage, response.getStatus());
        }
    }

    public void close() {
        logger.info("Cerrando el cliente REST.");
        client.close();
        logger.info("Cliente REST cerrado.");
    }
}
