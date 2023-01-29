package order;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Bill implements Comparable<Bill>{
	public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	public static SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	public static SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
	public static SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	public static SimpleDateFormat yearmonthdayFormat = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat loadFormat = new SimpleDateFormat("yyyyMMddHHmm");
	public static SimpleDateFormat showFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
	private String time;
	private Date date;
	private int m,d,h,y;
	private Vector<Order> orders = new Vector<Order>();
	private int totalPrice = 0;
	public Bill(Table table) {
		orders = table.getOrders();
		date = Calendar.getInstance().getTime();
		time = showFormat.format(date.getTime());
		y = Integer.parseInt(yearFormat.format(date));
		m = Integer.parseInt(monthFormat.format(date));
		d = Integer.parseInt(dayFormat.format(date));
		h = Integer.parseInt(hourFormat.format(date));
	}
	public Bill(String dateString) {
		try {
			date = loadFormat.parse(dateString);
			time = showFormat.format(date.getTime());
			y = Integer.parseInt(yearFormat.format(date));
			m = Integer.parseInt(monthFormat.format(date));
			d = Integer.parseInt(dayFormat.format(date));
			h = Integer.parseInt(hourFormat.format(date));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public Date getDate() {
		return date;
	}
	public int getM() {
		return m;
	}
	public int getD() {
		return d;
	}
	public int getH() {
		return h;
	}
	public int getY() {
		return y;
	}
	public void addOrder(Order order) {
		orders.add(order);
	}
	public int redundancyCheckOrder(Vector<Order> tmp , String name) { // 상품이름 중복검사
		for(int j = 0 ; j <tmp.size();j++) {
			if(tmp.get(j).getProductName().equals(name)) {
				return j;
			}
		}
		return -1;
	}
	public void refreshOrders() {
		Vector<Order> tmp = new Vector<Order>();
		for(Order o : orders) {
			if(tmp.isEmpty()) tmp.add(o);
			else if(redundancyCheckOrder(tmp, o.getProductName()) >= 0){
				tmp.get(redundancyCheckOrder(tmp, o.getProductName())).add(o.getProductCount());
			}
			else tmp.add(o);
		}
		orders = tmp;
	}
	public static int WonFormatToInt(String str) {
		try {
			return new DecimalFormat("\u00A4 ###,###,###,##0 ").parse(str).intValue();
		} catch (ParseException e) {
			return 0;
		}
	}
	public static String intToWonFormat(int num) {
		return new DecimalFormat("\u00A4 ###,###,###,##0 ").format(num);
	}
	public String toString() {
		return time + " , " + intToWonFormat(getTotalPrice());
	}
	public String getTime() {
		return time;
	}
	public String getDateString() {
		return loadFormat.format(date);
	}
	public Vector<Order> getOrders() {
		return orders;
	}
	public int getTotalPrice() {
		totalPrice = 0;
		for(Order o : orders) totalPrice += o.getTotalPrice();
		return totalPrice;
	}
	@Override
	public int compareTo(Bill o) {
		return this.date.compareTo(o.date);
	}
	
}
