package io.jdev.address;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public interface Address {

	String getStreetLine1();
	String getStreetLine2();
	String getStreetLine3();
	String getDependentLocality();
	String getCity();
	String getArea();
	String getPostalCode();
	String getSortingCode();
	String getCountryCode();
}
