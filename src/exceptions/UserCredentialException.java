/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Exception thrown to indicate invalid user credentials, typically during authentication.
 * This exception is used to signal that the provided username or password is incorrect.
 * 
 * Usage Example:
 * if (!credentialsAreValid) {
 *     throw new UserCredentialException("Invalid username or password");
 * }
 * 
 * @see Exception
 * 
 * @author Aitziber
 */
public class UserCredentialException extends Exception {

    /**
     * Constructs a new UserCredentialException with null as its detail message.
     */
    public UserCredentialException() {
    }

    /**
     * Constructs a new UserCredentialException with the specified detail message.
     *
     * @param msg the detail message
     */
    public UserCredentialException(String msg) {
        super(msg);
    }
}
