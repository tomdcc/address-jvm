package io.jdev.address;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public interface AddressFormatter {
	String formatAddress(Address address, String defaultCountryCode);
	String formatAddress(Address address, String defaultCountryCode, String name);
	String formatAddress(Address address, String defaultCountryCode, String name, String organisation);
}
