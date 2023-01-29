package menu;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Database;
import database.FileIOMeneger;
import database.Session;
import product.Product;
import product.ProductCategory;
import product.ProductCategoryListPanel;
import product.ProductListPanel;

public class ProductSettingMenuPanel extends JPanel{
	private ProductCategoryListPanel pclp;
	private ProductListPanel plp;
	private JPanel pcep;
	private JPanel pep;

	private JLabel categoryEditTitle = new JLabel();
	private JLabel categoryName = new JLabel();
	private JTextField categorytf = new JTextField();
	private JButton[] categorybtns = new JButton[3];
	private ProductCategory selectedCategory;

	private JLabel productEditTitle = new JLabel();
	private JLabel productName = new JLabel();
	private JLabel productPrice = new JLabel();
	private JTextField productNametf = new JTextField();
	private JTextField productPricetf = new JTextField();
	private JButton[] productbtns = new JButton[3];
	private Product selectedProduct;

	private Database DB = Account.getDBbyID(Session.getLogin_ID());

	public ProductSettingMenuPanel(Rectangle s) {
		setBounds(s);
		setLayout(null);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setUp();
	}
	public void setUp() {
		removeAll();
		Rectangle categoryListRectangle = new Rectangle(0, 0, getWidth()/3*2, getHeight()/4);
		Rectangle categoryEditRectangle = new Rectangle(getWidth()/3*2,0,getWidth()/3,getHeight()/4);
		Rectangle productListRectangle = new Rectangle(0, getHeight()/4, getWidth()/3*2, getHeight()/4*3);
		Rectangle productEditRectangle = new Rectangle(getWidth()/3*2,getHeight()/4,getWidth()/3,getHeight()/4 + 60);
		selectedCategory = DB.getProductCategories().isEmpty() ? null :DB.getProductCategories().firstElement();
		selectedProduct = new Product("",0);
		if(selectedCategory != null) {
			plp = new ProductListPanel(DB.getProductCategories().firstElement(),productListRectangle,1) {
				public void buttonClicked(Product clickedProduct) {
					selectedProduct = clickedProduct;
					productEditTitle.setText("��ǰ ����");
					productNametf.setText(clickedProduct.getProductName());
					productPricetf.setText(""+clickedProduct.getProductPrice());
					pep.remove(productbtns[0]);
					pep.add(productbtns[1]);
					pep.add(productbtns[2]);
					pep.repaint();
				}
				public void settingButtonClicked() {
					productEditTitle.setText("��ǰ �߰�");
					productNametf.setText("");
					productPricetf.setText("");
					pep.add(productbtns[0]);
					pep.remove(productbtns[1]);
					pep.remove(productbtns[2]);
					pep.repaint();
				}
			};
		}
		else plp = null;
		pclp = new ProductCategoryListPanel(categoryListRectangle,1) {
			public void buttonClicked(ProductCategory clickedProductCategory) {
				selectedCategory = clickedProductCategory;
				categoryEditTitle.setText("��ǰ ī�װ� ����");
				categorytf.setText(clickedProductCategory.getCategoryName());
				pcep.remove(categorybtns[0]);
				pcep.add(categorybtns[1]);
				pcep.add(categorybtns[2]);
				pcep.repaint();

				plp.setProductCategory(clickedProductCategory);
				plp.setUp();
			}
			public void settingButtonClicked() {
				categoryEditTitle.setText("��ǰ ī�װ� �߰�");
				categorytf.setText("");
				pcep.add(categorybtns[0]);
				pcep.remove(categorybtns[1]);
				pcep.remove(categorybtns[2]);
				pcep.repaint();
			}
		};
		pcep = new JPanel(null);
		pcep.setBounds(categoryEditRectangle);
		pcep.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		pcep.setBackground(Color.white);
		categoryEditTitle = new JLabel("��ǰ ī�װ� �߰�");
		categoryEditTitle.setBounds(0,0,pcep.getWidth(),50);
		categoryEditTitle.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		categoryEditTitle.setOpaque(true);
		categoryEditTitle.setHorizontalAlignment(JLabel.CENTER);

		categoryName = new JLabel("ī�װ� �̸�");
		categoryName.setBounds(0, 65, pcep.getWidth()/3, 40);
		categoryName.setHorizontalAlignment(JLabel.CENTER);

		categorytf = new JTextField("");
		categorytf.setBounds(pcep.getWidth()/3 + 20,65,pcep.getWidth()/2,40);

		categorybtns[0] = new JButton("�߰�");
		categorybtns[0].setBounds(pcep.getWidth()/3,pcep.getHeight() - 55,pcep.getWidth()/3,40);

		categorybtns[1] = new JButton("����");
		categorybtns[1].setBounds(pcep.getWidth()/4 - 10,pcep.getHeight() - 55,pcep.getWidth()/4,40);

		categorybtns[2] = new JButton("����");
		categorybtns[2].setBounds(pcep.getWidth()/4*2 + 10,pcep.getHeight() - 55,pcep.getWidth()/4,40);

		ActionListener cel = new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if(e.getSource().equals(categorybtns[2])) { // ��ǰ ī�װ� ����
					if(DB.removeProductcategory(selectedCategory)) {
						setUp();
						FileIOMeneger.saveProductbyID(Session.getLogin_ID());
						JOptionPane.showMessageDialog(Session.getFrame(),"ī�װ� ������ �����ϼ̽��ϴ�.");

					}
				}
				else {
					String text = categorytf.getText();
					if(FileIOMeneger.STANDARD_PATTERN.matcher(text).matches()) {
						if(DB.redundancyCheckProductCategoryName(text)) {
							if(e.getSource().equals(categorybtns[0])) {
								DB.addProductCategory(new ProductCategory(text));								
								if(DB.getProductCategories().size() == 1) {
									setUp();
									FileIOMeneger.saveProductbyID(Session.getLogin_ID());
									JOptionPane.showMessageDialog(Session.getFrame(),"ī�װ� �߰��� �����ϼ̽��ϴ�.");
									return;
								}
								pclp.setUp();
								plp.setProductCategory(DB.getProductCategories().firstElement());
								plp.setUp();
								FileIOMeneger.saveProductbyID(Session.getLogin_ID());
								JOptionPane.showMessageDialog(Session.getFrame(),"ī�װ� �߰��� �����ϼ̽��ϴ�.");
							}
							else if(e.getSource().equals(categorybtns[1])) {
								DB.editProductCategory(selectedCategory, text);
								pclp.setUp();
								plp.setProductCategory(DB.getProductCategories().firstElement());
								plp.setUp();
								FileIOMeneger.saveProductbyID(Session.getLogin_ID());
								JOptionPane.showMessageDialog(Session.getFrame(),"ī�װ� ������ �����ϼ̽��ϴ�.");
							}
							return;								
						}
						else {
							JOptionPane.showMessageDialog(Session.getFrame(),"ī�װ� �̸��� �ߺ��Ǿ����ϴ�.");
						}
					}
					else {
						JOptionPane.showMessageDialog(Session.getFrame(),"ī�װ� �̸��� �ùٸ����ʽ��ϴ�.");
					}
				}

			}
		};
		categorybtns[0].addActionListener(cel);
		categorybtns[1].addActionListener(cel);
		categorybtns[2].addActionListener(cel);

		pcep.add(categoryEditTitle);
		pcep.add(categoryName);
		pcep.add(categorytf);
		pcep.add(categorybtns[0]);



		pep = new JPanel(null);		
		pep.setBounds(productEditRectangle);
		pep.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		pep.setBackground(Color.white);

		productEditTitle = new JLabel("��ǰ �߰�");
		productEditTitle.setBounds(0,0,pep.getWidth(),50);
		productEditTitle.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		productEditTitle.setOpaque(true);
		productEditTitle.setHorizontalAlignment(JLabel.CENTER);

		productName = new JLabel("��ǰ �̸�");
		productName.setBounds(0, 65, pep.getWidth()/3, 40);
		productName.setHorizontalAlignment(JLabel.CENTER);

		productPrice = new JLabel("��ǰ ����");
		productPrice.setBounds(0, 120, pep.getWidth()/3, 40);
		productPrice.setHorizontalAlignment(JLabel.CENTER);

		productNametf = new JTextField("");
		productNametf.setBounds(pep.getWidth()/3 + 20,65,pep.getWidth()/2,40);

		productPricetf = new JTextField("");
		productPricetf.setBounds(pep.getWidth()/3 + 20,120,pep.getWidth()/2,40);

		productbtns[0] = new JButton("�߰�");
		productbtns[0].setBounds(pcep.getWidth()/3,pep.getHeight() - 55,pcep.getWidth()/3,40);

		productbtns[1] = new JButton("����");
		productbtns[1].setBounds(pcep.getWidth()/4 - 10,pep.getHeight() - 55,pcep.getWidth()/4,40);

		productbtns[2] = new JButton("����");
		productbtns[2].setBounds(pcep.getWidth()/4*2 + 10,pep.getHeight() - 55,pcep.getWidth()/4,40);

		ActionListener pel = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(productbtns[2])) { // ��ǰ�� ����
					if(selectedCategory.removeProduct(selectedProduct)) {
						plp.setUp();
						FileIOMeneger.saveProductbyID(Session.getLogin_ID());
						JOptionPane.showMessageDialog(Session.getFrame(),"��ǰ ������ �����ϼ̽��ϴ�.");
	;
					}
					return;
				}
				else {
					String price = productPricetf.getText();
					String name = productNametf.getText();
					if(FileIOMeneger.STANDARD_PATTERN.matcher(price).matches()
							&& Pattern.compile("^[0-9]{3,20}$").matcher(price).matches()) {
						if(selectedCategory.redundancyCheckProductName(name)) {
							if(e.getSource().equals(productbtns[0])) {
								selectedCategory.addProduct(new Product(name,Integer.parseInt(price)));
								plp.setUp();
								FileIOMeneger.saveProductbyID(Session.getLogin_ID());
								JOptionPane.showMessageDialog(Session.getFrame(),"��ǰ �߰��� �����ϼ̽��ϴ�.");
							}
							if(e.getSource().equals(productbtns[1])) {
								selectedCategory.editProduct(selectedProduct, name,price);
								plp.setUp();
								FileIOMeneger.saveProductbyID(Session.getLogin_ID());
								JOptionPane.showMessageDialog(Session.getFrame(),"��ǰ ������ �����ϼ̽��ϴ�.");
							}
						}
						else if(name.equals(selectedProduct.getProductName())){
							if(e.getSource().equals(productbtns[1])) {
								selectedCategory.editProduct(selectedProduct, name,price);
								plp.setUp();
								FileIOMeneger.saveProductbyID(Session.getLogin_ID());
								JOptionPane.showMessageDialog(Session.getFrame(),"��ǰ ������ �����ϼ̽��ϴ�.");
							}
						}
						else {
							JOptionPane.showMessageDialog(Session.getFrame(),"��ǰ �̸��� �ߺ��Ǿ����ϴ�.");
						}
					}
					else {
						JOptionPane.showMessageDialog(Session.getFrame(),"�Է��� ������ �ùٸ��� �ʽ��ϴ�.");
					}
				}

			}
		};

		productbtns[0].addActionListener(pel);
		productbtns[1].addActionListener(pel);
		productbtns[2].addActionListener(pel);

		pep.add(productEditTitle);
		pep.add(productName);
		pep.add(productPrice);
		pep.add(productNametf);
		pep.add(productPricetf);
		pep.add(productbtns[0]);

		add(pclp);
		add(pcep);
		if(selectedCategory != null) {
		add(plp);
		add(pep);
		}
		repaint();
	}
}
