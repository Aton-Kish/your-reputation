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
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.cache.IronGolemCache;

public enum IronGolemProvider implements IEntityComponentProvider, IServerDataProvider<IronGolemEntity> {

    INSTANCE;

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        NbtCompound data = accessor.getServerData().getCompound(DataKeys.REPUTATION_MOD_DATA);
        PlayerEntity player = accessor.getPlayer();
        IronGolemEntity golem = accessor.getEntity();

        IronGolemCache.Data golemData = IronGolemProvider.getIronGolemData(data, player, golem);

        @Nullable
        UUID angryAt = golemData.getAngryAt();

        if (player.getUuid().equals(angryAt)) {
            MutableText text = Text.translatable("entity." + ReputationMod.MOD_ID + ".iron_golem.angry")
                    .formatted(Formatting.DARK_RED);
            tooltip.addLine(text);
        }
    }

    @Override
    public final void appendServerData(NbtCompound data, IServerAccessor<IronGolemEntity> accessor,
            IPluginConfig config) {
        IronGolemEntity golem = accessor.getTarget();
        NbtCompound golemData = new NbtCompound();

        @Nullable
        UUID angryAt = golem.getAngryAt();
        golemData.putUuid(DataKeys.IRON_GOLEM_ANGRY_AT, angryAt);

        data.put(DataKeys.REPUTATION_MOD_DATA, golemData);
    }

    private static IronGolemCache.Data getIronGolemData(NbtCompound data, PlayerEntity player, IronGolemEntity golem) {
        Cache<IronGolemEntity, IronGolemCache.Data> golemCache = IronGolemCache.getOrCreate(player);
        IronGolemCache.Data golemData = Optional
                .ofNullable(golemCache.getIfPresent(golem))
                .orElse(new IronGolemCache.Data());

        @Nullable
        UUID angryAt = data.contains(DataKeys.IRON_GOLEM_ANGRY_AT)
                ? data.getUuid(DataKeys.IRON_GOLEM_ANGRY_AT)
                : null;
        golemData.setAngryAt(angryAt);

        golemCache.put(golem, golemData);

        return golemData;
    }
}
