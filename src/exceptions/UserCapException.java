/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Exception thrown to indicate that the user capacity limit has been reached.
 * This exception is used when a server or application cannot accept more users due to capacity restrictions.
 * 
 * Usage Example:
 * if (userLimitReached) {
 *     throw new UserCapException("User capacity limit has been reached");
 * }
 * 
 * @see Exception
 * 
 * @author Aitziber
 */
public class UserCapException extends Exception {

    /**
     * Constructs a new UserCapException with null as its detail message.
     */
    public UserCapException() {
    }

    /**
     * Constructs a new UserCapException with the specified detail message.
     *
     * @param msg the detail message
     */
    public UserCapException(String msg) {
        super(msg);
    }
}
