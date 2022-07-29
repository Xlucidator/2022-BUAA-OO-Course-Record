import com.oocourse.spec2.main.Runner;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Main {
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    public static void main(String[] args) throws Exception {
        long begin = getCpuTime();
        ExceptionCounter.getInstance().initial();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class, MyMessage.class);
        runner.run();
        long end = getCpuTime();
        System.out.println((end - begin) / 1000000000.0);
    }
}
