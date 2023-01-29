package database;

import javax.swing.JPanel;

import account.Account;
import menu.MainFrame;
import others.ConsolePrint;

public class Session {
	private static String login_ID = null;
	private static String display_Menu = null;
	private static MainFrame frame = null;
	private static JPanel displayPanel = null;
	private static Database DB = null;
	
	public static String getLogin_ID() {
		return login_ID;
	}
	public static void setLogin_ID(String login_ID) {
		ConsolePrint.printlnSessionMessage("���� �α��� ���� ���� ��..");
		DB = Account.getDBbyID(login_ID);
		Session.login_ID = login_ID;
		ConsolePrint.printlnSessionMessage("���� �α��� ���� ���� �Ϸ�.");
	}
	public static String getDisplay_Menu() {
		return display_Menu;
	}
	public static void setDisplay_Menu(String display_Menu) {
		Session.display_Menu = display_Menu;
		ConsolePrint.printlnSessionMessage("���θ޴� �ǳ� ���� ���� ��..");
		frame.setTitle("P O S_"+Session.getDisplay_Menu());
		frame.getContentPane().repaint();
		ConsolePrint.printlnSessionMessage("���θ޴� �ǳ� ���� ���� �Ϸ�. (" +display_Menu +")");
	}
	public static void setFrame(MainFrame frame) {
		ConsolePrint.printlnSessionMessage("���� ���� ���� ������ ����");
		Session.frame = frame;
	}
	public static MainFrame getFrame() {
		return frame;
	}
}
