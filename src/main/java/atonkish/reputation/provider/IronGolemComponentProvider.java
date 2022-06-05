package atonkish.reputation.provider;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.IronGolemData;

public class IronGolemComponentProvider implements IEntityComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        IronGolemData golemData = getIronGolemData(accessor);

        @Nullable
        UUID angryAt = golemData.getAngryAt();

        if (player.getUuid().equals(angryAt)) {
            MutableText text = Text.translatable("entity." + ReputationMod.MOD_ID + ".iron_golem.angry")
                    .formatted(Formatting.DARK_RED);
            tooltip.addLine(text);
        }
    }

    private IronGolemData getIronGolemData(IEntityAccessor accessor) {
        PlayerEntity player = accessor.getPlayer();
        IronGolemEntity golem = accessor.getEntity();

        IronGolemData golemData = loadIronGolemCache(player, golem);

        NbtCompound data = accessor.getServerData().getCompound(ReputationMod.REPUTATION_CUSTOM_DATA_KEY);

        if (data.contains(ReputationMod.IRON_GOLEM_ANGRY_AT_DATA)) {
            String angryAtString = data.getString(ReputationMod.IRON_GOLEM_ANGRY_AT_DATA);

            @Nullable
            UUID angryAt = angryAtString != "" ? UUID.fromString(angryAtString) : null;
            golemData.setAngryAt(angryAt);
        }

        storeIronGolemCache(player, golem, golemData);

        return golemData;
    }

    private IronGolemData loadIronGolemCache(PlayerEntity player, IronGolemEntity golem) {
        if (!ReputationMod.IRON_GOLEM_DATA_CACHE_MAP.containsKey(player)) {
            Cache<IronGolemEntity, IronGolemData> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(ReputationMod.MAXIMUM_CACHE_SIZE)
                    .build();
            ReputationMod.IRON_GOLEM_DATA_CACHE_MAP.put(player, cache);
        }

        IronGolemData golemData = Optional
                .ofNullable(ReputationMod.IRON_GOLEM_DATA_CACHE_MAP.get(player).getIfPresent(golem))
                .orElse(new IronGolemData());

        return golemData;
    }

    private void storeIronGolemCache(PlayerEntity player, IronGolemEntity golem, IronGolemData golemData) {
        ReputationMod.IRON_GOLEM_DATA_CACHE_MAP.get(player).put(golem, golemData);
    }
}
