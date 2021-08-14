/*
 * Copyright (c) 2021 Toshiaki Maki <makingx@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package am.ik.timeflake.spring;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import am.ik.timeflake.Timeflake;

import org.springframework.util.IdGenerator;

public class TimeflakeIdGenerator implements IdGenerator {
	private final Supplier<Random> randomFactory;

	public TimeflakeIdGenerator(Supplier<Random> randomFactory) {
		this.randomFactory = randomFactory;
	}

	public TimeflakeIdGenerator() {
		this(ThreadLocalRandom::current);
	}

	@Override
	public UUID generateId() {
		final Random random = this.randomFactory.get();
		return Timeflake.generate(random).toUuid();
	}
}
