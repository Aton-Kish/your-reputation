package atonkish.reputation.provider;

import javax.annotation.Nullable;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.ReputationStatus;

public class VillagerComponentProvider implements IEntityComponentProvider {
    private void append(ITooltip tooltip, IEntityAccessor accessor) {
        VillagerEntity villager = accessor.getEntity();

        @Nullable
        Integer reputation = ReputationMod.REPUTATION_CACHE.getIfPresent(villager.getId());
        ReputationStatus status = ReputationStatus.getStatus(reputation);

        MutableText text = new TranslatableText(status.getTranslateKey());
        if (reputation != null) {
            text = text.append(String.format("(%+d)", reputation));
        }
        text = text.formatted(status.getFormatting());
        tooltip.addLine(text);
    };

    @Override
    public void appendHead(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        append(tooltip, accessor);
    }

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        append(tooltip, accessor);
    }

    @Override
    public void appendTail(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        append(tooltip, accessor);
    }
}
