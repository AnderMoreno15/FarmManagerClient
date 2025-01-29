/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Exception thrown to indicate that a user attempting to register already exists in the system.
 * This exception is typically thrown during the registration process to prevent duplicate user entries.
 * 
 * Usage Example:
 * if (userAlreadyExists) {
 *     throw new ExistingUserException("User already exists");
 * }
 * 
 * @see Exception
 * 
 * @author Aitziber
 */
public class ExistingUserException extends Exception {

    /**
     * Constructs a new ExistingUserException with null as its detail message.
     */
    public ExistingUserException() {
    }

    /**
     * Constructs a new ExistingUserException with the specified detail message.
     *
     * @param msg the detail message
     */
    public ExistingUserException(String msg) {
        super(msg);
    }
}
