package nl.dirkgroot.adventofcode.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

public interface AoCTest {
    default <Result> Result invokeWith(Function<String, Result> f, String input) {
        var start = System.currentTimeMillis();
        var result = f.apply(input);
        var end = System.currentTimeMillis();
        var duration = Duration.ofMillis(end - start);

        System.out.printf("Time:     %d ms%n", duration.toMillis());
        System.out.printf("Solution: %s%n", result);

        return result;
    }

    default String input(int day) {
        try (var stream = AoCTest.class.getResourceAsStream("/inputs/%02d.txt".formatted(day))) {
            return new String(Objects.requireNonNull(stream).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Input for day %d not available!".formatted(day), e);
        }
    }
}
