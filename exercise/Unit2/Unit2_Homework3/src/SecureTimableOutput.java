import com.oocourse.TimableOutput;

public class SecureTimableOutput {
    public static synchronized void println(String str) {
        TimableOutput.println(str);
    }
}
