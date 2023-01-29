package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Savepoint;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Database;
import database.FileIOMeneger;
import database.Session;
import order.Bill;
import order.OrderList;
import order.Table;
import product.Product;
import product.ProductCategory;
import product.ProductCategoryListPanel;
import product.ProductListPanel;

public class TableMenuPanel extends JPanel{
	private TableScrollViewPanel tsvp;
	private ProductCategoryListPanel pclp;
	private ProductListPanel plp;
	private Database DB = Account.getDBbyID(Session.getLogin_ID());

	ProductCategory selectedCategory = null;

	private Table table = null;
	private int tableNo;

	public TableMenuPanel(Rectangle s,Table table) {
		this.table = table;
		this.tableNo = table.getTableNo();
		setBounds(s);
		setLayout(null);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setUp();
	}
	public void setUp() {
		Rectangle tsvpRectangle = new Rectangle(0,50,getWidth()/3,getHeight() - 100);
		Rectangle pclpRectangle = new Rectangle(getWidth()/3,0,getWidth()/3*2,120);
		Rectangle plpRectangle = new Rectangle(getWidth()/3,120,getWidth()/3*2,getHeight() - 170);

		selectedCategory = DB.getProductCategories().firstElement();

		JLabel title = new JLabel("테이블 "+tableNo);
		title.setBounds(0,0,getWidth()/3,50);
		title.setOpaque(true);
		title.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		title.setHorizontalAlignment(JLabel.CENTER);
		if(table.getOrderList().getTotalPrice() > 0 )title.setBackground(new Color(227 ,196 ,255));
		else title.setBackground(Color.WHITE);
		title.setFont(new Font("",Font.BOLD,20));

		JLabel totalPrice = new JLabel("총 금액 : "+ table.getOrderList().getTotalPriceWon());
		totalPrice.setBounds(0,getHeight()-50,getWidth()/3,50);
		totalPrice.setOpaque(true);
		totalPrice.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		totalPrice.setHorizontalAlignment(JLabel.CENTER);
		totalPrice.setBackground(Color.WHITE);
		totalPrice.setFont(new Font("",Font.BOLD,20));
		removeAll();
		tsvp = new TableScrollViewPanel(tsvpRectangle, table.getOrderList().getOrderListData(), OrderList.getOrderListColumns());
		pclp = new ProductCategoryListPanel(pclpRectangle, 0) {
			@Override
			public void buttonClicked(ProductCategory clickedProductCategory) {
				selectedCategory = clickedProductCategory;
				plp.setProductCategory(clickedProductCategory);
				plp.setUp();
			}
		};
		plp = new ProductListPanel(selectedCategory, plpRectangle, 0) {
			@Override
			public void buttonClicked(Product clickedProduct) {
				Vector<String> tmp = new Vector<String>();
				tmp.add(clickedProduct.getProductName());
				tmp.add("1");
				tmp.add(Bill.intToWonFormat(clickedProduct.getProductPrice()));
				tsvp.addRow(tmp);
				totalPrice.setText("총 금액 : "+ new OrderList(tsvp.getTableData()).getTotalPriceWon());
			}
		};



		JButton[] btns = new JButton[5];
		String[] btntexts = {"뒤로가기","지정취소","전체취소","계산","주문"};
		String[] btntoltips = {"영업 메인화면으로 이동합니다.",
				"선택한 주문을 1개씩 취소합니다.","전체 주문내역을 취소합니다.",
				"현재 테이블을 계산합니다.","변경된 주문사항을 저장합니다."};
		JPanel btnCon = new JPanel(null);
		btnCon.setBackground(Color.white);
		btnCon.setBounds(getWidth()/3,getHeight()-50,getWidth()/3*2,50);
		btnCon.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));

		ActionListener tbl = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MarketingMainPanel tvp = (MarketingMainPanel)getParent();
				if(e.getSource().equals(btns[0])) {
					tvp.backUp();
				}
				if(e.getSource().equals(btns[1])) {
					tsvp.cancelSelectedProduct();
					totalPrice.setText("총 금액 : "+ new OrderList(tsvp.getTableData()).getTotalPriceWon());
				}
				if(e.getSource().equals(btns[2])) {
					tsvp.cancelAllRow();
					totalPrice.setText("총 금액 : " + Bill.intToWonFormat(0));
				}
				if(e.getSource().equals(btns[3])) {
					OrderList ol = new OrderList(tsvp.getTableData());
					ol.refreshOrders();
					if(ol.getTotalPrice() <= 0) {
						JOptionPane.showMessageDialog(Session.getFrame(),"주문 내역이 없습니다.");
					}
					else{
						if(JOptionPane.showConfirmDialog(Session.getFrame(),
								"총 금액 : " + ol.getTotalPriceWon() + "\n계산하시겠습니까?"
								,"테이블 계산", JOptionPane.YES_NO_OPTION) == 0) {
						table.setOrderList(ol);
						DB.addBill(new Bill(table));
						table.setOrderList(new OrderList());
						tvp.backUp();
						FileIOMeneger.saveBillbyID(Session.getLogin_ID());
						FileIOMeneger.saveTablebyID(Session.getLogin_ID());
						JOptionPane.showMessageDialog(Session.getFrame(),
								"계산이 완료되었습니다.\n총 금액 : " + ol.getTotalPriceWon());
						}
					}
				}
				if(e.getSource().equals(btns[4])) {
					OrderList ol = new OrderList(tsvp.getTableData());
					ol.refreshOrders();
					table.setOrderList(ol);
					tvp.backUp();
					FileIOMeneger.saveTablebyID(Session.getLogin_ID());
					JOptionPane.showMessageDialog(Session.getFrame(),"주문이 완료되었습니다.");
				}
			}
		};

		int colw = (getWidth()/3*2 - 10)/5;
		for(int i = 0 ; i<5 ; i++) {
			btns[i] = new JButton(btntexts[i]);
			btns[i].setToolTipText(btntoltips[i]);
			btns[i].setFont(new Font("",Font.BOLD,15));
			btns[i].setBounds((colw * i) + 10, 5,colw - 10,40);
			btns[i].addActionListener(tbl);
			btnCon.add(btns[i]);
		}
		add(btnCon);
		add(title);
		add(totalPrice);
		add(tsvp);
		add(pclp);
		add(plp);
		repaint();
	}
}
