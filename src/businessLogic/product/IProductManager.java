/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.product;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author InigoFreire
 */
public interface IProductManager {
    public void deleteProductById(String id) throws WebApplicationException;
    public void updateProduct(Object requestEntity) throws WebApplicationException;
    public void createProduct(Object requestEntity) throws WebApplicationException;
    public <T> T getProductsByCreatedDate(GenericType<T> responseType, String date) throws WebApplicationException;
    public <T> T getProductByName(GenericType<T> responseType, String name) throws WebApplicationException;
    public <T> T getAllProducts(GenericType<T> responseType) throws WebApplicationException;
    public void close();
}