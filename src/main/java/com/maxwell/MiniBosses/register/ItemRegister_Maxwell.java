package com.maxwell.MiniBosses.register;

import com.maxwell.MiniBosses.Item.ArmorMain.Nullarmor;
import com.maxwell.MiniBosses.Item.ArmorMain.Omega_blue_armor;
import com.maxwell.MiniBosses.Item.CustomArmorTier;
import com.maxwell.MiniBosses.Item.CustomTiered;
import com.maxwell.MiniBosses.Item.ItemMain.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ItemRegister_Maxwell {
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final RegistryObject<Item> DISAPPEARING_BLOCK;
    public static final RegistryObject<Item> NULLSPEC_INGOT;
    public static final RegistryObject<Item> FRAGMENT_OF_COSMILITE;
    public static final RegistryObject<Item> GHOST_INGOT;
    public static final RegistryObject<Item> STEAVEN_INGOT;
    public static final RegistryObject<Item> NULLUPGREAD_INGOT;
    public static final RegistryObject<Item> PHANTOM_INGOT;
    public static final RegistryObject<Item> NOTCH_SWORD;
    public static final RegistryObject<Item> HEROBRINE_SWORD;
    public static final RegistryObject<Item> STARDUSTSWORD;
    public static final RegistryObject<Item> NULL_HEAD;
    public static final RegistryObject<Item> NULL_CHEST;
    public static final RegistryObject<Item> NULL_LEGGINGS;
    public static final RegistryObject<Item> NULL_BOOTS;
    public static final RegistryObject<Item> OMEGA_HEAD;
    public static final RegistryObject<Item> OMEGA_CHEST;
    public static final RegistryObject<Item> OMEGA_LEGGINGS;
    public static final RegistryObject<Item> OMEGA_BOOTS;
    public static final RegistryObject<Item> COSMILITE_INGOT;
    public static final RegistryObject<Item> LASTPRISM;
    public static final RegistryObject<Item> TERRABLADE;
    public static final RegistryObject<Item> TERRAPRISM;
    public static final RegistryObject<Item> NOT_AWAKEND_MURASAMA;
    public static final RegistryObject<Item> NIGHTEDGE;
    public static final RegistryObject<Item> METOFRAGMENT;
    public static final RegistryObject<Item> PHANTOMSUMMONITEM;
    public static final RegistryObject<Item> METORITEINGOT;
    public static final RegistryObject<Item> HEROBRINESUMMONITEM;
    public static final RegistryObject<Item> MURASAMA;
    public static final RegistryObject<Item> FALLINGSTAREVENTITEM;
    public static final RegistryObject<Item> SPEAROFREMNANT;
    public static final RegistryObject<CreativeModeTab> MINIBOSS_TAB;
    static
    {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "miniboss");
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "miniboss");
        DISAPPEARING_BLOCK = registerWithTab("disapperblocks", () -> new BlockItem((Block) ModBlocks_Miniboss.DISAPPEARING_BLOCK.get(), new Item.Properties()));
        NULLSPEC_INGOT = registerWithTab("nullspec_ingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
        FRAGMENT_OF_COSMILITE = registerWithTab("fragmentofcosmilite", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
        PHANTOMSUMMONITEM = registerWithTab("phantomsummonitem", () -> new PhantomKingSummonItem(new Item.Properties().rarity(Rarity.RARE)));
        PHANTOM_INGOT = registerWithTab("phantom_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
        GHOST_INGOT = registerWithTab("ghost_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
        STEAVEN_INGOT = registerWithTab("steaven_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
        NULLUPGREAD_INGOT = registerWithTab("nullspec_ingot_upgreaded", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
        COSMILITE_INGOT = registerWithTab("cosmilite_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
        METOFRAGMENT = registerWithTab("meteofragment", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
        METORITEINGOT = registerWithTab("meteoriteingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
        FALLINGSTAREVENTITEM = registerWithTab("fallingstar_event", () -> new FallingStarEventItem(new Item.Properties().rarity(Rarity.EPIC)));
        NOTCH_SWORD = registerWithTab("notchsword", () -> new NotchSword(CustomTiered.Tiers.INF,10,-3, new Item.Properties()));
        NOT_AWAKEND_MURASAMA = registerWithTab("murasama_notawakend", () -> new Murasama_notawakend(CustomTiered.Tiers.INF,5,-2.3f, new Item.Properties()));
        NIGHTEDGE = registerWithTab("nightedge", () -> new NightEdge(CustomTiered.Tiers.INF,17,-2.5f, new Item.Properties()));
        TERRABLADE = registerWithTab("terrablade_minecraft", () -> new TerraBlade(CustomTiered.Tiers.INF,28,-1.5f, new Item.Properties()));
        LASTPRISM = registerWithTab("lastprism", () -> new Lastprism(new Item.Properties().rarity(Rarity.EPIC)));
        MURASAMA = registerWithTab("murasama", () -> new Muramasa(CustomTiered.Tiers.INF,38 ,-1.5f, new Item.Properties()));
        TERRAPRISM = registerWithTab("terraprism_minecraft", () -> new terraprism(CustomTiered.Tiers.INF,11,-1.2f, new Item.Properties()));
        STARDUSTSWORD = registerWithTab("stardustsword", () -> new StardastSword(CustomTiered.Tiers.INF,18,-3.5f, new Item.Properties()));
        HEROBRINE_SWORD = registerWithTab("herobrinesword", () -> new HerobrineSword(CustomTiered.Tiers.INF,5,-2, new Item.Properties()));
        SPEAROFREMNANT = registerWithTab("spearofremannt", () -> new SpearofRemnannt(CustomTiered.Tiers.INF,13,-3, new Item.Properties()));
        NULL_HEAD = registerWithTab("null_helmet", () -> new Nullarmor(CustomArmorTier.ArmorMaterials.INF, ArmorItem.Type.HELMET, new Item.Properties()));
        NULL_CHEST = registerWithTab("null_chestplate", () -> new Nullarmor(CustomArmorTier.ArmorMaterials.INF, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
        NULL_LEGGINGS = registerWithTab("null_leggings", () -> new Nullarmor(CustomArmorTier.ArmorMaterials.INF, ArmorItem.Type.LEGGINGS, new Item.Properties()));
        NULL_BOOTS = registerWithTab("null_boots", () -> new Nullarmor(CustomArmorTier.ArmorMaterials.INF, ArmorItem.Type.BOOTS, new Item.Properties()));
        OMEGA_HEAD = registerWithTab("omega_helmet", () -> new Omega_blue_armor(CustomArmorTier.ArmorMaterials.PHANTOM, ArmorItem.Type.HELMET, new Item.Properties()));
        OMEGA_CHEST = registerWithTab("omega_chestplate", () -> new Omega_blue_armor(CustomArmorTier.ArmorMaterials.PHANTOM, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
        OMEGA_LEGGINGS = registerWithTab("omega_leggings", () -> new Omega_blue_armor(CustomArmorTier.ArmorMaterials.PHANTOM, ArmorItem.Type.LEGGINGS, new Item.Properties()));
        OMEGA_BOOTS = registerWithTab("omega_boots", () -> new Omega_blue_armor(CustomArmorTier.ArmorMaterials.PHANTOM, ArmorItem.Type.BOOTS, new Item.Properties()));
        HEROBRINESUMMONITEM = registerWithTab("herobrine_summonitem", () -> new HerobrineSummonItem((new Item.Properties()).fireResistant().rarity(Rarity.RARE)));
        MINIBOSS_TAB = TABS.register("miniboss_item", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.miniboss.miniboss_item")).icon(() -> new ItemStack((ItemLike)NULLSPEC_INGOT.get())).displayItems((enabledFeatures, entries) -> {
            entries.accept((ItemLike)NULL_BOOTS.get());
            entries.accept((ItemLike)NULL_LEGGINGS.get());
            entries.accept((ItemLike)NULL_CHEST.get());
            entries.accept((ItemLike)NULL_HEAD.get());
            entries.accept((ItemLike)OMEGA_HEAD.get());
            entries.accept((ItemLike)OMEGA_CHEST.get());
            entries.accept((ItemLike)OMEGA_LEGGINGS.get());
            entries.accept((ItemLike)OMEGA_BOOTS.get());
            entries.accept((ItemLike)SPEAROFREMNANT.get());
            entries.accept((ItemLike)NIGHTEDGE.get());
            entries.accept((ItemLike)NOT_AWAKEND_MURASAMA.get());
            entries.accept((ItemLike)LASTPRISM.get());
            entries.accept((ItemLike)STARDUSTSWORD.get());
            entries.accept((ItemLike)TERRABLADE.get());
            entries.accept((ItemLike)TERRAPRISM.get());
            entries.accept((ItemLike)HEROBRINE_SWORD.get());
            entries.accept((ItemLike)NOTCH_SWORD.get());
            entries.accept((ItemLike)FALLINGSTAREVENTITEM.get());
            entries.accept((ItemLike)PHANTOMSUMMONITEM.get());
            entries.accept((ItemLike)HEROBRINESUMMONITEM.get());
            entries.accept((ItemLike)COSMILITE_INGOT.get());
            entries.accept((ItemLike)NULLUPGREAD_INGOT.get());
            entries.accept((ItemLike)METORITEINGOT.get());
            entries.accept((ItemLike)GHOST_INGOT.get());
            entries.accept((ItemLike)FRAGMENT_OF_COSMILITE.get());
            entries.accept((ItemLike)METOFRAGMENT.get());
            entries.accept((ItemLike)STEAVEN_INGOT.get());
            entries.accept((ItemLike)PHANTOM_INGOT.get());
            entries.accept((ItemLike)NULLSPEC_INGOT.get());
        }).build());
    }
    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
        return block;
    }


}
