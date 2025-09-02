package bg.sofia.uni.fmi.mjt.splitwise.api.env;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.env.InvalidEnvironmentVariable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class EnvironmentVariableLoaderTest {
    private static final String VALID_ENV_CONTENT = "VALID_KEY=valid-value1234567890\n";
    private static final String INVALID_KEY_LENGTH = "K=valid-value1234567890\n";
    private static final String INVALID_VALUE_LENGTH = "VALID_KEY=short\n";
    private static final String INVALID_CHARACTERS = "VALID_KEY=12312hfdh@dfgdfgfdgfdg@invalid@value\n";
    private static final String EMPTY_KEY = "=valid-value1234567890\n";
    private static final String EMPTY_VALUE = "VALID_KEY=\n";

    @Test
    void testLoadEnvironmentVariablesWithValidEntry() {
        StringReader reader = new StringReader(VALID_ENV_CONTENT);

        assertDoesNotThrow(() -> EnvironmentVariableLoader.loadEnvironmentVariables(reader),
            "Expected no exception when loading valid environment variables.");
    }

    @Test
    void testLoadEnvironmentVariablesWithInvalidKeyLength() {
        StringReader reader = new StringReader(INVALID_KEY_LENGTH);

        assertThrows(InvalidEnvironmentVariable.class, () -> EnvironmentVariableLoader.loadEnvironmentVariables(reader),
            "Expected a InvalidEnvironmentVariable for an invalid key length.");
    }

    @Test
    void testLoadEnvironmentVariablesWithInvalidValueLength() {
        StringReader reader = new StringReader(INVALID_VALUE_LENGTH);

        assertThrows(InvalidEnvironmentVariable.class, () -> EnvironmentVariableLoader.loadEnvironmentVariables(reader),
            "Expected a InvalidEnvironmentVariable for an invalid value length.");
    }

    @Test
    void testLoadEnvironmentVariablesWithInvalidCharactersInValue() {
        StringReader reader = new StringReader(INVALID_CHARACTERS);

        assertThrows(InvalidEnvironmentVariable.class, () -> EnvironmentVariableLoader.loadEnvironmentVariables(reader),
            "Expected a InvalidEnvironmentVariable for invalid characters in the API key value.");
    }

    @Test
    void testGetWithValidKey() {
        StringReader reader = new StringReader(VALID_ENV_CONTENT);
        EnvironmentVariableLoader.loadEnvironmentVariables(reader);

        assertEquals("valid-value1234567890", EnvironmentVariableLoader.get("VALID_KEY"),
            "The retrieved value should match the one in the environment file.");
    }

    @Test
    void testLoadEnvironmentVariablesWithEmptyKey() {
        StringReader reader = new StringReader(EMPTY_KEY);

        assertThrows(InvalidEnvironmentVariable.class, () -> EnvironmentVariableLoader.loadEnvironmentVariables(reader),
            "Expected a InvalidEnvironmentVariable for an empty key.");
    }

    @Test
    void testLoadEnvironmentVariablesWithEmptyValue() {
        StringReader reader = new StringReader(EMPTY_VALUE);

        assertThrows(InvalidEnvironmentVariable.class, () -> EnvironmentVariableLoader.loadEnvironmentVariables(reader),
            "Expected a InvalidEnvironmentVariable for an empty value.");
    }

    @Test
    void testGetWithInvalidKeyThrowsException() {
        String invalidKey = "NON_EXISTENT_KEY";

        assertThrows(InvalidEnvironmentVariable.class, () -> EnvironmentVariableLoader.get(invalidKey),
            "Expected InvalidEnvironmentVariable to be thrown when the key is not found in the environment variables.");
    }

    @Test
    void testLoadEnvironmentVariablesWithIOException() throws IOException {
        Reader reader = mock();

        // This is just ingenious
        doReturn(-1).when(reader).read(any(char[].class), anyInt(), anyInt());
        doThrow(new IOException("Simulated IOException")).when(reader).close();

        assertThrows(RuntimeException.class, () -> EnvironmentVariableLoader.loadEnvironmentVariables(reader));
    }
}
