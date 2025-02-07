/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The `ManagerBean` class represents a manager in the system.
 * It extends the `UserBean` class and implements the `Serializable` and `Cloneable` interfaces.
 * 
 * @author Ander
 * @author Aitziber
 */
@XmlRootElement(name = "manager")
public class ManagerBean extends UserBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private boolean isActive;
    private String password;
    private List<AnimalGroupBean> animalGroups;

    /**
     * Default constructor for the `ManagerBean` class.
     */
    public ManagerBean() {
    }

    /**
     * Parameterized constructor for the `ManagerBean` class.
     * 
     * @param isActive Indicates whether the manager is active.
     * @param password The manager's password.
     * @param name The manager's name.
     * @param email The manager's email.
     * @param phone The manager's phone number.
     * @param city The manager's city.
     * @param zip The manager's zip code.
     * @param street The manager's street address.
     */
    public ManagerBean(boolean isActive, String password, String name, String email, String phone, String city, String zip, String street) {
        super(name, email, phone, city, zip, street);
        this.isActive = isActive;
        this.password = password;
    }

    /**
     * Creates and returns a copy of this `ManagerBean` object.
     * 
     * @return A clone of this `ManagerBean` instance.
     * @throws CloneNotSupportedException If cloning is not supported.
     */
    public ManagerBean clone() throws CloneNotSupportedException {
        return (ManagerBean) super.clone();
    }

    /**
     * Returns the ID of the manager.
     * 
     * @return The manager's ID.
     */
    @Override
    public Long getId() {
        return super.getId();
    }

    /**
     * Sets the ID of the manager.
     * 
     * @param id The manager's ID.
     */
    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    /**
     * Checks if the manager is active.
     * 
     * @return `true` if the manager is active, `false` otherwise.
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Sets the active status of the manager.
     * 
     * @param isActive The active status to set.
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns the manager's password.
     * 
     * @return The manager's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the manager's password.
     * 
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the list of animal groups associated with the manager.
     * 
     * @return The list of `AnimalGroupBean` objects.
     */
    public List<AnimalGroupBean> getAnimalGroups() {
        return animalGroups;
    }

    /**
     * Sets the list of animal groups associated with the manager.
     * 
     * @param animalGroups The list of `AnimalGroupBean` objects to set.
     */
    public void setAnimalGroups(List<AnimalGroupBean> animalGroups) {
        this.animalGroups = animalGroups;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ManagerBean)) {
            return false;
        }
        ManagerBean other = (ManagerBean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the `ManagerBean` object.
     * 
     * @return A string containing the manager's details.
     */
    @Override
    public String toString() {
        return "Manager[ id=" + id + " name=" + name + " email=" + email + " password=" + password + " address=" + street + " city=" + city + " zip=" + zip + "]";
    }
}