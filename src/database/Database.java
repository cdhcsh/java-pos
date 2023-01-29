package database;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;

import order.Bill;
import order.Order;
import order.OrderList;
import order.Table;
import product.Product;
import product.ProductCategory;

public class Database {
	private Vector<ProductCategory> productCategorys = new Vector<ProductCategory>();
	private Vector<Table> tables = 	new Vector<Table>();
	private Vector<Bill> bills =new Vector<Bill>();
	private int tableCols = 6;
	private int tableRows = 5;
	public Database() {
		defalutTableSetting();
	}
	public boolean redundancyCheckProductCategoryName(String name) { // 상품 카테고리 이름 중복검사
		for(ProductCategory pc : productCategorys) {
			if(pc.getCategoryName().equals(name)) return false;
		}
		return true;
	}
	public boolean removeProductcategory(ProductCategory productCategory) {
		return productCategorys.remove(productCategory);
	}
	public boolean addProductCategory(ProductCategory productCategory) {
		if(redundancyCheckProductCategoryName(productCategory.getCategoryName())) {
			productCategorys.add(productCategory);
		}
		return false;
	}
	public void setProductCategories(Vector<ProductCategory> productCategorys) {
		this.productCategorys = productCategorys;
	}
	public Vector<ProductCategory> getProductCategories(){
		Vector<ProductCategory> tmp = new Vector<ProductCategory>();
		for(ProductCategory pc : productCategorys){
			tmp.add(pc);
		}
		return tmp;

	}
	public Vector<String> getProductColumns(){
		Vector<String> columns = new Vector<String>();
		columns.add("상품 카테고리");
		columns.add("상품명");
		columns.add("상품 기격");
		return columns;
	}
	public Vector<Vector<String>> getProductData(){
		Vector<Vector<String>> datas = new Vector<Vector<String>>();
		Vector<String> data;
		for(ProductCategory pc : productCategorys) {
			data = new Vector<String>();
			for(int i = 0 ; i < 3 ; i++) data.add("");
			data.set(0,pc.getCategoryName());
			datas.add(data);
			for(Product p : pc.getProducts()) {
				data = new Vector<String>();
				for(int i = 0 ; i < 3 ; i++) data.add("");
				data.set(1, p.getProductName());
				data.set(2, Bill.intToWonFormat(p.getProductPrice()));
				datas.add(data);
			}
		}
		return datas;
	}
	public void editProductCategory(ProductCategory productCategory,String name) {
		productCategory.setCategoryName(name);

	}

	public int getRows() {
		return tableRows;
	}
	public int getCols() {
		return tableCols;
	}
	public void defalutTableSetting() {
		tables.removeAllElements();
		for(int i = 0 ; i < 6 ; i++) {
			tables.add(new Table(i,i+1));
		}	
	}
	public void setTableLayout(int cols,int rows) {
		this.tableCols = cols;
		this.tableRows = rows;
	}	
	public Vector<Table> getTables(){
		return tables;
	}
	public void setTables(Vector<Table> tables) {
		this.tables = tables;
	}
	public void addTable(int pos) {
		tables.add(new Table(pos,tables.size()+1));
	}
	public void removeTable(Table table) {
		tables.remove(table);
		for(int i = 0 ; i < tables.size() ;i++) {
			tables.get(i).setTableNo(i+1);
		}
	}
	public Vector<Vector<String>> getTableData(){
		Vector<Vector<String>> datas = new Vector<Vector<String>>();
		Vector<String> data;
		for(Table table : tables) {
			data = new Vector<String>();
			for(int i = 0 ; i < 5 ; i++) data.add("");
			data.set(0, Integer.toString(table.getTableNo()));
			data.set(1, Integer.toString(table.getPos()));
			datas.add(data);
			for(Order order : table.getOrders()) {
				data = new Vector<String>();
				for(int i = 0 ; i < 5 ; i++) data.add("");
				data.set(2,order.getProductName());
				data.set(3,Integer.toString(order.getProductCount()));
				data.set(4,Bill.intToWonFormat(order.getTotalPrice()));
				datas.add(data);
			}
			if(table.isOrdered()) {
				data = new Vector<String>();
				for(int i = 0 ; i < 5 ; i++) data.add("");
				data.set(3, "  금액 합계");
				data.set(4, table.getOrderList().getTotalPriceWon());
				datas.add(data);
			}
		}
		return datas;
	}
	public Vector<String> getTableColumns(){
		Vector<String> columns = new Vector<String>();
		columns.add("테이블 번호");
		columns.add("테이블 위치");
		columns.add("상품명");
		columns.add("수량");
		columns.add("가격");
		return columns;
	}

