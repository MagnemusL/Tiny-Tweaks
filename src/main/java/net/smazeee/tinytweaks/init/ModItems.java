package net.smazeee.tinytweaks.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smazeee.tinytweaks.TinyTweaks;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TinyTweaks.MODID);

    /*public static final RegistryObject<Item> TEST_BLOCK = ITEMS.register("block_detector",
            () -> new Item(new Item.Properties().stacksTo(1)));

     */

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
