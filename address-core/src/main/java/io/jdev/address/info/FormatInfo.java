package io.jdev.address.info;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
public class FormatInfo {
	private String countryName;
	private String countryCode;
	private String postCodeRegex;
	private String defaultLanguage;
	private String[] languages;
	private String rawFormat;
	private String formatMessage;
	private String latinFormat;
	private SubAreaFormatInfo[] subAreas;
	private PostCodeNameType postalCodeNameType;
	private AreaNameType areaNameType;
	private Set<AddressField> requiredFields;
	private Set<AddressField> allowedFields;
	private List<AddressField> formatFieldOrder;
	private String postalCodeRegex;
	private String examplePostalCodes;

	private Set<AddressField> upperCaseFields;

	private static final String DEFAULT_PROPS_CODE = "ZZ";
	public static final char FORMAT_ESCAPE_MARKER = '%';

	public static FormatInfo load(String countryCode) {
		Properties props = loadProperties(countryCode);
		Properties defaultProps = new Properties(); // lazy load
		FormatInfo info = new FormatInfo();
		info.setCountryCode(countryCode);
		info.setCountryName(getProperty(props, defaultProps, "name"));
		info.setPostalCodeRegex(getProperty(props, defaultProps, "zip"));
		info.setExamplePostalCodes(getProperty(props, defaultProps, "zipex"));
		info.setDefaultLanguage(getProperty(props, defaultProps, "lang"));
		info.setLanguages(split(getProperty(props, defaultProps, "languages")));
		info.setRequiredFields(decodeFieldSet(getProperty(props, defaultProps, "require")));
		info.setUpperCaseFields(decodeFieldSet(getProperty(props, defaultProps, "upper")));
		parseFormat(info, getProperty(props, defaultProps, "fmt"));
		info.setPostalCodeNameType(Enum.valueOf(PostCodeNameType.class, getProperty(props, defaultProps, "zip_name_type").toUpperCase()));
		info.setAreaNameType(Enum.valueOf(AreaNameType.class, getProperty(props, defaultProps, "state_name_type").toUpperCase()));

		parseSubAreas(info, props, defaultProps);
		return info;
	}

	private static void parseSubAreas(FormatInfo info, Properties props, Properties defaultProps) {
		String[] keys = split(getProperty(props, defaultProps, "sub_keys"));
		if(keys.length == 0) {
			// nothing more to do
			return;
		}

		SubAreaFormatInfo[] areas = new SubAreaFormatInfo[keys.length];
		String[] postalCodePrefixRegexes = split(getProperty(props, defaultProps, "sub_zips"));
		String[] postalCodeExamples = split(getProperty(props, defaultProps, "sub_zipexs"));
		String[] names = split(getProperty(props, defaultProps, "sub_names"));
		for(int i = 0; i < keys.length; i++) {
			SubAreaFormatInfo areaInfo = new SubAreaFormatInfo();
			areaInfo.setKey(keys[i]);
			if(!(names.length <= i)) {
				areaInfo.setName(names[i]);
			}
			if(!(postalCodePrefixRegexes.length <= i)) {
				areaInfo.setPostalCodePrefixRegex(postalCodePrefixRegexes[i]);
			}
			if(!(postalCodeExamples.length <= i)) {
				areaInfo.setPostalCodeExample(postalCodeExamples[i]);
			}

			areas[i] = areaInfo;
		}
		info.setSubAreas(areas);
	}

	private static void parseFormat(FormatInfo info, String format) {
		List<AddressField> fields = new ArrayList<AddressField>();
		StringBuilder formatMessageBuilder = new StringBuilder();
		boolean afterMarker = false;
		int formatParamterIndex = 0;
		for(char c : format.toCharArray()) {
			if(afterMarker) {
				switch(c) {
					case 'n':
						formatMessageBuilder.append('\n');
						break;
					case '%':
						formatMessageBuilder.append('%');
					default:
						AddressField field = AddressField.getByFormatCharacter(c);
						if(field != null) {
							fields.add(field);
							formatMessageBuilder.append('{');
							formatMessageBuilder.append(formatParamterIndex++);
							formatMessageBuilder.append('}');
						}
				}
				afterMarker = false;
			} else if(c == FORMAT_ESCAPE_MARKER) {
				afterMarker = true;
			} else {
				formatMessageBuilder.append(c);
			}
		}
		info.setRawFormat(format);
		info.setFormatMessage(formatMessageBuilder.toString());
		info.setFormatFieldOrder(fields);
		info.setAllowedFields(EnumSet.copyOf(fields));
	}

