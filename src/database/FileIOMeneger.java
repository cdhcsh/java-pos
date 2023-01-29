package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import com.sun.org.apache.bcel.internal.generic.IF_ICMPLE;

import account.Account;
import order.Bill;
import order.Order;
import order.OrderList;
import order.Table;
import others.ConsolePrint;
import product.Product;
import product.ProductCategory;

public class FileIOMeneger {
	public static final Pattern STANDARD_PATTERN = Pattern.compile("^[a-z|A-Z|0-9|��-�R]*$");
	public static Vector<String> read(String fileName){
		ConsolePrint.printlnFileMenegerMessage("[ " + fileName + " ]�� �����͸� �ҷ��ɴϴ� .");
		String readString = new String();
		StringTokenizer readStrings;
		Vector<String> datas = new Vector<String>();
		char[] data = new char[1024];
		File f = getFile(fileName);
		try {
			FileReader fr= new FileReader(f.toString());
			while(fr.read(data) > 0) {
				readString = readString.concat(new String(data));
				data = new char[1024];
			}
			if(data!=null) {
				readStrings = new StringTokenizer(readString, " \n\t;");
				while(readStrings.hasMoreTokens()) {
					String tmp = readStrings.nextToken();
					while(!STANDARD_PATTERN.matcher(tmp).matches()) {
						if(readStrings.hasMoreTokens())
							tmp = readStrings.nextToken();
						else {
							tmp = null;
							break;
						}
					}
					if(tmp != null)	datas.add(tmp);
				}
			}
		}
		catch (Exception e) {
			ConsolePrint.printlnFileMenegerMessage("�����߻�!");
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + fileName + " ]�� �����͸� �ҷ����� �Ϸ�.");
		return datas;
	}
	public static File getFile(String filename) {
		File f = new File(filename);
		try {
			if(!f.isFile()) {
				f.createNewFile();
				ConsolePrint.printlnFileMenegerMessage("������ ���������ʾ� ���� �����մϴ�.");
			}
			return f;
		}
		catch(IOException e) {
			ConsolePrint.printlnFileMenegerMessage("�����߻�!");	
			return null;
		}
	}
	public static BufferedWriter getWriter(File f) {
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]�� �����͸� �����մϴ� ");
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			return bw;
		}
		catch(IOException e) {
			ConsolePrint.printlnFileMenegerMessage("�����߻�!");
			return null;
		}
	}
	public static void readAccounts() {
		ConsolePrint.printlnFileMenegerMessage("���̵� ����� �ҷ��ɴϴ�.");
		Vector<String> datas = read("rsrc/accounts.dat");
		for(int i = 0 ; i < datas.size()/3 ; i++) {
			Account.memberAccount(new Account(datas.get(i*3),datas.get(i*3+1),datas.get(i*3+2)));
			File tmp = new File("rsrc/" + datas.get(i*3));
			tmp.mkdir();
		}
		ConsolePrint.printlnFileMenegerMessage("���̵� ��� �ҷ����� �Ϸ�.");

	}
	public static void readProductbyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + "ȸ���� ��ǰ������ �ҷ��ɴϴ�. ");	
		Account.getDBbyID(ID).setProductCategories(readProducts("rsrc/"+ID+"/product.dat"));
		ConsolePrint.printlnFileMenegerMessage(ID + "ȸ���� ��ǰ���� �ҷ����� �Ϸ�.");
	}
	public static Vector<ProductCategory> readProducts(String filename) {
		Vector<String> datas = read(filename);
		Vector<ProductCategory> productCategorys = new Vector<ProductCategory>();
		ProductCategory tmp = null;
		for(int i = 0 ; i <datas.size();i++) {
			if(datas.get(i).toLowerCase().equals("category")) {
				if(tmp != null)productCategorys.add(tmp);
				tmp = new ProductCategory(datas.get(++i));
			}
			else if(datas.get(i).toLowerCase().equals("end")) {
				if(tmp != null)productCategorys.add(tmp);
			}
			else if(tmp != null) {
				tmp.addProduct(new Product(datas.get(i),Integer.parseInt(datas.get(++i))));
			}
		}
		return productCategorys;
	}
	public static void readBillbyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + "ȸ���� ���⳻���� �ҷ��ɴϴ�. ");
		Account.getDBbyID(ID).setBills(readBills("rsrc/"+ID+"/bill.dat"));
		ConsolePrint.printlnFileMenegerMessage(ID + "ȸ���� ���⳻�� �ҷ����� �Ϸ�.");
	}
	public static Vector<Bill> readBills(String filename) {
		Vector<String> datas = read(filename);
		Vector<Bill> bills = new Vector<Bill>();
		Bill tmp = null;
		for(int i = 0 ; i<datas.size();i++) {
			if(datas.get(i).toLowerCase().equals("bill")) {
				if(tmp != null) {
					tmp.refreshOrders();
					bills.add(tmp);
				}
				tmp = new Bill(datas.get(++i));
			}
			else if(datas.get(i).toLowerCase().equals("end")) {
				if(tmp != null) {
					tmp.refreshOrders();
					bills.add(tmp);
				}
			}
			else if(tmp != null) {
				tmp.addOrder(new Order(new Product(datas.get(i),Integer.parseInt(datas.get(++i)),
						Integer.parseInt(datas.get(++i)))));
			}
		}
		return bills;
	}
	public static void readTablebyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + "ȸ���� ���̺� ������ �ҷ��ɴϴ�. ");
		Vector<Table> tables = readTables("rsrc/"+ID+"/table.dat");
		if(!tables.isEmpty()) {
			Account.getDBbyID(ID).setTables(tables);
			ConsolePrint.printlnFileMenegerMessage(ID + "ȸ���� ���̺� ���� �ҷ����� �Ϸ�.");
		}
		else {
			ConsolePrint.printlnFileMenegerMessage("���̺� ������ ���� �⺻ ������ �����մϴ�.");
		}

	}
	public static Vector<Table> readTables(String filename){
		Vector<String> datas = read(filename);
		Vector<Table> tables = new Vector<Table>();
		Table tmp = null;
		for(int i = 0 ; i<datas.size();i++) {
			if(datas.get(i).toLowerCase().equals("table")) {
				if(tmp != null) {
					tables.add(tmp);
				}
				tmp = new Table(Integer.parseInt(datas.get(++i)),Integer.parseInt(datas.get(++i)));
			}
			else if(datas.get(i).toLowerCase().equals("end")) {
				if(tmp != null) {
					tables.add(tmp);
				}
			}
			else if(tmp != null) {
				tmp.getOrderList().addOrder(new Order(new Product(datas.get(i),Integer.parseInt(datas.get(++i)),
						Integer.parseInt(datas.get(++i)))));
			}	
		}
		return tables;
	}
	public static void saveProductbyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ���� ��ǰ������ �����մϴ�. ");
		saveProducts(Account.getDBbyID(Session.getLogin_ID()).getProductCategories(), "rsrc/" + ID + "/product.dat");
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ���� ��ǰ���� ���� �Ϸ�. ");
	}
	public static void saveProducts(Vector<ProductCategory> productCategorys,String filename) {
		File f = getFile(filename);
		BufferedWriter bw = getWriter(f);
		try {
			for(ProductCategory pc : productCategorys) {
				bw.write("category "+pc.getCategoryName() +";");
				bw.newLine();
				Vector<Product> ps = pc.getProducts();
				for(Product p : ps) {
					bw.write(p + ";");
					bw.newLine();
				}
			}
			bw.write("end;");
			bw.flush();

		}
		catch(IOException e) {
			ConsolePrint.printlnFileMenegerMessage("�����߻�!");
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]�� ������ ���� �Ϸ� ");
	}

	public static void saveBillbyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ���� �Ǹų����� �����մϴ�. ");
		saveBills(Account.getDBbyID(ID).getBills(),"rsrc/" +  ID + "/bill.dat");
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ���� �Ǹų��� ���� �Ϸ�. ");
	}
	public static void saveBills(Vector<Bill> bills,String filename) {
		File f = getFile(filename);	
		BufferedWriter bw = getWriter(f);
		try {
			for(Bill bill : bills) {
				bw.write("bill " + bill.getDateString() +";");
				bw.newLine();
				Vector<Order> orders = bill.getOrders();
				for(Order o : orders) {
					bw.write(o +";");
					bw.newLine();
				}
			}
			bw.write("end;");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]�� ������ ���� �Ϸ� ");


	}
	public static void saveSales(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ���� ���⳻���� �����մϴ�. ");
		File f = getFile("rsrc/" +  ID + "/sales.txt");	
		Database DB = Account.getDBbyID(ID);
		Vector<Bill> bills = DB.getBills();
		BufferedWriter bw = getWriter(f);
		int num = 1;
		try {
			for(Bill bill : bills) {

				bw.write("* �Ǹų��� " + num++ + " *");
				bw.newLine();
				bw.write(" "+bill.getTime());
				bw.newLine();
				bw.write(" ��ǰ ��� :");
				bw.newLine();
				String totalPrice = Bill.intToWonFormat(bill.getTotalPrice());
				for(Order o : bill.getOrders()) {
					String productName = o.getProductName();
					int productCount = o.getProductCount();
					String productPrice = Bill.intToWonFormat(o.getTotalPrice());
					bw.write(" " + productName + "   " + productCount + "   " + productPrice);
					bw.newLine();					
				}
				bw.write(" �ݾ��հ� : " + totalPrice);
				bw.newLine();
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void saveTablebyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ���� ���̺� ������ �����մϴ�. ");
		saveTables(Account.getDBbyID(ID).getTables(),"rsrc/" +  ID + "/table.dat");
		ConsolePrint.printlnFileMenegerMessage(ID + " ȸ����  ���̺� ���� ���� �Ϸ�. ");
	}
	public static void saveTables(Vector<Table> tables,String filename ) {
		File f = getFile(filename);	
		BufferedWriter bw = getWriter(f);
		try {
			for(Table table : tables) {
				bw.write("table "+table +";");
				bw.newLine();
				Vector<Order> orders = table.getOrders();
				for(Order o : orders) {
					bw.write(o +";");
					bw.newLine();
				}
			}
			bw.write("end;");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]�� ������ ���� �Ϸ� ");
	}
	public static void saveAccount() {
		ConsolePrint.printlnFileMenegerMessage("���̵� ����� �����մϴ�.");
		File f = getFile("rsrc/accounts.dat");
		Vector<Account> accounts = Account.getAccounts();
		BufferedWriter bw = getWriter(f);
		try {
			for(Account ac : accounts) {
				bw.write(ac.toString() +";");
				bw.newLine();
				File tmp = new File("rsrc/" + ac.getID());
				tmp.mkdir();
			}
			bw.flush();

		}
		catch(IOException e) {
			ConsolePrint.printlnFileMenegerMessage("�����߻�!");
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]�� ������ ���� �Ϸ� ");
		ConsolePrint.printlnFileMenegerMessage("���̵� ��� ���� �Ϸ�.");

	}
	public static void loadData() {
		ConsolePrint.printlnFileMenegerMessage("������ �ҷ����⸦ �����մϴ�.");
		readAccounts();
		for(Account ac : Account.getAccounts()) {
			readProductbyID(ac.getID());
			readBillbyID(ac.getID());
			readTablebyID(ac.getID());
		}
		ConsolePrint.printlnFileMenegerMessage("������ �ҷ����� �Ϸ�.");
	}
}
