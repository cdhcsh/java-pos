package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import account.Account;
import database.Database;
import database.Session;
import order.Bill;
import order.Order;
import others.drawGraph;

public class SalesMainPanel extends JPanel{
	private Rectangle mainRectangle,subRectangle,viewRectangle;
	private JPanel subMenuPanel,subViewPanel,top5Panel;
	private JLabel title,totalprice,averageprice;
	private JLabel top5title;
	private JLabel[] top5label = new JLabel[10];
	private SpinnerDateModel totimeSdm,fromtimeSdm,yearSdm,monthSdm;
	private JSpinner totimeSpinner,fromtimeSpinner,yearSpinner,monthSpinner;
	private JButton[] btns = new JButton[6];
	private TableScrollViewPanel allBillTsvp,daysBillTsvp,productBillTsvp;
	private Database DB = Account.getDBbyID(Session.getLogin_ID());
	public SalesMainPanel(Rectangle s) {
		setBounds(s);
		setBackground(Color.white);
		setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		setLayout(null);
		setVisible(true);
		setUp();
	}
	public void setUp() {
		mainRectangle = new Rectangle(getWidth()/4,0,getWidth()/4*3, getHeight()-50);
		subRectangle = new Rectangle(0,0,getWidth()/4,getHeight());
		viewRectangle = new Rectangle(getWidth()/4,getHeight()-50,getWidth()/4*3,50);
		
		subMenuPanel = new JPanel(null);
		subMenuPanel.setBounds(subRectangle);
		subMenuPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));

		subViewPanel = new JPanel(null);
		subViewPanel.setBounds(viewRectangle);
		subViewPanel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1)));
		
		allBillTsvp = new TableScrollViewPanel(mainRectangle,DB.getBillData(null), DB.getBillColumns());
		productBillTsvp = new TableScrollViewPanel(mainRectangle, DB.getProductBillData(), DB.getProductBillCoumns());
		
		title = new JLabel("매출 현황 ");
		title.setBounds(15,10,subMenuPanel.getWidth()-30,40);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setOpaque(true);
		title.setBackground(Color.WHITE);
		title.setFont(new Font("",Font.BOLD,19));
		title.setBorder(new TitledBorder(new LineBorder(Color.GRAY,2)));
		
		ActionListener sbl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource().equals(btns[0])) {
					remove(2);
					add(allBillTsvp,2);
					totalprice.setText(" 총 금액 합계 : " + DB.getBillTotalPrice(null));
					averageprice.setText(" 건당 평균 판매 금액 : " + DB.getBillAveragePrice(null));
					repaint();
				}
				else if(e.getSource().equals(btns[1])) {
					remove(2);
					add(productBillTsvp,2);
					totalprice.setText(" 총 금액 합계 : " + DB.getBillTotalPrice(null));
					averageprice.setText("");
					repaint();
				}
				else if(e.getSource().equals(btns[2])) {
					remove(2);
					add(new drawGraph(mainRectangle,DB.getbillbyHour(),DB.getHourColumns()),2);
					totalprice.setText(" 총 금액 합계 : " + DB.getBillTotalPrice(null));
					averageprice.setText("시간별 평균판매 금액 : " + DB.getBillAveragePricebyHour());
					repaint();
				}
				else if(e.getSource().equals(btns[3])) {
					Date from = (Date) fromtimeSpinner.getValue();
					Date to = (Date) totimeSpinner.getValue();
					Vector<Bill> bills = DB.getBillsToFrom(from, to);
					daysBillTsvp = new TableScrollViewPanel(mainRectangle, DB.getBillData(bills), DB.getBillColumns());
					remove(2);
					add(daysBillTsvp,2);
					totalprice.setText(" 총 금액 합계 : " + DB.getBillTotalPrice(bills));
					averageprice.setText(" 건당 평균 판매 금액 : " + DB.getBillAveragePrice(bills));
					repaint();
				}
				else if(e.getSource().equals(btns[4])) {
					int year = Integer.parseInt(Bill.yearFormat.format(yearSpinner.getValue()));
					remove(2);
					add(new drawGraph(mainRectangle,DB.getBillbyYear(year),DB.getMonthColumns()),2);
					totalprice.setText(" 총 금액 합계 : " + DB.getBillTotalPricebyYear(year));
					averageprice.setText("월 평균 판매 금액 : " + DB.getBillAveragePricebyYear(year));
					repaint();
				}
				else if(e.getSource().equals(btns[5])) {
					int month = Integer.parseInt(Bill.monthFormat.format(monthSpinner.getValue()));
					remove(2);
					add(new drawGraph(mainRectangle,DB.getbillbyMonth(month),DB.getDayColumns(month)),2);
					totalprice.setText(" 총 금액 합계 : " + DB.getBillTotalPricebyMonth(month));
					averageprice.setText("일 평균판매 금액 : " + DB.getBillAveragePricebyMonth(month));
					repaint();
				}
			}
		};
		
		btns[0] = new JButton("전체 매출");
		btns[0].setToolTipText("전체 판매내역을 표로 나타냅니다.");
		btns[0].setBounds(30,60,subMenuPanel.getWidth()-60,40);
		btns[0].addActionListener(sbl);
	
		btns[1] = new JButton("상품별 매출");
		btns[1].setToolTipText("전체 상품별 판매내역을 표로 나타냅니다.");
		btns[1].setBounds(30,110,(subMenuPanel.getWidth()-10)/2-30,40);
		btns[1].addActionListener(sbl);

		btns[2] = new JButton("시간별 매출");
		btns[2].setToolTipText("시간대 별로 매출을 그래프로 나타냅니다.");
		btns[2].setBounds((subMenuPanel.getWidth()-10)/2 + 10,110,(subMenuPanel.getWidth()-10)/2-30,40);
		btns[2].addActionListener(sbl);
		
		
		Date now = new Date();
		
		fromtimeSdm = new SpinnerDateModel(now,null,now,Calendar.DAY_OF_YEAR);
		fromtimeSpinner = new JSpinner(fromtimeSdm);
		fromtimeSpinner.setEditor(new JSpinner.DateEditor(fromtimeSpinner,"yyyy-MM-dd"));
		fromtimeSpinner.setBounds(30,160,95,40);
		
		totimeSdm = new SpinnerDateModel(now,null,now,Calendar.DAY_OF_YEAR);
		totimeSpinner = new JSpinner(totimeSdm);
		totimeSpinner.setEditor(new JSpinner.DateEditor(totimeSpinner,"yyyy-MM-dd"));
		totimeSpinner.setBounds(125,160,95,40);
		
		btns[3] = new JButton("매출");
		btns[3].setToolTipText("선택한 기간의 판매 내역을 표로 나타냅니다.");
		btns[3].setBounds(225,160,subMenuPanel.getWidth()-255,40);
		btns[3].addActionListener(sbl);
		
		yearSdm = new SpinnerDateModel(now,null,now,Calendar.YEAR);
		yearSpinner = new JSpinner(yearSdm);
		yearSpinner.setEditor(new JSpinner.DateEditor(yearSpinner,"yyyy"));
		yearSpinner.setBounds(30,210,100,40);
		
		btns[4] = new JButton("월별 매출");
		btns[4].setToolTipText("선택한 년도의  월별 매출을 그래프로 나타냅니다.");
		btns[4].setBounds(160,210,subMenuPanel.getWidth()-190,40);
		btns[4].addActionListener(sbl);

		monthSdm = new SpinnerDateModel(now,null,now,Calendar.MONTH);
		monthSpinner = new JSpinner(monthSdm);
		monthSpinner.setEditor(new JSpinner.DateEditor(monthSpinner,"MM"));
		monthSpinner.setBounds(30,260,100,40);
		
		btns[5] = new JButton("일별 매출");
		btns[5].setToolTipText("선택한 달의  일별 매출을 그래프로 나타냅니다.");
		btns[5].setBounds(160,260,subMenuPanel.getWidth()-190,40);
		btns[5].addActionListener(sbl);		
		
		top5Panel = new JPanel(null) {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(Color.gray);
				g.drawLine(37,40, 37, getHeight());
				g.drawLine(getWidth()-60,40, getWidth()-60, getHeight());
				for(int i = 0 ; i < 4 ; i++) {
					g.drawLine(0,(i+2)*40,getWidth(),(i+2)*40);
				}
			}
		};
		top5Panel.setBounds(30,subMenuPanel.getHeight()-260,subMenuPanel.getWidth()-60,240);
		top5Panel.setBorder(new TitledBorder(new LineBorder(Color.gray, 2)));
		top5Panel.setBackground(Color.WHITE);
		
		top5title = new JLabel("  TOP 5 상품");
		top5title.setBounds(0, 0, top5Panel.getWidth(), 40);
		top5title.setOpaque(true);
		top5title.setFont(new Font("",Font.BOLD,17));
		top5title.setBackground(Color.LIGHT_GRAY);
		top5title.setForeground(Color.WHITE);
		top5title.setBorder(new TitledBorder(new LineBorder(Color.GRAY,2)));
		
		top5Panel.add(top5title);
		Vector<Order> top5 = DB.getTop5Product();
		for(int i = 0 ; i < 5 && i < top5.size() ; i++) {
			top5label[i*2] = new JLabel("   " + (i+1) + "    " + top5.get(i).getProductName());
			top5label[i*2].setBounds(0,(i+1)*40,top5Panel.getWidth()-60,40);
			top5Panel.add(top5label[i*2]);
			top5label[i*2+1] = new JLabel(" " + top5.get(i).getProductCount());
			top5label[i*2+1].setBounds(top5Panel.getWidth()-60,(i+1)*40,60,40);
			top5label[i*2+1].setHorizontalAlignment(JLabel.CENTER);
			top5Panel.add(top5label[i*2+1]);
		}
	
		
		subMenuPanel.add(title);
		for(int i = 0 ; i < 6 ; i++) {
			subMenuPanel.add(btns[i]);
		}
		subMenuPanel.add(totimeSpinner);
		subMenuPanel.add(fromtimeSpinner);
		subMenuPanel.add(yearSpinner);
		subMenuPanel.add(monthSpinner);
		subMenuPanel.add(top5Panel);
		
		
		totalprice = new JLabel(" 총 금액 합계 : " + DB.getBillTotalPrice(null));
		totalprice.setBounds(5,5,subViewPanel.getWidth()/3 - 10,40);
		
		averageprice = new JLabel("건당 평균 판매 금액 : " + DB.getBillAveragePrice(null));
		averageprice.setBounds(subViewPanel.getWidth()/3 + 5,5,subViewPanel.getWidth()/3 -10,40);

		subViewPanel.add(totalprice);
		subViewPanel.add(averageprice);
		
		add(subMenuPanel,0);
		add(subViewPanel,1);
		add(allBillTsvp,2);
		repaint();
	}
}
