package com.maxwell.MiniBosses.register;
import com.maxwell.MiniBosses.entity.block.DisappearingBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks_Miniboss {
    public static final DeferredRegister<Block> BLOCKS;
    public static final RegistryObject<Block> DISAPPEARING_BLOCK;

     static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "miniboss");
        DISAPPEARING_BLOCK = BLOCKS.register("disapperblocks", () -> new DisappearingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
    }
}

