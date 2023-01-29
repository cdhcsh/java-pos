package menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.FileIOMeneger;
import database.Session;
import others.ConsolePrint;
import others.GridBagMeneger;

public class MainFrame extends JFrame{
	public static final String PROGRAM_TITLE = "POS";
	public static final double RATIO = 9.0/14.0;
	public static final int WIDTH = 1400;
	public static final int HEIGHT = (int)(WIDTH*RATIO);
	public static final int LOGIN = 0;
	public static final int SUBHEIGHT = 40;
	public static final Rectangle MAIN_RECTANGLE = new Rectangle(0,SUBHEIGHT, WIDTH-19, HEIGHT - SUBHEIGHT - 50);
	public static final Rectangle SUB_RECTANGLE = new Rectangle(0,0, WIDTH-19, SUBHEIGHT);
	private Container con;
	public JPanel mainMenuPanel;
	public JPanel loginMenuPanel;
	public JButton marketingButton;
	public JButton salesButton;
	public JButton settingButton;	
	//-------------------------------------------------------
	public MainFrame() {
		ConsolePrint.printlnMainFrameMessage("프로그램 창 생성 중..");
		this.setResizable(false);
		this.setSize(WIDTH,HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.con = getContentPane();
		Session.setFrame(this);
		this.setUpFrame();
		ConsolePrint.printlnMainFrameMessage("프로그램 창 생성 완료.");
		this.setVisible(true);
	}
	public void setUpMainMenu() {
		ConsolePrint.printlnMainFrameMessage("메인 메뉴 생성 중..");
		mainMenuPanel = new JPanel();
		mainMenuPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		mainMenuPanel.setBounds(MAIN_RECTANGLE);
		GridBagLayout gbl = new GridBagLayout();
		GridBagMeneger gbm = new GridBagMeneger(gbl, mainMenuPanel);
		ClickEvent ce = new ClickEvent();
		mainMenuPanel.setLayout(gbl);
		marketingButton = new JButton("영업");
		marketingButton.setFont(new Font("돋움", Font.BOLD, 30));
		marketingButton.addActionListener(ce);
		salesButton = new JButton("정산");

		salesButton.setFont(new Font("돋움", Font.BOLD, 30));
		salesButton.addActionListener(ce);
		settingButton = new JButton("설정");
		settingButton.setFont(new Font("돋움", Font.BOLD, 30));
		settingButton.addActionListener(ce);

		gbm.gblAdd(new JPanel(), 0, 0, 12, 1);
		gbm.gblAdd(new JPanel(), 0, 1, 2, 1);
		gbm.gblAdd(new JPanel(), 4, 1, 1, 1);
		gbm.gblAdd(new JPanel(), 7, 1, 1, 1);
		gbm.gblAdd(new JPanel(), 10, 1, 2, 1);
		gbm.gblAdd(new JPanel(), 0, 2, 12, 1);
		for(Component c :mainMenuPanel.getComponents()) {
			c.setBackground(Color.WHITE);
		}
		gbm.gblAdd(marketingButton, 2, 1, 2, 1);
		gbm.gblAdd(salesButton, 5, 1, 2, 1);
		gbm.gblAdd(settingButton, 8, 1, 2, 1);
		ConsolePrint.printlnMainFrameMessage("메인 메뉴 생성 완료.");
	}
	public void setupLoginMenu() {
		ConsolePrint.printlnMainFrameMessage("로그인 메뉴 생성 중..");
		loginMenuPanel = new LoginMenuPanel(SUB_RECTANGLE);	
		ConsolePrint.printlnMainFrameMessage("로그인 메뉴 생성 완료.");

	}
	public void setUpFrame() {
		ConsolePrint.printlnMainFrameMessage("프로그램 창 기본 설정 중..");
//		this.setResizable(false);
		con.removeAll();
		con.setLayout(null);
		con.setBackground(Color.white);
		this.setupLoginMenu();
		this.setUpMainMenu();
		con.add(loginMenuPanel,0);
		con.add(mainMenuPanel,1);
		Session.setDisplay_Menu("main");
		ConsolePrint.printlnMainFrameMessage("프로그램 창 기본 설정 완료.");
	}
	public void displayMainMenu() {
		con.remove(1);
		con.add(mainMenuPanel,1);
		Session.setDisplay_Menu("main");
	}
	class ClickEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(Session.getLogin_ID() != null) {
				if(e.getSource().equals(marketingButton)) {
					if(Account.getDBbyID(Session.getLogin_ID()).getProductCategories().isEmpty()) {
						JOptionPane.showMessageDialog(Session.getFrame(),"상품이 존재하지 않습니다.");
						displayMainMenu();
						return;
					}
					con.remove(1);
					con.add(new MarketingMainPanel(MAIN_RECTANGLE),1);
					Session.setDisplay_Menu("marketing");
				}
				if(e.getSource().equals(salesButton)) {
					con.remove(1);
					con.add(new SalesMainPanel(MAIN_RECTANGLE),1);
					Session.setDisplay_Menu("sales");
				}
				if(e.getSource().equals(settingButton)) {
					con.remove(1);
					con.add(new SettingMainPanel(MAIN_RECTANGLE),1);
					Session.setDisplay_Menu("setting");
				}
			}
			else {
				JOptionPane.showMessageDialog(Session.getFrame(),"로그인 후 이용가능 합니다.");
			}
		}

	}
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public static void main(String[] args) {
		try {		
			setUIFont (new javax.swing.plaf.FontUIResource("", Font.PLAIN, 17));
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileIOMeneger.loadData();
		new MainFrame();
	}
}
