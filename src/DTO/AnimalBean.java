/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object used in UI and client side for representing Animal entity.
 * It is also used as data model for a TableView in the UI.
 * @author Aitziber
 */
@XmlRootElement(name="animal")
public class AnimalBean implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String subespecies;
    private Date birthdate;
    private float monthlyConsume;
    private AnimalGroupBean animalGroup;
    private SpeciesBean species;
    
    public AnimalBean(){
    }
    
    public AnimalBean(Long id, String name, String subespecies, Date birthdate, 
                      float monthlyConsume, AnimalGroupBean animalGroup, SpeciesBean species) {
        this.id = id;
        this.name = name;
        this.subespecies = subespecies;
        this.birthdate = birthdate;
        this.monthlyConsume = monthlyConsume;
        this.animalGroup = animalGroup;
        this.species = species;
    }
    
    public AnimalBean clone() throws CloneNotSupportedException{
        
        return (AnimalBean) super.clone();
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

    public String getSubespecies() {
        return subespecies;
    }

    public void setSubespecies(String subespecies) {
        this.subespecies = subespecies;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public float getMonthlyConsume() {
        return monthlyConsume;
    }

    public void setMonthlyConsume(float monthlyConsume) {
        this.monthlyConsume = monthlyConsume;
    }

    public AnimalGroupBean getAnimalGroup() {
        return animalGroup;
    }

    public void setAnimalGroup(AnimalGroupBean animalGroup) {
        this.animalGroup = animalGroup;
    }

    public SpeciesBean getSpecies() {
        return species;
    }

    public void setSpecies(SpeciesBean species) {
        this.species = species;
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
        if (!(object instanceof AnimalBean)) {
            return false;
        }
        AnimalBean other = (AnimalBean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Animal[ id=" + id + " ]";
    }
    
}
