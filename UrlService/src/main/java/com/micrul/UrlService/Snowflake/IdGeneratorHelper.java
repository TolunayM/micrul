package com.micrul.UrlService.Snowflake;

import java.net.InetAddress;
import java.util.zip.CRC32;


/*
* ALL OF THESE NEEDS TO BE REFACTORED
* GETTING VALUES FROM MOD 32 IS SO MUCH STUPID
* THIS WILL CREATE DUPLICATE VALUES
* CHANGE THIS TO REAL WORKER ID AND DATACENTER ID FROM ZOOKEEPER OR FIND ANOTHER WAY
*
* THIS IS JUST FOR TESTING PURPOSES
*
* */
public class IdGeneratorHelper {

    public static long getDatacenterId() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            CRC32 crc = new CRC32();
            crc.update(hostAddress.getBytes());
            System.out.println(hostAddress + " name datacenter with " +crc.getValue());
            return crc.getValue() % 32; // 0–31
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getWorkerId() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            CRC32 crc = new CRC32();
            crc.update(hostname.getBytes());
            System.out.println(hostname + " named host with " +crc.getValue());
            return crc.getValue() % 32; // 0–31
        } catch (Exception e) {
            return 0;
        }
    }
}
