package me.gepronix.decaliumcustomitems.example.bag;

import com.manya.pdc.DataTypes;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.utils.AirSafeItemDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BagMeta implements CustomMeta {
    private static final PersistentDataType<?, List<ItemStack>> ITEM_STACK_LIST_DATA_TYPE = DataTypes.list(AirSafeItemDataType.ITEM_STACK);
    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private List<ItemStack> content;

    private BagMeta(List<ItemStack> content) {
        this.content = content;
    }

    public List<ItemStack> content() {
        return content;
    }

    public void setContent(List<ItemStack> content) {
        this.content = content;
    }

    @Override
    public ItemMetaFactory<?> getFactory() {
        return Factory.INSTANCE;
    }


    public static class Factory implements ItemMetaFactory<BagMeta> {
        public static final Factory INSTANCE = new Factory();

        private Factory() {
        }

        private static final NamespacedKey CONTENT = new NamespacedKey(DecaliumCustomItems.get(), "content");

        @Override
        public void save(BagMeta meta, PersistentDataContainer to) {
            to.set(
                    CONTENT,
                    ITEM_STACK_LIST_DATA_TYPE,
                    meta.content.stream().map(item -> item == null ? AIR : item)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        @Override
        public BagMeta load(PersistentDataContainer from) {
            return new BagMeta(from.get(CONTENT, ITEM_STACK_LIST_DATA_TYPE));
        }

        @Override
        public BagMeta createDefault() {
            return new BagMeta(List.of());
        }
    }

}
