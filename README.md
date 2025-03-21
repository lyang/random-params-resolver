# random-params-resolver
[![Build](https://github.com/lyang/random-params-resolver/actions/workflows/build.yaml/badge.svg)](https://github.com/lyang/random-params-resolver/actions/workflows/build.yaml)
[![CodeQL](https://github.com/lyang/random-params-resolver/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/lyang/random-params-resolver/actions/workflows/github-code-scanning/codeql)
[![Dependabot Updates](https://github.com/lyang/random-params-resolver/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/lyang/random-params-resolver/actions/workflows/dependabot/dependabot-updates)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.lyang%3Arandom-params-resolver&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=io.github.lyang%3Arandom-params-resolver)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=io.github.lyang%3Arandom-params-resolver&metric=bugs)](https://sonarcloud.io/summary/new_code?id=io.github.lyang%3Arandom-params-resolver)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=io.github.lyang%3Arandom-params-resolver&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=io.github.lyang%3Arandom-params-resolver)
[![codecov](https://codecov.io/gh/lyang/random-params-resolver/graph/badge.svg?token=YFYM1CYDL3)](https://codecov.io/gh/lyang/random-params-resolver)
[![GitHub License](https://img.shields.io/github/license/lyang/random-params-resolver)](LICENSE)
[![GitHub Release](https://img.shields.io/github/v/release/lyang/random-params-resolver)](https://github.com/lyang/random-params-resolver/releases)
[![javadoc](https://javadoc.io/badge2/io.github.lyang/random-params-resolver/Javadoc.svg)](https://javadoc.io/doc/io.github.lyang/random-params-resolver)

Junit 5 randomized parameter resolver

## Features
### [RandomParametersExtension](https://javadoc.io/doc/io.github.lyang/random-params-resolver/latest/io/github/lyang/randomparamsresolver/RandomParametersExtension.html)
A JUnit 5 [ParameterResolver](https://javadoc.io/doc/org.junit.jupiter/junit-jupiter-api/latest/org/junit/jupiter/api/extension/ParameterResolver.html) that generates random values for parameters annotated with [Randomize](#randomize).

### [Randomize](https://javadoc.io/doc/io.github.lyang/random-params-resolver/latest/io/github/lyang/randomparamsresolver/RandomParametersExtension.Randomize.html)
Annotation to generate random values for parameters. The supported parameter types are:
* RandomGenerator
* Integer / int
* Long / long
* String
* byte[]

### Examples

```java
import io.github.lyang.randomparamsresolver.RandomParametersExtension;
import io.github.lyang.randomparamsresolver.RandomParametersExtension.Randomize;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(RandomParametersExtension.class)
class RandomizedParameterTest {
  @Test
  void randomized_generator(@Randomize RandomGenerator generator) {
    // random generator with seed from System.nanoTime()
  }

  @Test
  void randomized_generator_with_seed(
      @Randomize(seed = 787681803879958L) RandomGenerator generator) {
    // random generator with fixed seed to reproduce failures
    // seed value from previous test runs are logged like this:
    // INFO: Using seed 787681803879958 for RandomizedParameterTest#randomized_generator#arg0
  }

  @Test
  void randomized_integer(@Randomize int value, @Randomize Integer anotherValue) {
    // value is a random integer
    // anotherValue is a random integer with a different value
  }

  @Test
  void randomized_long_with_range(@Randomize(longMin = 0, longMax = 100) long value) {
    // value is a random long within [0, 100)
  }

  @Test
  void randomized_string(@Randomize String value, @Randomize(length = 10) String anotherValue) {
    // value is a random string with default length 5
    // anotherValue is a random string with length 10
    // default characters are from Character.UnicodeBlock.BASIC_LATIN
  }

  @Test
  void randomized_emoji_string(@Randomize(unicodeBlocks = "EMOTICONS") String value) {
    // characters are from Character.UnicodeBlock.EMOTICONS
  }

  @Test
  void randomized_byte_array(@Randomize byte[] value, @Randomize(length = 10) byte[] anotherValue) {
    // value is a random byte array with default length 5
    // anotherValue is a random byte array with length 10
  }
}

```

## License

[Apache-2.0](LICENSE)
