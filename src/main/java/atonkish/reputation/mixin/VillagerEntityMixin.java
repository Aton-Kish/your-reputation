package atonkish.reputation.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import atonkish.reputation.entity.passive.VillagerEntityInterface;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin implements VillagerEntityInterface {
    private Map<PlayerEntity, Boolean> snitchRecords = new HashMap<>();

    public boolean isSnitch(PlayerEntity player) {
        return Optional.ofNullable(this.snitchRecords.get(player)).orElse(false);
    }

    public void setSnitch(PlayerEntity player) {
        this.snitchRecords.put(player, true);
    }

    public void resetSnitch(PlayerEntity player) {
        this.snitchRecords.put(player, false);
    }
}
