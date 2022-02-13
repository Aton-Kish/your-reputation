package atonkish.reputation.entity.passive;

import net.minecraft.entity.player.PlayerEntity;

public interface VillagerEntityInterface {
    public boolean isSnitch(PlayerEntity player);

    public void setSnitch(PlayerEntity player);

    public void resetSnitch(PlayerEntity player);
}
