/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animalGroup;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Ander
 */
public interface IAnimalGroup {
    
    //CREATE
    public void createAnimalGroup(Object requestEntity) throws WebApplicationException;

    //GETS
    public <T> T getAnimalGroupByName(GenericType<T> responseType, String name, String managerId) throws WebApplicationException;

    public <T> T getAnimalGroupsByManager(GenericType<T> responseType, String managerId) throws WebApplicationException;
    
    // public <T> T getAnimalGroups(GenericType<T> responseType) throws WebApplicationException;
    
    //UPDATE
    public void updateAnimalGroup(Object requestEntity) throws WebApplicationException;

    //DELETE
    public void deleteAnimalGroupById(String id) throws WebApplicationException;
    
    // public void deleteAnimalGroup(Object requestEntity) throws WebApplicationException;
    
    public void close();
}
