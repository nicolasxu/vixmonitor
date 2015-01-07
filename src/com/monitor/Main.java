package com.monitor;


import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        JFrame mainFrame = new vixform2();

        ApiHandler handler = new TestHandler();
        ShortStraddleHedge hedge = new ShortStraddleHedge(19, 18, 30, 0, handler);
        ConsoleLogger cLogger = new ConsoleLogger();
        hedge.assignLogger(cLogger);




    }
}


// 1. request data & save to file
// 2. load the file get the data
// 3. feed the data to hedge object