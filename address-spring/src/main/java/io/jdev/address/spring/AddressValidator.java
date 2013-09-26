package io.jdev.address.spring;

import static io.jdev.address.info.AddressField.*;

import io.jdev.address.Address;
import io.jdev.address.info.AddressField;
import io.jdev.address.info.FormatInfo;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public class AddressValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Address.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Address address = (Address) target;
		String countryCode = address.getCountryCode();
		if(countryCode == null || countryCode.length() == 0) {
			rejectField(errors, "countryCode", "required");
		} else {
			FormatInfo info = FormatInfo.load(countryCode);
			Set<AddressField> requiredFields = info.getRequiredFields();

			checkAddressLines(requiredFields, errors, address);
			checkFieldRequired(requiredFields, errors, CITY,               "city",              address.getCity());
			checkFieldRequired(requiredFields, errors, POSTAL_CODE,        "postalCode",        address.getPostalCode());
			checkFieldRequired(requiredFields, errors, AREA,               "area",              address.getArea());
			checkFieldRequired(requiredFields, errors, DEPENDENT_LOCALITY, "dependentLocality", address.getDependentLocality());
			checkFieldRequired(requiredFields, errors, SORTING_CODE,       "sortingCode",       address.getSortingCode());
		}
	}

	private void checkAddressLines(Set<AddressField> requiredFields, Errors errors, Address address) {
		if(requiredFields.contains(AddressField.STREET_ADDRESS_LINES)) {
			if(!(hasVal(address.getStreetLine1()) || hasVal(address.getStreetLine2()) || hasVal(address.getStreetLine3()))) {
				rejectField(errors, "streetLine1", "required");
			}
		}
	}

	private boolean hasVal(String val) {
		return val != null && val.length() > 0;
	}

	private void rejectField(Errors errors, String fieldName, String error) {
		errors.rejectValue(fieldName, "io.jdev.address.Address." + fieldName + "." + error);
	}

	private void checkFieldRequired(Set<AddressField> requiredFields, Errors errors, AddressField field, String fieldName, String val) {
		if(requiredFields.contains(field) && !hasVal(val)) {
			rejectField(errors, fieldName, "required");
		}
	}
}