	public void setBills(Vector<Bill> bills) {
		this.bills = bills;
	}
	public Vector<Bill> getBills(){
		Collections.sort(bills);
		return bills;
	}
	public boolean removeBill(Bill bill) {
		return bills.remove(bill);
	}
	public boolean addBill(Bill bill) {
		return bills.add(bill);
	}
	public boolean editBill(Bill oldBill,Bill newBill) {
		bills.remove(oldBill);
		return bills.add(newBill);
	}
	public Vector<Vector<String>> getBillData(Vector<Bill> bills){
		if(bills == null) bills = this.bills;
		Collections.sort(bills);
		Vector<Vector<String>> datas = new Vector<Vector<String>>();
		Vector<String> data;
		for(Bill bill : bills) {
			data = new Vector<String>();
			for(int i = 0 ; i < 4 ; i++) data.add("");
			data.set(0, bill.getTime());
			datas.add(data);
			for(Order order : bill.getOrders()) {
				data = new Vector<String>();
				for(int i = 0 ; i < 4 ; i++) data.add("");
				data.set(1,order.getProductName());
				data.set(2,Integer.toString(order.getProductCount()));
				data.set(3,Bill.intToWonFormat(order.getTotalPrice()));
				datas.add(data);
			}
			data = new Vector<String>();
			for(int i = 0 ; i < 4 ; i++) data.add("");
			data.set(2, "  금액 합계");
			data.set(3, Bill.intToWonFormat(bill.getTotalPrice()));
			datas.add(data);
		}
		return datas;

	}
	public Vector<Vector<String>> getProductBillData(){
		Vector<Vector<String>> datas = new Vector<Vector<String>>();
		Vector<String> data;
		Vector<Order> orders = getTop5Product();
		int totalprice;
		for(ProductCategory pc : productCategorys) {
			data = new Vector<String>();
			for(int i = 0 ; i < 4 ; i++) data.add("");
			data.set(0, pc.getCategoryName());
			datas.add(data);
			totalprice = 0;
			for(Order order : orders) {
				if(!pc.redundancyCheckProductName(order.getProductName())) {
					data = new Vector<String>();
					for(int i = 0 ; i < 4 ; i++) data.add("");
					totalprice += order.getTotalPrice();
					data.set(1,order.getProductName());
					data.set(2,Integer.toString(order.getProductCount()));
					data.set(3,Bill.intToWonFormat(order.getTotalPrice()));
					order.setProductName("$$$");
					datas.add(data);					
				}
			}
			data = new Vector<String>();
			for(int i = 0 ; i < 4 ; i++) data.add("");
			data.set(2, "  금액 합계");
			data.set(3, Bill.intToWonFormat(totalprice));
			datas.add(data);			
		}
		if(orders != null) {
			data = new Vector<String>();
			for(int i = 0 ; i < 4 ; i++) data.add("");
			data.set(0, "해당없음");
			datas.add(data);
			totalprice = 0;
			for(Order order : orders) {
				if(!order.getProductName().equals("$$$")) {
					data = new Vector<String>();
					for(int i = 0 ; i < 4 ; i++) data.add("");
					totalprice += order.getTotalPrice();
					data.set(1,order.getProductName());
					data.set(2,Integer.toString(order.getProductCount()));
					data.set(3,Bill.intToWonFormat(order.getTotalPrice()));
					datas.add(data);
				}
			}
			data = new Vector<String>();
			for(int i = 0 ; i < 4 ; i++) data.add("");
			data.set(2, "  금액 합계");
			data.set(3, Bill.intToWonFormat(totalprice));
			datas.add(data);
		}
		return datas;
	}
	public Vector<String> getBillColumns(){
		Vector<String> columns = new Vector<String>();
		columns.add("결제 시간");
		columns.add("상품명");
		columns.add("수량");
		columns.add("가격");		
		return columns;
	}
	public Vector<String> getProductBillCoumns(){
		Vector<String> columns = new Vector<String>();
		columns.add("상품 카테고리");
		columns.add("상품명");
		columns.add("수량");
		columns.add("가격");		
		return columns;

	}
	public String getBillTotalPrice(Vector<Bill> bills) {
		int totalprice = 0;
		if(bills == null) bills = this.bills;
		for(Bill bill : bills) {
			totalprice += bill.getTotalPrice();
		}
		return Bill.intToWonFormat(totalprice);
	}
	public Vector<Bill> getBillsToFrom(Date from,Date to){
		Vector<Bill> tmp  = new Vector<Bill>();
		for(Bill bill : bills) {
			if(bill.yearmonthdayFormat.format(from).equals(Bill.yearmonthdayFormat.format(bill.getDate()))
					|| (bill.getDate().compareTo(from) >= 0 && bill.getDate().compareTo(to) <= 0)){
				tmp.add(bill);
			}
		}
		return tmp;
	}
	public int getMaximumDaysofMonth(int month) {
		Vector<Integer> tmp = new Vector<Integer>();
		Calendar cal = Calendar.getInstance();
		cal.set(2018, month-1, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);		
	}
	public String getBillAveragePrice(Vector<Bill> bills) {
		if(bills == null) bills = this.bills;
		int totalprice = Bill.WonFormatToInt(getBillTotalPrice(bills));
		return Bill.intToWonFormat(totalprice/bills.size());
	}
	public String getBillTotalPricebyYear(int year) {
		int totalprice = 0;
		for(Bill bill : bills) {
			if(bill.getY() == year)
				totalprice += bill.getTotalPrice();
		}
		return Bill.intToWonFormat(totalprice);
	}
	public String getBillAveragePricebyYear(int year) {
		int totalprice = Bill.WonFormatToInt(getBillTotalPricebyYear(year));
		return Bill.intToWonFormat(totalprice/12);
	}
	public String getBillTotalPricebyMonth(int month) {
		int totalprice = 0;
		for(Bill bill : bills) {
			if(bill.getM() == month)
				totalprice += bill.getTotalPrice();
		}
		return Bill.intToWonFormat(totalprice);
	}
	public String getBillAveragePricebyMonth(int month) {
		int totalprice = Bill.WonFormatToInt(getBillTotalPricebyMonth(month));
		return Bill.intToWonFormat(totalprice/getMaximumDaysofMonth(month));
	}
	public String getBillAveragePricebyHour() {
		int totalprice = Bill.WonFormatToInt(getBillTotalPrice(null));
		return Bill.intToWonFormat(totalprice/24);
	}
	public Vector<Integer> getBillbyYear(int year){
		Collections.sort(bills);
		Vector<Integer> tmp = new Vector<Integer>();
		int[] prices = new int[12];
		for(int i = 0 ; i < 12 ; i++) {
			prices[i] = 0;
		}
		for(Bill bill : bills) {
			if(bill.getY() == year) {
				prices[bill.getM()-1] += bill.getTotalPrice();
			}
		}
		for(int i = 0 ; i < 12 ; i++) {
			tmp.add(prices[i]);
		};
		return tmp;

	}
	public Vector<Integer> getbillbyMonth(int month){
		Vector<Integer> tmp = new Vector<Integer>();
		int maxdays = getMaximumDaysofMonth(month);
		int[] prices = new int[maxdays];
		for(int i = 0 ; i < maxdays ; i++) {
			prices[i] = 0;
		}
		for(Bill bill : bills) {
			if(bill.getM() == month) {
				prices[bill.getD()-1] += bill.getTotalPrice();
			}
		}
		for(int i = 0 ; i < maxdays ;i++) {
			tmp.add(prices[i]);
		}
		return tmp;
	}
	public Vector<Integer> getbillbyHour(){
		Vector<Integer> tmp = new Vector<Integer>();
		int[] prices = new int[24];
		for(int i = 0 ; i < 24 ; i++) {
			prices[i] = 0;
		}
		for(Bill bill : bills) {
			prices[bill.getH()] += bill.getTotalPrice();
		}
		for(int i = 0 ; i < 24 ;i++) {
			tmp.add(prices[i]);
		}
		return tmp;
	}
	public Vector <String> getDayColumns(int month){
		int maxdays = getMaximumDaysofMonth(month);
		Vector<String> tmp = new Vector<String>();
		for(int i = 0 ; i < maxdays ; i++) {
			tmp.add(i+1+"");
		}
		return tmp;
	}
	public Vector<String> getMonthColumns(){
		Vector<String> tmp = new Vector<String>();
		for(int i = 0 ; i < 12 ; i++) {
			tmp.add(i+1 + " 월");
		}
		return tmp;
	}
	public Vector<Order> getTop5Product(){
		OrderList tmp = new OrderList();
		for(Bill bill : bills) {
			for(Order order : bill.getOrders()) {
				Order o = new Order(
						new Product(order.getProductName(), order.getProductCount(), order.getProductPrice()));
				tmp.addOrder(o);
			}
		}
		tmp.refreshOrders();
		Vector<Order> orders = tmp.getOrders();
		Collections.sort(orders);
		return orders;

	}
	public Vector<String> getHourColumns(){
		Vector<String> tmp = new Vector<String>();
		for(int i = 0 ; i < 24 ; i++) {
			tmp.add(i+"h");
		}
		return tmp;
	}

}
