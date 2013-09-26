package io.jdev.address.info;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public enum AddressField {

	/** Addressee name, e.g. M Burns */
	NAME('N'),

	/** Organisation name, e.g. FooCorp */
	ORGANISATION('O'),

	/** Street address lines, may be multiple of these */
	STREET_ADDRESS_LINES('A'),

	/** Dependent locality (may be an inner-city district or a suburb) */
	DEPENDENT_LOCALITY('D'),

	/** City or Locality */
	CITY('C'),

	/** Administrative area such as a state, province, island etc */
	AREA('S'),

	/** Zip or postal code */
	POSTAL_CODE('Z'),

	/** Sorting code */
	SORTING_CODE('X');

	private final static Map<Character,AddressField> FIELD_CHARS = new HashMap<Character, AddressField>();

	static {
		for(AddressField field : AddressField.values()) {
			FIELD_CHARS.put(field.formatCharacter, field);
		}
	}

	private final char formatCharacter;

	private AddressField(char formatCharacter) {
		this.formatCharacter = formatCharacter;
	}

	public static AddressField getByFormatCharacter(char c) {
		return FIELD_CHARS.get(c);
	}

	public char getFormatCharacter() {
		return this.formatCharacter;
	}
}
