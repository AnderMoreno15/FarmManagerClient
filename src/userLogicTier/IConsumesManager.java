/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userLogicTier;

import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Usuario
 */
interface IConsumesManager {
  
  public <T> T getConsumesByDateFrom(Class<T> responseType, String from) throws WebApplicationException ;
  public <T> T findConsumesByProduct(Class<T> responseType, String ProductId) throws WebApplicationException;
  public void deleteConsume(String productId, String animalGroupId) throws WebApplicationException;
  public <T> T findConsumesByAnimalGroup(Class<T> responseType, String animalGroupId) throws WebApplicationException;
  public <T> T getConsumesByDateTo(Class<T> responseType, String to) throws WebApplicationException;
  public <T> T getAllConsumes(Class<T> responseType) throws WebApplicationException;
  public <T> T getConsumesByDate(Class<T> responseType, String from, String to) throws WebApplicationException;
  public void createConsume(Object requestEntity) throws WebApplicationException ;
  public void updateConsume(Object requestEntity, String productId, String animalGroupId) throws WebApplicationException;
}
