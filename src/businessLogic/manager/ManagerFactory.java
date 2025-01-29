/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic.manager;

import businessLogic.manager.IManager;

/**
 * 
 * @author Ander
 */
public class ManagerFactory {
    
    private static IManager managerManager;

    public static IManager get() {
        if (managerManager == null) {
            managerManager = new ManagerRESTClient();
        }
        return managerManager;
    }
}
