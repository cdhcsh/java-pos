package account;

import java.util.HashMap;
import java.util.Vector;

import database.Database;
import database.Session;
import others.ConsolePrint;

public class Account {
	private static HashMap<String, Account> accounts = new HashMap<String,Account>();
	private String ID;
	private String password;
	private String name;
	private Database DB = new Database();
	public Account(String ID,String password,String name) {
		this.ID = ID;
		this.password = password;
		this.name = name;
		DB = new Database();
		this.ID.replace(" ",""); // 공백제거
		this.password.replace(" ","");
	}
	public static Database getDBbyID(String ID) {
		if(ID ==null) return null;
		return accounts.get(ID).DB;		
	}
	public static void setDBbyID(String ID,Database DB) {
		if(ID ==null) return;
		else {
			accounts.get(ID).DB = DB;
		}
	}
	public static boolean login(String ID,String password) { // 로그인
		if(!redundancyCheckID(ID)) {
			if(accounts.get(ID).password.compareTo(password) == 0) {
				Session.setLogin_ID(ID);
				return true;
			}
			else return false;
		}
		else return false;
	}
	public static boolean memberAccount(Account account) {
		if(redundancyCheckID(account.ID)) {
			accounts.put(account.ID,account);
			return true;
		}
		else return false;
	}
	public static boolean redundancyCheckID(String ID) { // true 중복 x false 중복 o
		return !accounts.containsKey(ID);
	}
	public static String getNamebyID(String ID) {
		return accounts.get(ID).name;
	}
	public static Vector<Account> getAccounts(){
		return new Vector<Account>(accounts.values());
	}
	public String getID(){
		return ID;
	}
	@Override
	public String toString() {
		return ID + " " + password + " " + name;
	}
}
