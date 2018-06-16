package net.netnook.repeg.examples.persons.csv;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.netnook.repeg.examples._utils.ResourceLoader;
import net.netnook.repeg.examples.persons.Person;
import net.netnook.repeg.examples.persons.Person.Gender;
import net.netnook.repeg.examples.persons.Persons;
import net.netnook.repeg.expressions.ParsingRule;

public class PersonsTest {

	private ParsingRule rule;

	@Before
	public void init() {
		rule = new ParserFactory().build();
	}

	@Test
	public void test1() {
		CharSequence input = ResourceLoader.load("persons/persons.csv");
		Persons persons = rule.parse(input);
		assertThat(persons.getPersons()).hasSize(10000);

		{
			Person person = persons.get(20);
			assertThat(person.getFirstName()).isNull();
			assertThat(person.getLastName()).isEqualTo("McAvey");
			assertThat(person.getEmail()).isEqualTo("tmcaveyk@omniture.com");
			assertThat(person.getGender()).isEqualTo(Gender.Male);
			assertThat(person.getAddress()).isNull();
			assertThat(person.getCoordinates().getLongitude()).isCloseTo(10.0807485f, Offset.offset(0.0001f));
			assertThat(person.getCoordinates().getLatitude()).isCloseTo(53.5754959f, Offset.offset(0.0001f));
		}

		{
			Person person = persons.get(21);
			assertThat(person.getFirstName()).isEqualTo("Rafa");
			assertThat(person.getLastName()).isEqualTo("Priver");
			assertThat(person.getEmail()).isEqualTo("rpriverl@whitehouse.gov");
			assertThat(person.getGender()).isEqualTo(Gender.Female);
			assertThat(person.getAddress().getStreet()).isEqualTo("29 Morningstar Hill");
			assertThat(person.getAddress().getCity()).isEqualTo("Nogent-le-Rotrou");
			assertThat(person.getAddress().getCountry()).isEqualTo("FR");
			assertThat(person.getCoordinates()).isNull();
		}
	}

	@Test
	@Ignore
	public void performance() {
		CharSequence input = ResourceLoader.load("persons/persons.csv");
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 100; i++) {
				rule.parse(input);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (endTime - startTime) + " millis");
		}
		System.out.println("#############################################");
	}
}
