package me.gepronix.decaliumcustomitems.utils.serialization;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class ItemStackGsonAdapter extends TypeAdapter<ItemStack> {
    private static final String ID = "id";
    private static final String AMOUNT = "amount";
    private static final String TAG = "tag";
    private MethodHandle asNMSMethod, asBukkitMethod, parseNBTMethod;
    private final JsonParser parser = new JsonParser();
    private MethodHandle getTagMethod, setTagMethod;
    private MethodHandle asStringMethod;

    public ItemStackGsonAdapter() {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Class<?> craftItemStack = Class
                    .forName("org.bukkit.craftbukkit.{v}.inventory.CraftItemStack".replace("{v}", version));
            Class<?> mojangsonParser = Class.forName("net.minecraft.server.{v}.MojangsonParser".replace("{v}", version));
            Class<?> nmsItemStack = Class.forName("net.minecraft.server.{v}.ItemStack".replace("{v}", version));
            Class<?> nbtTagCompound = Class.forName("net.minecraft.server.{v}.NBTTagCompound".replace("{v}", version));

            asNMSMethod = lookup.unreflect(craftItemStack.getMethod("asNMSCopy", ItemStack.class));
            asBukkitMethod = lookup.unreflect(craftItemStack.getMethod("asBukkitCopy", nmsItemStack));
            parseNBTMethod = lookup.unreflect(mojangsonParser.getMethod("parse", String.class));
            getTagMethod = lookup.unreflect(nmsItemStack.getMethod("getTag"));
            setTagMethod = lookup.unreflect(nmsItemStack.getMethod("setTag", nbtTagCompound));
            asStringMethod = lookup.unreflect(nbtTagCompound.getMethod("asString"));



        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void write(JsonWriter out, ItemStack value) throws IOException {
        out.beginObject();
        out.name(ID).value(value.getType().getKey().asString());
        out.name(AMOUNT).value(value.getAmount());
        out.name(TAG).jsonValue(getNbt(value));
        out.endObject();
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        JsonObject object = parser.parse(in).getAsJsonObject();
        Material material = Material.matchMaterial(object.get(ID).getAsString());
        int amount = object.get(AMOUNT).getAsInt();
        String tag = object.get(TAG).toString();
        ItemStack item = new ItemStack(material, amount);
        try {
            Object nmsItem = asNMSMethod.invoke(item);
            setTagMethod.invoke(nmsItem, parseNBTMethod.invoke(tag));

            return (ItemStack) asBukkitMethod.invoke(nmsItem);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("something goes wrong with deserialization");
        }


    }
    private String getNbt(ItemStack itemStack) {
        try {
            Object tag = getTagMethod.invoke(asNMSMethod.invoke(itemStack));
            if(tag != null) {
                return (String) asStringMethod.invoke(tag);
            } else {
                return "{}";
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException("something goes wrong with getting item nbt.");
        }
    }
}
