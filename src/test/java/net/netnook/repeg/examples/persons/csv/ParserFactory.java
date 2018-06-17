package net.netnook.repeg.examples.persons.csv;

import java.util.ArrayList;
import java.util.List;

import net.netnook.repeg.Context;
import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.ParsingExpressionBuilder;
import net.netnook.repeg.RuleEnum;
import net.netnook.repeg.examples.persons.Address;
import net.netnook.repeg.examples.persons.Coordinates;
import net.netnook.repeg.examples.persons.Person;
import net.netnook.repeg.examples.persons.Person.Gender;
import net.netnook.repeg.examples.persons.Persons;

public class ParserFactory extends ParserFactoryBase<Persons> {

	@Override
	protected RuleEnum getStartRule() {
		return Rules.START;
	}

	public enum Rules implements RuleEnum {
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

		HeaderLine {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						zeroOrMore(crlf().not()), //
						one(crlf()) //
				);
			}
		},

		PersonLine {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableString()), // first_name
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableString()), // last_name
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableString()), // email
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(ParserFactory::convertToGender), // gender
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableString()), // street
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableString()), // city
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableString()), // country
						one(','), //
						zeroOrMore(character(',').not()).onSuccess(pushTextAsNullableFloat()), // long
						one(','), //
						zeroOrMore(crlf().not()).onSuccess(pushTextAsNullableFloat()), // lat
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
						coordinates.setLongitude(longitude == null ? null : longitude.floatValue());
						coordinates.setLatitude(latitude == null ? null : latitude.floatValue());
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
