package net.floodlightcontroller.dhcp;

import java.util.Arrays;

import org.projectfloodlight.openflow.types.MacAddress;

import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.Ethernet;


import java.lang.String;

/**
 * The class representing a DHCP Binding -- Mac and IP.
 * It also contains important information regarding the lease status
 * --active
 * --inactive
 * the lease type of the binding
 * --dynamic
 * --fixed/static
 * and the lease times
 * --start time in seconds
 * --duration in seconds
 * 
 * @author Ryan Izard (rizard@g.clemson.edu)
 */
public class DHCPBinding {
	
	public static final int IP_ADDRESS_LENGTH = 4;
	public static final int Mac_ADDRESS_LENGTH = (int) Ethernet.DATALAYER_ADDRESS_LENGTH;
	
	private byte[] Mac = new byte[Mac_ADDRESS_LENGTH];
	private byte[] IP = new byte[IP_ADDRESS_LENGTH];
	private boolean LEASE_STATUS;
	private boolean PERMANENT_LEASE;
	
	private long LEASE_START_TIME_SECONDS;
	private long LEASE_DURATION_SECONDS;
	
	protected DHCPBinding(byte[] ip, byte[] mac) {
		this.setMacAddress(mac);
		this.setIPv4Addresss(ip);
		this.setLeaseStatus(false);
	}
	
	public byte[] getIPv4AddressBytes() {
		return IP;
	}
	
	public String getIPv4AddresString() {
		return IPv4.fromIPv4Address(IPv4.toIPv4Address(IP));
	}
	
	public byte[] getMacAddressBytes() {
		return Mac;
	}
	
	public String getMacAddressString() {
		return MacAddress.of(new String(Mac)).toString();
	}
	
	private void setIPv4Addresss(byte[] ip) {
		IP = Arrays.copyOf(ip, IP_ADDRESS_LENGTH); 
	}
	
	public void setMacAddress(byte[] mac) {
		Mac = Arrays.copyOf(mac, Mac_ADDRESS_LENGTH);
	}
	
	public void setMacAddress(String mac) {
		Mac = Ethernet.toMACAddress(mac);
	}
	
	public boolean isActiveLease() {
		return LEASE_STATUS;
	}
	
	public void setStaticIPLease(boolean staticIP) {
		PERMANENT_LEASE = staticIP;
	}
	
	public boolean isStaticIPLease() {
		return PERMANENT_LEASE;
	}
	
	public void setLeaseStatus(boolean status) {
		LEASE_STATUS = status;
	}
	
	public boolean isLeaseExpired() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime / 1000) >= (LEASE_START_TIME_SECONDS + LEASE_DURATION_SECONDS)) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void setLeaseStartTimeSeconds() {
		LEASE_START_TIME_SECONDS = System.currentTimeMillis() / 1000;
	}
	
	protected void setLeaseDurationSeconds(long time) {
		LEASE_DURATION_SECONDS = time;
	}
	
	protected void clearLeaseTimes() {
		LEASE_START_TIME_SECONDS = 0;
		LEASE_DURATION_SECONDS = 0;
	}
	
	protected boolean cancelLease() {
		this.clearLeaseTimes();
		this.setLeaseStatus(false);
		return true;
	}
}
