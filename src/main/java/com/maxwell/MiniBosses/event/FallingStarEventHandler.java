package com.maxwell.MiniBosses.event;

import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FallingStarEventHandler {
    private static final int MIN_STARS = 10;
    private static final int MAX_STARS = 20;
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() == ItemRegister_Maxwell.FALLINGSTAREVENTITEM.get()) {

            int starCount = MIN_STARS + player.getRandom().nextInt(MAX_STARS - MIN_STARS + 1);
            for (int i = 0; i < starCount; i++) {
                spawnFallingStar(level, player);
            }
        }
    }

    private static void spawnFallingStar(Level level, Player player) {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI * 2; // ランダムな方向
        double distance = MIN_DISTANCE + random.nextInt(MAX_DISTANCE - MIN_DISTANCE + 1);
        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;
        double y = level.getMaxBuildHeight(); // 高い位置から落とす

        ItemEntity fallingStar = new ItemEntity(level, x, y, z, new ItemStack(ItemRegister_Maxwell.METOFRAGMENT.get()));
        fallingStar.setDeltaMovement(0, -0.5, 0); // ゆっくり落下
        fallingStar.setGlowingTag(true); // ✅ 発光エフェクトを追加
        level.addFreshEntity(fallingStar);
    }
}
