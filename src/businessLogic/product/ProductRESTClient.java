/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.product;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import DTO.ProductBean;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ui.utilities.ConfigReader;

/**
 * Jersey REST client generated for REST resource:ProductEntityFacadeREST [productentity]<br>
 * USAGE:
 * <pre>
 * ProductRESTClient client = new ProductRESTClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author InigoFreire
 */
public class ProductRESTClient implements IProductManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ConfigReader.getBaseUri();

    public ProductRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("productentity");
    }

    public void deleteProductById(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("delete/{0}", new Object[]{id}))
                .request().delete(ProductBean.class);
    }

    public void updateProduct(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity,
                        javax.ws.rs.core.MediaType.APPLICATION_XML), ProductBean.class);
    }

    public void createProduct(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity,
                        javax.ws.rs.core.MediaType.APPLICATION_XML), ProductBean.class);
    }

    public <T> T getProductsByCreatedDate(GenericType<T> responseType, String date) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("date/{0}", new Object[]{date}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T getProductByName(GenericType<T> responseType, String name) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("name/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T getAllProducts(GenericType<T> responseType) throws WebApplicationException {
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
        client.close();
    }

}
