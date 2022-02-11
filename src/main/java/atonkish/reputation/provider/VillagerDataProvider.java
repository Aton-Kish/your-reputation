package atonkish.reputation.provider;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import mcp.mobius.waila.api.IServerDataProvider;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.entity.passive.VillagerEntityInterface;
import atonkish.reputation.util.VillagerData;

public class VillagerDataProvider implements IServerDataProvider<VillagerEntity> {
    @Override
    public final void appendServerData(NbtCompound data, ServerPlayerEntity player, World world,
            VillagerEntity villager) {
        int reputation = villager.getReputation(player);
        boolean snitch = ((VillagerEntityInterface) villager).isSnitch(player);

        if (!ReputationMod.PLAYER_REPUTATION_CACHE_MAP.containsKey(player)) {
            Cache<VillagerEntity, VillagerData> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(ReputationMod.MAXIMUM_CACHE_SIZE)
                    .build();
            ReputationMod.PLAYER_REPUTATION_CACHE_MAP.put(player, cache);
        }

        VillagerData villagerData = new VillagerData(reputation, snitch);
        ReputationMod.PLAYER_REPUTATION_CACHE_MAP.get(player).put(villager, villagerData);
    }
}
