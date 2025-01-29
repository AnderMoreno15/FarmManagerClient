/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package businessLogic.consumes;
/**
 *
 * @author Pablo
 */
public class ConsumesManagerFactory {
    
    private static IConsumesManager consumesManager;
    
    public static IConsumesManager get(){
        if(consumesManager==null){
           consumesManager=  new ConsumesRestClient();
        }
        return consumesManager;
    }    
}