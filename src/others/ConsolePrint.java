package others;

public class ConsolePrint {
	public static String mainFrameMessage = " * Frame : ";
	public static String loginMessage = " * Login : ";
	public static String sessionMessage = " * Session : ";
	public static String fileMenegerMessage = " * FileMeneger : ";
	
	public static void printlnMainFrameMessage(String msg) {
		System.out.println(mainFrameMessage + msg);
	}
	public static void printlnLginMessage(String msg) {
		System.out.println(loginMessage + msg);
	}
	public static void printlnSessionMessage(String msg) {
		System.out.println(sessionMessage + msg);
	}
	public static void printlnFileMenegerMessage(String msg) {
		System.out.println(fileMenegerMessage + msg);
	}
}
