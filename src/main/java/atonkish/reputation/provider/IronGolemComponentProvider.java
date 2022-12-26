package atonkish.reputation.provider;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

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
import atonkish.reputation.util.IronGolemCache;

public class IronGolemComponentProvider implements IEntityComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        IronGolemCache.Data golemData = getIronGolemData(accessor);

        @Nullable
        UUID angryAt = golemData.getAngryAt();

        if (player.getUuid().equals(angryAt)) {
            MutableText text = Text.translatable("entity." + ReputationMod.MOD_ID + ".iron_golem.angry")
                    .formatted(Formatting.DARK_RED);
            tooltip.addLine(text);
        }
    }

    private IronGolemCache.Data getIronGolemData(IEntityAccessor accessor) {
        PlayerEntity player = accessor.getPlayer();
        IronGolemEntity golem = accessor.getEntity();

        Cache<IronGolemEntity, IronGolemCache.Data> golemCache = IronGolemCache.getOrCreate(player);
        IronGolemCache.Data golemData = Optional
                .ofNullable(golemCache.getIfPresent(golem))
                .orElse(new IronGolemCache.Data());

        NbtCompound data = accessor.getServerData().getCompound(ReputationMod.REPUTATION_CUSTOM_DATA_KEY);
        if (data.contains(ReputationMod.IRON_GOLEM_ANGRY_AT_DATA)) {
            String angryAtString = data.getString(ReputationMod.IRON_GOLEM_ANGRY_AT_DATA);

            @Nullable
            UUID angryAt = angryAtString != "" ? UUID.fromString(angryAtString) : null;
            golemData.setAngryAt(angryAt);
        }

        golemCache.put(golem, golemData);

        return golemData;
    }
}
