/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animalGroup;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import DTO.AnimalGroupBean;

/**
 * Jersey REST client generated for REST resource:AnimalGroupFacadeREST [animalgroup]<br>
 * USAGE:
 * <pre>
 * AnimalGroupRESTClient client = new AnimalGroupRESTClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author Ander
 */
public class AnimalGroupRESTClient implements IAnimalGroup {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/farmapp/webresources";

    public AnimalGroupRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("animalgroup");
    }

    //CREATE
    @Override
    public void createAnimalGroup(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), AnimalGroupBean.class);
    }

    //GET
    @Override
    public <T> T getAnimalGroupsByManager(GenericType<T> responseType, String managerId) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}", new Object[]{managerId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

//    @Override
//    public <T> T getAnimalGroups(Class<T> responseType) throws ClientErrorException {
//        WebTarget resource = webTarget;
//        return resource.get(responseType);
//    }
    @Override
    public <T> T getAnimalGroupByName(GenericType<T> responseType, String name, String managerId) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}/{1}", new Object[]{name, managerId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    //UPDATE
    @Override
    public void updateAnimalGroup(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), AnimalGroupBean.class);
    }

    //DELETE
    @Override
    public void deleteAnimalGroupById(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("delete/{0}", new Object[]{id})).request().delete(AnimalGroupBean.class);
    }

//    @Override
//    public void deleteAnimalGroup(Object requestEntity) throws ClientErrorException {
//        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).delete(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
//    }
    @Override
    public void close() {
        client.close();
    }

}
