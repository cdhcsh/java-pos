package menu;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Session;
import others.ConsolePrint;

public class LoginMenuPanel extends JPanel{
	JLabel lgnIdLb,lgnPwLb;
	JTextField lgnIdTf;
	JPasswordField lgnPwTf;
	JButton submitBtn,joinBtn,logoutBtn,mainBtn;
	@Override
	public void repaint() {
		loginMenu();
		super.repaint();
	}
	public LoginMenuPanel(Rectangle s) {
		setBounds(s);
		setLayout(null);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
	//	Session.setLogin_ID(null);
		Session.setLogin_ID("sushi");// 테스트로 추가
		loginMenu();
	}
	private void loginMenu(){
		this.removeAll();
		ClickListener cl = new ClickListener();
		if(Session.getLogin_ID() == null) {
			lgnIdLb = new JLabel("아이디");
			lgnIdLb.setBounds(20,0,60,this.getHeight());
			lgnPwLb = new JLabel("비밀번호");
			lgnPwLb.setBounds(280,0,80,this.getHeight());
			lgnIdTf = new JTextField();
			lgnIdTf.setBounds(80,0,180,this.getHeight());
			lgnPwTf = new JPasswordField();
			lgnPwTf.setBounds(360,0,180,this.getHeight());
			lgnPwTf.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						cl.login();
					}
				}
			});
			joinBtn = new JButton("회원가입");
			joinBtn.setToolTipText("회원가입 화면으로 이동합니다.");
			joinBtn.setBounds(this.getWidth() - 290,0,120,this.getHeight());
			joinBtn.addActionListener(cl);
			submitBtn = new JButton("로그인");
			submitBtn.setBounds(this.getWidth() - 430,0,120,this.getHeight());
			submitBtn.addActionListener(cl);
			add(lgnIdLb);
			add(lgnIdTf);
			add(lgnPwLb);
			add(lgnPwTf);
			add(submitBtn);
			add(joinBtn);
		}
		else {
			lgnIdLb = new JLabel("환영합니다 . ");
			lgnIdLb.setBounds(20, 0, 100, this.getHeight());
			lgnPwLb = new JLabel(Account.getNamebyID(Session.getLogin_ID()) + " 님");
			lgnPwLb.setHorizontalAlignment(JLabel.LEFT);
			lgnPwLb.setBounds(120, 0, 300, this.getHeight());
			logoutBtn = new JButton("로그아웃");
			logoutBtn.setToolTipText("로그아웃하고 메인화면으로 이동합니다.");
			logoutBtn.setBounds(this.getWidth() - 290,0,120,this.getHeight());
			logoutBtn.addActionListener(cl);
			add(lgnIdLb);
			add(lgnPwLb);
			add(logoutBtn);
		}
		mainBtn = new JButton("메인화면");
		mainBtn.setToolTipText("메인화면으로 이동합니다.");
		mainBtn.setBounds(this.getWidth() -150,0,120,this.getHeight());
		mainBtn.addActionListener(cl);
		add(mainBtn);
	}
	class ClickListener implements ActionListener{
		public void login() {
			if(Account.login(lgnIdTf.getText(), new String(lgnPwTf.getPassword()))){
				JOptionPane.showMessageDialog(Session.getFrame(),"로그인에 성공하셨습니다!");
				ConsolePrint.printlnLginMessage(lgnIdTf.getText() + " 로그인 ");
				Session.getFrame().displayMainMenu();
				repaint();
			}
			else {
				JOptionPane.showMessageDialog(Session.getFrame(),"아이디나 비밀번호가 잘못되었습니다.");
				ConsolePrint.printlnLginMessage("로그인 실패");
				}		
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(submitBtn)) {
				login();
			}
			if(e.getSource().equals(logoutBtn)) {
				ConsolePrint.printlnLginMessage("로그아웃");
				Session.getFrame().displayMainMenu();
				Session.setLogin_ID(null);
				repaint();
				JOptionPane.showMessageDialog(Session.getFrame(),"이용 감사합니다.");
			}
			if(e.getSource().equals(joinBtn)) {
				Container con = Session.getFrame().getContentPane();
				con.remove(1);
				con.add(new AccountJoinPanel(MainFrame.MAIN_RECTANGLE),1);
				Session.setDisplay_Menu("join");
			}
			if(e.getSource().equals(mainBtn)) {
				if(!Session.getDisplay_Menu().equals("main"))
				Session.getFrame().displayMainMenu();
			}
		}
		
	}
}
