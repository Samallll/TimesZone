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
    
    private byte[] image;
    
    private long size;
    
    @ManyToOne
    private Product product;

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

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public ProductImage(Integer imageId, String imageName, Product product,byte[] image) {
		super();
		this.imageId = imageId;
		this.imageName = imageName;
		this.product = product;
		this.image=image;
	}

	public ProductImage() {
		super();
	}
	
	public ProductImage(String imageName, Product product,byte[] image) {
		super();
		this.image = image;
		this.imageName = imageName;
		this.product = product;
	}
}
