import java.util.stream.IntStream;

public class Example {
    private static final String FIZZ = "Fizz";
    private static final String BUZZ = "Buzz";

    public static void main(String[] args) {
        Matcher<Integer, String> fizzBuzzMatcher = Matcher.<Integer, String>builder()
                .addCase(i -> i % 15 == 0, i -> FIZZ + BUZZ)
                .addCase(i -> i % 3 == 0, i -> FIZZ)
                .addCase(i -> i % 5 == 0, i -> BUZZ)
                .defaultCase(i -> Integer.toString(i))
                .build();

        IntStream.rangeClosed(1, 15)
                .mapToObj(fizzBuzzMatcher::match)
                .forEach(System.out::println);
    }
}
