package com.maxwell.MiniBosses;

import com.maxwell.MiniBosses.Item.ItemMain.NotchSword;
import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import com.maxwell.MiniBosses.register.ModBlocks_Miniboss;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(com.maxwell.MiniBosses.MiniBossse.MODID)
public class MiniBossse
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "miniboss";
    public MiniBossse(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlocks_Miniboss.BLOCKS.register(modEventBus);
        ItemRegister_Maxwell.ITEMS.register(modEventBus);
        ItemRegister_Maxwell.TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new NotchSword.DamageBlocker());
    }
}
