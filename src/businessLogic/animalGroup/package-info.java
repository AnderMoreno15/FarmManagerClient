/**
 * Contains classes related to the REST client for managing animal groups.
 * <p>
 * The REST client is responsible for handling communication with the external 
 * service for animal groups, encapsulating the logic for making API calls, 
 * and processing the responses. It includes the client implementation, 
 * its interface for defining available operations, and the factory for 
 * creating instances of the client.
 * </p>
 * 
 * <h2>Main Classes:</h2>
 * <ul>
 *   <li>{@link AnimalGroupRESTClient} - Handles REST communication for managing animal groups.</li>
 *   <li>{@link IAnimalGroup} - Defines the operations for interacting with the REST service.</li>
 *   <li>{@link AnimalGroupFactory} - Responsible for creating instances of {@link AnimalGroupRESTClient}.</li>
 * </ul>
 *
 * @see IAnimalGroup
 * @author Ander
 * @version 1.0
 */
package businessLogic.animalGroup;