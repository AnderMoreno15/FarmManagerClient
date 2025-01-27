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
 * @author Ander
 */
public interface IManager {

    public <T> T getManager(GenericType<T> responseType, String email, String password) throws WebApplicationException;

    public void updateManager(Object requestEntity) throws WebApplicationException;

    public void createManager(Object requestEntity) throws WebApplicationException;

    public <T> T getManagers(GenericType<T> responseType) throws WebApplicationException;
    
    public <T> T getManagerByEmail(GenericType<T> responseType, String email) throws WebApplicationException;

    public void close();
}
