package atonkish.reputation.entity.passive;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface IronGolemEntityInterface {
    public boolean addReport(PlayerEntity player, VillagerEntity villager);

    public void clearReports();
}
