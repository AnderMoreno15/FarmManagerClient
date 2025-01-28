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
 * @author Aitziber
 */
interface ISpeciesManager {
    public <T> T getAllSpecies(GenericType<T> responseType) throws WebApplicationException;
    public void close();
}
