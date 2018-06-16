package net.netnook.repeg.examples.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.examples._utils.ResourceLoader;
import net.netnook.repeg.examples.template.data.House;
import net.netnook.repeg.examples.template.data.HouseRepository;
import net.netnook.repeg.examples.template.model.Context;
import net.netnook.repeg.examples.template.model.Template;
import net.netnook.repeg.expressions.ParsingRule;

public class TemplateTest {

	private ParsingRule rule;

	@Before
	public void init() {
		rule = new ParserFactory().build();
	}

	@Test
	public void test_render() {
		CharSequence input = ResourceLoader.load("template/house-list.txt");
		Template template = rule.parse(input);

		List<House> houses = new HouseRepository().getHouses();

		Map<String, Object> data = new HashMap<>();
		data.put("houses", houses);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		//		PrintWriter pw = new PrintWriter(System.out);

		template.render(new Context(data), pw);
		pw.flush();

		String result = sw.toString();

		assertThat(result).isEqualTo("Available houses:\n" //
				+ "\n"  //
				+ "* Munich:  78 m2, 3 room(s)  (furnished): 810.00 €/month\n" //
				+ "* Munich:  152 m2, 5 room(s) : 2105.00 €/month\n" //
		);
	}

}
