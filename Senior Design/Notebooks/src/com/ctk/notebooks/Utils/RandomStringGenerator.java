package com.ctk.notebooks.Utils;

import java.util.Random;

public class RandomStringGenerator {

	private static final char[] symbols = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
											'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
											'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
											'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 
											'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
											'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
											'Y', 'Z' };

	private final Random random = new Random();

	private final char[] buf;

	public RandomStringGenerator(int length) {
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
	
		buf = new char[length];
	}

	public String nextString() {
		for (int i = 0; i < buf.length; i++)
			buf[i] = symbols[random.nextInt(symbols.length)];
		
		return new String(buf);
	}
}
