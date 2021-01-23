package am.ik.timeflake;

import java.math.BigInteger;
import java.util.UUID;

/**
 * https://gist.github.com/drmalex07/9008c611ffde6cb2ef3a2db8668bc251
 */
class Util {

	public static final BigInteger HALF = BigInteger.ONE.shiftLeft(64); // 2^64

	public static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

	public static BigInteger uuid2BigInteger(UUID uuid) {
		BigInteger lo = BigInteger.valueOf(uuid.getLeastSignificantBits());
		BigInteger hi = BigInteger.valueOf(uuid.getMostSignificantBits());
		// If any of lo/hi parts is negative interpret as unsigned
		if (hi.signum() < 0) {
			hi = hi.add(HALF);
		}
		if (lo.signum() < 0) {
			lo = lo.add(HALF);
		}
		return lo.add(hi.multiply(HALF));
	}

	public static UUID bigInteger2Uuid(BigInteger i) {
		final BigInteger[] parts = i.divideAndRemainder(HALF);
		BigInteger hi = parts[0];
		BigInteger lo = parts[1];

		if (LONG_MAX.compareTo(lo) < 0) {
			lo = lo.subtract(HALF);
		}
		if (LONG_MAX.compareTo(hi) < 0) {
			hi = hi.subtract(HALF);
		}
		return new UUID(hi.longValueExact(), lo.longValueExact());
	}
}
