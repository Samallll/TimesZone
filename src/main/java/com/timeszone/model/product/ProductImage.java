package com.timeszone.model.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ProductImage")
public class ProductImage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;
	
    private String imageName;
    
    @ManyToOne
    private Product product;

	public Integer getIamgeId() {
		return imageId;
	}

	public void setIamgeId(Integer imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductImage(Integer imageId, String imageName, Product product) {
		super();
		this.imageId = imageId;
		this.imageName = imageName;
		this.product = product;
	}

	public ProductImage() {
		super();
	}

}
