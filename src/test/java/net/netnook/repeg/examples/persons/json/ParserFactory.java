package net.netnook.repeg.examples.persons.json;

import java.util.ArrayList;
import java.util.List;

import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.ParsingExpressionBuilder;
import net.netnook.repeg.RuleEnum;
import net.netnook.repeg.chars.CharMatchers;
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
						SkipWhitespace, //
						one('{'), //
						SkipWhitespace, //
						string("\"persons\""), //
						SkipWhitespace, //
						one(':'), //
						SkipWhitespace, //
						one('['), //
						SkipWhitespace, //
						zeroOrMore(PersonObject), //
						SkipWhitespace, //
						one(']'), //
						SkipWhitespace, //
						one('}'), //
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
						one('{'), //
						SkipWhitespace, //
						zeroOrMore( //
								choice( //
										stringField("firstName"), //
										stringField("lastName"), //
										stringField("email"), //
										Gender_Rule, //
										AddressObject, //
										CoordinatesObject //
								) //
						), //
						one('}'), //
						SkipWhitespace, //
						optional(','), //
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
						one(':'), //
						SkipWhitespace, //
						one('"'), //
						zeroOrMore(character('"').not()).onSuccess(context -> {
							context.push(Gender.valueOf(context.getCharSequence().toString()));
						}), //
						one('"'), //
						SkipWhitespace, //
						optional(','), //
						SkipWhitespace //
				);
			}
		},

		AddressObject {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						string("\"address\""), //
						SkipWhitespace, //
						one(':'), //
						SkipWhitespace, //
						one('{'), //
						SkipWhitespace, //
						zeroOrMore( //
								choice( //
										stringField("street"), //
										stringField("city"), //
										stringField("country") //
								) //
						), //
						one('}'), //
						SkipWhitespace, //
						optional(','), //
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
						string("\"coordinates\""), //
						SkipWhitespace, //
						one(':'), //
						SkipWhitespace, //
						one('{'), //
						SkipWhitespace, //
						zeroOrMore( //
								choice( //
										Longitude, //
										Latitude //
								) //
						), //
						one('}'), //
						SkipWhitespace, //
						optional(','), //
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
						one(':'), //
						SkipWhitespace, //
						parseFloat(), //
						SkipWhitespace, //
						optional(','), //
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
						one(':'), //
						SkipWhitespace, //
						parseFloat(), //
						SkipWhitespace, //
						optional(','), //
						SkipWhitespace //
				);
			}
		}, //
	}

	private static ParsingExpressionBuilder stringField(String name) {
		return sequence( //
				string("\"" + name + "\"").onSuccess(push(name)), //
				SkipWhitespace, //
				one(':'), //
				SkipWhitespace, //
				one('"'), //
				zeroOrMore(character('"').not()).onSuccess(pushTextAsString()), //
				one('"'), //
				SkipWhitespace, //
				optional(','), //
				SkipWhitespace //
		);
	}

	private static final ParsingExpressionBuilder SkipWhitespace = zeroOrMore(CharMatchers.asciiWhitespace());
}
