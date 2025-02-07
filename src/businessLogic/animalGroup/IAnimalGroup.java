/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animalGroup;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * Interface for managing animal groups through a REST API.
 * Provides methods for creating, retrieving, updating, and deleting animal groups.
 * 
 * Defines the contract for managing animal groups in the system.
 * Provides CRUD operations for interacting with the RESTful API.
 * 
 * @author Ander
 */
public interface IAnimalGroup {

    /**
     * Creates a new animal group.
     * 
     * @param requestEntity The animal group data to be created.
     * @throws WebApplicationException if an error occurs during the request.
     */
    public void createAnimalGroup(Object requestEntity) throws WebApplicationException;

    /**
     * Retrieves an animal group by name and manager ID.
     * 
     * @param <T> The expected response type.
     * @param responseType The generic response type.
     * @param name The name of the animal group.
     * @param managerId The ID of the manager.
     * @return The requested animal group data.
     * @throws WebApplicationException if an error occurs during the request.
     */
    public <T> T getAnimalGroupByName(GenericType<T> responseType, String name, String managerId) throws WebApplicationException;

    /**
     * Retrieves all animal groups associated with a specific manager.
     * 
     * @param <T> The expected response type.
     * @param responseType The generic response type.
     * @param managerId The ID of the manager.
     * @return A list of animal groups managed by the specified manager.
     * @throws WebApplicationException if an error occurs during the request.
     */
    public <T> T getAnimalGroupsByManager(GenericType<T> responseType, String managerId) throws WebApplicationException;

    /**
     * Updates an existing animal group.
     * 
     * @param requestEntity The updated animal group data.
     * @throws WebApplicationException if an error occurs during the request.
     */
    public void updateAnimalGroup(Object requestEntity) throws WebApplicationException;

    /**
     * Deletes an animal group by its ID.
     * 
     * @param id The ID of the animal group to be deleted.
     * @throws WebApplicationException if an error occurs during the request.
     */
    public void deleteAnimalGroupById(String id) throws WebApplicationException;

    /**
     * Closes the connection or any resources used by the client.
     */
    public void close();
}
