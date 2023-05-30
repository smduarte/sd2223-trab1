package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {

    public static String hostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "?.?.?.?";
        }
    }

    public static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown hostname";
        }

    }
}