package atonkish.reputation.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;

public class IronGolemCache {
    private static final Map<PlayerEntity, Cache<IronGolemEntity, IronGolemCache.Data>> IRON_GOLEM_CACHE_MAP = new HashMap<>();

    public static Cache<IronGolemEntity, IronGolemCache.Data> getOrCreate(PlayerEntity player) {
        if (!IronGolemCache.IRON_GOLEM_CACHE_MAP.containsKey(player)) {
            Cache<IronGolemEntity, IronGolemCache.Data> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(CacheConfig.MAXIMUM_CACHE_SIZE)
                    .build();
            IronGolemCache.IRON_GOLEM_CACHE_MAP.put(player, cache);
        }

        return IronGolemCache.IRON_GOLEM_CACHE_MAP.get(player);
    }

    public static class Data {
        @Nullable
        private UUID angryAt;

        public Data(@Nullable UUID angryAt) {
            this.angryAt = angryAt;
        }

        public Data() {
            this(null);
        }

        @Nullable
        public UUID getAngryAt() {
            return this.angryAt;
        }

        public void setAngryAt(@Nullable UUID angryAt) {
            this.angryAt = angryAt;
        }
    }
}
