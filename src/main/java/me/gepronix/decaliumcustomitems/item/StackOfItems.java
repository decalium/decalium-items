package me.gepronix.decaliumcustomitems.item;

import com.google.common.base.Preconditions;
import com.manya.pdc.DataTypes;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.ItemRegistry;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.utils.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An Item Stack wrapper class for custom items.
 * provides methods for interacting with custom stacks.
 */
public class StackOfItems {
    private static final NamespacedKey METADATA = DataType.key("item_meta");
    private final ItemRegistry registry = DecaliumCustomItems.get().getItemRegistry();
    private final Item item;
    @Nullable
    private ItemMetaFactory<?> metaFactory;
    private final ItemStack handle;

    @Nullable
    private CustomMeta customMeta;

    /**
     * creates new Custom Stack.
     *
     * @param item   - custom item type
     * @param amount - stack's amount
     */
    public StackOfItems(@NotNull Item item, int amount) {
        this.handle = new ItemStack(item.original(), amount);
        this.item = item;

        item.modifier().modify(handle);
        ItemMeta meta = handle.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(registry.getTypeKey(), DataTypes.NAMESPACED_KEY, item.key());

        if (item instanceof MetadataHolder) {
            metaFactory = ((MetadataHolder) item).metaFactory();
            customMeta = metaFactory.createDefault();
            PersistentDataContainer metadata = container.getAdapterContext().newPersistentDataContainer();
            metaFactory.saveMeta(customMeta, metadata);
            container.set(METADATA, PersistentDataType.TAG_CONTAINER, metadata);
        }

        handle.setItemMeta(meta);
    }

    /**
     * Creates an custom stack with 1 amount
     *
     * @param item - custom item type.
     */
    public StackOfItems(@NotNull Item item) {
        this(item, 1);
    }

    private StackOfItems(ItemStack handle) {
        this.handle = handle;
        this.item = DecaliumCustomItems.get().getItemRegistry()
                .of(handle).orElseThrow(() -> new IllegalArgumentException("unknown custom type"));
        if (item instanceof MetadataHolder) {
            metaFactory = ((MetadataHolder) item).metaFactory();
            this.customMeta = metaFactory.load(handle.getItemMeta().getPersistentDataContainer().get(METADATA, PersistentDataType.TAG_CONTAINER));
        }

    }

    /**
     * sets the custom meta of item.
     *
     * @param meta - meta to set.
     */
    public void setCustomMeta(CustomMeta meta) {
        this.customMeta = meta;
        handle.editMeta(m -> {
            PersistentDataContainer metaContainer = m.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
            metaFactory.saveMeta(meta, metaContainer);
            m.getPersistentDataContainer().set(METADATA, PersistentDataType.TAG_CONTAINER, metaContainer);
        });
    }

    /**
     * Wraps bukkit itemstack.
     * works only with Custom ItemStacks.
     *
     * @param value - item to wrap
     * @return wrapped item stack.
     */
    public static StackOfItems of(@NotNull ItemStack value) {
        Preconditions.checkArgument(isCustom(value), "itemstack is not custom!");
        return new StackOfItems(value);
    }

    public PersistentDataContainer metadataContainer() {
        return handle.getItemMeta().getPersistentDataContainer().get(METADATA, PersistentDataType.TAG_CONTAINER);
    }

    public void metadata(PersistentDataContainer container) {
        handle.editMeta(meta -> meta.getPersistentDataContainer().set(METADATA, PersistentDataType.TAG_CONTAINER, container));
    }

    public void editMeta(Consumer<PersistentDataContainer> transaction) {
        handle.editMeta(meta -> {
            var pdc = meta.getPersistentDataContainer();
            var metadata = pdc.get(METADATA, PersistentDataType.TAG_CONTAINER);
            if(metadata == null) metadata = pdc.getAdapterContext().newPersistentDataContainer();
            transaction.accept(metadata);
            pdc.set(METADATA, PersistentDataType.TAG_CONTAINER, metadata);
        });
    }

    /**
     * returns an customMeta instance, if item has meta.
     *
     * @return Optional customMeta
     */
    public Optional<CustomMeta> getCustomMeta() {
        return Optional.ofNullable(customMeta);
    }

    /**
     * checks if item is custom and can be wrapped.
     *
     * @param stack - item to check
     * @return true if item is custom.
     */
    public static boolean isCustom(@NotNull ItemStack stack) {
        return !stack.hasItemMeta() || DecaliumCustomItems.get().getItemRegistry().of(stack).isPresent();
    }

    /**
     * @return wrapped Item Stack
     */
    public ItemStack get() {
        return handle;
    }


    /**
     * @return custom item type.
     */
    public Item getItem() {
        return registry.of(handle).orElseThrow();
    }

}
