package io.jdev.address

import spock.lang.Specification

/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
class PlainTextAddressFormatterSpec extends Specification {
	PlainTextAddressFormatter formatter
	FullAddress address

	void setup() {
		formatter = new PlainTextAddressFormatter()

		address = new FullAddress()
		address.streetLine1 = 'Unit 2A'
		address.streetLine2 = '1 Foo Place'
		address.city = 'Adelaide'
		address.area = 'sa'
		address.postalCode = '5000'
		address.countryCode = 'AU'
	}


	void "formatter formats address correctly within same country"() {
		expect: 'formats correctly'
			formatter.formatAddress(address, 'AU') == """Unit 2A\n1 Foo Place\nADELAIDE SA 5000"""
	}

	void "formatter formats address correctly for different country"() {
		expect: 'formats correctly'
			formatter.formatAddress(address, 'UK') == """Unit 2A\n1 Foo Place\nADELAIDE SA 5000\nAUSTRALIA"""
	}

	void "formatter formats address with name and org"() {
		given: 'name and org'
			address.name = 'J Ava'
			address.organisation = 'Sun^H^HOracle'

		expect: 'formats correctly'
			formatter.formatAddress(address, 'AU') == """Sun^H^HOracle\nJ Ava\nUnit 2A\n1 Foo Place\nADELAIDE SA 5000"""
	}

	void "formatter overrides address with given name"() {
		given: 'name and org'
		address.name = 'J Ava'
		address.organisation = 'Sun^H^HOracle'

		expect: 'formats correctly'
		formatter.formatAddress(address, 'AU', 'A Person') == """Sun^H^HOracle\nA Person\nUnit 2A\n1 Foo Place\nADELAIDE SA 5000"""
	}

	void "formatter overrides address with given name and org"() {
		given: 'name and org'
			address.name = 'J Ava'
			address.organisation = 'Sun^H^HOracle'

		expect: 'formats correctly'
			formatter.formatAddress(address, 'AU', 'A Person', 'Some Org') == """Some Org\nA Person\nUnit 2A\n1 Foo Place\nADELAIDE SA 5000"""
	}
}
