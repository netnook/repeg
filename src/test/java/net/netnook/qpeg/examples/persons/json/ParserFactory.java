package net.netnook.qpeg.examples.persons.json;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import net.netnook.qpeg.ParserFactoryBase;
import net.netnook.qpeg.examples.persons.Address;
import net.netnook.qpeg.examples.persons.Coordinates;
import net.netnook.qpeg.examples.persons.Person;
import net.netnook.qpeg.examples.persons.Person.Gender;
import net.netnook.qpeg.examples.persons.Persons;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;
import net.netnook.qpeg.expressions.chars.CharTesters;
import net.netnook.qpeg.expressions.extras.SkipMatcher;

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
						SkipWhitespace, //
						character('{').ignore(), //
						SkipWhitespace, //
						string("\"persons\"").ignore(), //
						SkipWhitespace, //
						character(':').ignore(), //
						SkipWhitespace, //
						character('[').ignore(), //
						SkipWhitespace, //
						zeroOrMore(PersonObject), //
						SkipWhitespace, //
						character(']').ignore(), //
						SkipWhitespace, //
						character('}').ignore(), //
						SkipWhitespace, //
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

		PersonObject {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						character('{').ignore(), //
						SkipWhitespace, //
						zeroOrMore( //
								choice( //
										stringField("firstName", Person::setFirstName), //
										stringField("lastName", Person::setLastName), //
										stringField("email", Person::setEmail), //
										Gender_Rule, //
										AddressObject, //
										CoordinatesObject //
								) //
						), //
						character('}').ignore(), //
						SkipWhitespace, //
						optional(character(',').ignore()), //
						SkipWhitespace //
				).onSuccess(context -> { //
					Person person = new Person();
					int count = context.stackSize();
					for (int i = 0; i < count; i += 2) {
						String prop = context.get(i);
						Object value = context.get(i + 1);
						if ("firstName".equals(prop)) {
							person.setFirstName((String) value);
						} else if ("lastName".equals(prop)) {
							person.setLastName((String) value);
						} else if ("email".equals(prop)) {
							person.setEmail((String) value);
						} else if ("gender".equals(prop)) {
							person.setGender((Gender) value);
						} else if ("address".equals(prop)) {
							person.setAddress((Address) value);
						} else if ("coordinates".equals(prop)) {
							person.setCoordinates((Coordinates) value);
						} else {
							throw new RuntimeException("Expected address field");
						}
					}
					context.replaceWith(person);
				});
			}
		},

		Gender_Rule {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						string("\"gender\"").onSuccess(push("gender")), //
						SkipWhitespace, //
						character(':').ignore(), //
						SkipWhitespace, //
						character('"').ignore(), //
						character('"').invert() //
								.zeroOrMore() //
								.onSuccess(context -> {
									context.push(Gender.valueOf(context.getCharSequence().toString()));
								}), //
						character('"').ignore(), //
						SkipWhitespace, //
						optional(character(',').ignore()), //
						SkipWhitespace //
				);
			}
		},

		AddressObject {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						string("\"address\"").ignore(), //
						SkipWhitespace, //
						character(':').ignore(), //
						SkipWhitespace, //
						character('{').ignore(), //
						SkipWhitespace, //
						zeroOrMore( //
								choice( //
										stringField("street", Address::setStreet), //
										stringField("city", Address::setCity), //
										stringField("country", Address::setCountry) //
								) //
						), //
						character('}').ignore(), //
						SkipWhitespace, //
						optional(character(',').ignore()), //
						SkipWhitespace //
				).onSuccess(context -> { //
					Address address = new Address();
					int count = context.stackSize();
					for (int i = 0; i < count; i += 2) {
						String prop = context.get(i);
						String value = context.get(i + 1);
						if ("street".equals(prop)) {
							address.setStreet(value);
						} else if ("city".equals(prop)) {
							address.setCity(value);
						} else if ("country".equals(prop)) {
							address.setCountry(value);
						} else {
							throw new RuntimeException("Expected street, city or country");
						}
					}
					context.replaceWith("address");
					context.push(address);
				});
			}
		},

		CoordinatesObject {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						string("\"coordinates\"").ignore(), //
						SkipWhitespace, //
						character(':').ignore(), //
						SkipWhitespace, //
						character('{').ignore(), //
						SkipWhitespace, //
						zeroOrMore( //
								choice( //
										Longitude, //
										Latitude //
								) //
						), //
						character('}').ignore(), //
						SkipWhitespace, //
						optional(character(',').ignore()), //
						SkipWhitespace //
				).onSuccess(context -> { //
					Coordinates coordinates = new Coordinates();
					int count = context.stackSize();
					for (int i = 0; i < count; i += 2) {
						String prop = context.get(i);
						Float value = context.get(i + 1);
						if ("longitude".equals(prop)) {
							coordinates.setLongitude(value);
						} else if ("latitude".equals(prop)) {
							coordinates.setLatitude(value);
						} else {
							throw new RuntimeException("Expected longitude or latitude");
						}
					}
					context.replaceWith("coordinates");
					context.push(coordinates);
				});
			}
		},

		Longitude {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						string("\"longitude\"").onSuccess(push("longitude")), //
						SkipWhitespace, //
						character(':').ignore(), //
						SkipWhitespace, //
						parseFloat(), //
						SkipWhitespace, //
						optional(character(',').ignore()), //
						SkipWhitespace //
				);
			}
		},

		Latitude {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						string("\"latitude\"").onSuccess(push("latitude")), //
						SkipWhitespace, //
						character(':').ignore(), //
						SkipWhitespace, //
						parseFloat(), //
						SkipWhitespace, //
						optional(character(',').ignore()), //
						SkipWhitespace //
				);
			}
		}, //
	}

	private static <O> ParsingExpressionBuilder stringField(String name, BiConsumer<O, String> setter) {
		return sequence( //
				string("\"" + name + "\"").onSuccess(push(name)), //
				SkipWhitespace, //
				character(':').ignore(), //
				SkipWhitespace, //
				character('"').ignore(), //
				character('"').invert().zeroOrMore().onSuccess(pushText()), //
				character('"').ignore(), //
				SkipWhitespace, //
				optional(character(',').ignore()), //
				SkipWhitespace //
		);
	}

	//private static final ParsingExpressionBuilder SkipWhitespace = whitespace().zeroOrMore().ignore();
	private static final ParsingExpressionBuilder SkipWhitespace = SkipMatcher.using(CharTesters.isWhitespace());
}
