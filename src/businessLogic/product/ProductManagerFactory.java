/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.product;

import DTO.ProductBean;
import businessLogic.consumes.ConsumesRestClient;
import businessLogic.consumes.IConsumesManager;

/**
 *
 * @author InigoFreire
 */
public class ProductManagerFactory {
     private static IProductManager productManager;
       public static IProductManager get(){
        if(productManager==null){
           productManager =  new ProductRESTClient();
        }
        return productManager;
    }  
}