	private static Set<AddressField> decodeFieldSet(String value) {
		EnumSet<AddressField> fields = EnumSet.noneOf(AddressField.class);
		for(char c : value.toCharArray()) {
			AddressField field = AddressField.getByFormatCharacter(c);
			if(field != null) {
				fields.add(field);
			}
		}
		return fields;
	}

	private static String[] split(String value) {
		if(value == null || value.length() == 0) {
			return new String[0];
		} else {
			return value.split("~");
		}
	}

	private static String getProperty(Properties countryProps, Properties defaultProps, String key) {
		String value = countryProps.getProperty(key);
		if(value == null) {
			if(defaultProps.isEmpty()) {
				// load lazily, may not be needed
				loadProperties(defaultProps, DEFAULT_PROPS_CODE);
			}
			value = defaultProps.getProperty(key);
		}
		return value;
	}

	private static Properties loadProperties(String countryCode) {
		Properties props = new Properties();
		loadProperties(props, countryCode);
		return props;
	}

	private static void loadProperties(Properties props, String countryCode) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream = cl.getResourceAsStream("io/jdev/address/rules/" + countryCode + "/index.properties");
		if(stream == null) {
			// just go with defaults
			return;
		}
		try {
			props.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				// whatever
			}
		}
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPostCodeRegex() {
		return postCodeRegex;
	}

	public void setPostCodeRegex(String postCodeRegex) {
		this.postCodeRegex = postCodeRegex;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}

	public String getRawFormat() {
		return rawFormat;
	}

	public void setRawFormat(String rawFormat) {
		this.rawFormat = rawFormat;
	}

	public String getLatinFormat() {
		return latinFormat;
	}

	public void setLatinFormat(String latinFormat) {
		this.latinFormat = latinFormat;
	}

	public SubAreaFormatInfo[] getSubAreas() {
		return subAreas;
	}

	public void setSubAreas(SubAreaFormatInfo[] subAreas) {
		this.subAreas = subAreas;
	}

	public PostCodeNameType getPostalCodeNameType() {
		return postalCodeNameType;
	}

	public void setPostalCodeNameType(PostCodeNameType postalCodeNameType) {
		this.postalCodeNameType = postalCodeNameType;
	}

	public AreaNameType getAreaNameType() {
		return areaNameType;
	}

	public void setAreaNameType(AreaNameType areaNameType) {
		this.areaNameType = areaNameType;
	}

	public Set<AddressField> getRequiredFields() {
		return requiredFields;
	}

	public void setRequiredFields(Set<AddressField> requiredFields) {
		this.requiredFields = requiredFields;
	}

	public Set<AddressField> getAllowedFields() {
		return allowedFields;
	}

	public void setAllowedFields(Set<AddressField> allowedFields) {
		this.allowedFields = allowedFields;
	}

	public Set<AddressField> getUpperCaseFields() {
		return upperCaseFields;
	}

	public void setUpperCaseFields(Set<AddressField> upperCaseFields) {
		this.upperCaseFields = upperCaseFields;
	}

	public String getFormatMessage() {
		return formatMessage;
	}

	public void setFormatMessage(String formatMessage) {
		this.formatMessage = formatMessage;
	}

	public List<AddressField> getFormatFieldOrder() {
		return formatFieldOrder;
	}

	public void setFormatFieldOrder(List<AddressField> formatFieldOrder) {
		this.formatFieldOrder = formatFieldOrder;
	}

	public String getPostalCodeRegex() {
		return postalCodeRegex;
	}

	public void setPostalCodeRegex(String postalCodeRegex) {
		this.postalCodeRegex = postalCodeRegex;
	}

	public String getExamplePostalCodes() {
		return examplePostalCodes;
	}

	public void setExamplePostalCodes(String examplePostalCodes) {
		this.examplePostalCodes = examplePostalCodes;
	}
}
