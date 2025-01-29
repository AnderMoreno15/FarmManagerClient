/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animalGroup;

/**
 *
 * @author Ander
 */
public class AnimalGroupFactory {

    private static IAnimalGroup animalGroupManager;

    public static IAnimalGroup get() {
        if (animalGroupManager == null) {
            animalGroupManager = new AnimalGroupRESTClient();
        }
        return animalGroupManager;
    }
}
