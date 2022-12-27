package atonkish.reputation.util.cache;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class VillagerCache {
    private static final Map<PlayerEntity, Cache<VillagerEntity, VillagerCache.Data>> VILLAGER_CACHE_MAP = new HashMap<>();

    public static Cache<VillagerEntity, VillagerCache.Data> getOrCreate(PlayerEntity player) {
        if (!VillagerCache.VILLAGER_CACHE_MAP.containsKey(player)) {
            Cache<VillagerEntity, VillagerCache.Data> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(CacheConfig.MAXIMUM_CACHE_SIZE)
                    .build();
            VillagerCache.VILLAGER_CACHE_MAP.put(player, cache);
        }

        return VillagerCache.VILLAGER_CACHE_MAP.get(player);
    }

    public static class Data {
        @Nullable
        private Integer reputation;
        private boolean isSnitch;

        public Data(@Nullable Integer reputation, boolean isSnitch) {
            this.reputation = reputation;
            this.isSnitch = isSnitch;
        }

        public Data() {
            this(null, false);
        }

        @Nullable
        public Integer getReputation() {
            return this.reputation;
        }

        public void setReputation(@Nullable Integer reputation) {
            this.reputation = reputation;
        }

        public boolean isSnitch() {
            return this.isSnitch;
        }

        public void setIsSnitch(boolean isSnitch) {
            this.isSnitch = isSnitch;
        }
    }
}
