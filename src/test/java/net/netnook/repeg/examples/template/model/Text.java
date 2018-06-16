package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;

public class Text extends Node {

	private String string;

	public Text(String string) {
		this.string = string;
	}

	@Override
	public void render(Context ctxt, PrintWriter out) {
		if (string != null) {
			out.print(string);
		}
	}

	@Override
	void preTrim(boolean trimStart, boolean trimEnd) {
		if (trimStart) {
			string = trimStart(string);
		}
		if (trimEnd) {
			string = trimEnd(string);
		}
	}

	private String trimStart(String in) {
		if (in == null) {
			return null;
		}
		int i = 0;
		for (; i < in.length(); i++) {
			if (!Character.isWhitespace(in.charAt(i))) {
				break;
			}
		}

		if (i == 0) {
			return in;
		} else if (i == in.length()) {
			return null;
		} else {
			return in.substring(i);
		}
	}

	private String trimEnd(String in) {
		if (in == null) {
			return null;
		}
		int i = in.length() - 1;
		for (; i >= 0; i--) {
			if (!Character.isWhitespace(in.charAt(i))) {
				break;
			}
		}

		if (i == in.length() - 1) {
			return in;
		} else if (i == -1) {
			return null;
		} else {
			return in.substring(0, i + 1);
		}
	}
}
