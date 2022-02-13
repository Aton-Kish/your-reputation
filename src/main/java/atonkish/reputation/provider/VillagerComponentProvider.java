package atonkish.reputation.provider;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.WailaConstants;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.VillagerData;
import atonkish.reputation.util.ReputationStatus;

public class VillagerComponentProvider implements IEntityComponentProvider {
    @Override
    public void appendHead(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        VillagerEntity villager = accessor.getEntity();
        VillagerData villagerData = getVillagerData(accessor);

        MutableText text = new LiteralText("");
        if (villagerData.isSnitch()) {
            text = text.append(new LiteralText(villager.getDisplayName().getString())
                    .formatted(Formatting.WHITE, Formatting.STRIKETHROUGH));
            text = text.append(" ");
            text = text.append(new TranslatableText("entity." + ReputationMod.MOD_ID + ".villager.snitch")
                    .formatted(Formatting.DARK_RED));
        } else {
            text = text.append(new LiteralText(villager.getDisplayName().getString()).formatted(Formatting.WHITE));
        }

        tooltip.setLine(WailaConstants.OBJECT_NAME_TAG, text);
    }

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        VillagerData villagerData = getVillagerData(accessor);

        @Nullable
        Integer reputation = villagerData.getReputation();
        ReputationStatus status = ReputationStatus.getStatus(reputation);

        MutableText text = new TranslatableText(status.getTranslateKey());

        if (reputation != null) {
            text = text.append(String.format(" (%d)", reputation));
        }

        text = text.formatted(status.getFormatting());

        tooltip.addLine(text);
    }

    private VillagerData getVillagerData(IEntityAccessor accessor) {
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();

        VillagerData villagerData = loadVillagerCache(player, villager);

        NbtCompound data = accessor.getServerData().getCompound(ReputationMod.REPUTATION_CUSTOM_DATA_KEY);

        @Nullable
        Integer reputation = data.contains(ReputationMod.VILLAGER_REPUTATION_KEY)
                ? data.getInt(ReputationMod.VILLAGER_REPUTATION_KEY)
                : null;

        @Nullable
        Boolean isSnitch = data.contains(ReputationMod.VILLAGER_IS_SNITCH_KEY)
                ? data.getBoolean(ReputationMod.VILLAGER_IS_SNITCH_KEY)
                : null;

        if (reputation != null) {
            villagerData.setReputation(reputation);
        }

        if (isSnitch != null) {
            if (isSnitch) {
                villagerData.setSnitch();
            } else {
                villagerData.resetSnitch();
            }
        }

        storeVillagerCache(player, villager, villagerData);

        return villagerData;
    }

    private VillagerData loadVillagerCache(PlayerEntity player, VillagerEntity villager) {
        if (!ReputationMod.VILLAGER_DATA_CACHE_MAP.containsKey(player)) {
            Cache<VillagerEntity, VillagerData> cache = CacheBuilder
                    .newBuilder()
                    .maximumSize(ReputationMod.MAXIMUM_CACHE_SIZE)
                    .build();
            ReputationMod.VILLAGER_DATA_CACHE_MAP.put(player, cache);
        }

        VillagerData villagerData = Optional
                .ofNullable(ReputationMod.VILLAGER_DATA_CACHE_MAP.get(player).getIfPresent(villager))
                .orElse(new VillagerData());

        return villagerData;
    }

    private void storeVillagerCache(PlayerEntity player, VillagerEntity villager, VillagerData villagerData) {
        ReputationMod.VILLAGER_DATA_CACHE_MAP.get(player).put(villager, villagerData);
    }
}
