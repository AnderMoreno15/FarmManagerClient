/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 * Data Transfer Object for representing the embedded ID for Species-Products-Age.
 * This DTO is used to transfer data related to this composite key.
 * @author Aitziber
 */
public class ID_Species_Products_AgeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private long productId;
    private long speciesId;

    public ID_Species_Products_AgeBean() {
    }

    public ID_Species_Products_AgeBean(long productId, long speciesId) {
        this.productId = productId;
        this.speciesId = speciesId;
    }

    @XmlElement(name="productId")
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @XmlElement(name="speciesId")
    public long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(long speciesId) {
        this.speciesId = speciesId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productId;
        hash += (int) speciesId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ID_Species_Products_AgeBean)) {
            return false;
        }
        ID_Species_Products_AgeBean other = (ID_Species_Products_AgeBean) object;
        return this.productId == other.productId && this.speciesId == other.speciesId;
    }

    @Override
    public String toString() {
        return "DTO.ID_Species_Products_AgeBean[ productId=" + productId + ", speciesId=" + speciesId + " ]";
    }
}
