package atonkish.reputation.provider;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.ReputationStatus;

public class VillagerComponentProvider implements IEntityComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();

        @Nullable
        Cache<Integer, Integer> cache = ReputationMod.PLAYER_REPUTATION_CACHE_MAP.get(player.getUuid());

        @Nullable
        Integer reputation = cache == null ? null : cache.getIfPresent(villager.getId());
        ReputationStatus status = ReputationStatus.getStatus(reputation);

        MutableText text = new TranslatableText(status.getTranslateKey());
        if (reputation != null) {
            text = text.append(String.format(" (%d)", reputation));
        }
        text = text.formatted(status.getFormatting());
        tooltip.addLine(text);
    }
}
