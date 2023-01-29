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

import account.Account;
import database.FileIOMeneger;
import database.Session;
import product.ProductListPanel.ButtonClickListener;
import product.ProductListPanel.ProductClickListener;

public class ProductCategoryListPanel extends JPanel{
	public static final int COLS = 6;
	public static final int ROWS = 1;
	private int rowh,colw,mode; // mode 0 :영업 mode 1 : 설정
	private Vector<JLabel> labels;
	private Vector<ProductCategory> productCategories;
	private JPanel displayPanel = new JPanel(null);
	private Vector<JPanel> displayPanels = new Vector<JPanel>();
	private JPanel btnPanel = new JPanel(null);
	private JLabel indexl = new JLabel();
	private JButton btns[] = new JButton[2];
	private ProductCategoryClickListener pl = new ProductCategoryClickListener();
	private ButtonClickListener bl = new ButtonClickListener();
	
	public ProductCategoryListPanel(Rectangle s ,int mode) {
		this.mode = mode;
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
		pl = new ProductCategoryClickListener();
		bl = new ButtonClickListener();		
		labels = new Vector<JLabel>();
		
		productCategories =	Account.getDBbyID(Session.getLogin_ID()).getProductCategories();
		if(mode == 1) {
			productCategories.add(new ProductCategory("+"));
		}
		btnPanel.setBounds(0,0,this.getWidth(),50);
		btnPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		
		btns[0] = new JButton("◁");
		btns[0].setBounds(btnPanel.getWidth()/2 - 110, 5, 70, 40);
		btns[0].addActionListener(bl);

		btns[1] = new JButton("▷");
		btns[1].setBounds(btnPanel.getWidth()/2 + 60, 5, 70, 40);
		btns[1].addActionListener(bl);
		
		indexl.setText("1");
		indexl.setOpaque(true);
		indexl.setBackground(Color.white);
		indexl.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		indexl.setFont(new Font("굴림",Font.BOLD,20));
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
		
		
		for(int i = 0 ; i < productCategories.size() ; i+=(ROWS*COLS)) {
			JPanel tmpP = new JPanel(null);			
			tmpP.setBounds(displayPanel.getBounds());
			tmpP.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
			
			for(int j = i ; j <productCategories.size() && j < (i + (ROWS*COLS));j++) {
				JLabel tmp = null;
				tmp = new JLabel(productCategories.get(j).getCategoryName());
				tmp.setFont(new Font("",Font.BOLD,19));
				tmp.setOpaque(true);
				tmp.setBackground(Color.WHITE);
				tmp.setHorizontalAlignment(JLabel.CENTER);
				tmp.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
				if(tmp.getText().equals("+")) {
					tmp.setToolTipText("클릭하여 상품 카테고리를 추가합니다.");
					tmp.setFont(new Font("",Font.PLAIN,20));
				}
				tmp.setBounds(((j-i)%COLS)*colw + 5, ((j-i)/COLS)*rowh + 5, colw-5, rowh-5);
				labels.add(tmp);
				tmp.addMouseListener(pl);
				tmpP.add(tmp);
			}
			displayPanels.add(tmpP);
		}
		this.remove(1);
		this.add(displayPanels.firstElement(), 1);
		this.repaint();
	}
	public void buttonClicked(ProductCategory clickedProductCategory) {
		// 클릭될시에 실행되는 메소드
	}
	public void settingButtonClicked() {
		// 추가 버튼 클릭 될시에 실행되는 메소드
	}
	public class ProductCategoryClickListener extends MouseAdapter{
		JLabel o;
		@Override
		public void mousePressed(MouseEvent e) {
			o = (JLabel)e.getSource();
			o.setBackground(new Color(225, 225, 225));
			if(o.getText().equals("+")) {
				settingButtonClicked();
			}
			else if(labels.indexOf(o) >= 0) {
				ProductCategory clickedProductCategory = productCategories.get(labels.indexOf(o));
				buttonClicked(clickedProductCategory);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			o = (JLabel)e.getSource();
			o.setBackground(Color.white);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			o = (JLabel)e.getSource();
			o.setBorder(new TitledBorder(new LineBorder(Color.gray, 3)));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			o = (JLabel)e.getSource();
			o.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		}

	}
	public class ButtonClickListener implements ActionListener{
		int index = 0;
		public void actionPerformed(ActionEvent e) {
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
