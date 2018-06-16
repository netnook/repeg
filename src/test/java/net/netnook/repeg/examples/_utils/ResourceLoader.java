package net.netnook.repeg.examples._utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {

	public static CharSequence load(String resource) {
		StringWriter out = new StringWriter();

		try ( //
			  InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(resource); //
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
