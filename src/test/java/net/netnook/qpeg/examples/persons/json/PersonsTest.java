package net.netnook.qpeg.examples.persons.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.netnook.qpeg.examples.persons.Person;
import net.netnook.qpeg.examples.persons.Person.Gender;
import net.netnook.qpeg.examples.persons.Persons;
import net.netnook.qpeg.expressions.ParsingRule;

public class PersonsTest {

	private ParsingRule rule;

	@Before
	public void init() {
		rule = new ParserFactory().build();
	}

	@Test
	public void test1() {
		CharSequence input = loadData("persons/persons.json");
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
		CharSequence input = loadData("persons/persons.json");
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 100; i++) {
				Persons persons = rule.parse(input);
				assertThat(persons.getPersons()).hasSize(10000);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (endTime - startTime) + " millis");
		}
		System.out.println("#############################################");
	}

	@Test
	@Ignore
	public void performance_using_jackson() throws IOException {
		String input = loadData("persons/persons.json").toString();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 100; i++) {
				Persons persons = mapper.readValue(input, Persons.class);
				assertThat(persons.getPersons()).hasSize(10000);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (endTime - startTime) + " millis");
		}
		System.out.println("#############################################");
	}

	private CharSequence loadData(String resource) {
		StringWriter out = new StringWriter();

		try ( //
			  InputStream is = getClass().getClassLoader().getResourceAsStream(resource); //
			  InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8) //
		) {
			char[] buf = new char[1024];
			while (true) {
				int count = reader.read(buf, 0, 1024);
				if (count < 0) {
					break;
				}
				out.write(buf, 0, count);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toString();
	}
}
