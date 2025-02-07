/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package businessLogic.consumes;

/**
 * Factory class for managing instances of {@code IConsumesManager}.
 * <p>
 * This class provides a singleton instance of {@code IConsumesManager},
 * ensuring that only one instance of {@code ConsumesRestClient} is created
 * and reused throughout the application.
 * </p>
 * <p>
 * The {@code get()} method initializes the {@code consumesManager} instance
 * if it has not been created yet and returns it.
 * </p>
 *
 * @author Pablo
 */
public class ConsumesManagerFactory {

    /**
     * Singleton instance of {@code IConsumesManager}.
     */
    private static IConsumesManager consumesManager;

    /**
     * Returns a singleton instance of {@code IConsumesManager}.
     * <p>
     * If the instance has not been initialized, it creates a new {@code ConsumesRestClient}
     * and assigns it to {@code consumesManager}. Otherwise, it returns the existing instance.
     * </p>
     *
     * @return a singleton instance of {@code IConsumesManager}.
     */
    public static IConsumesManager get() {
        if (consumesManager == null) {
            consumesManager = new ConsumesRestClient();
        }
        return consumesManager;
    }
}
