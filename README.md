# Random Params Resolver

A JUnit 5 extension that automatically generates random test data for your test method parameters. Stop writing boilerplate test fixtures — just annotate your parameters with `@Randomize` and focus on what matters.

[![Build](https://github.com/lyang/random-params-resolver/actions/workflows/build.yaml/badge.svg)](https://github.com/lyang/random-params-resolver/actions/workflows/build.yaml)
[![codecov](https://codecov.io/gh/lyang/random-params-resolver/graph/badge.svg?token=YFYM1CYDL3)](https://codecov.io/gh/lyang/random-params-resolver)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.lyang%3Arandom-params-resolver&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=io.github.lyang%3Arandom-params-resolver)
[![javadoc](https://javadoc.io/badge2/io.github.lyang/random-params-resolver/Javadoc.svg)](https://javadoc.io/doc/io.github.lyang/random-params-resolver)
[![GitHub Release](https://img.shields.io/github/v/release/lyang/random-params-resolver)](https://github.com/lyang/random-params-resolver/releases)
[![GitHub License](https://img.shields.io/github/license/lyang/random-params-resolver)](LICENSE)

## Quick Start

```java
@ExtendWith(RandomParametersExtension.class)
class MyTest {
  @Test
  void test_with_random_data(@Randomize int value, @Randomize String name) {
    // value and name are randomly generated each run
  }
}
```

## Installation

### Maven
```xml
<dependency>
  <groupId>io.github.lyang</groupId>
  <artifactId>random-params-resolver</artifactId>
  <version>VERSION</version>
  <scope>test</scope>
</dependency>
```

### Gradle
```groovy
testImplementation 'io.github.lyang:random-params-resolver:VERSION'
```

## Supported Types

| Type | Annotation Options | Default Range |
|---|---|---|
| `int` / `Integer` | `intMin`, `intMax` | `[Integer.MIN_VALUE, Integer.MAX_VALUE)` |
| `long` / `Long` | `longMin`, `longMax` | `[Long.MIN_VALUE, Long.MAX_VALUE)` |
| `float` / `Float` | `floatMin`, `floatMax` | `[0, 1)` |
| `double` / `Double` | `doubleMin`, `doubleMax` | `[0, 1)` |
| `BigInteger` | `longMin`, `longMax` | `[Long.MIN_VALUE, Long.MAX_VALUE)` |
| `BigDecimal` | `doubleMin`, `doubleMax` | `[0, 1)` |
| `byte[]` | `length` | 5 bytes |
| `String` | `length`, `unicodeBlocks` | 5 chars, `BASIC_LATIN` |
| `RandomGenerator` | `seed` | seeded from `System.nanoTime()` |

## Examples

### Numeric Types

```java
@ExtendWith(RandomParametersExtension.class)
class NumericTest {
  @Test
  void random_integers(@Randomize int a, @Randomize Integer b) {
    // each parameter gets a different random value
  }

  @Test
  void bounded_range(@Randomize(intMin = 1, intMax = 100) int value) {
    // value is in [1, 100)
  }

  @Test
  void floating_point(
      @Randomize(floatMin = 0.0f, floatMax = 1.0f) float ratio,
      @Randomize(doubleMin = -180.0, doubleMax = 180.0) double longitude) {
  }

  @Test
  void big_numbers(@Randomize BigInteger bigInt, @Randomize BigDecimal bigDec) {
  }
}
```

### Strings and Byte Arrays

```java
@ExtendWith(RandomParametersExtension.class)
class StringTest {
  @Test
  void random_string(@Randomize String value) {
    // 5 random BASIC_LATIN characters
  }

  @Test
  void custom_string(@Randomize(length = 20, unicodeBlocks = "EMOTICONS") String emojis) {
    // 20 random emoji characters
  }

  @Test
  void random_bytes(@Randomize(length = 16) byte[] token) {
    // 16 random bytes
  }
}
```

### Reproducible Tests with Seeds

Every test run logs the seed used for each parameter:

```
INFO: Using seed 787681803879958 for MyTest#my_test#arg0
```

To reproduce a failure, pin the seed:

```java
@Test
void reproduce_failure(@Randomize(seed = 787681803879958L) int value) {
  // always generates the same value
}
```

### Direct Access to RandomGenerator

```java
@Test
void custom_generation(@Randomize RandomGenerator random) {
  // use the generator directly for complex test data setup
  var ids = random.ints(10, 1, 1000).boxed().toList();
}
```

## License

[Apache-2.0](LICENSE)
