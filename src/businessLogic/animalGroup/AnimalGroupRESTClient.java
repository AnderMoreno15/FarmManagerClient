/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animalGroup;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import DTO.AnimalGroupBean;
import ui.utilities.ConfigReader;

/**
 * REST client for interacting with the AnimalGroup API.
 * <p>
 * This class provides methods to perform CRUD operations on animal groups
 * using RESTful web services.
 * </p>
 * 
 * <p>Usage example:</p>
 * <pre>
 * AnimalGroupRESTClient client = new AnimalGroupRESTClient();
 * client.createAnimalGroup(animalGroup);
 * List&lt;AnimalGroupBean&gt; groups = client.getAnimalGroupsByManager(new GenericType&lt;List&lt;AnimalGroupBean&gt;&gt;() {}, "1");
 * client.close();
 * </pre>
 * 
 * @author Ander
 */
public class AnimalGroupRESTClient implements IAnimalGroup {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ConfigReader.getBaseUri();

    /**
     * Initializes the REST client and sets the base URI for the API.
     */
    public AnimalGroupRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("animalgroup");
    }

    /**
     * Creates a new animal group.
     * 
     * @param requestEntity The animal group data to be created.
     * @throws WebApplicationException if an error occurs during the request.
     */
    @Override
    public void createAnimalGroup(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                 .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), AnimalGroupBean.class);
    }

    /**
     * Retrieves all animal groups managed by a specific manager.
     * 
     * @param <T> The expected response type.
     * @param responseType The generic type to store the response.
     * @param managerId The ID of the manager.
     * @return A list of animal groups associated with the given manager.
     * @throws WebApplicationException if an error occurs during the request.
     */
    @Override
    public <T> T getAnimalGroupsByManager(GenericType<T> responseType, String managerId) throws WebApplicationException {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("search/{0}", new Object[]{managerId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Retrieves an animal group by its name and manager ID.
     * 
     * @param <T> The expected response type.
     * @param responseType The generic type to store the response.
     * @param name The name of the animal group.
     * @param managerId The ID of the manager.
     * @return The requested animal group.
     * @throws WebApplicationException if an error occurs during the request.
     */
    @Override
    public <T> T getAnimalGroupByName(GenericType<T> responseType, String name, String managerId) throws WebApplicationException {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("search/{0}/{1}", new Object[]{name, managerId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Updates an existing animal group.
     * 
     * @param requestEntity The updated animal group data.
     * @throws WebApplicationException if an error occurs during the request.
     */
    @Override
    public void updateAnimalGroup(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                 .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), AnimalGroupBean.class);
    }

    /**
     * Deletes an animal group by its ID.
     * 
     * @param id The ID of the animal group to delete.
     * @throws WebApplicationException if an error occurs during the request.
     */
    @Override
    public void deleteAnimalGroupById(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("delete/{0}", new Object[]{id}))
                 .request().delete(AnimalGroupBean.class);
    }

    /**
     * Closes the REST client to release resources.
     */
    @Override
    public void close() {
        client.close();
    }
}
