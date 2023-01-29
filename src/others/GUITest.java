package others;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.JFrame;

import product.Product;
import product.ProductCategory;
import product.ProductListPanel;
public class GUITest {
	static JFrame frame = new JFrame("GUITest");;
	public static void setUpFrame01() {
		frame.setSize(800, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
//	public static void setUpFrame03_ProductCategoryTest() {
//
//		ProductCategory pc = new ProductCategory("TestCategory");
//		for(int i = 0 ; i < 16 ; i++) {
//			pc.addProduct(new Product("test" + i, i*1000));
//		}
//		ProductCategoryPanel pcp = new ProductCategoryPanel(pc);
//		frame.add(pcp);
//	}
	//	public static void setUpFrame04_GridBagLayoutTest() {
	//		GridBagLayout gbl = new GridBagLayout();
	//		GridBagMeneger gbc = new GridBagMeneger(gbl,frame.getContentPane());
	//		frame.setLayout(gbl);
	//		JPanel red = new JPanel();
	//		JPanel red1 = new JPanel();
	//		JPanel red2 = new JPanel();
	//		JPanel red3 = new JPanel();
	//		JPanel red4 = new JPanel();
	//		red.setBackground(Color.red);
	//		red1.setBackground(Color.blue);
	//		red2.setBackground(Color.white);
	//		red3.setBackground(Color.black);
	//		red4.setBackground(Color.pink);
	//		ProductCategoryPanel pc = new ProductCategoryPanel("TestCategory");
	//		for(int i = 0 ; i < 16 ; i++) {
	//			pc.productInsert(new ProductButton("test" + Integer.toString(i), i*1000));
	//		}
	//		gbc.gblAdd(red, 0, 0, 1, 10);
	//		gbc.gblAdd(red1, 1, 0, 3, 2);
	//		gbc.gblAdd(red2, 1, 2, 3, 1);
	//		gbc.gblAdd(pc,1, 3, 3, 6);
	//		gbc.gblAdd(red3, 1, 9, 3, 2);
	//		gbc.gblAdd(red4, 0, 10, 1, 1);
	//	}
	public static void show() {
		frame.setVisible(true);
	}
	public static void minMax(Vector<Integer> v, double limitmax,double limitmin) {
		int max,min;
		double q;
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		for(Integer i :v) {
			if(i > max) max = i;
			if(i < min) min = i;
		}
		q = (limitmax-limitmin) / (max - min);
		for(Integer i :v) {
			double j = (i - min)* q + limitmin;	
			i = (int)Math.round(j);
			System.out.println(i);
		}
		
	}
	public static void main(String[] args) {
		Vector<Integer> v = new Vector<Integer>();
		for(int i = 0 ; i < 10 ; i++) {
			v.add(i*15);
		}
		minMax(v,300,100);
	}
}
