package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.Constant;

public class Context {

	private final CharSequence input;
	private int position;

	public Context(CharSequence input) {
		this.input = input;
		this.position = 0;
	}

	public char peekChar() {
		if (position < input.length()) {
			return input.charAt(position);
		} else {
			return Constant.EOICHAR;
		}
	}

	public int incrementPosition() {
		position++;
		return position;
	}

	public int position() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public CharSequence getInput(int start, int end) {
		return input.subSequence(start, end);
	}
}
