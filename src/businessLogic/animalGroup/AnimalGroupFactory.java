/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.animalGroup;

/**
 * Factory class for managing {@link IAnimalGroup} instances.
 * <p>
 * Provides a singleton instance of {@link AnimalGroupRESTClient}.
 * </p>
 *
 * @author Ander
 */
public class AnimalGroupFactory {

    private static IAnimalGroup animalGroupManager;

    /**
     * Returns the singleton instance of {@link IAnimalGroup}. 
     * If not already created, it initializes an {@link AnimalGroupRESTClient}.
     *
     * @return the {@link IAnimalGroup} instance
     */
    public static IAnimalGroup get() {
        if (animalGroupManager == null) {
            animalGroupManager = new AnimalGroupRESTClient();
        }
        return animalGroupManager;
    }
}
