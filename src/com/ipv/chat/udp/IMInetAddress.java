
package com.ipv.chat.udp;

import java.net.InetAddress;
import java.util.Enumeration;


public class IMInetAddress {

    /**
     * The host address/name
     */
    String address;
    /**
     * The InetAddress
     */
    InetAddress inet_address;

    // ********************* Protected *********************
    /**
     * Creates an IpAddress
     */
    public IMInetAddress(InetAddress iaddress) {
        init(null, iaddress);
    }

    /**
     * Inits the IpAddress
     */
    private void init(String address, InetAddress iaddress) {
        this.address = address;
        this.inet_address = iaddress;
    }

    /**
     * Gets the InetAddress
     */
    InetAddress getInetAddress() {
        if (inet_address == null) {
            try {
                inet_address = InetAddress.getByName(address);
            } catch (java.net.UnknownHostException e) {
            }
        }
        return inet_address;
    }

    // ********************** Public ***********************
    /**
     * Creates an IpAddress
     */
    public IMInetAddress(String address) {
        init(address, null);
    }

    /**
     * Creates an IpAddress
     */
    public IMInetAddress(IMInetAddress ipaddr) {
        init(ipaddr.address, ipaddr.inet_address);
    }

    /**
     * Gets the host address
     */
    /*public String getAddress()
     {  if (address==null) address=inet_address.getHostAddress();
     return address;
     }*/
    /**
     * Makes a copy
     */
    public Object clone() {
        return new IMInetAddress(this);
    }

    /**
     * Wthether it is equal to Object <i>obj</i>
     */
    public boolean equals(Object obj) {
        try {
            IMInetAddress ipaddr = (IMInetAddress) obj;
            if (!toString().equals(ipaddr.toString())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets a String representation of the Object
     */
    public String toString() {
        if (address == null && inet_address != null) {
            address = inet_address.getHostAddress();
        }
        return address;
    }

    // *********************** Static ***********************
    /**
     * Gets the IpAddress for a given fully-qualified host name.
     */
    public static IMInetAddress getByName(String host_addr) throws java.net.UnknownHostException {
        InetAddress iaddr = InetAddress.getByName(host_addr);
        return new IMInetAddress(iaddr);
    }

    /**
     * Detects the default IP address of this host.
     */
    public static IMInetAddress getLocalHostAddress() {  // Note:
        // 1) in this method, java reflection is used in order to keep backward compatibility
        //    with java3 VM (e.g. jdk1.3) at both compiling and/or execution time
        // 2) it can be compiled with java3, regardless it is executed with java3 or java4 VMs
        IMInetAddress ip_address = null;
        try {  // continue only with java4 VM (e.g. jdk1.4) or later, otherwise ClassNotFoundException is thrown
            Class network_interface_class = Class.forName("java.net.NetworkInterface");
            Class inet4_address_class = Class.forName("java.net.Inet4Address");
            //System.out.println("IpAddress: java4 VM (or later) detected");
            // only with java4 VM
            //Enumeration networks=java.net.NetworkInterface.getNetworkInterfaces(); // this line can be compiled only with java4
            Enumeration networks = (Enumeration) network_interface_class.getMethod("getNetworkInterfaces", (Class[]) null).invoke((Class) null, (Class) null);
            while (networks.hasMoreElements()) {  //Enumeration iaddrs=((java.net.NetworkInterface)networks.nextElement()).getInetAddresses(); // this line can be compiled only with java4
                Enumeration iaddrs = (Enumeration) network_interface_class.getMethod("getInetAddresses", (Class[]) null).invoke(networks.nextElement(), (Class) null);
                while (iaddrs.hasMoreElements()) {
                    java.net.InetAddress iaddr = (java.net.InetAddress) iaddrs.nextElement();
                    //if (iaddr.getClass().getName().equals("java.net.Inet4Address") && !iaddr.isLoopbackAddress()) return new IpAddress(iaddr); // this line can be compiled only with java4
                    if (iaddr.getClass().getName().equals("java.net.Inet4Address") && !((Boolean) inet4_address_class.getMethod("isLoopbackAddress", (Class) null).invoke(iaddr, (Class) null)).booleanValue()) {
                        ip_address = new IMInetAddress(iaddr);
                    }
                }
            }
        } catch (java.lang.ClassNotFoundException e) {
        } catch (Exception e) {
        }
        // else
        if (ip_address == null) {
            try {  //System.out.println("IpAddress: java3 VM detected");
                ip_address = new IMInetAddress(InetAddress.getLocalHost());
            } catch (java.net.UnknownHostException e) {
            }
        }
        // else
        if (ip_address == null) {
            ip_address = new IMInetAddress("127.0.0.1");
        }

        //System.out.println("IpAddress: "+ip_address.toString());
        return ip_address;
    }
}
