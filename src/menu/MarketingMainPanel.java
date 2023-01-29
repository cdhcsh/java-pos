package menu;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Database;
import database.Session;
import order.Bill;
import order.OrderList;
import order.Table;

public class MarketingMainPanel extends JPanel{
	private JPanel subMenuPanel = new JPanel();
	private JPanel mainMenuPanel = new JPanel();	
	
	private Database DB = Account.getDBbyID(Session.getLogin_ID());
	
	private Vector<Table> tables = DB.getTables();
	private Vector<JPanel> tablePanels = new Vector<JPanel>();
	
	private JLabel tpl = new JLabel();
	private JButton sbtn = new JButton();
	
	private int rows = DB.getRows();
	private int cols = DB.getCols();
	private int rowh;
	private int colw;
	private int totalPrice = 0;
	public MarketingMainPanel(Rectangle s) {
		setBounds(s);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setLayout(null);
		backUp();
		setVisible(true);
	}
	public void backUp() {
		removeAll();
		totalPrice = 0;
		mainMenuPanel = new JPanel(null);
		mainMenuPanel.setBounds(0,0,getWidth(),getHeight()-80);
		mainMenuPanel.setBackground(Color.white);
		mainMenuPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		
		subMenuPanel = new JPanel(null);
		subMenuPanel.setBounds(0,getHeight()-80,getWidth(),80);
		subMenuPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		
		TableListener tl = new TableListener();
		int i = 1;
		colw = (mainMenuPanel.getWidth()-15)/cols;
		rowh = (mainMenuPanel.getHeight()-15)/rows;
		tablePanels = new Vector<JPanel>();
		for(Table table : tables) {
			totalPrice += table.getOrderList().getTotalPrice();
			int pos = table.getPos();
			JPanel tmp = new JPanel(null);
			tmp.setBounds(((pos)%cols)*colw + 15, ((pos)/cols)*rowh + 15, colw-15, rowh-15);
			tmp.addMouseListener(tl);
			tmp.setBackground(Color.WHITE);
			tmp.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
			
			JLabel l1 = new JLabel("  " + table.getTableNo());
			JLabel l2 = new JLabel(table.getOrderList().getTotalPriceWon() + " ");
			
			l1.setBounds(0,0,tmp.getWidth(),40);
			l1.setOpaque(true);
			l1.setBackground(Color.LIGHT_GRAY);
			l1.setFont(new Font("",Font.BOLD,21));
			l1.setForeground(Color.WHITE);
			l1.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
			
			l2.setBounds(0,tmp.getHeight()-40,tmp.getWidth(),30);
			l2.setFont(new Font("",Font.BOLD,19));
			l2.setHorizontalAlignment(JLabel.RIGHT);

			tmp.add(l1);
			tmp.add(l2);
			if(table.isOrdered()) {
				tmp.setBackground(new Color(227 ,196 ,255));
			}
			tablePanels.add(tmp);
			mainMenuPanel.add(tmp);
		}
		
		tpl = new JLabel(" 총 금액 : " + Bill.intToWonFormat(totalPrice));
		tpl.setBounds(20,0,subMenuPanel.getWidth()/4,subMenuPanel.getHeight());
		tpl.setFont(new Font("",Font.BOLD,20));
	
		sbtn = new JButton("이동 / 합석");
		sbtn.setToolTipText("테이블의 주문정보를 다른 테이블로 옮깁니다.");
		sbtn.setFont(new Font("",Font.BOLD,20));
		sbtn.setBounds(subMenuPanel.getWidth() - getWidth()/8 - 20,5,getWidth()/8,subMenuPanel.getHeight()-10);
		sbtn.addMouseListener(tl);
		sbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		subMenuPanel.add(sbtn);		
		subMenuPanel.add(tpl);
		add(mainMenuPanel);
		add(subMenuPanel);
		repaint();
	}
	class TableListener extends MouseAdapter{
		Table o;
		JPanel p;
		boolean move = false;
		Table table = null;
		public void mousePressed(MouseEvent e) {
			if(e.getSource().equals(sbtn)) {
				if(move) {
					sbtn.setText("이동 / 합석");
					if(table!= null)
							p.setBackground(new Color(227 ,196 ,255));
					move = false;
				}
				else {
					sbtn.setText("취소하기");
					move = true;
				}
				table = null;
			}
			else if(move) {
				JPanel b = (JPanel)e.getSource();
				if(table == null) {
					table = tables.get(tablePanels.indexOf(b));
					if(table.getOrders().isEmpty()) table = null;
					else {
						p = b;
						b.setBackground(new Color(225, 225, 225));
					}
				}
				else {
					Table dtable = tables.get(tablePanels.indexOf(b));
					if(!dtable.equals(table)) {
						dtable.getOrderList().addOrders(table.getOrders());
						table.setOrderList(new OrderList());
						move = false;
						backUp();
						JOptionPane.showMessageDialog(Session.getFrame(),"테이블 이동/합석이 완료되었습니다.");
					}
				}
			}
			else {
			JPanel b = (JPanel)e.getSource();
			b.setBackground(new Color(225, 225, 225));
			o = tables.get(tablePanels.indexOf(b));
			removeAll();
			add(new TableMenuPanel(new Rectangle(0, 0, getWidth(), getHeight()),o));
			repaint();
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(!e.getSource().equals(sbtn)) {
			JPanel b = (JPanel)e.getSource();
			b.setBorder(new TitledBorder(new LineBorder(Color.gray, 3)));
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(!e.getSource().equals(sbtn)) {
			JPanel b = (JPanel)e.getSource();
			b.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
			}
		}
	}
}
