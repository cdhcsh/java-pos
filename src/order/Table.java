package order;

import java.awt.Rectangle;
import java.util.Vector;

import database.FileIOMeneger;

public class Table{
	private int tableNo;
	private boolean ordered = false;
	private OrderList orderlist = new OrderList();
	private int pos;
	public Table(int pos,int tableNo) {
		this.tableNo = tableNo;
		this.pos = pos;
	}
	public int getTableNo() {
		return tableNo;
	}
	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public Vector<Order> getOrders(){
		return orderlist.getOrders();
	}
	public void setOrderList(OrderList orderlist) {
		this.orderlist = orderlist;
		if(this.orderlist.getOrders().isEmpty())ordered = false;
		else ordered = true;
	}
	public OrderList getOrderList() {
		return orderlist;
	}
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
	public boolean isOrdered() {
		ordered = !getOrders().isEmpty();
		return ordered;
	}
	public String toString() {
		return pos + " " + tableNo;
	}
}
