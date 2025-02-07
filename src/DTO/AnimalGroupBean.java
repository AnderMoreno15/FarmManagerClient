/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an animal group with its related attributes.
 * <p>
 * This bean contains information about an animal group, including its name, area, description, creation date, consumption value and a list of managers. It is designed to be used in a JPA and JAXB environment, as indicated by its implementation of Serializable, Cloneable and the use of the {@code @XmlRootElement} annotation.
 * </p>
 * <p>
 * Note: The {@code clone()} method provides a shallow copy of the object.
 * </p>
 *
 * @author Ander
 */
@XmlRootElement(name = "animalGroup")
public class AnimalGroupBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier of the animal group.
     */
    private Long id;

    /**
     * The name of the animal group.
     */
    private String name;

    /**
     * The area where the animal group is located.
     */
    private String area;

    /**
     * A description of the animal group.
     */
    private String description;

    /**
     * The creation date of the animal group.
     */
    private Date creationDate;

    /**
     * The consumption value associated with the animal group.
     */
    private Double consume;

    /**
     * The list of managers responsible for the animal group.
     */
    private List<ManagerBean> managers;

    /**
     * Default no-argument constructor.
     */
    public AnimalGroupBean() {
    }
    
    /**
     * Constructs an AnimalGroupBean with the specified attributes for testing purposes.
     *
     * @param id id of the animal group.
     * @param name the name of the animal group.
     * @param area the area where the animal group is located.
     * @param description a description of the animal group.
     * @param creationDate the creation date of the animal group.
     */
    public AnimalGroupBean(Long id, String name, String area, String description, Date creationDate) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.description = description;
        this.creationDate = creationDate;
    }

    /**
     * Constructs an AnimalGroupBean with the specified attributes.
     *
     * @param id the unique identifier of the animal group.
     * @param name the name of the animal group.
     * @param area the area where the animal group is located.
     * @param description a description of the animal group.
     * @param creationDate the creation date of the animal group.
     * @param managers the list of managers associated with the animal group.
     */
    public AnimalGroupBean(Long id, String name, String area, String description, Date creationDate, List<ManagerBean> managers) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.description = description;
        this.creationDate = creationDate;
        this.managers = managers;
    }

    /**
     * Creates and returns a shallow copy of this AnimalGroupBean.
     *
     * @return a clone of this AnimalGroupBean.
     * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface.
     */
    @Override
    public AnimalGroupBean clone() throws CloneNotSupportedException {
        return (AnimalGroupBean) super.clone();
    }

    /**
     * Returns the consumption value associated with the animal group.
     *
     * @return the consumption value.
     */
    public Double getConsume() {
        return consume;
    }

    /**
     * Sets the consumption value for the animal group.
     *
     * @param consume the consumption value to set.
     */
    public void setConsume(Double consume) {
        this.consume = consume;
    }

    /**
     * Returns the unique identifier of the animal group.
     *
     * @return the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the animal group.
     *
     * @param id the id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the animal group.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the animal group.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the area where the animal group is located.
     *
     * @return the area.
     */
    public String getArea() {
        return area;
    }

    /**
     * Sets the area where the animal group is located.
     *
     * @param area the area to set.
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Returns the description of the animal group.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the animal group.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the creation date of the animal group.
     *
     * @return the creation date.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the animal group.
     *
     * @param creationDate the creation date to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the list of managers associated with the animal group.
     *
     * @return the list of managers.
     */
    public List<ManagerBean> getManagers() {
        return managers;
    }

    /**
     * Sets the list of managers for the animal group.
     *
     * @param managers the list of managers to set.
     */
    public void setManagers(List<ManagerBean> managers) {
        this.managers = managers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AnimalGroupBean)) {
            return false;
        }
        AnimalGroupBean other = (AnimalGroupBean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
