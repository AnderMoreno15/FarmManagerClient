/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userLogicTier;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Usuario
 */
public interface IConsumesManager {
  
  public <T> T getConsumesByDateFrom(GenericType<T> responseType, String from) throws WebApplicationException ;
  public <T> T findConsumesByProduct(GenericType<T> responseType, String ProductId) throws WebApplicationException;
  public void deleteConsume(String consumesId) throws WebApplicationException;
  public <T> T findConsumesByAnimalGroup(GenericType<T> responseType, String nameAnimalGroup) throws WebApplicationException;
  public <T> T getConsumesByDateTo(GenericType<T> responseType, String to) throws WebApplicationException;
  public <T> T getAllConsumes(GenericType<T> responseType) throws WebApplicationException;
  public <T> T getConsumesByDate(GenericType<T> responseType, String from, String to) throws WebApplicationException;
  public void createConsume(Object requestEntity) throws WebApplicationException ;
  public void updateConsume(Object requestEntity) throws WebApplicationException;
 //Cambiar el metodo Update del Servidor 


}
