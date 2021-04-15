package com.manya.decaliumcustomitems.item;

import com.manya.decaliumcustomitems.item.meta.CustomMeta;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;
import com.manya.decaliumcustomitems.utils.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static org.bukkit.persistence.PersistentDataType.STRING;

public class WrappedStack {
    private static final NamespacedKey METADATA = DataType.key("item_meta");
    @Nullable
    private ItemMetaFactory metaFactory;
    private final ItemStack handle;
    private final CustomMaterial material;
    @Nullable
    private CustomMeta customMeta;

   public WrappedStack(@NotNull CustomMaterial material) {
     this.handle = new ItemStack(material.getItem().getOriginal());
     this.material = material;
       material.getItem().itemModifier().modify(handle);
       ItemMeta meta = handle.getItemMeta();
       PersistentDataContainer container = meta.getPersistentDataContainer();
     container.set(CustomMaterial.TYPE, DataType.NAMESPACE_KEY, material.getKey());
     if(material.getItem() instanceof MetadataHolder) {
         metaFactory = ((MetadataHolder) material.getItem()).metaFactory();
        customMeta = metaFactory.createDefault();
         PersistentDataContainer metadata = container.getAdapterContext().newPersistentDataContainer();
         metaFactory.save(customMeta, metadata);
         container.set(METADATA, PersistentDataType.TAG_CONTAINER, metadata);
     }

     handle.setItemMeta(meta);
   }

   private WrappedStack(ItemStack handle) {
       this.handle = handle;
       this.material = CustomMaterial.of(handle);
       if(material.getItem() instanceof MetadataHolder) {
           metaFactory = ((MetadataHolder) material.getItem()).metaFactory();
           this.customMeta = metaFactory.load(handle.getItemMeta().getPersistentDataContainer().get(METADATA, PersistentDataType.TAG_CONTAINER));

                   //.load(handle.getItemMeta().getPersistentDataContainer().get(METADATA, PersistentDataType.TAG_CONTAINER));
       }
       // this.customMeta = getCustomMeta(handle);
   }

   public void setCustomMeta(CustomMeta meta) {
       this.customMeta = meta;
       ItemMeta m = handle.getItemMeta();
       PersistentDataContainer metaContainer = m.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
       metaFactory.save(meta, metaContainer);
       m.getPersistentDataContainer().set(METADATA, PersistentDataType.TAG_CONTAINER, metaContainer);
       handle.setItemMeta(m);
   }

   public static WrappedStack asCustomStack(@NotNull ItemStack value) {
       return new WrappedStack(value);
   }
    @Nullable
   public CustomMeta getCustomMeta() {
    return customMeta;
   }


   public ItemStack get() {return handle;}



   public CustomMaterial getMaterial() {
       return material;
   }

}
