package net.smazeee.tinytweaks.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smazeee.tinytweaks.TinyTweaks;
import net.smazeee.tinytweaks.init.blockentities.DuperBlockEntity;
import net.smazeee.tinytweaks.init.blockentities.MinerBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TinyTweaks.MODID);

    public static final RegistryObject<BlockEntityType<MinerBlockEntity>> MINER_BE =
            BLOCK_ENTITIES.register("miner_be", () ->
                    BlockEntityType.Builder.of(MinerBlockEntity::new,
                            ModBlocks.MINER.get()).build(null));

    public static final RegistryObject<BlockEntityType<DuperBlockEntity>> DUPER_BE =
            BLOCK_ENTITIES.register("duper_be", () ->
                    BlockEntityType.Builder.of(DuperBlockEntity::new,
                            ModBlocks.DUPER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
