package DTO;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a user in the system.
 * <p>
 * This class serves as a Data Transfer Object (DTO) for user-related information, 
 * encapsulating details such as name, email, phone number, and address.
 * It is serializable and can be used for data exchange, including XML-based communication.
 * </p>
 *
 * @author Ander
 * @version 1.0
 */
@XmlRootElement(name = "farmUser")
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     */
    protected Long id;

    /**
     * The user's full name.
     */
    protected String name;

    /**
     * The user's email address.
     */
    protected String email;

    /**
     * The user's phone number.
     */
    protected String phone;

    /**
     * The user's city of residence.
     */
    protected String city;

    /**
     * The user's postal code.
     */
    protected String zip;

    /**
     * The user's street address.
     */
    protected String street;

    /**
     * Default constructor.
     */
    public UserBean() {
    }

    /**
     * Constructs a new user with the given details.
     *
     * @param name   The user's full name.
     * @param email  The user's email address.
     * @param phone  The user's phone number.
     * @param city   The user's city of residence.
     * @param zip    The user's postal code.
     * @param street The user's street address.
     */
    public UserBean(String name, String email, String phone, String city, String zip, String street) {
        super();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.zip = zip;
        this.street = street;
    }

    /**
     * Gets the user's ID.
     *
     * @return The user's unique identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user's ID.
     *
     * @param id The user's unique identifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user's full name.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's full name.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's email address.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's phone number.
     *
     * @return The user's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phone The new phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the user's city of residence.
     *
     * @return The user's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the user's city of residence.
     *
     * @param city The new city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the user's postal code.
     *
     * @return The user's zip code.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the user's postal code.
     *
     * @param zip The new zip code.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Gets the user's street address.
     *
     * @return The user's street.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the user's street address.
     *
     * @param street The new street.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserBean)) {
            return false;
        }
        UserBean other = (UserBean) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A string with the user's ID.
     */
    @Override
    public String toString() {
        return "User[ id=" + id + " ]";
    }
}
