package atonkish.reputation.provider;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import mcp.mobius.waila.api.IServerDataProvider;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.IronGolemData;

public class IronGolemDataProvider implements IServerDataProvider<IronGolemEntity> {
    @Override
    public final void appendServerData(NbtCompound data, ServerPlayerEntity player, World world,
            IronGolemEntity golem) {
        @Nullable
        UUID angryAt = golem.getAngryAt();

        if (!ReputationMod.IRON_GOLEM_ANGRY_CACHE_MAP.containsKey(player)) {
            Cache<IronGolemEntity, IronGolemData> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(ReputationMod.MAXIMUM_CACHE_SIZE)
                    .build();
            ReputationMod.IRON_GOLEM_ANGRY_CACHE_MAP.put(player, cache);
        }

        IronGolemData golemData = new IronGolemData(angryAt);
        ReputationMod.IRON_GOLEM_ANGRY_CACHE_MAP.get(player).put(golem, golemData);
    }
}
