package io.jdev.address;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public class FullAddress extends SimpleAddress implements PostalAddress {

	private String name;
	private String organisation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

}
