package me.gepronix.decaliumcustomitems.item.meta;

import org.bukkit.persistence.PersistentDataContainer;

/**
 * Factory of custom metas.
 *
 * @param <T> - Meta class
 */
public interface ItemMetaFactory<T extends CustomMeta> {
    /**
     * saves ItemMeta data to container.
     *
     * @param meta - meta to save
     * @param to   - container
     */
    void save(T meta, PersistentDataContainer to);

    @SuppressWarnings("unchecked")
    default void saveMeta(CustomMeta meta, PersistentDataContainer to) {
        save((T) meta, to);
    }

    /**
     * loads metadata from container
     *
     * @param from - container with metadata.
     * @return loaded meta.
     */
    T load(PersistentDataContainer from);

    /**
     * creates default meta
     *
     * @return created meta.
     */
    T createDefault();
}
