package product;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import database.FileIOMeneger;
import database.Session;
import sun.java2d.pipe.hw.AccelDeviceEventListener;

public class ProductListPanel extends JPanel{
	public static final int COLS = 4;
	public static final int ROWS = 4;
	private int rowh,colw,mode; // mode : 0 ???? , mode : 1 ????
	private Vector<ProductPanel> productButtons;
	private ProductCategory productCategory;
	private JPanel displayPanel = new JPanel(null);
	private Vector<JPanel> displayPanels = new Vector<JPanel>();
	private JPanel btnPanel = new JPanel(null);
	private JLabel indexl = new JLabel();
	private JButton btns[] = new JButton[2];
	private ProductClickListener pl = new ProductClickListener();
	private ButtonClickListener bl = new ButtonClickListener();
	
	public ProductListPanel(ProductCategory productCategory,Rectangle s,int mode) {
		this.mode = mode;
		this.productCategory = productCategory;
		this.setBounds(s);
		this.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		this.setLayout(null);
		this.setUp();
	}
	public void setUp() {
		this.removeAll();
		rowh = 0;
		colw = 0;
		displayPanel = new JPanel(null);
		displayPanels = new Vector<JPanel>();
		btnPanel = new JPanel(null);
		indexl = new JLabel();
		btns = new JButton[2];
		pl = new ProductClickListener();
		bl = new ButtonClickListener();
		
		Vector<Product> products = productCategory.getProducts();
		if(mode == 1) {
			products.add(new Product("+", 0));
		}
		productButtons = new Vector<ProductPanel>();

		btnPanel.setBounds(0,0,this.getWidth(),50);
		btnPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		
		btns[0] = new JButton("??");
		btns[0].setBounds(btnPanel.getWidth()/2 - 110, 5, 70, 40);
		btns[0].addActionListener(bl);

		btns[1] = new JButton("??");
		btns[1].setBounds(btnPanel.getWidth()/2 + 60, 5, 70, 40);
		btns[1].addActionListener(bl);
		
		indexl.setText("1");
		indexl.setOpaque(true);
		indexl.setBackground(Color.white);
		indexl.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		indexl.setFont(new Font("????",Font.BOLD,20));
		indexl.setHorizontalAlignment(JLabel.CENTER);
		indexl.setBounds(btnPanel.getWidth()/2 - 20,5,60,40);
		
		btnPanel.add(indexl);
		btnPanel.add(btns[0]);
		btnPanel.add(btns[1]);
		
		
		
		displayPanel.setBounds(0,50,this.getWidth(),this.getHeight()-50);
		displayPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		this.add(btnPanel,0);
		this.add(displayPanel,1);	

		rowh = (displayPanel.getHeight()-5)/ROWS;
		colw = (displayPanel.getWidth()-5)/COLS;			

		for(int i = 0 ; i < products.size() ; i+=(ROWS*COLS)) {
			JPanel tmpP = new JPanel(null);
			tmpP.setBounds(displayPanel.getBounds());
			tmpP.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
			
			for(int j = i ; j <products.size() && j < (i + (ROWS*COLS));j++) {
				ProductPanel tmp = new ProductPanel(
						new Rectangle(((j-i)%COLS)*colw + 5, ((j-i)/COLS)*rowh + 5, colw-5, rowh-5),products.get(j));
				productButtons.add(tmp);
				tmp.addMouseListener(pl);
				tmpP.add(tmp);
			}
			displayPanels.add(tmpP);
		}
		this.remove(1);
		this.add(displayPanels.isEmpty() ? displayPanel : displayPanels.firstElement(), 1);
		this.repaint();
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void buttonClicked(Product clickedProduct) {
		// Ŭ???ɽÿ? ?????Ǵ? ?޼ҵ?
	}
	public void settingButtonClicked() {
		// ?߰? ??ư Ŭ?? ?ɽÿ? ?????Ǵ? ?޼ҵ?
	}
	public class ProductClickListener extends MouseAdapter{
		ProductPanel o;
		@Override
		public void mousePressed(MouseEvent e) {
			o = (ProductPanel)e.getSource();
			o.setBackground(new Color(225, 225, 225));
			if(o.getProductName().equals("+")) {
				settingButtonClicked();
			}
			else if(productButtons.indexOf(o) >= 0) {
				buttonClicked(o.getProduct());
			}
		}
		public void mouseReleased(MouseEvent e) {
			o = (ProductPanel)e.getSource();
			o.setBackground(Color.white);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			o = (ProductPanel) e.getSource();
			if(productButtons.indexOf(o) >= 0) {
				o.setBorder(new TitledBorder(new LineBorder(Color.gray, 3)));
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			o = (ProductPanel)e.getSource();
			if(productButtons.indexOf(o) >= 0) {
				o.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
			}
		}

	}
	public class ButtonClickListener implements ActionListener{
		int index = 0;
		public void actionPerformed(ActionEvent e) {
			if(displayPanels.isEmpty()) return; // ??ǰ?? ?????? ?̵?x
			JButton clicked = (JButton) e.getSource();
			if(clicked.equals(btns[0])) {
				if(index > 0) index--;
			}
			if(clicked.equals(btns[1])) {
				if(index < (displayPanels.size()-1)) index++;
			}
			remove(1);
			indexl.setText(""+(index+1));
			add(displayPanels.get(index), 1);
			repaint();			
		}
		
	}
}
