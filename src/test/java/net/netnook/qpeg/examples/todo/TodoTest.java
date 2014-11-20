package net.netnook.qpeg.examples.todo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.netnook.qpeg.examples.todo.model.Project;
import net.netnook.qpeg.expressions.NoMatchException;
import net.netnook.qpeg.expressions.ParsingRule;
import net.netnook.qpeg.util.GrammarBuilder;

public class TodoTest {

	private static final char NEWLINE = '\n';

	private ParsingRule rule;

	@Before
	public void init() {
		rule = new ParserFactory().build();
	}

	@Test
	public void test_todo1() throws NoMatchException {
		String input = input1();

		System.out.println("##################");
		System.out.println(input);
		System.out.println("##################");

		//List<Project> projects = rule.parse(input, LoggingParseListener.builder().build());
		List<Project> projects = rule.parse(input);

		assertThat(projects).hasSize(3);
		{
			Project project = projects.get(0);
			assertThat(project.getName()).isEqualTo("qpeg");
			assertThat(project.getTasks()).hasSize(4);

			assertThat(project.getTask(0).isDone()).isTrue();
			assertThat(project.getTask(0).getDueDate()).isEqualTo(LocalDate.of(2014, 5, 6));
			assertThat(project.getTask(0).getDescription()).isEqualTo("Create 'qpeg' project");

			assertThat(project.getTask(1).isDone()).isTrue();
			assertThat(project.getTask(1).getDueDate()).isNull();
			assertThat(project.getTask(1).getDescription()).isEqualTo("Implement the parser");

			assertThat(project.getTask(2).isDone()).isTrue();
			assertThat(project.getTask(2).getDueDate()).isNull();
			assertThat(project.getTask(2).getDescription()).isEqualTo("Unit testing");

			assertThat(project.getTask(3).isDone()).isFalse();
			assertThat(project.getTask(3).getDueDate()).isNull();
			assertThat(project.getTask(3).getDescription()).isEqualTo("Push to github");
		}
		{
			Project project = projects.get(1);
			assertThat(project.getName()).isEqualTo("Project 2");
			assertThat(project.getTasks()).hasSize(0);
		}
		{
			Project project = projects.get(2);
			assertThat(project.getName()).isEqualTo("My other project");
			assertThat(project.getTasks()).hasSize(3);

			assertThat(project.getTask(0).isDone()).isFalse();
			assertThat(project.getTask(0).getDueDate()).isEqualTo(LocalDate.of(2014, 5, 7));
			assertThat(project.getTask(0).getDescription()).isEqualTo("Do X");

			assertThat(project.getTask(1).isDone()).isFalse();
			assertThat(project.getTask(1).getDueDate()).isEqualTo(LocalDate.of(2014, 5, 8));
			assertThat(project.getTask(1).getDescription()).isEqualTo("Do Y");

			assertThat(project.getTask(2).isDone()).isFalse();
			assertThat(project.getTask(2).getDueDate()).isNull();
			assertThat(project.getTask(2).getDescription()).isEqualTo("Do Z");
		}
	}

	private String input1() {
		StringBuilder buf = new StringBuilder();
		buf.append("qpeg").append(NEWLINE);
		buf.append("  (x) 2014-05-06 Create 'qpeg' project").append(NEWLINE);
		buf.append("  (x) Implement the parser  ").append(NEWLINE);
		buf.append("  (x)   Unit testing  ").append(NEWLINE);
		buf.append("  Push to github").append(NEWLINE);
		buf.append(" ").append(NEWLINE);
		buf.append("Project 2 ").append(NEWLINE);
		buf.append("My other project").append(NEWLINE);
		buf.append("  2014-05-07 Do X").append(NEWLINE);
		buf.append("  2014-05-08 Do Y").append(NEWLINE);
		buf.append("  Do Z");
		return buf.toString();
	}

	@Test
	@Ignore
	public void performance() throws NoMatchException {
		String input = input1();
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				rule.parse(input);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (endTime - startTime) + " millis");
		}
		System.out.println("#############################################");
	}

	@Test
	@Ignore
	public void printGrammar() {
		String grammar = GrammarBuilder.buildGrammar(rule);
		System.out.println(grammar);
	}
}
