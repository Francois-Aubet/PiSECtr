package com.aubet.francois.pisectr;

/**
 * Created by root on 31.10.17.
 */

public class State {
	public static boolean connectedPi = false;
    public static boolean connectedWifi = false;
    public static boolean connectedWeb = false;
    public static boolean connectedIPSEC = false;
	public static String wifiSSID = "";
	public static String wifiPass = "";


	public State(){
		connectedPi = false;
		connectedWifi = false;
		connectedWeb = false;
		connectedIPSEC = false;
	}


	public static void connectionLost(){
		connectedPi = false;
		connectedWifi = false;
		connectedWeb = false;
		connectedIPSEC = false;
	}

}
