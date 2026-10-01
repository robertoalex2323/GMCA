package GMCA.gmca_api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class GmcaApiApplicationTests {
    @Test
    void applicationEntryPointIsCallable() {
        assertDoesNotThrow(() -> GmcaApiApplication.class.getDeclaredMethod("main", String[].class));
    }
}
