package am.ik.timeflake;

import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeflakeTest {

	@Test
	void generate() throws Exception {
		Timeflake timeflake1 = Timeflake.generate();
		Thread.sleep(1);
		Timeflake timeflake2 = Timeflake.generate();
		Thread.sleep(1);
		Timeflake timeflake3 = Timeflake.generate();
		assertThat(timeflake2).isGreaterThan(timeflake1);
		assertThat(timeflake3).isGreaterThan(timeflake2);
	}

	@Test
	void max() {
		final Timeflake timeflake = Timeflake.create(Timeflake.MAX_TIMESTAMP, Timeflake.MAX_RANDOM);
		assertThat(timeflake).isEqualTo(Timeflake.valueOf(Timeflake.MAX_VALUE));
		assertThat(timeflake.toUuid()).isEqualTo(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"));
	}

	@Test
	void fromUuid() {
		final Timeflake timeflake = Timeflake.fromUuid(UUID.fromString("016fa936-bff0-997a-0a3c-428548fee8c9"));
		assertThat(timeflake.value()).isEqualTo(new BigInteger("1909005012028578488143182045514754249"));
		assertThat(timeflake.toInstant().toEpochMilli()).isEqualTo(1579091935216L);
		assertThat(timeflake.base62()).isEqualTo("2i1KoFfY3auBS745gImbZ");
	}

	@Test
	void create() {
		final Timeflake timeflake = Timeflake.create(Instant.ofEpochMilli(1579091935216L), new BigInteger("724773312193627487660233"));
		assertThat(timeflake.value()).isEqualTo(new BigInteger("1909005012028578488143182045514754249"));
		assertThat(timeflake.toUuid()).isEqualTo(UUID.fromString("016fa936-bff0-997a-0a3c-428548fee8c9"));
		assertThat(timeflake.base62()).isEqualTo("2i1KoFfY3auBS745gImbZ");
	}

	@Test
	void fromBase62() {
		final Timeflake timeflake = Timeflake.fromBase62("2i1KoFfY3auBS745gImbZ");
		assertThat(timeflake.value()).isEqualTo(new BigInteger("1909005012028578488143182045514754249"));
		assertThat(timeflake.toInstant().toEpochMilli()).isEqualTo(1579091935216L);
		assertThat(timeflake.toUuid()).isEqualTo(UUID.fromString("016fa936-bff0-997a-0a3c-428548fee8c9"));
	}
}