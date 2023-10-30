package engine.util;

/**
 * Class containing static methods to check the validity of the parameters of
 * the methods.
 * 
 * @version 1.0
 */
public final class Assertions {

  private Assertions() {
  }

  /**
   * Asserts that the condition is true
   *
   * @param condition the condition
   * @param message   the message in case the assertion fails
   * @throws IllegalArgumentException if the condition is false
   */
  public static void assertTrue(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that the condition is true
   *
   * @param value   the object to check
   * @param message the message in case the assertion fails
   * @throws IllegalArgumentException if the value is null
   */
  public static void assertNotNull(Object value, String message) {
    if (null == value) {
      throw new IllegalArgumentException(message);
    }
  }
}
