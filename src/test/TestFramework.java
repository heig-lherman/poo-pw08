package test;

import java.util.Objects;

/**
 * A simple test framework.
 */
public class TestFramework {

    public static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but got false");
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but got true");
        }
    }

    public static void assertThrows(
            Class<? extends Throwable> expected,
            ThrowingRunnable runnable
    ) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (!expected.isInstance(t)) {
                throw new AssertionError("Expected " + expected + " but got " + t);
            }

            return;
        }

        throw new AssertionError("Expected " + expected + " but nothing was thrown");
    }

    public static void assertDoesNotThrow(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            throw new AssertionError("Expected nothing to be thrown but got " + t);
        }
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError(
                    "Expected array of length " + expected.length + " but got " + actual.length
            );
        }

        for (int i = 0; i < expected.length; i++) {
            if (!Objects.equals(expected[i], actual[i])) {
                throw new AssertionError(
                        "Expected " + expected[i] + " at index " + i + " but got " + actual[i]
                );
            }
        }
    }

    public interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
