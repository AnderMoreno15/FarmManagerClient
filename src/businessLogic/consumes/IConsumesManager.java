package businessLogic.consumes;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Interface for managing consume operations.
 * Defines the methods to interact with consume data, including fetching, creating, updating, 
 * and deleting consumes in the system.
 * 
 * Each method may throw a {@link WebApplicationException} in case of errors during the 
 * communication with the backend or when handling the request.
 * 
 * @author Usuario
 */
public interface IConsumesManager {
  
  /**
   * Retrieves a collection of consumes that occurred after the specified date.
   * 
   * @param responseType The type of the response to be returned.
   * @param from The start date (inclusive) for the range of consumes to fetch.
   * @param <T> The type of the response object.
   * @return A collection of consumes that occurred after the specified date.
   * @throws WebApplicationException If an error occurs while fetching the data.
   */
  public <T> T getConsumesByDateFrom(GenericType<T> responseType, String from) throws WebApplicationException;
  
  /**
   * Retrieves a collection of consumes for a specific product.
   * 
   * @param responseType The type of the response to be returned.
   * @param ProductId The ID of the product to search for consumes.
   * @param <T> The type of the response object.
   * @return A collection of consumes related to the specified product.
   * @throws WebApplicationException If an error occurs while fetching the data.
   */
  public <T> T findConsumesByProduct(GenericType<T> responseType, String ProductId) throws WebApplicationException;
  
  /**
   * Deletes a specific consume based on product and animal group IDs.
   * 
   * @param productId The ID of the product associated with the consume to be deleted.
   * @param animalGroupId The ID of the animal group associated with the consume to be deleted.
   * @return A response indicating the result of the delete operation.
   * @throws WebApplicationException If an error occurs during the delete operation.
   */
  public Response deleteConsume(String productId, String animalGroupId) throws WebApplicationException;

  /**
   * Retrieves a collection of consumes related to a specific animal group.
   * 
   * @param responseType The type of the response to be returned.
   * @param nameAnimalGroup The name of the animal group to search for consumes.
   * @param <T> The type of the response object.
   * @return A collection of consumes related to the specified animal group.
   * @throws WebApplicationException If an error occurs while fetching the data.
   */
  public <T> T findConsumesByAnimalGroup(GenericType<T> responseType, String nameAnimalGroup) throws WebApplicationException;

  /**
   * Retrieves a collection of consumes that occurred before the specified date.
   * 
   * @param responseType The type of the response to be returned.
   * @param to The end date (inclusive) for the range of consumes to fetch.
   * @param <T> The type of the response object.
   * @return A collection of consumes that occurred before the specified date.
   * @throws WebApplicationException If an error occurs while fetching the data.
   */
  public <T> T getConsumesByDateTo(GenericType<T> responseType, String to) throws WebApplicationException;

  /**
   * Retrieves a collection of all consumes in the system.
   * 
   * @param responseType The type of the response to be returned.
   * @param <T> The type of the response object.
   * @return A collection of all consumes in the system.
   * @throws WebApplicationException If an error occurs while fetching the data.
   */
  public <T> T getAllConsumes(GenericType<T> responseType) throws WebApplicationException;

  /**
   * Retrieves a collection of consumes within a specific date range.
   * 
   * @param responseType The type of the response to be returned.
   * @param from The start date (inclusive) for the range of consumes to fetch.
   * @param to The end date (inclusive) for the range of consumes to fetch.
   * @param <T> The type of the response object.
   * @return A collection of consumes within the specified date range.
   * @throws WebApplicationException If an error occurs while fetching the data.
   */
  public <T> T getConsumesByDate(GenericType<T> responseType, String from, String to) throws WebApplicationException;

  /**
   * Creates a new consume record in the system.
   * 
   * @param requestEntity The consume data to be created.
   * @throws WebApplicationException If an error occurs while creating the consume record.
   */
  public void createConsume(Object requestEntity) throws WebApplicationException;

  /**
   * Updates an existing consume record in the system.
   * 
   * This method is used to update the details of an existing consume, including any 
   * associated information such as product, animal group, date, and quantity.
   * If the consume record does not exist or there is an issue updating it, 
   * a {@link WebApplicationException} will be thrown.
   * 
   * @param requestEntity The consume data to be updated.
   * @throws WebApplicationException If an error occurs while updating the consume record.
   */
  public void updateConsume(Object requestEntity) throws WebApplicationException;
}


