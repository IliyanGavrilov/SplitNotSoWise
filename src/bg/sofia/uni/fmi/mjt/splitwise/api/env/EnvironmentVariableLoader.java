package bg.sofia.uni.fmi.mjt.splitwise.api.env;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.env.InvalidEnvironmentVariable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class EnvironmentVariableLoader {
    private static final Path ENV_FILE_PATH = Paths.get("resources", ".env");
    private static final String ENV_FILE_MESSAGE_SYMBOL = "#";
    private static final String ENV_KEY_NAME_AND_VALUE_SEPARATOR = "=";

    // Validate API key
    // API keys can only contain letters, digits and '-' '_' '+' '=' ':' '/' '?' '&' '.'
    private static final String API_KEY_PATTERN = "^[a-zA-Z0-9-_+=:/?&.]*$";
    private static final int API_KEY_PARAMETERS_CNT = 2;
    private static final int MIN_API_KEY_NAME_LENGTH = 3;
    private static final int MAX_API_KEY_NAME_LENGTH = 1024;
    private static final int MIN_API_KEY_VALUE_LENGTH = 20;
    private static final int MAX_API_KEY_VALUE_LENGTH = 256;

    private static final Map<String, String> ENV_VARS = new HashMap<>();

    private static void isValidKey(String key, String value) {
        if (key == null || key.length() < MIN_API_KEY_NAME_LENGTH || key.length() > MAX_API_KEY_NAME_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Invalid API key name length: '%s'. Length must be in range: [%d, %d] characters!", key,
                    MIN_API_KEY_NAME_LENGTH, MAX_API_KEY_NAME_LENGTH));
        }

        if (value == null || value.length() < MIN_API_KEY_VALUE_LENGTH || value.length() > MAX_API_KEY_VALUE_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Invalid API key value name length: '%s'. Length must be in range: [%d, %d] characters!",
                    value, MIN_API_KEY_VALUE_LENGTH, MAX_API_KEY_VALUE_LENGTH));
        }

        if (!value.matches(API_KEY_PATTERN)) {
            throw new IllegalArgumentException(
                String.format("Invalid API key value name: '%s'. It contains illegal characters!", key));
        }
    }

    public static void loadEnvironmentVariables(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            ENV_VARS.putAll(bufferedReader.lines()
                .map(String::trim) // Trim each line
                // Skip empty lines and comments
                .filter(line -> !line.isEmpty() && !line.startsWith(ENV_FILE_MESSAGE_SYMBOL))
                .map(line -> line.split(ENV_KEY_NAME_AND_VALUE_SEPARATOR, API_KEY_PARAMETERS_CNT)) // Split into pairs
                .filter(parts -> {
                    try {
                        isValidKey(parts[0], parts[1]);
                        return parts.length == API_KEY_PARAMETERS_CNT;
                    } catch (IllegalArgumentException e) {
                        throw new InvalidEnvironmentVariable("Error when reading environment variable!", e);
                    }
                })
                .collect(HashMap::new, (map, parts) -> map.put(parts[0].trim(), parts[1].trim()), HashMap::putAll));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file!", e);
        }
    }

    public static void loadEnvironmentVariables() {
        try (FileReader fileReader = new FileReader(ENV_FILE_PATH.toFile())) {
            loadEnvironmentVariables(fileReader);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load .env file!", e);
        }
    }

    public static String get(String key) {
        if (!ENV_VARS.containsKey(key) || ENV_VARS.get(key) == null || ENV_VARS.get(key).isEmpty()) {
            throw new InvalidEnvironmentVariable(String.format("Invalid environment variable: '%s' not found!", key));
        }

        return ENV_VARS.get(key);
    }
}