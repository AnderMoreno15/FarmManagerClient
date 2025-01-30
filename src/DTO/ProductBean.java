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
 *
 * @author InigoFreire
 */
@XmlRootElement
public class ProductBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    Long id;
    private String name;
    private Float monthlyConsume;
    private Float price;
    private Integer stock;
    private ProviderBean providerId;
    private Date createDate;

    public ProductBean() {
    }
    
    public ProductBean(Long id, String name, Float monthlyConsume, Float price, Integer stock, ProviderBean providerId, Date createDate) {
        this.id = id;
        this.name = name;
        this.monthlyConsume = monthlyConsume;
        this.price = price;
        this.stock = stock;
        this.providerId = providerId;
        this.createDate = createDate;
    }
    
    public ProductBean clone() throws CloneNotSupportedException{
        
        return (ProductBean) super.clone();
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

    public Float getMonthlyConsume() {
        return monthlyConsume;
    }

    public void setMonthlyConsume(Float monthlyConsume) {
        this.monthlyConsume = monthlyConsume;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ProviderBean getProviderId() {
        return providerId;
    }

    public void setProviderId(ProviderBean providerId) {
        this.providerId = providerId;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        if (!(object instanceof ProductBean)) {
            return false;
        }
        ProductBean other = (ProductBean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProductEntity[ id=" + id + " ]";
    }

}