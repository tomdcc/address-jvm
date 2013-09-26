package io.jdev.address.info;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public class SubAreaFormatInfo {
	private String key;
	private String name;
	private String postalCodePrefixRegex;
	private String postalCodeExample;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostalCodePrefixRegex() {
		return postalCodePrefixRegex;
	}

	public void setPostalCodePrefixRegex(String postalCodePrefixRegex) {
		this.postalCodePrefixRegex = postalCodePrefixRegex;
	}

	public String getPostalCodeExample() {
		return postalCodeExample;
	}

	public void setPostalCodeExample(String postalCodeExample) {
		this.postalCodeExample = postalCodeExample;
	}
}
