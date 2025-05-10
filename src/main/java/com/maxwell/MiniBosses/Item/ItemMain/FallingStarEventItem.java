package com.maxwell.MiniBosses.Item.ItemMain;

import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class FallingStarEventItem extends Item {
    public FallingStarEventItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) { // サーバーサイドでのみ実行
            player.displayClientMessage(Component.literal("§6流れ星が振り始める..."), true);
            player.getCooldowns().addCooldown(this, 10000);
        }
        if (!world.isNight()) { // ✅ 夜でなければ使用不可
            if (!world.isClientSide) {
                    BlockPos playerPosition = player.blockPosition();
                    Direction facingDirection = player.getDirection();
                    BlockPos summonPosition = playerPosition.relative(facingDirection, 9);
                    ModEntities.EMPLESSOFRIGHT.get().spawn((ServerLevel) world, summonPosition, MobSpawnType.TRIGGERED);
                player.displayClientMessage(Component.literal("§c宵闇の女帝は怒りをあらわにする..."), true);
            }
            return InteractionResultHolder.fail(stack); // ✅ 使用失敗
        }
        return InteractionResultHolder.success(stack);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.miniboss.fallingstar_event.desc1").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("item.miniboss.fallingstar_event.desc2").withStyle(ChatFormatting.WHITE));
    }
}
