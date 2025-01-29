/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Data Transfer Object used in UI and client side for representing Species entity.
 * It is also used as data model for a TableView in the UI.
 * @author Aitziber
 */
@XmlRootElement(name="species")
public class SpeciesBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private List<AnimalBean> animals;
    private ID_Species_Products_AgeBean perAge;
    
    public SpeciesBean() {
    }

    public SpeciesBean(Long id, String name, List<AnimalBean> animals, ID_Species_Products_AgeBean perAge) {
        this.id = id;
        this.name = name;
        this.animals = animals;
        this.perAge = perAge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<AnimalBean> getAnimals() {
        return animals;
    }

    public void setAnimals(List<AnimalBean> animals) {
        this.animals = animals;
    }

    public ID_Species_Products_AgeBean getPerAge() {
        return perAge;
    }

    public void setPerAge(ID_Species_Products_AgeBean perAge) {
        this.perAge = perAge;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SpeciesBean)) {
            return false;
        }
        SpeciesBean other = (SpeciesBean) object;
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