import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String testBranch = "Тест на слияние веток";
        ArrayList<String> s = new ArrayList<>();
        for (int a = 0; a < 50; a++){
            s.add(testBranch);
        }
        s.stream().forEach(q -> System.out.println(q));
    }
}
