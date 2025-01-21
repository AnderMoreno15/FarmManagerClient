/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userLogicTier;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:ConsumesFacadeREST
 * [entities.consumes]<br>
 * USAGE:
 * <pre>
        ConsumesRestClient client = new ConsumesRestClient();
        Object response = client.XXX(...);
        // do whatever with response
        client.close();
 </pre>
 *
 * @author Usuario
 */
public class ConsumesRestClient implements IConsumesManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/farmapp/webresources";

    public ConsumesRestClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.consumes");
    }

    @Override
    public <T> T getConsumesByDateFrom(Class<T> responseType, String from) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Desde/{0}", new Object[]{from}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     @Override
    public <T> T findConsumesByProduct(Class<T> responseType, String ProductId) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Producto/{0}", new Object[]{ProductId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     @Override
    public void deleteConsume(String productId, String animalGroupId) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{productId, animalGroupId})).request().delete();
    }
     @Override
    public <T> T findConsumesByAnimalGroup(Class<T> responseType, String animalGroupId) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Animal/{0}", new Object[]{animalGroupId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     @Override
    public <T> T getConsumesByDateTo(Class<T> responseType, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Hasta/{0}", new Object[]{to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     @Override
    public <T> T getAllConsumes(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     @Override
    public <T> T getConsumesByDate(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Rango/{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     @Override
    public void createConsume(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
     @Override
    public void updateConsume(Object requestEntity, String productId, String animalGroupId) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{productId, animalGroupId})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void close() {
        client.close();
    }
    
}
