package io.jdev.address;

import io.jdev.address.info.AddressField;
import io.jdev.address.info.FormatInfo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public class PlainTextAddressFormatter implements AddressFormatter {

	private Map<String,FormatInfo> formatInfoCache = new HashMap<String,FormatInfo>();

	@Override
	public String formatAddress(Address address, String defaultCountryCode) {
		String name = null;
		String org  = null;
		if(address instanceof PostalAddress) {
			PostalAddress pa = (PostalAddress) address;
			name = pa.getName();
			org = pa.getOrganisation();
		}
		return formatAddress(address, defaultCountryCode, name, org);
	}

	@Override
	public String formatAddress(Address address, String defaultCountryCode, String name) {
		String org  = null;
		if(address instanceof PostalAddress) {
			PostalAddress pa = (PostalAddress) address;
			org = pa.getOrganisation();
		}
		return formatAddress(address, defaultCountryCode, name, org);
	}

	@Override
	public String formatAddress(Address address, String defaultCountryCode, String name, String organisation) {
		FormatInfo formatInfo = getFormatInfo(address, defaultCountryCode);
		List<AddressField> fields = formatInfo.getFormatFieldOrder();
		Set<AddressField> upperCaseFields = formatInfo.getUpperCaseFields();
		MessageFormat fmt = new MessageFormat(formatInfo.getFormatMessage());
		Object[] args = new Object[fields.size()];
		for(int i = 0; i < args.length; i++) {
			String val = null;
			AddressField field = fields.get(i);
			switch (field) {
				case NAME:                 val = name;                           break;
				case ORGANISATION:         val = organisation;                   break;
				case STREET_ADDRESS_LINES: val = joinStreetLines(address);       break;
				case CITY:                 val = address.getCity();              break;
				case AREA:                 val = address.getArea();              break;
				case POSTAL_CODE:          val = address.getPostalCode();        break;
				case DEPENDENT_LOCALITY:   val = address.getDependentLocality(); break;
				case SORTING_CODE:         val = address.getSortingCode();       break;
			}
			val = getArg(val);
			if(upperCaseFields.contains(field)) {
				val = val.toUpperCase();
			}
			args[i] = val;
		}
		String result = fmt.format(args);
		result = appendCountryName(result, formatInfo, defaultCountryCode);

		// clear blank lines
		result = result.replaceAll("\\n{2,}", "\n");
		// strip leading and trailing newlines
		result = result.replaceAll("^\\n", "");
		result = result.replaceAll("\\n$", "");

		return result;
	}

	private String appendCountryName(String address, FormatInfo formatInfo, String defaultCountryCode) {
		if(formatInfo.getCountryCode().equals(defaultCountryCode)) {
			return address;
		} else {
			return address + "\n" + formatInfo.getCountryName();
		}
	}

	private String joinStreetLines(Address address) {
		StringBuilder sb = new StringBuilder();
		String val = address.getStreetLine1();
		if(val != null && val.length() > 0) {
			sb.append(val);
		}
		val = address.getStreetLine2();
		if(val != null && val.length() > 0) {
			if(sb.length() > 0) {
				sb.append('\n');
			}
			sb.append(val);
		}
		val = address.getStreetLine3();
		if(val != null && val.length() > 0) {
			if(sb.length() > 0) {
				sb.append('\n');
			}
			sb.append(val);
		}

		return sb.toString();
	}

	private String getArg(String val) {
		return val == null ? "" : val;
	}

	private FormatInfo getFormatInfo(Address address, String defaultCountryCode) {
		String countryCode = address.getCountryCode();
		if(countryCode == null) {
			countryCode = defaultCountryCode;
		}
		countryCode = countryCode.toUpperCase();
		return getFormatInfo(countryCode);
	}

	private FormatInfo getFormatInfo(String countryCode) {
		FormatInfo details = formatInfoCache.get(countryCode);
		if(details == null) {
			details = FormatInfo.load(countryCode);
			formatInfoCache.put(countryCode, details);
		}
		return details;
	}


}
