
public class MyUtil {
	// Solo es para que sea un poco mas ergonomico hacer el Thread.sleep
	// Deberia ser inlines o incluso un macro
	public static void ezSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
