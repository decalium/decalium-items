import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilationTest {
    public static void main(String[] args) {
            List<String> eba = new ArrayList<>(Arrays.asList("test", "test2"));
            List<String> eb2 = new ArrayList<>(Arrays.asList("ebin", "ebi3"));
            ArrayList<String> eb3 = new ArrayList<>(eba);
            eb3.addAll(eb2);
            System.out.println(String.join("\n", eb3));
            System.out.println(eb2);
            System.out.println(eb3.subList(eba.size(), eb3.size()));

    }
}
