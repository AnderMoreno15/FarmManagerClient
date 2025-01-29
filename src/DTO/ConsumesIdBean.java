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
 *
 * @author Pablo
 */
@XmlRootElement(name="consumesId")
public class ConsumesIdBean implements Serializable, Cloneable {
    
    
    private Long animalGroupId;   
    private Long productId;

    
    
    //private Long productId;
    
    // Constructor vacío necesario
    public ConsumesIdBean() {
    }
    
    // Constructor con parámetros
    public ConsumesIdBean(Long productId, Long animalGroupId) {
       
        
        this.productId= productId;
        this.animalGroupId = animalGroupId;
    }
    
    // Getters y setters con nombres que coincidan exactamente
    public Long getAnimalGroupId() {
        return animalGroupId;
    }
    
    public void setAnimalGroupId(Long animalGroupId) {
        this.animalGroupId = animalGroupId;
    }
    public void setProductId(Long productId) {
        this.productId =productId;
    }

    public Long getProductId() {
        return productId;
    }
    
 
    
    // Equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumesIdBean that = (ConsumesIdBean) o;
        return Objects.equals(animalGroupId, that.animalGroupId) &&
               Objects.equals(productId, that.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(animalGroupId, productId);
    }
}

    
    
   
    

