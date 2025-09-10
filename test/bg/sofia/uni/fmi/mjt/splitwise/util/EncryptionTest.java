package bg.sofia.uni.fmi.mjt.splitwise.util;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EncryptionTest {
    private static final String PASSWORD = "password123";
    private static final String SALT = "somesalt";

    @Test
    void testEncryptWithValidPasswordAndSalt() throws ExpenseSplitException {
        String hashedPassword = Encryption.encrypt(PASSWORD, SALT);
        assertNotNull(hashedPassword, "Hash should not be null");
    }

    @Test
    void testEncryptWithSamePasswordAndSalt() throws ExpenseSplitException {
        String hashedPassword1 = Encryption.encrypt(PASSWORD, SALT);
        String hashedPassword2 = Encryption.encrypt(PASSWORD, SALT);
        assertEquals(hashedPassword1, hashedPassword2, "Hash should be the same for the same password and salt");
    }

    @Test
    void testEncrypt_withDifferentSalt() throws ExpenseSplitException {
        String hashedPassword1 = Encryption.encrypt(PASSWORD, SALT);
        String hashedPassword2 = Encryption.encrypt(PASSWORD, "differentSalt");
        assertNotEquals(hashedPassword1, hashedPassword2, "Hash should be different for different salts");
    }

    @Test
    void testEncryptWithEmptyPassword() throws ExpenseSplitException {
        String hashedPassword = Encryption.encrypt("", SALT);
        assertNotNull(hashedPassword, "Hash should not be null for empty password");
    }

    @Test
    void testEncryptWithEmptySalt() {
        assertThrows(IllegalArgumentException.class, () -> Encryption.encrypt(PASSWORD, ""),
            "Expected exception to be thrown with empty salt!");
    }
}
