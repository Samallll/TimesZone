package com.timeszone.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeszone.model.dto.ProductDTO;
import com.timeszone.model.product.Category;
import com.timeszone.model.product.Product;
import com.timeszone.model.product.SubCategory;
import com.timeszone.repository.CategoryRepository;
import com.timeszone.repository.ProductRepository;
import com.timeszone.repository.SubCategoryRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	public List<Product> getAllProducts(){
		
		return productRepository.findAll();
	}

	public void updateProduct(ProductDTO p) {
		
		Product editProduct = productRepository.findById(p.getProductId()).get();
		SubCategory scpd;
		editProduct.setCaseSize(p.getCaseSize());
		editProduct.setDescription(p.getDescription());
		editProduct.setEnabled(p.getIsEnabled());
		editProduct.setPrice(p.getPrice());
		editProduct.setQuantity(p.getQuantity());
		editProduct.setProductName(p.getProductName());
		
		Set<SubCategory> subCategoriesOfEditProduct = editProduct.getSubcategories();
		
		if(subCategoriesOfEditProduct.isEmpty()) {

			for(String s:p.getSelectedSubCategories()) {
				scpd = subCategoryRepository.findBySubCategoryName(s);
				Category c = scpd.getCategory();
				c.getProducts().add(editProduct);
				categoryRepository.save(c);
				editProduct.getCategories().add(c);
				editProduct.getSubcategories().add(scpd);
			}
		}
		else {
			
			for(String s:p.getSelectedSubCategories()) {
				scpd = subCategoryRepository.findBySubCategoryName(s);
				for(SubCategory scp:subCategoriesOfEditProduct) {
					if((scp.getCategory().getCategoryName() == scpd.getCategory().getCategoryName())&&
							scp.getSubCategoryName() != scpd.getSubCategoryName()) {
						
						editProduct.getSubcategories().remove(scp);
						editProduct.getSubcategories().add(scpd);
					}
				}
			}
		}

		productRepository.save(editProduct);
		
	}
	
	

	public void deleteProduct(Integer id) {
		
		Product deleteProduct = productRepository.findById(id).get();
		Set<SubCategory> subCategories = deleteProduct.getSubcategories();
		Category c;
		for(SubCategory s:subCategories) {
			s.getProducts().remove(deleteProduct);
			c = s.getCategory();
			c.getProducts().remove(deleteProduct);
			categoryRepository.save(c);
			subCategoryRepository.save(s);
		}
		productRepository.deleteById(id);
	}
	
	public ProductDTO convertToProduct(Product p) {
		
		ProductDTO pd = new ProductDTO();
		List<String> pdSubCategoryList = new ArrayList<>();	
		Map<String,String> combo = new HashMap<>();
		
		pd.setCaseSize(p.getCaseSize());
		pd.setEnabled(p.getIsEnabled());
		pd.setDescription(p.getDescription());
		pd.setPrice(p.getPrice());
		pd.setProductId(p.getProductId());
		pd.setProductName(p.getProductName());
		pd.setQuantity(p.getQuantity()); 
		for(SubCategory sc:p.getSubcategories()) {
			pdSubCategoryList.add(sc.getSubCategoryName());
			combo.put(sc.getCategory().getCategoryName(), sc.getSubCategoryName());
			
		}
		pd.setSelectedSubCategories(pdSubCategoryList);
		pd.setCategoryAndSubCategory(combo);
		pd.setProductImages(p.getProductImages());
		
		return pd;
	}

	public void modifyProduct(Product product) {
		
		productRepository.save(product);
	}
}
