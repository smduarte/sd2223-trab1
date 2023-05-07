package utils;

public class Sleep {
	
	public static void ms(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	public static void seconds(int s) {
		ms(s * 1000);
	}

}