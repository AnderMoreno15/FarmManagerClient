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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:ConsumesFacadeREST
 * [consumes]<br>
 * USAGE:
 * <pre>
 *        ConsumesRestClient client = new ConsumesRestClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Usuario
 */
public class ConsumesRestClient implements IConsumesManager {
    
   
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/farmapp/webresources";
     
      public ConsumesRestClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("consumes");
    }
    
        private static final Logger LOGGER = Logger.getLogger("logger");
 
  @Override
    public <T> T getConsumesByDateFrom(GenericType<T> responseType, String from) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Desde/{0}", new Object[]{from}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
   @Override
    public <T> T findConsumesByProduct(GenericType<T> responseType, String ProductId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Producto/{0}", new Object[]{ProductId}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
   @Override
    public <T> T findConsumesByAnimalGroup(GenericType<T> responseType, String animalGroupName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("AnimalGroup/{0}", new Object[]{animalGroupName}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
   @Override
    public <T> T getConsumesByDateTo(GenericType<T> responseType, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Hasta/{0}", new Object[]{to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
   
//    public void deleteConsume(String productId, String animalGroupId) throws ClientErrorException  {
//        Client client = ClientBuilder.newClient(); // Crea un cliente RESTful
//        WebTarget target = client.target(BASE_URI).path("Delete/{productId}/{animalGroupId}")
//                                 .resolveTemplate("productId", productId)
//                                 .resolveTemplate("animalGroupId", animalGroupId);
//
//        try {
//            // Enviamos la solicitud DELETE
//            Response response = target.request().delete();
//
//            // Comprobamos si la eliminación fue exitosa (código 204 No Content)
//            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
//                System.out.println("Consume successfully deleted.");
//            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
//                System.out.println("Consume not found.");
//            } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
//                System.out.println("Invalid ID format.");
//            } else {
//                System.out.println("Error: " + response.getStatus());
//            }
//
//        } catch (ClientErrorException e) {
//            System.out.println("ClientErrorException: " + e.getResponse().getStatus());
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
  @Override
public Response deleteConsume(String productId, String animalGroupId) throws WebApplicationException {
    return webTarget.path("Delete/" + productId + "/" + animalGroupId)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
}



   @Override
    public <T> T getAllConsumes(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("All");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
  @Override
    public <T> T getConsumesByDate(GenericType<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("Rango/{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
  
    @Override
    public void createConsume(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
      
  
        
   @Override
    public void updateConsume(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void close() {
        client.close();
    }
//        public static void main(String[] args) {
//        // Crear el cliente REST
//        ConsumesRestClient client = new ConsumesRestClient();
//
//        try {
//              // Crear el ID del consumo
//            ConsumesIdBean consumesId = new ConsumesIdBean();
//            consumesId.setAnimalGroupId(6L); // Asigna un ID válido según la lógica de tu backend
//            consumesId.setProductId(6L);
//
//            // Crear el grupo de animales asociado al consumo
//            AnimalGroupBean animalGroup = new AnimalGroupBean();
//            animalGroup.setId(6L); // Asigna un ID válido
//
//            // Crear el producto asociado al consumo
//            ProductBean product = new ProductBean();
//            product.setId(6L); // Asigna un ID válido
//
//            // Crear el objeto ConsumesBean con los datos de consumo
//            ConsumesBean newConsume = new ConsumesBean();
//            newConsume.setConsumesId(consumesId);
//            newConsume.setAnimalGroup(animalGroup);
//            newConsume.setProduct(product);
//           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = sdf.parse("2000-08-01"); // Convertir la cadena a una fecha
//
//            newConsume.setDate(date); 
//            newConsume.setConsumeAmount(10.5f); // Cantidad consumida
//
//            // Llamar al método para crear el consumo
//            System.out.println("Creando un nuevo consumo...");
//            client.createConsume(newConsume);
//            System.out.println("Consumo creado con éxito: " + newConsume);
//
//        } catch (Exception e) {
//            System.err.println("Error al crear el consumo: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            client.close();
//        }
//    }
     
      //Probandooooo by Date From
//  public static void main(String[] args) {
//        ConsumesRestClient client = new ConsumesRestClient();
//        
//        try {
//            String fromDate = "2024-01-01"; // Ajusta la fecha según tu base de datos
//
//            System.out.println("Consultando consumos desde la fecha: " + fromDate);
//            String from = "2000-03-10";
//            // Realiza la petición
//            List<ConsumesBean>   consumesList = ConsumesManagerFactory.get().getConsumesByDateFrom(new GenericType<List<ConsumesBean>>() {},from);
//            
//            // Muestra la respuesta
//            System.out.println("Lista de Consumos desde " + fromDate + ": " + consumesList);
//
//        } catch (ClientErrorException e) {
//            System.err.println("Error en la petición REST: " + e.getResponse().getStatus());
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("Error inesperado: " + e.getMessage());
//            e.printStackTrace();
//        }
//  }
//          Probando get by animal name

    public static void main(String[] args) {
   ConsumesRestClient client = new ConsumesRestClient();
        try {
            String animalGroupName = "North Cows"; // Ajusta el ID según tu base de datos

            System.out.println("Consultando consumos para el grupo de animales con nombre: " + animalGroupName);
            
            // Realiza la petición
            List<ConsumesBean> consumesList = ConsumesManagerFactory.get()
                .findConsumesByAnimalGroup(new GenericType<List<ConsumesBean>>() {}, animalGroupName);
            
            // Muestra la respuesta
            System.out.println("Lista de Consumos para el Grupo " + animalGroupName + ": " + consumesList);

        } catch (ClientErrorException e) {
            System.err.println("Error en la petición REST: " + e.getResponse().getStatus());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
