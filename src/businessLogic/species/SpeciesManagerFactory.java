/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.species;

/**
 *
 * @author Aitziber
 */
public class SpeciesManagerFactory {
    
    private static ISpeciesManager speciesManager;
    
    public static ISpeciesManager get(){
        if(speciesManager==null){
           speciesManager=new SpeciesRESTClient();
        }
        return speciesManager;
    }        
}
