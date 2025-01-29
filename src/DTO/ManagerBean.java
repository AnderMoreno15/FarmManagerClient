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
 *
 * @author Ander
 */
@XmlRootElement(name = "manager")
public class ManagerBean extends UserBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean isActive;
    private String password;
    //private List<PurchaseEntity> purchases;
    private List<AnimalGroupBean> animalGroups;

    public ManagerBean() {
    }

    public ManagerBean(boolean isActive, String password, String name, String email, String phone, String city, String zip, String street) {
        super(name, email, phone, city, zip, street);
        this.isActive = isActive;
        this.password = password;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//    public List<PurchaseEntity> getPurchases() {
//        return purchases;
//    }
//
//    public void setPurchases(List<PurchaseEntity> purchases) {
//        this.purchases = purchases;
//    }

    public List<AnimalGroupBean> getAnimalGroups() {
        return animalGroups;
    }

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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManagerBean)) {
            return false;
        }
        ManagerBean other = (ManagerBean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Manager[ id=" + id + " name=" + name + " email=" + email + " password=" + password + " address=" + street + " city=" + city + " zip=" + zip + "]";
    }

}
