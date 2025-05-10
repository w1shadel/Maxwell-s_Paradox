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

public class PhantomKingSummonItem extends Item {
    public PhantomKingSummonItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
            if (!world.isClientSide) {
                BlockPos playerPosition = player.blockPosition();
                Direction facingDirection = player.getDirection();
                BlockPos summonPosition = playerPosition.relative(facingDirection, 9);
                ModEntities.PHANTOMKING.get().spawn((ServerLevel) world, summonPosition, MobSpawnType.TRIGGERED);
                player.displayClientMessage(Component.literal("§c宵闇の君主は姿を現す..."), true);
            }
        return InteractionResultHolder.success(stack);
    }
}
