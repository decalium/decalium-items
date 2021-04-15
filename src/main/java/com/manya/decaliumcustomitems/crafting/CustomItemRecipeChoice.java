package com.manya.decaliumcustomitems.crafting;

import com.manya.decaliumcustomitems.item.CustomMaterial;
import com.manya.decaliumcustomitems.item.WrappedStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

public class CustomItemRecipeChoice implements RecipeChoice {
    private final CustomMaterial material;
    private final ItemStack stack;
    public CustomItemRecipeChoice(CustomMaterial material) {
        this.material = material;
        this.stack = new WrappedStack(material).get();
    }
    @Override
    public @NotNull ItemStack getItemStack() {
        return stack;
    }

    @Override
    public @NotNull RecipeChoice clone() {
        return new CustomItemRecipeChoice(material);
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack) {
        CustomMaterial material = CustomMaterial.of(itemStack);
        return this.material.equals(material);
    }
    public static CustomItemRecipeChoice customMaterial(CustomMaterial m) {return new CustomItemRecipeChoice(m); }
}
