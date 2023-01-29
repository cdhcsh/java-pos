package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Database;
import database.FileIOMeneger;
import database.Session;

public class DataMenegerMenuPanel extends JPanel{
	private Rectangle tsvpRectangle;
	private Database DB = Account.getDBbyID(Session.getLogin_ID());
	private TableScrollViewPanel tsvp = null;;
	private Database readedDB = new Database();
	private JLabel l1,l2;
	private JTextField loadfilename,savefilename;
	private JSpinner loadfiletype,savefiletype;
	private JButton loadbtn,resetLoadbtn,savebtn,applybtn;
	private String defaultname = Session.getLogin_ID() + "/";
	private String[] types = {"product","bill","table"};
	private SpinnerListModel slm1,slm2;
	private String datatype = "notting";
	public DataMenegerMenuPanel(Rectangle s) {
		setBounds(s);
		setLayout(null);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		setUp();
		repaint();
	}
	public JPanel getEmptyPanel() {
		JPanel tmp = new JPanel();
		tmp.setBounds(tsvpRectangle);
		tmp.setBackground(Color.white);
		tmp.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		return tmp;
	}
	public void setUp() {
		removeAll();
		tsvpRectangle = new Rectangle(0,0,getWidth()/4*3,getHeight());
		l1 = new JLabel("������ �ҷ�����");
		l1.setBounds(getWidth()/4*3 + 10,5,getWidth()/4 - 20 , 40);
		l1.setHorizontalAlignment(JLabel.CENTER);
		l1.setOpaque(true);
		l1.setBackground(Color.WHITE);
		l1.setFont(new Font("",Font.BOLD,19));
		l1.setBorder(new TitledBorder(new LineBorder(Color.GRAY,2)));

		slm1  = new SpinnerListModel(types);
		slm2  = new SpinnerListModel(types);

		loadfiletype = new JSpinner(slm1);		
		loadfiletype.setBounds(getWidth()/4*3 + 10, 55, (getWidth()/4/3) - 20, 40);

		loadfilename = new JTextField(defaultname);
		loadfilename.setBounds(getWidth()/6*5 + 10 ,55,getWidth()/4/3*2- 20,40);

		loadbtn = new JButton("�ҷ�����");
		loadbtn.setToolTipText("������ ������ �ҷ��ɴϴ�.");
		loadbtn.setBounds(getWidth()/4*3 + 10,105,getWidth()/8- 20,40);

		resetLoadbtn= new JButton("�ʱ�ȭ");
		resetLoadbtn.setToolTipText("����ȭ���� �ʱ�ȭ�մϴ�.");
		resetLoadbtn.setBounds(getWidth()/8*7 + 10,105,getWidth()/8- 20,40);

		l2 = new JLabel("������ �����ϱ�");
		l2.setBounds(getWidth()/4*3 + 10,200,getWidth()/4 - 20 , 40);
		l2.setHorizontalAlignment(JLabel.CENTER);
		l2.setOpaque(true);
		l2.setBackground(Color.WHITE);
		l2.setFont(new Font("",Font.BOLD,19));
		l2.setBorder(new TitledBorder(new LineBorder(Color.GRAY,2)));

		savefiletype = new JSpinner(slm2);		
		savefiletype.setBounds(getWidth()/4*3 + 10, 250, (getWidth()/4/3) - 20, 40);

		savefilename = new JTextField(defaultname);
		savefilename.setBounds(getWidth()/6*5 + 10 ,250,getWidth()/4/3*2- 20,40);

		savebtn = new JButton("�����ϱ�");
		savebtn.setToolTipText("�ҷ��� �����͸� �ٸ� ���Ͽ� �����մϴ�");
		savebtn.setBounds(getWidth()/4*3 + 10,300,getWidth()/8- 20,40);

		applybtn= new JButton("�����ϱ�");
		applybtn.setToolTipText("�ҷ��� �����͸� ���α׷��� �����մϴ�.");
		applybtn.setBounds(getWidth()/8*7 + 10,300,getWidth()/8- 20,40);		




		ActionListener pl = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(loadbtn)) {
					File f = new File("rsrc/"+loadfilename.getText());
					if(!f.isFile()) {
						JOptionPane.showMessageDialog(Session.getFrame(),"������ �������� �ʽ��ϴ�.");
					}
					else {
						TableScrollViewPanel tmp = loadDatas(f.toString(), loadfiletype.getValue().toString());
						if(tmp != null) {
							datatype = loadfiletype.getValue().toString();
							remove(10);
							tsvp = tmp;
							add(tsvp,10);
							repaint();
							JOptionPane.showMessageDialog(Session.getFrame(),"���� �ҷ����⸦ �Ϸ��߽��ϴ�.");
						}
					}

				}
				else if(e.getSource().equals(resetLoadbtn)) {
					loadfilename.setText(defaultname);
					datatype = "notting";
					remove(10);
					add(getEmptyPanel(),10);
					repaint();
				}
				if(e.getSource().equals(savebtn)) {
					if(datatype.equals(savefiletype.getValue())) {
						File f = new File("rsrc/"+savefilename.getText());
						if(f.isDirectory()) {
							JOptionPane.showMessageDialog(Session.getFrame(),"���� �̸��� �Է����ּ���.");							
						}
						else if(f.isFile() && JOptionPane.showConfirmDialog(Session.getFrame(),
								"�̹� ������ �����մϴ�. ����ðڽ��ϱ�?","���� ����", JOptionPane.YES_NO_OPTION) == 1) {
						}
						else {
							if(datatype.equals("product"))
								FileIOMeneger.saveProducts(readedDB.getProductCategories(), f.toString());
							else if(datatype.equals("bill"))
								FileIOMeneger.saveBills(readedDB.getBills(), f.toString());
							else if(datatype.equals("table"))
								FileIOMeneger.saveTables(readedDB.getTables(), f.toString());
							JOptionPane.showMessageDialog(Session.getFrame(),"���� ������ �Ϸ��߽��ϴ�.");
						}
					}
					else {
						JOptionPane.showMessageDialog(Session.getFrame(),"���� ������ Ÿ�԰� ���� ������ Ÿ���� �ٸ��ϴ�.");
					}
				}
				if(e.getSource().equals(applybtn)) {
					if(datatype.equals("notting")) {
						JOptionPane.showMessageDialog(Session.getFrame(),"�ҷ��� �����Ͱ� �����ϴ�.");
					}
					else {
						if(datatype.equals("product")) {
							FileIOMeneger.saveProducts(DB.getProductCategories(), "rsrc/"+defaultname+"product.datbk");
							DB.setProductCategories(readedDB.getProductCategories());
							FileIOMeneger.saveProductbyID(Session.getLogin_ID());
						}
						else if(datatype.equals("bill")) {
							FileIOMeneger.saveBills(DB.getBills(), "rsrc/"+defaultname+"bill.datbk");
							DB.setBills(readedDB.getBills());
							FileIOMeneger.saveBillbyID(Session.getLogin_ID());
						}
						else if(datatype.equals("table")) {
							FileIOMeneger.saveTables(DB.getTables(), "rsrc/"+defaultname+"table.datbk");
							DB.setTables(readedDB.getTables());
							FileIOMeneger.saveTablebyID(Session.getLogin_ID());
						}	
						JOptionPane.showMessageDialog(Session.getFrame(),"������ �Ϸ��߽��ϴ�.\n���� �����ʹ� "+datatype +".datbk�� ����˴ϴ�.");
					}
				}

			}
		};		

		loadbtn.addActionListener(pl);
		resetLoadbtn.addActionListener(pl);
		savebtn.addActionListener(pl);
		applybtn.addActionListener(pl);

		add(l1,0);
		add(l2,1);
		add(loadfiletype,2);
		add(loadfilename,3);
		add(savefiletype,4);
		add(savefilename,5);
		add(loadbtn,6);
		add(savebtn,7);
		add(resetLoadbtn,8);
		add(applybtn,9);
		add(getEmptyPanel(),10);
	}
	public TableScrollViewPanel loadDatas(String filename,String type) {
		Vector<Vector<String>> data = null;
		Vector<String> columns = null;
		if(type == "product") {
			readedDB.setProductCategories(FileIOMeneger.readProducts(filename));
			data = readedDB.getProductData();
			columns = readedDB.getProductColumns();
		}
		else if(type == "table") {
			readedDB.setTables(FileIOMeneger.readTables(filename));
			data = readedDB.getTableData();
			columns = readedDB.getTableColumns();			
		}
		else if(type == "bill") {
			readedDB.setBills(FileIOMeneger.readBills(filename));
			data = readedDB.getBillData(null);
			columns = readedDB.getBillColumns();		
		}
		if(data == null || columns == null) {
			JOptionPane.showMessageDialog(Session.getFrame(),"������ �ҷ����µ� �����߽��ϴ�.");
			return null;
		}
		if(data.isEmpty()) {
			JOptionPane.showMessageDialog(Session.getFrame(),"�ùٸ��� ���� Ÿ���̰ų� �� �����Դϴ�.");
			return null;
		}
		return new TableScrollViewPanel(tsvpRectangle, data, columns);	
	}
}
