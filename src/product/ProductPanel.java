package product;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import order.Bill;

public class ProductPanel extends JPanel{
	private	Product product;
	private JLabel name = new JLabel();
	private JLabel price = new JLabel();
	
	public ProductPanel(Rectangle s,Product product) {
		this.setBounds(s);
		this.setLayout(null);
		this.setBorder(new TitledBorder(new LineBorder(Color.GRAY,2)));
		this.setBackground(Color.white);
		this.product = product;
		if(product.getProductName().equals("+")) {
			name.setText(product.getProductName());
			name.setBounds(0,0,getWidth(),getHeight());
			name.setFont(new Font("",Font.PLAIN,27));
			name.setHorizontalAlignment(JLabel.CENTER);
			name.setForeground(Color.GRAY);
			setToolTipText("클릭하여 상품을 추가합니다.");
			add(name);
		}
		else {
		name.setText(" " + product.getProductName());
		name.setBounds(0,0,getWidth(),30);
		name.setFont(new Font("",Font.BOLD,21));
		price.setText(getPrice() + " ");
		price.setBounds(0,getHeight()-30,getWidth(),30);
		price.setFont(new Font("",Font.PLAIN,18));
		price.setHorizontalAlignment(JLabel.RIGHT);
		add(name);
		add(price);
		}
	}
	public Product getProduct() {
		return product;
	}
	public String getPrice() {
		return Bill.intToWonFormat(product.getProductPrice());
	}
	public String getProductName() {
		return product.getProductName();
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
//private	Product product;
//
//
//public ProductButton(Product product) {
//	this.product = product;	
//	this.setText(getProductName());
//	this.setToolTipText(product.getProductName());
//}
//public Product getProduct() {
//	return product;
//}
//public String getPrice() {
//	return Bill.intToWonFormat(product.getProductPrice());
//}
//public String getProductName() {
//	return product.getProductName();
//}
//public void setProduct(Product product) {
//	this.product = product;
//}
