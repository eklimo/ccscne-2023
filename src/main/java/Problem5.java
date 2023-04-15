import java.util.*;

public class Problem5 {
    public static void main(String[] args) {
        List<List<Integer>> inputs = readTestCases();
        inputs.stream()
                .map(Problem5::processTestCase)
                .forEach(System.out::println);
        System.out.println("done");
    }

    private record X(List<Integer> list, int leftProduct, int rightProduct) {
    }

    private static int processTestCase(List<Integer> testCase) {
        List<List<Integer>> permutations = permutations(testCase);

        return asNumber(permutations.stream()
                .map(list -> {
                    boolean odd = list.size() % 2 == 1;
                    List<Integer> left = list.subList(0, list.size() / 2);
                    List<Integer> right = odd
                            ? list.subList(list.size() / 2 + 1, list.size())
                            : list.subList(list.size() / 2, list.size());
                    return new X(list, product(left), product(right));
                })
                .filter(x -> x.leftProduct > x.rightProduct)
                .min(Comparator.comparingInt(x -> asNumber(x.list)))
                .get()
                .list);
    }

    private static int product(List<Integer> list) {
        return list.stream().reduce((a, b) -> a * b).get();
    }

    private static int asNumber(List<Integer> list) {
        return list.stream().reduce(0, (acc, digit) -> 10 * acc + digit);
    }

    private static List<List<Integer>> permutations(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> available = new ArrayList<>(list);
        List<Integer> buffer = new ArrayList<>();

        permutationsHelper(list, available, buffer, result);

        return result;
    }

    private static void permutationsHelper(List<Integer> list, List<Integer> available, List<Integer> buffer, List<List<Integer>> result) {
        if (available.isEmpty()) {
            result.add(new ArrayList<>(buffer));
            return;
        }

        for (int i = 0; i < available.size(); i++) {
            int n = available.get(i);
            available.remove(i);
            buffer.add(n);
            permutationsHelper(list, available, buffer, result);
            buffer.remove(buffer.size() - 1);
            available.add(i, n);
        }
    }

    private static List<List<Integer>> readTestCases() {
        try (Scanner scanner = new Scanner(System.in)) {
            List<List<Integer>> result = new ArrayList<>();

            int numTestCases = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < numTestCases; i++) {
                String line = scanner.nextLine();
                List<Integer> nums = Arrays.stream(line.split(" "))
                        .map(Integer::parseInt)
                        .toList();

                result.add(nums);
            }

            return result;
        }
    }
}
