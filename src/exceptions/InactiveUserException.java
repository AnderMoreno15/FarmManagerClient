/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Exception thrown to indicate that a user is inactive and unable to perform the requested operation.
 * This exception is commonly used during authentication or authorization processes to prevent inactive users from accessing the system.
 * 
 * Usage Example:
 * if (!user.isActive()) {
 *     throw new InactiveUserException("User is inactive");
 * }
 * 
 * @see Exception
 * 
 * @author Aitziber
 */
public class InactiveUserException extends Exception {

    /**
     * Constructs a new InactiveUserException with null as its detail message.
     */
    public InactiveUserException() {
    }

    /**
     * Constructs a new InactiveUserException with the specified detail message.
     *
     * @param msg the detail message
     */
    public InactiveUserException(String msg) {
        super(msg);
    }
}
