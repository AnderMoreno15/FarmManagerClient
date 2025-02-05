/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.consumes;

import DTO.AnimalGroupBean;
import DTO.ConsumesBean;
import DTO.ConsumesIdBean;
import DTO.ProductBean;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST client for managing consume records via the Consumes RESTful service.
 * <p>
 * This class allows interaction with a REST API for CRUD operations related to 
 * consumption records. It uses Jersey's {@code javax.ws.rs.client} package to 
 * communicate with the backend service.
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>
 *     ConsumesRestClient client = new ConsumesRestClient();
 *     List&lt;ConsumesBean&gt; consumes = client.getAllConsumes(new GenericType&lt;List&lt;ConsumesBean&gt;&gt;() {});
 *     client.close();
 * </pre>
 * 
 * @author Usuario
 */
public class ConsumesRestClient implements IConsumesManager {

    /** Logger for logging messages */
    private static final Logger LOGGER = Logger.getLogger("logger");

    /** Base URI for the REST service */
    private static final String BASE_URI = "http://localhost:8080/farmapp/webresources";

    /** REST client for making HTTP requests */
    private Client client;

    /** Web target for the consumes endpoint */
    private WebTarget webTarget;

    /**
     * Constructs a new {@code ConsumesRestClient}, initializing the REST client 
     * and setting the base path for the consumes API.
     */
    public ConsumesRestClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("consumes");
    }

    /**
     * Retrieves consumption records from a specified date.
     *
     * @param responseType The expected response type.
     * @param from         The start date in the format "yyyy-MM-dd".
     * @param <T>          The generic type of the response.
     * @return A list of consumption records matching the criteria.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public <T> T getConsumesByDateFrom(GenericType<T> responseType, String from) throws ClientErrorException {
        return webTarget.path("Desde/" + from)
                        .request(MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Retrieves consumption records filtered by product ID.
     *
     * @param responseType The expected response type.
     * @param productId    The product ID.
     * @param <T>          The generic type of the response.
     * @return A list of consumption records matching the product ID.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public <T> T findConsumesByProduct(GenericType<T> responseType, String productId) throws ClientErrorException {
        return webTarget.path("Producto/" + productId)
                        .request(MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Retrieves consumption records filtered by animal group name.
     *
     * @param responseType     The expected response type.
     * @param animalGroupName  The name of the animal group.
     * @param <T>              The generic type of the response.
     * @return A list of consumption records matching the animal group.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public <T> T findConsumesByAnimalGroup(GenericType<T> responseType, String animalGroupName) throws ClientErrorException {
        return webTarget.path("AnimalGroup/" + animalGroupName)
                        .request(MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Retrieves consumption records up to a specified date.
     *
     * @param responseType The expected response type.
     * @param to           The end date in the format "yyyy-MM-dd".
     * @param <T>          The generic type of the response.
     * @return A list of consumption records up to the given date.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public <T> T getConsumesByDateTo(GenericType<T> responseType, String to) throws ClientErrorException {
        return webTarget.path("Hasta/" + to)
                        .request(MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Deletes a consumption record identified by product ID and animal group ID.
     *
     * @param productId     The ID of the product.
     * @param animalGroupId The ID of the animal group.
     * @return The HTTP response from the server.
     * @throws WebApplicationException if an error occurs during the request.
     */
    @Override
    public Response deleteConsume(String productId, String animalGroupId) throws WebApplicationException {
        return webTarget.path("Delete/" + productId + "/" + animalGroupId)
                        .request(MediaType.APPLICATION_XML)
                        .delete();
    }

    /**
     * Retrieves all consumption records.
     *
     * @param responseType The expected response type.
     * @param <T>          The generic type of the response.
     * @return A list of all consumption records.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public <T> T getAllConsumes(GenericType<T> responseType) throws ClientErrorException {
        return webTarget.path("All")
                        .request(MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Retrieves consumption records within a specified date range.
     *
     * @param responseType The expected response type.
     * @param from         The start date in the format "yyyy-MM-dd".
     * @param to           The end date in the format "yyyy-MM-dd".
     * @param <T>          The generic type of the response.
     * @return A list of consumption records within the date range.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public <T> T getConsumesByDate(GenericType<T> responseType, String from, String to) throws ClientErrorException {
        return webTarget.path("Rango/" + from + "/" + to)
                        .request(MediaType.APPLICATION_XML)
                        .get(responseType);
    }

    /**
     * Creates a new consumption record.
     *
     * @param requestEntity The consumption record to be created.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public void createConsume(Object requestEntity) throws ClientErrorException {
        webTarget.request(MediaType.APPLICATION_XML)
                 .post(javax.ws.rs.client.Entity.entity(requestEntity, MediaType.APPLICATION_XML));
    }

    /**
     * Updates an existing consumption record.
     *
     * @param requestEntity The updated consumption record.
     * @throws ClientErrorException if an error occurs during the request.
     */
    @Override
    public void updateConsume(Object requestEntity) throws ClientErrorException {
        webTarget.request(MediaType.APPLICATION_XML)
                 .put(javax.ws.rs.client.Entity.entity(requestEntity, MediaType.APPLICATION_XML));
    }

    /**
     * Closes the REST client, releasing resources.
     */
    public void close() {
        client.close();
    }
}
