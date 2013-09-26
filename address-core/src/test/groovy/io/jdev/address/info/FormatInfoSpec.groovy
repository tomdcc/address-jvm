package io.jdev.address.info

import spock.lang.Specification
import static io.jdev.address.info.AddressField.*
import static io.jdev.address.info.AreaNameType.*
import static io.jdev.address.info.PostCodeNameType.*

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
class FormatInfoSpec extends Specification {

	void "australian address info loads correctly"() {
		when: 'load aussie info'
			def info = FormatInfo.load("AU")

		then: 'has correct values'
			info.countryCode == 'AU'
			info.countryName == 'AUSTRALIA'
			info.defaultLanguage == 'en'
			info.areaNameType == STATE
			info.postalCodeNameType == POSTAL
			info.postalCodeRegex == /\d{4}/
			info.examplePostalCodes == '2060,3171,6430,4000,4006,3001'
			info.languages == ['en'] as String[]
			info.requiredFields == [STREET_ADDRESS_LINES, CITY, AREA, POSTAL_CODE] as Set
			info.upperCaseFields == [CITY, AREA] as Set
			info.allowedFields == [NAME, ORGANISATION, STREET_ADDRESS_LINES, CITY, AREA, POSTAL_CODE] as Set
			info.rawFormat == '%O%n%N%n%A%n%C %S %Z'
			info.formatMessage == '{0}\n{1}\n{2}\n{3} {4} {5}'
			info.formatFieldOrder == [ORGANISATION, NAME, STREET_ADDRESS_LINES, CITY, AREA, POSTAL_CODE]

		and: 'has correct state values'
			def states = info.subAreas
			states.length == 8
			states.key == ['ACT', 'NSW', 'NT', 'QLD', 'SA', 'TAS', 'VIC', 'WA']
			states[4].key == 'SA'
			states[4].name == 'South Australia'
			states[4].postalCodePrefixRegex == '5|0872'
			states[4].postalCodeExample == '5000,5999:0872'
	}

}
