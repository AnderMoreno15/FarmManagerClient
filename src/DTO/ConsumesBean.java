/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a consumption record in the system.
 * <p>
 * This bean stores information about a consumption event, including the associated
 * animal group, product, date of consumption, and amount consumed. It is designed
 * to be used in a JPA and JAXB environment, as indicated by its implementation of
 * {@code Serializable}, {@code Cloneable}, and the use of the {@code @XmlRootElement} annotation.
 * </p>
 * <p>
 * Note: The {@code clone()} method provides a shallow copy of the object.
 * </p>
 *
 * @author Pablo
 */
@XmlRootElement(name = "consumes")
public class ConsumesBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the consumption record.
     */
    private ConsumesIdBean consumesId;

    /**
     * The animal group associated with this consumption record.
     */
    private AnimalGroupBean animalGroup;

    /**
     * The product that was consumed.
     */
    private ProductBean product;

    /**
     * The date when the consumption occurred.
     */
    private Date date;

    /**
     * The amount of the product consumed.
     */
    private Float consumeAmount;

    /**
     * Returns the unique identifier of the consumption record.
     *
     * @return the consumption record ID.
     */
    public ConsumesIdBean getConsumesId() {
        return consumesId;
    }

    /**
     * Sets the unique identifier for the consumption record.
     *
     * @param id the consumption ID to set.
     */
    public void setConsumesId(ConsumesIdBean id) {
        this.consumesId = id;
    }

    /**
     * Returns the amount of the product consumed.
     *
     * @return the consumed amount.
     */
    public Float getConsumeAmount() {
        return consumeAmount;
    }

    /**
     * Sets the amount of the product consumed.
     *
     * @param consume the consumed amount to set.
     */
    public void setConsumeAmount(Float consume) {
        this.consumeAmount = consume;
    }

    /**
     * Returns the animal group associated with this consumption record.
     *
     * @return the associated animal group.
     */
    public AnimalGroupBean getAnimalGroup() {
        return animalGroup;
    }

    /**
     * Sets the animal group associated with this consumption record.
     *
     * @param animalGroup the animal group to set.
     */
    public void setAnimalGroup(AnimalGroupBean animalGroup) {
        this.animalGroup = animalGroup;
    }

    /**
     * Returns the product that was consumed.
     *
     * @return the consumed product.
     */
    public ProductBean getProduct() {
        return product;
    }

    /**
     * Sets the product that was consumed.
     *
     * @param product the product to set.
     */
    public void setProduct(ProductBean product) {
        this.product = product;
    }

    /**
     * Returns the date of the consumption.
     *
     * @return the date of consumption.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the consumption.
     *
     * @param date the date to set.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Creates and returns a shallow copy of this {@code ConsumesBean}.
     *
     * @return a clone of this {@code ConsumesBean}.
     * @throws CloneNotSupportedException if the object's class does not support cloning.
     */
    @Override
    public ConsumesBean clone() throws CloneNotSupportedException {
        return (ConsumesBean) super.clone();
    }

    /**
     * Generates a hash code based on the unique identifier of the consumption record.
     *
     * @return the hash code for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consumesId != null ? consumesId.hashCode() : 0);
        return hash;
    }

    /**
     * Compares this {@code ConsumesBean} to another object for equality.
     * <p>
     * Two consumption records are considered equal if they have the same unique identifier.
     * </p>
     *
     * @param object the object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConsumesBean)) {
            return false;
        }
        ConsumesBean other = (ConsumesBean) object;
        return (this.consumesId != null || other.consumesId == null) &&
               (this.consumesId == null || this.consumesId.equals(other.consumesId));
    }

    /**
     * Returns a string representation of the consumption record.
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return "entities.Consumes[ consumeId=" + consumesId + " consumeAmount=" + consumeAmount + " ]";
    }
}
