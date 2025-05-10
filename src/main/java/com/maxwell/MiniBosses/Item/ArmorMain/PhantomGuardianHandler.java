package com.maxwell.MiniBosses.Item.ArmorMain;

import com.maxwell.MiniBosses.DamagetypeRegister;
import com.maxwell.MiniBosses.entity.custom.CustomPhantom;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhantomGuardianHandler {
    private static final Map<UUID, List<CustomPhantom>> phantomMap = new HashMap<>();
    private static final Map<UUID, Integer> lastAttackTime = new HashMap<>();
    private static final Map<UUID, Float> rotationMap = new HashMap<>();
    private static final Map<UUID, Integer> damageBonusMap = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        UUID playerId = player.getUUID();

        if (!level.isClientSide) {
            if (!hasFullArmor(player)) {
                // ✅ 装備していない場合、該当プレイヤーのファントムをすべて削除
                if (phantomMap.containsKey(playerId)) {
                    List<CustomPhantom> phantoms = phantomMap.get(playerId);
                    for (CustomPhantom phantom : phantoms) {
                        phantom.discard(); // ✅ ファントムを削除
                    }
                    phantoms.clear(); // ✅ リストを空にする
                }
                return; // ✅ ここで処理を終了
            }

            // ✅ 装備している場合、ファントムの管理を続行
            phantomMap.putIfAbsent(playerId, new ArrayList<>());
            rotationMap.putIfAbsent(playerId, 0f);
            damageBonusMap.putIfAbsent(playerId, 0);

            List<CustomPhantom> phantoms = phantomMap.get(playerId);
            float rotation = rotationMap.get(playerId);

            if (phantoms.isEmpty()) {
                spawnPhantomAbovePlayer(player, level);
            }

            // ✅ ファントムを回転移動させる
            for (int i = 0; i < phantoms.size(); i++) {
                double angle = Math.toRadians(rotation + (i * 120));
                double xOffset = Math.cos(angle) * 2;
                double zOffset = Math.sin(angle) * 2;
                phantoms.get(i).setPos(player.getX() + xOffset, player.getY() + 5, player.getZ() + zOffset);
            }

            rotationMap.put(playerId, rotation + 5);

            // ✅ 10秒攻撃しなければ1体ずつ減少
            if (lastAttackTime.containsKey(playerId) && level.getGameTime() - lastAttackTime.get(playerId) > 200) {
                if (!phantoms.isEmpty()) {
                    CustomPhantom removedPhantom = phantoms.remove(0);
                    removedPhantom.discard();
                }
                lastAttackTime.put(playerId, (int) level.getGameTime());
            }

            // ✅ ダメージボーナス計算 (ファントムの数)
            int bonusLevel = phantoms.size();
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, bonusLevel - 1, true, true));
        }
    }


    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = player.level();
        UUID playerId = player.getUUID();

        if (!level.isClientSide && hasFullArmor(player)) {
            phantomMap.putIfAbsent(playerId, new ArrayList<>());
            List<CustomPhantom> phantoms = phantomMap.get(playerId);

            if (phantoms.size() < 3) {
                spawnPhantomAbovePlayer(player, level);
            }
            for (CustomPhantom phantom : phantoms) {
                phantom.getNavigation().moveTo(target, 1.5);
            }

            lastAttackTime.put(playerId, (int) level.getGameTime());
        }
    }

    private static void spawnPhantomAbovePlayer(Player player, Level level) {
        CustomPhantom phantom = new CustomPhantom(ModEntities.CUSTOMPHANTOM.get(), level);
        phantom.setPos(player.getX(), player.getY() + 5, player.getZ());
        phantomMap.get(player.getUUID()).add(phantom);
        level.addFreshEntity(phantom);
    }

    private static boolean hasFullArmor(Player player) {
        return player.getInventory().getArmor(0).getItem() instanceof Omega_blue_armor &&
                player.getInventory().getArmor(1).getItem() instanceof Omega_blue_armor &&
                player.getInventory().getArmor(2).getItem() instanceof Omega_blue_armor &&
                player.getInventory().getArmor(3).getItem() instanceof Omega_blue_armor;
    }
}
