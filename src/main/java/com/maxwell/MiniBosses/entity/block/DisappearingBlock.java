package com.maxwell.MiniBosses.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DisappearingBlock extends FallingBlock {
public int blockundotimer;
    public DisappearingBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public void onBrokenAfterFall(Level level, @NotNull BlockPos pos, @NotNull FallingBlockEntity pFallingBlock) {
        level.removeBlock(pos, false); // 着地後にブロックを削除
    }
    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (blockundotimer <= 200) {
            blockundotimer++;

        } else {
            blockundotimer = 0;
            int horizontalRange = 100; // 水平方向の半径
            int verticalRange = 100; // 垂直方向の高さ範囲

            for (int x = -horizontalRange; x <= horizontalRange; x++) {
                for (int z = -horizontalRange; z <= horizontalRange; z++) {
                    for (int y = -verticalRange; y <= verticalRange; y++) { // 高さ方向を含むチェック
                        BlockPos currentPos = pPos.offset(x, y, z); // 現在の座標を計算
                        BlockState currentState = pLevel.getBlockState(currentPos);

                        // **`DisappearingBlock`かどうかを確認**
                        if (currentState.getBlock() instanceof DisappearingBlock) {
                            pLevel.removeBlock(currentPos, false); // `DisappearingBlock`のみを削除
                        }
                    }
                }
            }
        }
    }



}

