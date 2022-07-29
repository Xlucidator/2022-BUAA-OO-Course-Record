import com.oocourse.TimableOutput;

public class Output {
    public static synchronized void output(String str) {
        TimableOutput.println(str);
    }
}

