package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.FileIOMeneger;
import database.Session;
import others.ConsolePrint;

public class AccountJoinPanel extends JPanel{
	JPanel inputForm = new JPanel();
	JLabel lb[] = new JLabel[5];
	JLabel elb[] = new JLabel[3];
	JTextField tf[] = new JTextField[4];
	JButton btn[] = new JButton[3];
	boolean IDrc = false,pwt = false ,pwequal = false;
	public AccountJoinPanel(Rectangle s) {
		setBounds(s);
		setLayout(null);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setUpInputForm();
		add(inputForm);
		Session.getFrame().repaint();
		setVisible(true);
	}
	public static boolean passwordCheck(String password) {
		Pattern pwPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{4,20}$");
		return pwPattern.matcher(password).matches();		

	}
	public static boolean IDCheck(String ID) {
		Pattern IDPattern = Pattern.compile("^[a-zA-Z0-9]{4,20}$");
		return IDPattern.matcher(ID).matches();		

	}
	public void setUpInputForm() {
		inputForm.setBounds(this.getWidth()/4,this.getHeight()/8,this.getWidth()/2,this.getHeight()/8*6);
		inputForm.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		inputForm.setLayout(null);

		int inputWidth = inputForm.getWidth();
		int inputHeight = inputForm.getHeight();
		Keylistener kl = new Keylistener();
		ButtonListener bl = new ButtonListener();

		lb[0] = new JLabel("회원 가입");
		lb[0].setBounds(inputWidth/4,inputHeight/20,inputWidth/2,inputHeight/15);
		lb[0].setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		lb[0].setOpaque(true);
		lb[0].setBackground(Color.white);
		lb[0].setFont(new Font("새굴림", Font.BOLD, 20));
		lb[0].setHorizontalAlignment(JLabel.CENTER);

		lb[1] = new JLabel("아이디");
		lb[1].setBounds(inputWidth/8,inputHeight/20*5,inputWidth/5,inputHeight/15);

		lb[2] = new JLabel("비밀번호");
		lb[2].setBounds(inputWidth/8,inputHeight/20*8,inputWidth/5,inputHeight/15);

		lb[3] = new JLabel("비밀번호 확인");
		lb[3].setBounds(inputWidth/8,inputHeight/20*11,inputWidth/5,inputHeight/15);

		lb[4] = new JLabel("이름");
		lb[4].setBounds(inputWidth/8,inputHeight/20*14,inputWidth/5,inputHeight/15);

		elb[0] = new JLabel("중복 확인이 필요합니다");
		elb[0].setOpaque(true);
		elb[0].setBounds(inputWidth/3,inputHeight/20*6,inputWidth/2,inputHeight/15);

		elb[1] = new JLabel("대,소문자,숫자가 포함되어야 합니다.");
		elb[1].setOpaque(true);
		elb[1].setBounds(inputWidth/3,inputHeight/20*9,inputWidth/2,inputHeight/15);

		elb[2] = new JLabel("비밀번호 확인이 일치하지 않습니다.");
		elb[2].setOpaque(true);
		elb[2].setBounds(inputWidth/3,inputHeight/20*12,inputWidth/2,inputHeight/15);

		tf[0] = new JTextField("");
		tf[0].setBounds(inputWidth/3,inputHeight/20*5,inputWidth/7*3,inputHeight/15);

		tf[1] = new JPasswordField();
		tf[1].setBounds(inputWidth/3,inputHeight/20*8,inputWidth/7*3,inputHeight/15);
		tf[1].addKeyListener(kl);

		tf[2] = new JPasswordField();
		tf[2].setBounds(inputWidth/3,inputHeight/20*11,inputWidth/7*3,inputHeight/15);
		tf[2].addKeyListener(kl);

		tf[3] = new JTextField("");
		tf[3].setBounds(inputWidth/3,inputHeight/20*14,inputWidth/7*3,inputHeight/15);

		btn[0] = new JButton("가입");
		btn[0].setBounds(inputWidth/4 - 20,inputHeight/20*17,inputWidth/4,inputHeight/15);

		btn[1] = new JButton("초기화");
		btn[1].setBounds(inputWidth/4 * 2 + 20,inputHeight/20*17,inputWidth/4,inputHeight/15);

		btn[2] = new JButton("중복확인");
		btn[2].setBounds(inputWidth/9*7,inputHeight/20*5,inputWidth/5,inputHeight/15);

		for(JLabel l : lb) inputForm.add(l);
		for(JTextField t : tf) inputForm.add(t);
		for(JButton b : btn) {
			inputForm.add(b);
			b.addActionListener(bl);
		}
		for(JLabel l : elb)inputForm.add(l);
	}
	class Keylistener extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			if(e.getSource().equals(tf[1]) || e.getSource().equals(tf[2])) {
				String tmp = tf[1].getText();
				if(e.getSource().equals(tf[1])) {
					if(passwordCheck(tmp)) {
						pwt = true;
						elb[1].setText("비밀번호가 적절합니다.");
						elb[1].setForeground(new Color(72,200,62));
					}
					else {
						pwt = false;
						elb[1].setText("대,소문자,숫자가 포함되어야 합니다.");
						elb[1].setForeground(Color.BLACK);
					}
				}
				if(tf[2].getText().equals(tmp)) {
					pwequal = true;
					elb[2].setText("비밀번호 확인이 일치합니다.");
					elb[2].setForeground(new Color(72,200,62));
				}
				else {
					pwequal = false;
					elb[2].setText("비밀번호 확인이 일치하지 않습니다.");
					elb[2].setForeground(Color.BLACK);
				}
			}
		}
	}
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(btn[0])) {
				String ID = tf[0].getText();
				String pw = tf[1].getText();
				String name = tf[3].getText();
				if(IDrc && pwt && pwequal && !name.equals("")) {
					Account.memberAccount(new Account(ID,pw,name));
					ConsolePrint.printlnLginMessage(ID + " 회원가입 완료.");
					JOptionPane.showMessageDialog(Session.getFrame(),"회원 가입에 성공하셨습니다. " + name + "님");
					FileIOMeneger.saveAccount();
					Session.setLogin_ID(ID);
					Session.getFrame().loginMenuPanel.repaint();
					Session.getFrame().displayMainMenu();
				}
				else {
					JOptionPane.showMessageDialog(Session.getFrame(),"모든 입력칸을 충족해주세요.");
				}
			}
			if(e.getSource().equals(btn[1])) {
				for(JTextField tf : tf) {
					tf.setText("");
				}
				IDrc = pwt = pwequal = false;
				elb[0].setText("중복 확인이 필요합니다");
				elb[0].setForeground(Color.black);
				tf[0].setFocusable(true);
				elb[1].setText("대,소문자,숫자가 포함되어야 합니다.");
				elb[1].setForeground(Color.BLACK);
				elb[2].setText("비밀번호 확인이 일치하지 않습니다.");
				elb[2].setForeground(Color.BLACK);				
			}
			if(e.getSource().equals(btn[2])) {
				if(IDCheck(tf[0].getText())) {
					if(Account.redundancyCheckID(tf[0].getText()) && 
							JOptionPane.showConfirmDialog(Session.getFrame(),
									tf[0].getText() + " 사용가능\n사용하시겠습니까?" 
									,"아이디 중복검사.", JOptionPane.YES_NO_OPTION) == 0) {
						elb[0].setText("사용 가능한 아이디입니다.");
						elb[0].setForeground(new Color(72,200,62));	
						tf[0].setFocusable(false);
						IDrc = true;
					}
					else {
						elb[0].setText("중복된 아이디입니다.");
						elb[0].setForeground(Color.red);	
						IDrc = false;
					}
				}
				else {
					elb[0].setText("4글자 이상이여야 합니다.");
					elb[0].setForeground(Color.red);	
					IDrc = false;
				}
			}
		}

	}
}
