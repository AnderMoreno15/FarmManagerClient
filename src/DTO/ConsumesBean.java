/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;


import DTO.ConsumesIdBean;
import java.beans.Beans;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pablo
 */



@XmlRootElement(name="consumes")
public class ConsumesBean implements Serializable, Cloneable {
   
    
     private static final long serialVersionUID = 1L;
    
   
    private ConsumesIdBean consumesId;
    private AnimalGroupBean animalGroup;
    private ProductBean product;
    private Date date;  
    private Float consumeAmount;  

    
    
    public ConsumesBean() {
    }
    

    @XmlElement
    public ConsumesIdBean getConsumesId() {
        return consumesId;
    }

    @XmlElement
    public Float getConsumeAmount() {
        return consumeAmount;
    }

   @XmlElement
    public AnimalGroupBean getAnimalGroup() {
        return animalGroup;
    }
    
    @XmlElement
    public ProductBean getProduct() {
        return product;
    }
    
    @XmlElement
    public Date getDate() {
        return date;
    }

    public void setConsumeAmount(Float consume) {
        this.consumeAmount = consume;
    }

    public void setAnimalGroup(AnimalGroupBean animalGroup) {
        this.animalGroup = animalGroup;
    }

    public void setProduct(ProductBean product) {  
    this.product = product;
    }


    public void setDate(Date date) {
        this.date = date;
    }
    

    public void setConsumesId(ConsumesIdBean id) {
        this.consumesId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consumesId != null ? consumesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsumesBean)) {
            return false;
        }
        ConsumesBean other = (ConsumesBean) object;
        if ((this.consumesId == null && other.consumesId != null) || (this.consumesId != null && !this.consumesId.equals(other.consumesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Consumes[ consume=" + consumeAmount + " ]";
    }
     @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
