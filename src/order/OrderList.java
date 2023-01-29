package order;

import java.util.Vector;

public class OrderList{
	private Vector<Order> orders = new Vector<Order>();
	
	public OrderList() {
		orders = new Vector<Order>();
	}
	public OrderList(Vector<Vector<String>> orders){
		this.orders = new Vector<Order>();
		for(Vector<String> v : orders) {
			this.orders.add(new Order(v.get(0),v.get(1),v.get(2)));
		}
	}
	public void addOrder(Order order) {
		orders.add(order);
	}
	public void addOrders(Vector<Order> orders) {
		for(Order o : orders) this.orders.add(o);
		refreshOrders();
	}
	public boolean removeOrder(Order order) {
		return orders.remove(order);
	}
	public Vector<Order> getOrders(){
		return orders;
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
	
	public static Vector<String> getOrderListColumns(){
		Vector<String> columns = new Vector<String>();
		columns.add("상품명");
		columns.add("수량");
		columns.add("가격");
		return columns;
	}
	public Vector<Vector<String>> getOrderListData(){
		Vector<Vector<String>> list = new Vector<Vector<String>>();
		if(orders.isEmpty()) return null;
		for(Order o : orders) {
			Vector<String> tmp = new Vector<String>();
			tmp.add(o.getProductName());
			tmp.add(Integer.toString(o.getProductCount()));
			tmp.add(Bill.intToWonFormat(o.getTotalPrice()));
			list.add(tmp);
		}
		return list;
	}
	public int getTotalPrice(){
		int totalPrice = 0;
		for (Order o : orders){
			totalPrice += o.getTotalPrice();
		}
		return totalPrice;
	}
	public String getTotalPriceWon() {
		return Bill.intToWonFormat(getTotalPrice());
	}


}
