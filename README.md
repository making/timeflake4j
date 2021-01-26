# Timeflake4j

[![MIT License](https://img.shields.io/github/license/making/timeflake4j.svg)](https://opensource.org/licenses/MIT) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/am.ik.timeflake/timeflake4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/am.ik.timeflake/timeflake4j) [![Javadocs](https://www.javadoc.io/badge/am.ik.timeflake/timeflake4j.svg)](https://www.javadoc.io/doc/am.ik.timeflake/timeflake4j) [![Actions Status](https://github.com/making/timeflake4j/workflows/CI/badge.svg)](https://github.com/making/timeflake4j/actions)

Java implementation of [Timeflake](https://github.com/anthonynsimon/timeflake).

> Timeflake is a 128-bit, roughly-ordered, URL-safe UUID. Inspired by Twitter's Snowflake, Instagram's ID and Firebase's PushID.

## How to use

```xml
<dependency>
    <groupId>am.ik.timeflake</groupId>
    <artifactId>timeflake4j</artifactId>
    <version>1.3.0</version>
</dependency>
```

## Example

### Generate a snowflake

```java
final Timeflake timeflake = Timeflake.generate();
System.out.println(timeflake.value()); // 1948067690345842174618850429941262698 (java.math.BigInteger)
System.out.println(timeflake.toUuid()); // 01772f27-10f3-091a-ea3f-28e7f6f7296a (java.util.UUID)
System.out.println(timeflake.toInstant()); // 2021-01-23T12:10:25.395Z (java.time.Instant)
System.out.println(timeflake.base62()); // 2lSUuPgi2bGXfZde730O2 (String)
```

`TheadLocalRandom` is used to generate a random number by default. You can change `Random` instance as follows:

```java
final Timeflake timeflake = Timeflake.generate(new SecureRandom());
```  

### Create a snowflake

from an `UUID` instance

```java
final Timeflake timeflake = Timeflake.valueOf(UUID.fromString("01772f27-10f3-091a-ea3f-28e7f6f7296a"));
System.out.println(timeflake.value());  // 1948067690345842174618850429941262698
```

from a base62 encoded string

```java
final Timeflake timeflake = Timeflake.valueOf("2lSUuPgi2bGXfZde730O2");
System.out.println(timeflake.value());  // 1948067690345842174618850429941262698
```

from a big integer value

```java
final Timeflake timeflake = Timeflake.valueOf(new BigInteger("1948067690345842174618850429941262698"));
System.out.println(timeflake.value());  // 1948067690345842174618850429941262698
```

from a 48-bit timestamp and 80-bit random part

```java
final Instant timestamp = Instant.now();
final BigInteger random = new BigInteger(80, ThreadLocalRandom.current());
final Timeflake timeflake = Timeflake.create(timestamp, random);
```

## Note on security

See [the original warning](https://github.com/anthonynsimon/timeflake#note-on-security).

## Required

* Java 8+

## License

Licensed under the MIT License