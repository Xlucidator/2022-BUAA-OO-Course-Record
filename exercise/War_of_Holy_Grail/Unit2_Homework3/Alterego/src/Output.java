import com.oocourse.TimableOutput;

public class Output {
    public static synchronized void println(String s) {
        TimableOutput.println(s);
    }
}
