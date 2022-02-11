package atonkish.reputation.provider;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import mcp.mobius.waila.api.IServerDataProvider;

import atonkish.reputation.ReputationMod;

public class VillagerDataProvider implements IServerDataProvider<VillagerEntity> {
    @Override
    public final void appendServerData(NbtCompound data, ServerPlayerEntity player, World world,
            VillagerEntity villager) {
        int reputation = villager.getReputation(player);
        ReputationMod.REPUTATION_CACHE.put(villager.getId(), reputation);
    }
}
