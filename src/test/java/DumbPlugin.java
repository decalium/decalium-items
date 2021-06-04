import org.bukkit.plugin.java.JavaPlugin;

public class DumbPlugin extends JavaPlugin {
    private static DumbPlugin instance;

    public  String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }

    private String field1;
    private String field2;
    private String field3;
    @Override
    public void onEnable() {
        instance = this;
        this.field1 = "1";
        this.field2 = "2";
        this.field3 = "3";
        DumbPlugin.getInstance().getField1();
        DumbPlugin.getInstance().getField2();


    }
    public static DumbPlugin getInstance() {
        return instance;
    }



}
