/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the composite key for a consumption record.
 * <p>
 * This bean serves as a unique identifier for a consumption event,
 * consisting of an {@code animalGroupId} and a {@code productId}.
 * It is designed for use in JPA and JAXB environments, as indicated
 * by its implementation of {@code Serializable}, {@code Cloneable}, and
 * the use of the {@code @XmlRootElement} annotation.
 * </p>
 * 
 * <p>
 * Note: The {@code equals()} and {@code hashCode()} methods are overridden
 * to ensure correct behavior when used as a key in collections or persistence frameworks.
 * </p>
 *
 * @author Pablo
 */
@XmlRootElement(name = "consumesId")
public class ConsumesIdBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * The identifier of the animal group associated with the consumption record.
     */
    private Long animalGroupId;

    /**
     * The identifier of the product associated with the consumption record.
     */
    private Long productId;

    /**
     * Default no-argument constructor required for serialization.
     */
    public ConsumesIdBean() {
    }

    /**
     * Constructs a {@code ConsumesIdBean} with the specified product ID and animal group ID.
     *
     * @param productId the ID of the product.
     * @param animalGroupId the ID of the animal group.
     */
    public ConsumesIdBean(Long productId, Long animalGroupId) {
        this.productId = productId;
        this.animalGroupId = animalGroupId;
    }

    /**
     * Returns the animal group ID.
     *
     * @return the animal group ID.
     */
    public Long getAnimalGroupId() {
        return animalGroupId;
    }

    /**
     * Sets the animal group ID.
     *
     * @param animalGroupId the animal group ID to set.
     */
    public void setAnimalGroupId(Long animalGroupId) {
        this.animalGroupId = animalGroupId;
    }

    /**
     * Returns the product ID.
     *
     * @return the product ID.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     *
     * @param productId the product ID to set.
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Compares this {@code ConsumesIdBean} with another object for equality.
     * <p>
     * Two instances are considered equal if they have the same {@code animalGroupId}
     * and {@code productId}.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if both objects have the same values, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumesIdBean that = (ConsumesIdBean) o;
        return Objects.equals(animalGroupId, that.animalGroupId) &&
               Objects.equals(productId, that.productId);
    }

    /**
     * Generates a hash code based on the {@code animalGroupId} and {@code productId}.
     *
     * @return the hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(animalGroupId, productId);
    }
}
