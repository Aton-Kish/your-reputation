package atonkish.reputation.provider;

import java.util.UUID;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

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

        UUID uuid = player.getUuid();
        if (!ReputationMod.PLAYER_REPUTATION_CACHE_MAP.containsKey(uuid)) {
            Cache<Integer, Integer> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(ReputationMod.MAXIMUM_CACHE_SIZE)
                    .build();
            ReputationMod.PLAYER_REPUTATION_CACHE_MAP.put(uuid, cache);
        }

        Cache<Integer, Integer> cache = ReputationMod.PLAYER_REPUTATION_CACHE_MAP.get(uuid);
        cache.put(villager.getId(), reputation);
    }
}
