package cse.java2.project.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This is a test class.
 */
public class test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println(address);
    }
}
