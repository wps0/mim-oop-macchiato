package pl.wieczorekp.po1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @ParameterizedTest
    @ValueSource(ints = {30})
    void executeSampleProgram(int n) {
        // given
        sampleProgramTest(n);
        String expected = output.toString();
        output.reset();

        // when
        Main.executeSampleProgram();
        String actual = output.toString();

        // then
        assertEquals(expected, actual);
    }

    public static void sampleProgramTest(int n) {
        for (int k = 0; k < n-1; k++) {
            int p = 1;
            k = k+2;
            for (int i = 0; i < k-2; i++) {
                i += 2;
                if (k % i == 0) {
                    p = 0;
                }
                // fors in Macchiato reset the control variable at the beginning of each loop
                i -= 2;
            }
            if (p == 1) {
                System.out.println(k);
            }
            // fors in Macchiato reset the control variable at the beginning of each loop
            k -= 2;
        }
        System.out.println("Program finished");
    }
}