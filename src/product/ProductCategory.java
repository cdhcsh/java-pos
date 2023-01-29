package product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class ProductCategory implements Comparable<ProductCategory>{
	private String categoryName;
	private Vector<Product> products = new Vector<Product>();
	
	public ProductCategory(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public boolean addProduct(Product product) {
		if(redundancyCheckProductName(product.getProductName())) {
			products.add(product);
			return true;
		}
		else return false;
	}
	public boolean removeProduct(Product product) {
		return products.remove(product);
	}
	public void editProduct(Product product,String name,String price) {
		product.setProductName(name);
		product.setProductPrice(Integer.parseInt(price));
	}
	public Vector<Product> getProducts(){
		Vector<Product> tmp = new Vector<Product>();
		for(Product p : products)
			tmp.add(p);
		return tmp;
	}
	public boolean redundancyCheckProductName(String name) { // 상품이름 중복검사
		for(Product p : products) {
			if(p.getProductName().equals(name))
				return false;
		}
		return true;
	}

	public int compareTo(ProductCategory o) {
		return this.getCategoryName().compareTo(o.getCategoryName());
	}
}
