/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animal;

/**
 *
 * @author Aitziber
 */
public class AnimalManagerFactory {
    
    private static IAnimalManager animalManager;
    
    public static IAnimalManager get(){
        if(animalManager==null){
           animalManager=new AnimalRESTClient();
        }
        return animalManager;
    }    
}
