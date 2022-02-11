package atonkish.reputation.provider;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import atonkish.reputation.util.CachedData;
import atonkish.reputation.util.ReputationStatus;

public class VillagerComponentProvider implements IEntityComponentProvider {
    @Override
    public void appendHead(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();

        CachedData cachedData = getCachedData(player, villager);

        MutableText text = new LiteralText("");
        if (cachedData.isSnitch()) {
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
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();

        CachedData cachedData = getCachedData(player, villager);

        @Nullable
        Integer reputation = cachedData.getReputation();
        ReputationStatus status = ReputationStatus.getStatus(reputation);

        MutableText text = new TranslatableText(status.getTranslateKey());

        if (reputation != null) {
            text = text.append(String.format(" (%d)", reputation));
        }

        text = text.formatted(status.getFormatting());

        tooltip.addLine(text);
    }

    private CachedData getCachedData(PlayerEntity player, VillagerEntity villager) {
        @Nullable
        Cache<VillagerEntity, CachedData> cache = ReputationMod.PLAYER_REPUTATION_CACHE_MAP.get(player);
        CachedData cachedData = new CachedData();
        if (cache != null) {
            cachedData = Optional.ofNullable(cache.getIfPresent(villager)).orElse(new CachedData());
        }

        return cachedData;
    }
}
