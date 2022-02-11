package atonkish.reputation.mixin;

import java.util.List;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackIronGolemTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reputation.entity.passive.IronGolemEntityInterface;

@Mixin(TrackIronGolemTargetGoal.class)
public class TrackIronGolemTargetGoalMixin {
    @Shadow
    private IronGolemEntity golem;

    @Shadow
    private TargetPredicate targetPredicate;

    @Inject(at = @At("HEAD"), method = "canStart")
    public void canStart(CallbackInfoReturnable<Boolean> infoReturnable) {
        Box box = this.golem.getBoundingBox().expand(10.0, 8.0, 10.0);
        List<VillagerEntity> villagers = this.golem.world.getTargets(VillagerEntity.class, this.targetPredicate,
                this.golem, box);
        List<PlayerEntity> players = this.golem.world.getPlayers(this.targetPredicate, this.golem, box);
        for (VillagerEntity villager : villagers) {
            for (PlayerEntity player : players) {
                int reputation = villager.getReputation(player);
                if (reputation > -100) {
                    continue;
                }

                ((IronGolemEntityInterface) this.golem).addReport(player, villager);
            }
        }
    }
}
