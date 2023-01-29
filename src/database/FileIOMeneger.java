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
	public static final Pattern STANDARD_PATTERN = Pattern.compile("^[a-z|A-Z|0-9|°¡-ÆR]*$");
	public static Vector<String> read(String fileName){
		ConsolePrint.printlnFileMenegerMessage("[ " + fileName + " ]ÀÇ µ¥ÀÌÅÍ¸¦ ºÒ·¯¿É´Ï´Ù .");
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
			ConsolePrint.printlnFileMenegerMessage("¿À·ù¹ß»ý!");
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + fileName + " ]ÀÇ µ¥ÀÌÅÍ¸¦ ºÒ·¯¿À±â ¿Ï·á.");
		return datas;
	}
	public static File getFile(String filename) {
		File f = new File(filename);
		try {
			if(!f.isFile()) {
				f.createNewFile();
				ConsolePrint.printlnFileMenegerMessage("ÆÄÀÏÀÌ Á¸ÀçÇÏÁö¾Ê¾Æ »õ·Î »ý¼ºÇÕ´Ï´Ù.");
			}
			return f;
		}
		catch(IOException e) {
			ConsolePrint.printlnFileMenegerMessage("¿À·ù¹ß»ý!");	
			return null;
		}
	}
	public static BufferedWriter getWriter(File f) {
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]¿¡ µ¥ÀÌÅÍ¸¦ ÀúÀåÇÕ´Ï´Ù ");
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			return bw;
		}
		catch(IOException e) {
			ConsolePrint.printlnFileMenegerMessage("¿À·ù¹ß»ý!");
			return null;
		}
	}
	public static void readAccounts() {
		ConsolePrint.printlnFileMenegerMessage("¾ÆÀÌµð ¸ñ·ÏÀ» ºÒ·¯¿É´Ï´Ù.");
		Vector<String> datas = read("rsrc/accounts.dat");
		for(int i = 0 ; i < datas.size()/3 ; i++) {
			Account.memberAccount(new Account(datas.get(i*3),datas.get(i*3+1),datas.get(i*3+2)));
			File tmp = new File("rsrc/" + datas.get(i*3));
			tmp.mkdir();
		}
		ConsolePrint.printlnFileMenegerMessage("¾ÆÀÌµð ¸ñ·Ï ºÒ·¯¿À±â ¿Ï·á.");

	}
	public static void readProductbyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + "È¸¿øÀÇ »óÇ°Á¤º¸¸¦ ºÒ·¯¿É´Ï´Ù. ");	
		Account.getDBbyID(ID).setProductCategories(readProducts("rsrc/"+ID+"/product.dat"));
		ConsolePrint.printlnFileMenegerMessage(ID + "È¸¿øÀÇ »óÇ°Á¤º¸ ºÒ·¯¿À±â ¿Ï·á.");
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
		ConsolePrint.printlnFileMenegerMessage(ID + "È¸¿øÀÇ ¸ÅÃâ³»¿ªÀ» ºÒ·¯¿É´Ï´Ù. ");
		Account.getDBbyID(ID).setBills(readBills("rsrc/"+ID+"/bill.dat"));
		ConsolePrint.printlnFileMenegerMessage(ID + "È¸¿øÀÇ ¸ÅÃâ³»¿ª ºÒ·¯¿À±â ¿Ï·á.");
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
		ConsolePrint.printlnFileMenegerMessage(ID + "È¸¿øÀÇ Å×ÀÌºí Á¤º¸¸¦ ºÒ·¯¿É´Ï´Ù. ");
		Vector<Table> tables = readTables("rsrc/"+ID+"/table.dat");
		if(!tables.isEmpty()) {
			Account.getDBbyID(ID).setTables(tables);
			ConsolePrint.printlnFileMenegerMessage(ID + "È¸¿øÀÇ Å×ÀÌºí Á¤º¸ ºÒ·¯¿À±â ¿Ï·á.");
		}
		else {
			ConsolePrint.printlnFileMenegerMessage("Å×ÀÌºí Á¤º¸°¡ ¾ø¾î ±âº» °ªÀ¸·Î ¼³Á¤ÇÕ´Ï´Ù.");
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
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ »óÇ°Á¤º¸¸¦ ÀúÀåÇÕ´Ï´Ù. ");
		saveProducts(Account.getDBbyID(Session.getLogin_ID()).getProductCategories(), "rsrc/" + ID + "/product.dat");
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ »óÇ°Á¤º¸ ÀúÀå ¿Ï·á. ");
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
			ConsolePrint.printlnFileMenegerMessage("¿À·ù¹ß»ý!");
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]¿¡ µ¥ÀÌÅÍ ÀúÀå ¿Ï·á ");
	}

	public static void saveBillbyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ ÆÇ¸Å³»¿ªÀ» ÀúÀåÇÕ´Ï´Ù. ");
		saveBills(Account.getDBbyID(ID).getBills(),"rsrc/" +  ID + "/bill.dat");
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ ÆÇ¸Å³»¿ª ÀúÀå ¿Ï·á. ");
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
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]¿¡ µ¥ÀÌÅÍ ÀúÀå ¿Ï·á ");


	}
	public static void saveSales(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ ¸ÅÃâ³»¿ªÀ» ÀúÀåÇÕ´Ï´Ù. ");
		File f = getFile("rsrc/" +  ID + "/sales.txt");	
		Database DB = Account.getDBbyID(ID);
		Vector<Bill> bills = DB.getBills();
		BufferedWriter bw = getWriter(f);
		int num = 1;
		try {
			for(Bill bill : bills) {

				bw.write("* ÆÇ¸Å³»¿ª " + num++ + " *");
				bw.newLine();
				bw.write(" "+bill.getTime());
				bw.newLine();
				bw.write(" »óÇ° ¸ñ·Ï :");
				bw.newLine();
				String totalPrice = Bill.intToWonFormat(bill.getTotalPrice());
				for(Order o : bill.getOrders()) {
					String productName = o.getProductName();
					int productCount = o.getProductCount();
					String productPrice = Bill.intToWonFormat(o.getTotalPrice());
					bw.write(" " + productName + "   " + productCount + "   " + productPrice);
					bw.newLine();					
				}
				bw.write(" ±Ý¾×ÇÕ°è : " + totalPrice);
				bw.newLine();
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void saveTablebyID(String ID) {
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ Å×ÀÌºí Á¤º¸¸¦ ÀúÀåÇÕ´Ï´Ù. ");
		saveTables(Account.getDBbyID(ID).getTables(),"rsrc/" +  ID + "/table.dat");
		ConsolePrint.printlnFileMenegerMessage(ID + " È¸¿øÀÇ  Å×ÀÌºí Á¤º¸ ÀúÀå ¿Ï·á. ");
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
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]¿¡ µ¥ÀÌÅÍ ÀúÀå ¿Ï·á ");
	}
	public static void saveAccount() {
		ConsolePrint.printlnFileMenegerMessage("¾ÆÀÌµð ¸ñ·ÏÀ» ÀúÀåÇÕ´Ï´Ù.");
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
			ConsolePrint.printlnFileMenegerMessage("¿À·ù¹ß»ý!");
		}
		ConsolePrint.printlnFileMenegerMessage("[ " + f.toString() + " ]¿¡ µ¥ÀÌÅÍ ÀúÀå ¿Ï·á ");
		ConsolePrint.printlnFileMenegerMessage("¾ÆÀÌµð ¸ñ·Ï ÀúÀå ¿Ï·á.");

	}
	public static void loadData() {
		ConsolePrint.printlnFileMenegerMessage("µ¥ÀÌÅÍ ºÒ·¯¿À±â¸¦ ½ÃÀÛÇÕ´Ï´Ù.");
		readAccounts();
		for(Account ac : Account.getAccounts()) {
			readProductbyID(ac.getID());
			readBillbyID(ac.getID());
			readTablebyID(ac.getID());
		}
		ConsolePrint.printlnFileMenegerMessage("µ¥ÀÌÅÍ ºÒ·¯¿À±â ¿Ï·á.");
	}
}
