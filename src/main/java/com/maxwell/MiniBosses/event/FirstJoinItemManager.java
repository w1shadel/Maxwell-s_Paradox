package com.maxwell.MiniBosses.event;

import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FirstJoinItemManager {
    private static final String FIRST_JOIN_TAG = "first_join";

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.getBoolean(FIRST_JOIN_TAG)) {
            giveStarterItem(player);
            playerData.putBoolean(FIRST_JOIN_TAG, true); // ✅ アイテムを受け取ったフラグを保存
        }
    }
    private static void giveStarterItem(Player player) {
        ItemStack starterItem = new ItemStack(ItemRegister_Maxwell.NOT_AWAKEND_MURASAMA.get()); // ✅ 初回アイテム
        player.getInventory().add(starterItem);
    }
}

