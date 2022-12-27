package atonkish.reputation.mixin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

import atonkish.reputation.entity.passive.IronGolemEntityInterface;
import atonkish.reputation.entity.passive.VillagerEntityInterface;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin implements Angerable, IronGolemEntityInterface {
    private Map<PlayerEntity, Set<VillagerEntity>> harmReports = new HashMap<>();

    public boolean addReport(PlayerEntity player, VillagerEntity villager) {
        if (!harmReports.containsKey(player)) {
            Set<VillagerEntity> victims = new HashSet<>();
            harmReports.put(player, victims);
        }

        ((VillagerEntityInterface) villager).setIsSnitch(player, true);
        return this.harmReports.get(player).add(villager);
    }

    public void clearReports() {
        for (PlayerEntity player : this.harmReports.keySet()) {
            for (VillagerEntity villager : this.harmReports.get(player)) {
                ((VillagerEntityInterface) villager).setIsSnitch(player, false);
            }
        }
        this.harmReports.clear();
    }

    public void stopAnger() {
        this.setAttacker(null);
        this.setAngryAt(null);
        this.setTarget(null);
        this.setAngerTime(0);
        this.clearReports();
    }
}
