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
 *
 * @author Ander
 */

@XmlRootElement(name = "animalGroup")
public class AnimalGroupBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String area;
    private String description;
    private Date creationDate;
    private Integer animals;
    private Integer consume;
    private List<ManagerBean> managers;

    public AnimalGroupBean() {
    }

    public AnimalGroupBean(Long id, String name, String area, String description, Date creationDate, Integer animals, List<ManagerBean> managers) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.description = description;
        this.creationDate = creationDate;
        this.animals = animals;
        this.managers = managers;
    }

    public AnimalGroupBean clone() throws CloneNotSupportedException {
        return (AnimalGroupBean) super.clone();
    }

    public Integer getConsume() {
        return consume;
    }

    public void setConsume(Integer consume) {
        this.consume = consume;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getAnimals() {
        return animals;
    }

    public void setAnimals(Integer animals) {
        this.animals = animals;
    }
//
//    public List<ConsumeEntity> getConsumes() {
//        return consumes;
//    }
//
//    public void setConsumes(List<ConsumeEntity> consumes) {
//        this.consumes = consumes;
//    }

    public List<ManagerBean> getManagers() {
        return managers;
    }

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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
