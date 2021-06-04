import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class CompilationTest {
    public static void main(String[] args) {
            byte[] bytes = new byte[] {1, 2, 3, 4};
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(bytes);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(bos.toByteArray()));


    }
}
