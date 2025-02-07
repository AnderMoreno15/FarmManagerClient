/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.manager;

/**
 * Fabrica de objetos que proporciona una instancia del servicio de gestión de gerentes (Manager).
 * <p>
 * Esta clase implementa el patrón de diseño Singleton para asegurar que solo exista una instancia 
 * de la interfaz {@link IManager}, que se obtiene a través del método {@link #get()}.
 * Si la instancia no ha sido creada previamente, la clase instanciará un objeto de tipo {@link ManagerRESTClient}.
 * </p>
 * 
 * @author Ander
 */
public class ManagerFactory {

    /**
     * Instancia única de {@link IManager} que se crea cuando se solicita.
     */
    private static IManager managerManager;

    /**
     * Obtiene la instancia única de {@link IManager}.
     * Si la instancia no existe, la crea y la devuelve.
     * 
     * @return La instancia de {@link IManager}.
     */
    public static IManager get() {
        if (managerManager == null) {
            managerManager = new ManagerRESTClient();
        }
        return managerManager;
    }
}
