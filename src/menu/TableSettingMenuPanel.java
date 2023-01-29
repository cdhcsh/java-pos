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
import database.FileIOMeneger;
import database.Session;
import order.Bill;
import order.OrderList;
import order.Table;

public class TableSettingMenuPanel extends JPanel{
	private JPanel subMenuPanel = new JPanel();
	private JPanel mainMenuPanel = new JPanel();	

	private Database DB = Account.getDBbyID(Session.getLogin_ID());

	private Vector<Table> tables = DB.getTables();
	private Vector<JPanel> tablePanels = new Vector<JPanel>();

	private JButton addbtn = new JButton();
	private JButton delbtn = new JButton();
	private JButton resetbtn = new JButton();

	private int[] tablePoss = new int[DB.getRows()*DB.getCols()];
	private int rows = DB.getRows();
	private int cols = DB.getCols();
	private int rowh;
	private int colw;
	private int totalPrice = 0;
	
	private boolean remove = false;
	
	private TableListener tl = new TableListener();;
	public TableSettingMenuPanel(Rectangle s) {
		setBounds(s);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setLayout(null);
		setUp();
		setVisible(true);
	}
	public void setUp() {
		remove = false;
		removeAll();
		totalPrice = 0;
		mainMenuPanel = new JPanel(null);
		mainMenuPanel.setBounds(0,0,getWidth(),getHeight()-80);
		mainMenuPanel.setBackground(Color.white);
		mainMenuPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		mainMenuPanel.addMouseListener(tl);
		mainMenuPanel.addMouseMotionListener(tl);

		subMenuPanel = new JPanel(null);
		subMenuPanel.setBounds(0,getHeight()-80,getWidth(),80);
		subMenuPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));


		colw = (mainMenuPanel.getWidth()-15)/cols;
		rowh = (mainMenuPanel.getHeight()-15)/rows;
		tablePanels = new Vector<JPanel>();
		for(int i = 0;i< tablePoss.length ; i++) {
			tablePoss[i] = 0;
		}
		for(Table table : tables) {
			totalPrice += table.getOrderList().getTotalPrice();
			int pos = table.getPos();
			tablePoss[pos] = table.getTableNo();
			JPanel tmp = new JPanel(null);
			tmp.setBounds(((pos)%cols)*colw + 15, ((pos)/cols)*rowh + 15, colw-15, rowh-15);
			tmp.setBackground(Color.WHITE);
			tmp.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));

			JLabel l1 = new JLabel("  " + table.getTableNo());
			JLabel l2 = new JLabel(pos + " 번 위치 ");

			l1.setBounds(0,0,tmp.getWidth(),40);
			l1.setOpaque(true);
			l1.setBackground(Color.LIGHT_GRAY);
			l1.setFont(new Font("",Font.BOLD,21));
			l1.setForeground(Color.WHITE);
			l1.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));

			l2.setBounds(0,tmp.getHeight()-40,tmp.getWidth(),30);
			l2.setFont(new Font("",Font.BOLD,19));
			l2.setHorizontalAlignment(JLabel.RIGHT);
			if(table.isOrdered()) {
				tmp.setBackground(new Color(227 ,196 ,255));
			}


			tmp.add(l1,0);
			tmp.add(l2,1);
			tablePanels.add(tmp);
			mainMenuPanel.add(tmp);
		}

		addbtn = new JButton("테이블 추가");
		addbtn.setFont(new Font("",Font.BOLD,20));
		addbtn.setToolTipText("테이블을 추가합니다.");
		addbtn.setBounds(subMenuPanel.getWidth() - getWidth()/7*1 - 20,5,getWidth()/7,subMenuPanel.getHeight()-10);
		
		delbtn = new JButton("테이블 삭제");
		delbtn.setFont(new Font("",Font.BOLD,20));
		delbtn.setToolTipText("테이블을 선택하고 삭제합니다.");
		delbtn.setBounds(subMenuPanel.getWidth() - getWidth()/7*2 - 40,5,getWidth()/7,subMenuPanel.getHeight()-10);
		
		resetbtn = new JButton("테이블 초기화");
		resetbtn.setToolTipText("전체 테이블 정보를 초기화하고 기본설정으로 합니다.");
		resetbtn.setFont(new Font("",Font.BOLD,20));		
		resetbtn.setBounds(subMenuPanel.getWidth() - getWidth()/7*3 - 60,5,getWidth()/7,subMenuPanel.getHeight()-10);
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(delbtn)) {
					if(remove) {
						delbtn.setText("테이블 삭제");
						remove = false;
					}
					else {
						delbtn.setText("취소하기");
						remove = true;
						
					}
				}
				if(e.getSource().equals(addbtn)){
					if(tables.size() < rows*cols) {
						int pos = 0;
						for(int i = 0 ; i < tablePoss.length ; i++) {
							if(tablePoss[i] == 0) {
								pos = i;
								break;
							}
						}
						DB.addTable(pos);
						setUp();
						FileIOMeneger.saveTablebyID(Session.getLogin_ID());
						JOptionPane.showMessageDialog(Session.getFrame(),"테이블 추가를 완료했습니다.");
					}
					else
						JOptionPane.showMessageDialog(Session.getFrame(),"테이블 최대 개수를 초과했습니다.");
				}
				if(e.getSource().equals(resetbtn)) {
					for(Table table : tables) {
						if(table.isOrdered()) {
							JOptionPane.showMessageDialog(Session.getFrame(),"주문받은 테이블이 있어 초기화 할 수 없습니다.");
							return;
						}
					}
					if(JOptionPane.showConfirmDialog(Session.getFrame(),
							"테이블을 기본 설정으로 초기화하시겠습니까?"
							,"테이블 초기화", JOptionPane.YES_NO_OPTION) == 0) {
						DB.defalutTableSetting();
						setUp();
						FileIOMeneger.saveTablebyID(Session.getLogin_ID());
						JOptionPane.showMessageDialog(Session.getFrame(),"테이블 설정 초기화를 완료했습니다.");
					}
					
				}
			}
		};
		addbtn.addActionListener(al);
		delbtn.addActionListener(al);
		resetbtn.addActionListener(al);
		subMenuPanel.add(addbtn);
		subMenuPanel.add(delbtn);
		subMenuPanel.add(resetbtn);
		add(mainMenuPanel);
		add(subMenuPanel);
		repaint();
	}
	class TableListener extends MouseAdapter{

		Table table;
		JPanel b;
		int index;
		boolean move = false;
		public boolean isTable(MouseEvent e) {
			b = (JPanel)mainMenuPanel.getComponentAt(e.getPoint());
			index  = tablePanels.indexOf(b);
			return index >= 0;
		}
		public void mousePressed(MouseEvent e) {
			if(!remove && isTable(e)) {
				table = tables.get(index);
				move = true;
				b.setBackground(new Color(225, 225, 225));	
			}
			else if(isTable(e)){
				table = tables.get(index);
				if(table.isOrdered()) JOptionPane.showMessageDialog(Session.getFrame(),"주문받은 테이블은 삭제할 수 없습니다.");
				else {
					DB.removeTable(table);
					setUp();
					FileIOMeneger.saveTablebyID(Session.getLogin_ID());
					JOptionPane.showMessageDialog(Session.getFrame(),"테이블 삭제를 완료했습니다.");
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			move = false;
			if(table != null && table.isOrdered()) {
				b.setBackground(new Color(227 ,196 ,255));
			}
			else
			b.setBackground(Color.WHITE);
			table = null;
			if(isTable(e)) FileIOMeneger.saveTablebyID(Session.getLogin_ID());

		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if(move) {
				int ppos = table.getPos();
				int pos = (e.getX() -15)/colw + cols*((e.getY()-15)/rowh);
				if(pos < cols*rows && tablePoss[pos] == 0 ) {
					b.setLocation(((pos)%cols)*colw + 15, ((pos)/cols)*rowh + 15);
					table.setPos(pos);
					JLabel tmp = (JLabel)b.getComponent(1);
					tmp.setText(pos + " 번 위치 ");
					tablePoss[pos] = table.getTableNo();
					tablePoss[ppos] = 0;
				}

			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(!e.getSource().equals(mainMenuPanel)) {
				JPanel b = (JPanel)e.getSource();
				b.setBorder(new TitledBorder(new LineBorder(Color.gray, 3)));
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(!e.getSource().equals(mainMenuPanel)) {
				JPanel b = (JPanel)e.getSource();
				b.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
			}
		}
	}
}
