package io.jdev.address.spring

import io.jdev.address.FullAddress
import org.springframework.validation.Errors
import spock.lang.Specification
/**
 * Copyright Tom Dunstan 2013. All rights reserved.
 */
class AddressValidatorSpec extends Specification {

	AddressValidator validator
	FullAddress address
	Errors errors

	void setup() {
		validator = new AddressValidator()

		errors = Mock(Errors)

		address = new FullAddress()
	}

	void "validator supports validating address"() {
		expect:
			validator.supports(address.getClass())
	}


	void "validates valid australian address correctly"() {
		given:
			address.streetLine1 = 'Unit 2A'
			address.streetLine2 = '1 Foo Place'
			address.city = 'Adelaide'
			address.area = 'sa'
			address.postalCode = '5000'
			address.countryCode = 'AU'

		when:
			validator.validate(address, errors)

		then:
			0 * errors._
	}

	void "validator rejects address with no country code"() {
		expect:
			validator.supports(address.getClass())

		when:
			validator.validate(address, errors)

		then:
			1 * errors.rejectValue('countryCode', 'io.jdev.address.Address.countryCode.required')
			0 * errors._
	}

	void "validates empty australian address correctly"() {
		given:
			def errors = Mock(Errors)
			address.countryCode = 'AU'

		when:
			validator.validate(address, errors)

		then:
			1 * errors.rejectValue('streetLine1', 'io.jdev.address.Address.streetLine1.required')
			1 * errors.rejectValue('city', 'io.jdev.address.Address.city.required')
			1 * errors.rejectValue('postalCode', 'io.jdev.address.Address.postalCode.required')
			1 * errors.rejectValue('area', 'io.jdev.address.Address.area.required')
			0 * errors._
	}
}
