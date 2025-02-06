package io.github.lyang.randomparamsresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Executable;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * A JUnit 5 extension that generates random values for parameters annotated with {@link Randomize}.
 * @see org.junit.jupiter.api.extension.ParameterResolver
 * @see RandomParametersExtension.Randomize
 */
public class RandomParametersExtension implements ParameterResolver {
  private static final Logger LOGGER = Logger.getLogger(RandomParametersExtension.class.getName());
  static final Map<Class<?>, BiFunction<ParameterContext, ExtensionContext, Object>> GENERATORS =
      Map.ofEntries(
          Map.entry(RandomGenerator.class, RandomParametersExtension::getRandom),
          Map.entry(int.class, RandomParametersExtension::generateInt),
          Map.entry(Integer.class, RandomParametersExtension::generateInt),
          Map.entry(long.class, RandomParametersExtension::generateLong),
          Map.entry(Long.class, RandomParametersExtension::generateLong),
          Map.entry(byte[].class, RandomParametersExtension::generateBytes),
          Map.entry(String.class, RandomParametersExtension::generateString));

  private static Randomize getAnnotation(ParameterContext parameterContext) {
    return parameterContext.getParameter().getAnnotation(Randomize.class);
  }

  private static RandomGenerator getRandom(
      ParameterContext parameterContext, ExtensionContext extensionContext) {
    Randomize annotation = getAnnotation(parameterContext);
    long seed = annotation.seed() == Long.MIN_VALUE ? System.nanoTime() : annotation.seed();
    LOGGER.info(() -> String.format("Using seed %d for %s", seed, getContext(parameterContext)));
    return new Random(seed);
  }

  private static String getContext(ParameterContext parameterContext) {
    Executable executable = parameterContext.getParameter().getDeclaringExecutable();
    return new StringJoiner("#")
        .add(executable.getDeclaringClass().getSimpleName())
        .add(executable.getName())
        .add(parameterContext.getParameter().getName())
        .toString();
  }

  private static int generateInt(
      ParameterContext parameterContext, ExtensionContext extensionContext) {
    Randomize annotation = getAnnotation(parameterContext);
    RandomGenerator random = getRandom(parameterContext, extensionContext);
    return random.nextInt(annotation.intMin(), annotation.intMax());
  }

  private static long generateLong(
      ParameterContext parameterContext, ExtensionContext extensionContext) {
    Randomize annotation = getAnnotation(parameterContext);
    RandomGenerator random = getRandom(parameterContext, extensionContext);
    return random.nextLong(annotation.longMin(), annotation.longMax());
  }

  private static byte[] generateBytes(
      ParameterContext parameterContext, ExtensionContext extensionContext) {
    Randomize annotation = getAnnotation(parameterContext);
    RandomGenerator random = getRandom(parameterContext, extensionContext);
    byte[] bytes = new byte[annotation.length()];
    random.nextBytes(bytes);
    return bytes;
  }

  private static String generateString(
      ParameterContext parameterContext, ExtensionContext extensionContext) {
    Randomize annotation = getAnnotation(parameterContext);
    RandomGenerator random = getRandom(parameterContext, extensionContext);
    Set<String> blocks = Set.of(annotation.unicodeBlocks());
    StringBuilder builder = new StringBuilder(annotation.length());
    random
        .ints(Character.MIN_CODE_POINT, Character.MAX_CODE_POINT)
        .filter(codepoint -> validCodePoint(codepoint, blocks))
        .limit(annotation.length())
        .forEach(builder::appendCodePoint);
    return builder.toString();
  }

  private static boolean validCodePoint(int codepoint, Set<String> blocks) {
    return Optional.ofNullable(Character.UnicodeBlock.of(codepoint))
        .map(Character.Subset::toString)
        .filter(blocks::contains)
        .isPresent();
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(Randomize.class)
        && GENERATORS.containsKey(parameterContext.getParameter().getType());
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return GENERATORS
        .get(parameterContext.getParameter().getType())
        .apply(parameterContext, extensionContext);
  }

  /**
   * Annotation to generate random values for parameters. The supported parameter types are {@link
   * RandomGenerator}, {@link Integer}, {@link Long}, {@link String}, {@code int}, {@code long} and
   * {@code byte[]}.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  public @interface Randomize {
    /** The minimum value for the generated integer. */
    int intMin() default Integer.MIN_VALUE;

    /** The maximum value for the generated integer. */
    int intMax() default Integer.MAX_VALUE;

    /** The minimum value for the generated long. */
    long longMin() default Long.MIN_VALUE;

    /** The maximum value for the generated long. */
    long longMax() default Long.MAX_VALUE;

    /** The length of the generated byte array or string. The default is {@code 5}. */
    int length() default 5;

    /**
     * The seed for the random number generator. The default is {@link System#nanoTime()}. The seed
     * value for each resolved parameter is logged for reproducibility.
     */
    long seed() default Long.MIN_VALUE;

    /**
     * The Unicode blocks to use for generating random strings. The default is {@link
     * Character.UnicodeBlock#BASIC_LATIN}.
     */
    String[] unicodeBlocks() default {"BASIC_LATIN"};
  }
}
