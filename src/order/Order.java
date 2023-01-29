package order;

import product.Product;

public class Order implements Comparable<Order>{
	private int productCount = 1;
	private String productName;
	private int productPrice;
	public Order(Product product) {
		this.productName = product.getProductName();
		this.productPrice = product.getProductPrice();
		this.productCount = product.getProductCount();
	}
	public Order(String productName,String productCount,String productPrice) {
		this.productName = productName;
		this.productCount = Integer.parseInt(productCount);
		this.productPrice = Bill.WonFormatToInt(productPrice) / this.productCount;
	}
	public void add(int count) {
		productCount+= count;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
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
	public int getTotalPrice() {
		return productCount*productPrice;
	}
	public String toString() {
		return productName + " " + productCount + " " + productPrice;
	}
	@Override
	public int compareTo(Order o) {
		return -Integer.compare(this.productCount, o.productCount);
	}
}
