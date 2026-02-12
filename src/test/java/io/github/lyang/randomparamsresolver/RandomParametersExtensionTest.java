package io.github.lyang.randomparamsresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.lyang.randomparamsresolver.RandomParametersExtension.Randomize;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;

class RandomParametersExtensionTest {

  private RandomParametersExtension extension;

  private static ParameterContext parameterContext(Parameter parameter) {
    ParameterContext parameterContext = mock();
    when(parameterContext.getParameter()).thenReturn(parameter);
    Randomize annotation = parameter.getAnnotation(Randomize.class);
    when(parameterContext.isAnnotated(Randomize.class)).thenReturn(annotation != null);
    return parameterContext;
  }

  @BeforeEach
  void setUp() {
    extension = new RandomParametersExtension();
  }

  @Test
  void supported_parameter_types() throws NoSuchMethodException {
    for (Class<?> type : RandomParametersExtension.GENERATORS.keySet()) {
      ParameterContext parameterContext = parameterContext(parameter("annotated", type));
      assertThat(extension.supportsParameter(parameterContext, mock()))
          .withFailMessage("Support %s", type.getSimpleName())
          .isTrue();
    }
  }

  @Test
  void unannotated_parameters() throws NoSuchMethodException {
    for (Class<?> type : RandomParametersExtension.GENERATORS.keySet()) {
      ParameterContext parameterContext = parameterContext(parameter("unannotated", type));
      assertThat(extension.supportsParameter(parameterContext, mock()))
          .withFailMessage("Support %s", type.getSimpleName())
          .isFalse();
    }
  }

  @Test
  void unsupported_parameter() throws NoSuchMethodException {
    ParameterContext parameterContext = parameterContext(parameter("annotated", Void.class));
    assertThat(extension.supportsParameter(parameterContext, mock())).isFalse();
  }

  @Test
  void resolve_parameter() throws NoSuchMethodException {
    for (Class<?> type : RandomParametersExtension.GENERATORS.keySet()) {
      ParameterContext parameterContext = parameterContext(parameter("annotated", type));
      assertThat(extension.resolveParameter(parameterContext, mock())).isNotNull();
    }
  }

  @Test
  void resolve_bounded_parameter() throws NoSuchMethodException {
    for (Class<?> type :
        List.of(byte.class, short.class, char.class, float.class, double.class, BigDecimal.class)) {
      ParameterContext parameterContext = parameterContext(parameter("boundedAnnotated", type));
      assertThat(extension.resolveParameter(parameterContext, mock())).isNotNull();
    }
  }

  private Parameter parameter(String method, Class<?> clazz) throws NoSuchMethodException {
    return getClass().getDeclaredMethod(method, clazz).getParameters()[0];
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize(seed = 0L) RandomGenerator generator) {
    throw new UnsupportedOperationException(String.valueOf(generator));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize int value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Integer value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize long value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Long value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize byte[] value) {
    throw new UnsupportedOperationException(Arrays.toString(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize float value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Float value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize double value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Double value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize BigInteger value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize BigDecimal value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize byte value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Byte value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize short value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Short value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize char value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Character value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize String value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void annotated(@Randomize Void value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void boundedAnnotated(@Randomize(byteMin = 0, byteMax = 100) byte value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void boundedAnnotated(@Randomize(shortMin = 0, shortMax = 100) short value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void boundedAnnotated(@Randomize(charMin = 'A', charMax = 'Z') char value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void boundedAnnotated(@Randomize(floatMin = 0.0f, floatMax = 1.0f) float value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void boundedAnnotated(@Randomize(doubleMin = 0.0, doubleMax = 1.0) double value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void boundedAnnotated(@Randomize(doubleMin = 0.0, doubleMax = 1.0) BigDecimal value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(RandomGenerator generator) {
    throw new UnsupportedOperationException(String.valueOf(generator));
  }

  @SuppressWarnings("unused")
  private void unannotated(int value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Integer value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(long value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Long value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(byte[] value) {
    throw new UnsupportedOperationException(Arrays.toString(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(float value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Float value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(double value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Double value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(BigInteger value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(BigDecimal value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(byte value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Byte value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(short value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Short value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(char value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Character value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(String value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }

  @SuppressWarnings("unused")
  private void unannotated(Void value) {
    throw new UnsupportedOperationException(String.valueOf(value));
  }
}
