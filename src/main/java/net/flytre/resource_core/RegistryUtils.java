package net.flytre.resource_core;

import com.google.common.collect.ImmutableSet;
import net.flytre.resource_core.ResourceCore;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class RegistryUtils {


    private static final Set<String> INFERRED_BOILER_NAMESPACES = new HashSet<>();

    public static void registerBlocks(Class<?> clazz, String namespace, @Nullable ItemGroup group, boolean registerBlockItems) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> blockFields = new ArrayList<>();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()) && Block.class.isAssignableFrom(field.getType()))
                blockFields.add(field);
        }
        blockFields.forEach(i -> {
                    try {
                        i.setAccessible(true);
                        Identifier id = new Identifier(namespace, i.getName().toLowerCase(Locale.ROOT));
                        if (!Registry.BLOCK.containsId(id)) {
                            Registry.register(Registry.BLOCK, id, (Block) i.get(null));
                            if (registerBlockItems)
                                Registry.register(Registry.ITEM, id, new BlockItem((Block) i.get(null), new Item.Settings().group(group == null ? ItemGroup.MISC : group)));
                        } else
                            ResourceCore.LOGGER.warning("Tried to register block " + id + " multiple times.");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public static void registerItems(Class<?> clazz, String namespace) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> itemFields = new ArrayList<>();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()) && Item.class.isAssignableFrom(field.getType()))
                itemFields.add(field);
        }
        itemFields.forEach(i -> {
                    try {
                        i.setAccessible(true);
                        Identifier id = new Identifier(namespace, i.getName().toLowerCase(Locale.ROOT));
                        if (!Registry.ITEM.containsId(id)) {
                            Registry.register(Registry.ITEM, id, (Item) i.get(null));
                        } else
                            ResourceCore.LOGGER.warning("Tried to register item " + id + " multiple times.");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public static void registerBoilerNamespace(String namespace) {
        INFERRED_BOILER_NAMESPACES.add(namespace);
    }

    public static Set<String> getInferredBoilerNamespaces() {
        return ImmutableSet.copyOf(INFERRED_BOILER_NAMESPACES);
    }
}
