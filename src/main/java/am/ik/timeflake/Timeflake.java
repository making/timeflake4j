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
package am.ik.timeflake;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Timeflake is a 128-bit, roughly-ordered, URL-safe UUID. Inspired by Twitter's Snowflake, Instagram's ID and Firebase's PushID.<br>
 *
 * The first 48 bits are a timestamp, which both reduces the chance of collision and allows consecutively created push IDs to sort chronologically. <br>
 * The timestamp is followed by 80 bits of randomness, which ensures that even two people creating Timeflakes at the exact same millisecond are extremely unlikely to generate identical IDs.
 *
 * @author Toshiaki Maki
 * @see <a href="https://github.com/anthonynsimon/timeflake">Timeflake</a>
 */
public final class Timeflake implements java.io.Serializable, Comparable<Timeflake> {
	private static final long serialVersionUID = -4856846361193249487L;

	/**
	 * internal representation of a Snowflake
	 */
	private final BigInteger value;

	/**
	 * the max value of Snowflake (2^128)
	 */
	static final BigInteger MAX_VALUE = new BigInteger("340282366920938463463374607431768211455");

	/**
	 * the max value of the timestamp part (2^48)
	 */
	static final long MAX_TIMESTAMP = 281474976710655L;

	/**
	 * the max value of the random part (2^60)
	 */
	static final BigInteger MAX_RANDOM = new BigInteger("1208925819614629174706175");

	/**
	 * Create a Snowflake instance from a 128-bit big integer.
	 * @param value 128-bit internal representation of a Snowflake.
	 * @throws IllegalArgumentException if the given value exceeds the max value
	 */
	private Timeflake(BigInteger value) {
		if (value == null) {
			throw new IllegalArgumentException("'value' must not be null.");
		}
		if (value.compareTo(MAX_VALUE) > 0) {
			throw new IllegalArgumentException("'value' must not be greater than " + MAX_VALUE + ".");
		}
		this.value = value;
	}

	/**
	 * Encode the snowflake to Base62 string
	 * @return base62 encoded snowflake
	 */
	@Override
	public String toString() {
		return this.base62();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Timeflake timeflake = (Timeflake) o;
		return Objects.equals(value, timeflake.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	/**
	 * Use {@link #valueOf(BigInteger)} instead
	 */
	@Deprecated
	public static Timeflake of(BigInteger value) {
		return valueOf(value);
	}

	/**
	 * Create a Snowflake instance from a 128-bit big integer.
	 * @param value 128-bit internal representation of a Snowflake.
	 * @return Snowflake instance
	 * @throws IllegalArgumentException if the given value exceeds the max value
	 */
	public static Timeflake valueOf(BigInteger value) {
		return new Timeflake(value);
	}


	/**
	 * Use {@link #valueOf(UUID)} instead
	 */
	@Deprecated
	public static Timeflake fromUuid(UUID uuid) {
		return valueOf(uuid);
	}

	/**
	 * Create a Snowflake instance from an UUID
	 * @param uuid UUID
	 * @return Snowflake instance
	 * @throws IllegalArgumentException if the given value exceeds the max value
	 */
	public static Timeflake valueOf(UUID uuid) {
		final BigInteger value = Util.uuid2BigInteger(uuid);
		return new Timeflake(value);
	}

	/**
	 * Create a Snowflake instance from timestamp and random value
	 * @param timestampMillis 48-bit timestamp part of a snowflake
	 * @param random 80-bit random part of a snowflake
	 * @return Snowflake instance
	 * @throws IllegalArgumentException if the given value exceeds the max value
	 */
	public static Timeflake create(long timestampMillis, BigInteger random) {
		if (timestampMillis > MAX_TIMESTAMP) {
			throw new IllegalArgumentException("'timestampMillis' must not be greater than " + MAX_TIMESTAMP + ".");
		}
		if (random.compareTo(MAX_RANDOM) > 0) {
			throw new IllegalArgumentException("'random' must not be greater than " + MAX_RANDOM + ".");
		}
		final BigInteger timestamp = BigInteger.valueOf(timestampMillis);
		final BigInteger value = timestamp.shiftLeft(80).or(random);
		return new Timeflake(value);
	}

	/**
	 * Create a Snowflake instance from timestamp and random value
	 * @param timestamp 48-bit timestamp part of a snowflake
	 * @param random 80-bit random part of a snowflake
	 * @return Snowflake instance
	 * @throws IllegalArgumentException if the given value exceeds the max value
	 */
	public static Timeflake create(Instant timestamp, BigInteger random) {
		return create(timestamp.toEpochMilli(), random);
	}

	/**
	 * Use {@link #valueOf(String)} instead
	 */
	@Deprecated
	public static Timeflake fromBase62(String base62) {
		return valueOf(base62);
	}

	/**
	 * Create a Snowflake instance from a base62 encoded string
	 * @param base62 base62 encoded snowflake
	 * @return Snowflake instance
	 * @throws IllegalArgumentException if the given value exceeds the max value
	 */
	public static Timeflake valueOf(String base62) {
		final BigInteger value = Base62.decode(base62);
		return new Timeflake(value);
	}

	/**
	 * Generate a Snowflake instance using current timestamp and <code>ThreadLocalRandom</code>.
	 * @return Snowflake instance
	 */
	public static Timeflake generate() {
		return generate(ThreadLocalRandom.current());
	}

	/**
	 * Generate a Snowflake instance using current timestamp and the given random instance.
	 * @return Snowflake instance
	 */
	public static Timeflake generate(Random random) {
		return create(System.currentTimeMillis(), new BigInteger(80, random));
	}

	public BigInteger value() {
		return this.value;
	}

	/**
	 * Convert the snowflake to UUID
	 * @return UUID representation of the snowflake
	 */
	public UUID toUuid() {
		return Util.bigInteger2Uuid(this.value);
	}

	/**
	 * Return timestamp part of the snowflake as an <code>Instance</code>
	 * @return timestamp part of the snowflake
	 */
	public Instant toInstant() {
		final long epochMilli = this.value.shiftRight(80).longValue();
		return Instant.ofEpochMilli(epochMilli);
	}

	/**
	 * Convert the snowflake to byte[]
	 * @return byte[] representation of the snowflake
	 */
	public byte[] toByteArray() {
		return this.value.toByteArray();
	}

	/**
	 * Encode the snowflake to Base62 string
	 * @return base62 encoded snowflake
	 */
	public String base62() {
		return Base62.encode(this.value);
	}

	@Override
	public int compareTo(Timeflake o) {
		return Objects.compare(this.value, o.value, BigInteger::compareTo);
	}
}
