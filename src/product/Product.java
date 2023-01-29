package product;

public class Product implements Comparable<Product>{
	private int productNo = 1;
	private int productPrice;
	private int productCount;
	
	public Product(String productName, int productPrice) {
		this.productName = productName;
		this.productPrice = productPrice;
	}
	
	public Product(String productName,int productCount, int productPrice) {
		this.productName = productName;
		this.productCount = productCount;
		this.productPrice = productPrice;
	}
	
	public int getProductCount() {
		return productCount;
	}
	
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	private String productName;
	public int getProductNo() {
		return productNo;
	}
	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public String toString() {
		return productName + " " + productPrice;
	}
	public Product getCopy() {
		return new Product(productName,productPrice);
	}
	public int compareTo(Product o) {
		return this.productName.compareTo(o.productName);
	}
	
}
