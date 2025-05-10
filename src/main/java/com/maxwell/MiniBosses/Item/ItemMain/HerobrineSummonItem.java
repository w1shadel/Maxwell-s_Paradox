package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HerobrineSummonItem extends Item {


    public HerobrineSummonItem(Properties pProperties) {
        super(pProperties);
    }
public int Notchteal;
    @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        Notchteal++;
        ItemStack stack = player.getItemInHand(hand);
        if (world.dimension() == Level.NETHER && !world.isClientSide) {
                BlockPos playerPosition = player.blockPosition();
                Direction facingDirection = player.getDirection();
                BlockPos summonPosition = playerPosition.relative(facingDirection, 5);
                ModEntities.HEROBRINE2.get().spawn((ServerLevel) world, summonPosition, MobSpawnType.TRIGGERED);
                player.displayClientMessage(Component.literal("§4楽しませて見せろ,,,"), true);
                stack.shrink(1);
                Notchteal = 0;
        }
        player.getCooldowns().addCooldown(this, 10);
            if (Notchteal == 1)
            {
                player.displayClientMessage(Component.literal("§e本当にいいのか？"), true);
            }
        if (Notchteal == 2)
        {
            player.displayClientMessage(Component.literal("§eこれを使い続ければ俺が封印してたやつが解き放たれるぞ"), true);
        }
        if (Notchteal == 3)
        {
            player.displayClientMessage(Component.literal("§e君はじきに後悔することになるぞ"), true);
        }
            if (!world.isClientSide && Notchteal == 4) { // サーバーサイドでのみ実行
                BlockPos playerPosition = player.blockPosition();
                Direction facingDirection = player.getDirection();
                BlockPos summonPosition = playerPosition.relative(facingDirection, 5);
                BlockPos summonPosition_notch = playerPosition.relative(facingDirection, 3);
                ModEntities.HEROBRINE.get().spawn((ServerLevel) world, summonPosition, MobSpawnType.TRIGGERED);
                ModEntities.NOTCH.get().spawn((ServerLevel) world, summonPosition_notch, MobSpawnType.TRIGGERED);
                player.displayClientMessage(Component.literal("§e私が消せなかった存在、消して見せろ、私も手伝ってやろう。"), true);
                stack.shrink(1);
                Notchteal = 0;
            }

            return InteractionResultHolder.success(stack);
        }
}
