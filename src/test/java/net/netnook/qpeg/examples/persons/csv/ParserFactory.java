package net.netnook.qpeg.examples.persons.csv;

import java.util.ArrayList;
import java.util.List;

import net.netnook.qpeg.ParserFactoryBase;
import net.netnook.qpeg.examples.persons.Address;
import net.netnook.qpeg.examples.persons.Coordinates;
import net.netnook.qpeg.examples.persons.Person;
import net.netnook.qpeg.examples.persons.Person.Gender;
import net.netnook.qpeg.examples.persons.Persons;
import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;

public class ParserFactory extends ParserFactoryBase {

	@Override
	protected ParsingRuleBuilder getStartRule() {
		return Rules.START;
	}

	public enum Rules implements ParsingRuleBuilder {
		START {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						HeaderLine, //
						zeroOrMore(PersonLine), //
						endOfInput() //
				).onSuccess(context -> {
					int count = context.stackSize();
					List<Person> list = new ArrayList<>(count);

					for (int i = 0; i < count; i++) {
						list.add(context.get(i));
					}

					Persons persons = new Persons();
					persons.setPersons(list);
					context.replaceWith(persons);
				});
			}
		},

		HeaderLine() {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						crlf().invert().zeroOrMore(), //
						crlf() //
				);
			}
		},

		PersonLine {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableString()), // first_name
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableString()), // last_name
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableString()), // email
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(ParserFactory::convertToGender), // gender
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableString()), // street
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableString()), // city
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableString()), // country
						character(','), //
						character(',').invert().zeroOrMore().onSuccess(pushTextAsNullableFloat()), // long
						character(','), //
						crlf().invert().zeroOrMore().onSuccess(pushTextAsNullableFloat()), // lat
						endOfLineOrInput() //
				).onSuccess(context -> {
					String firstName = context.get(0);
					String lastName = context.get(1);
					String email = context.get(2);
					Gender gender = context.get(3);
					String street = context.get(4);
					String city = context.get(5);
					String country = context.get(6);
					Float longitude = context.get(7);
					Float latitude = context.get(8);

					Person person = new Person();
					person.setFirstName(firstName);
					person.setLastName(lastName);
					person.setEmail(email);
					person.setGender(gender);

					if (street != null || city != null || country != null) {
						Address address = new Address();
						person.setAddress(address);
						address.setStreet(street);
						address.setCity(city);
						address.setCountry(country);
					}

					if (longitude != null || latitude != null) {
						Coordinates coordinates = new Coordinates();
						person.setCoordinates(coordinates);
						coordinates.setLongitude(longitude);
						coordinates.setLatitude(latitude);
					}

					context.replaceWith(person);
				});
			}
		}
	}

	private static void convertToGender(Context context) {
		CharSequence text = context.getCharSequence();
		if (text.length() > 0) {
			context.push(Gender.valueOf(text.toString()));
		} else {
			context.push(null);
		}
	}
}
