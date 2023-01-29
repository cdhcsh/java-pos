package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Database;
import database.FileIOMeneger;
import database.Session;
import product.Product;
import product.ProductPanel;
import product.ProductCategory;
import product.ProductCategoryListPanel;
import product.ProductListPanel;

public class SettingMainPanel extends JPanel{
	private JPanel productSettingPanel = new JPanel();
	private JPanel tableSettingPanel = new JPanel();
	private JPanel dataSettingPanel = new JPanel();

	private JTabbedPane tp = new JTabbedPane();
	private Database DB = Account.getDBbyID(Session.getLogin_ID());
	public SettingMainPanel(Rectangle s) {
		setBounds(s);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setBackground(Color.white);
		setLayout(null);
		tp.setFont(new Font("����",Font.BOLD,18));
		tp.setBounds(0, 0, getWidth(), getHeight());
		Rectangle mr = new Rectangle(0,0,getWidth(), getHeight()-40);
		productSettingPanel = new ProductSettingMenuPanel(mr);
		tableSettingPanel = new TableSettingMenuPanel(mr);
		dataSettingPanel = new DataMenegerMenuPanel(mr);
		tp.addTab("       �� ��   �� ��       ", productSettingPanel);
		tp.addTab("       �� �� ��   �� ��       ", tableSettingPanel);
		tp.addTab("       �� �� ��   �� ��       ", dataSettingPanel);
		tp.repaint();
		add(tp);
		setVisible(true);
	}
}
