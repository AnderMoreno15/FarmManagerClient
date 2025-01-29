/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Aitziber
 */
public interface IAnimalManager {
    public void updateAnimal(Object requestEntity) throws WebApplicationException;
    public <T> T getAnimalsByAnimalGroup(GenericType<T> responseType, String animalGroupName) throws WebApplicationException;
    public void createAnimal(Object requestEntity) throws WebApplicationException;
    public <T> T getAnimalsBySubespecies(GenericType<T> responseType, String subespecies) throws WebApplicationException;
    public <T> T getAnimalsByBirthdateTo(GenericType<T> responseType, String dateTo) throws WebApplicationException;
    public void deleteAnimalById(String id) throws WebApplicationException;
    public <T> T getAnimalsByBirthdateFrom(GenericType<T> responseType, String dateFrom) throws WebApplicationException;
    public <T> T getAnimalByName(GenericType<T> responseType, String name) throws WebApplicationException;
    public <T> T getAnimalsByBirthdate(GenericType<T> responseType, String dateFrom, String dateTo) throws WebApplicationException;
    public <T> T getAllAnimals(GenericType<T> responseType, String managerId) throws WebApplicationException;
    public void close();
}
