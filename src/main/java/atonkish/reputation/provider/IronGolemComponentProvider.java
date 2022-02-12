package atonkish.reputation.provider;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.cache.Cache;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
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
        IronGolemEntity golem = accessor.getEntity();

        NbtCompound data = accessor.getServerData();
        ReputationMod.LOGGER.info("IronGolem: {}", data);

        IronGolemData golemData = getIronGolemData(player, golem);

        @Nullable
        UUID angryAt = golemData.getAngryAt();

        if (player.getUuid() == angryAt) {
            MutableText text = new TranslatableText("entity." + ReputationMod.MOD_ID + ".iron_golem.angry")
                    .formatted(Formatting.DARK_RED);
            tooltip.addLine(text);
        }
    }

    private IronGolemData getIronGolemData(PlayerEntity player, IronGolemEntity golem) {
        @Nullable
        Cache<IronGolemEntity, IronGolemData> cache = ReputationMod.IRON_GOLEM_ANGRY_CACHE_MAP.get(player);
        IronGolemData data = new IronGolemData();
        if (cache != null) {
            data = Optional.ofNullable(cache.getIfPresent(golem)).orElse(new IronGolemData());
        }

        return data;
    }
}
