package atonkish.reputation.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

import atonkish.reputation.entity.passive.VillagerEntityInterface;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin implements VillagerEntityInterface {
    private Map<PlayerEntity, Boolean> snitchRecords = new HashMap<>();

    public boolean isSnitch(PlayerEntity player) {
        return Optional
                .ofNullable(this.snitchRecords.get(player))
                .orElse(false);
    }

    public void setIsSnitch(PlayerEntity player, boolean isSnitch) {
        this.snitchRecords.put(player, isSnitch);
    }
}
