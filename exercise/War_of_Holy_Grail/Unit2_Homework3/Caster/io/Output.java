package io;

import com.oocourse.TimableOutput;

public class Output {
    
    private static final Output OUTPUT = new Output();
    
    private Output(){}
    
    public static Output getInstance() {
        return OUTPUT;
    }
    
    public synchronized void println(String msg) {
        TimableOutput.println(msg);
    }
    
}