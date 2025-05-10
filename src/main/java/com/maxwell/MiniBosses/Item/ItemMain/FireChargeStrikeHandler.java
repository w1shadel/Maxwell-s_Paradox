package com.maxwell.MiniBosses.Item.ItemMain;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireChargeStrikeHandler {
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = player.level();

        // ✅ 剣で攻撃したかチェック
        ItemStack weapon = player.getMainHandItem();
        if (!(weapon.getItem() instanceof StardastSword)) {
            return; // ✅ 剣以外なら処理しない
        }

        // ✅ 攻撃対象の頭上+5ブロックの位置を取得
        Vec3 spawnPos = new Vec3(target.getX(), target.getY() + 5, target.getZ());

        // ✅ ファイアーチャージをスポーン
        PrimedTnt fireCharge = new PrimedTnt(EntityType.TNT, level);
        fireCharge.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        fireCharge.setDeltaMovement(0, -1.5, 0); // ✅ 下方向に落下させる

        level.addFreshEntity(fireCharge);
    }
}

