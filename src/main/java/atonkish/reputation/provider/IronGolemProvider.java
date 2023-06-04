package atonkish.reputation.provider;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.cache.IronGolemCache;

public enum IronGolemProvider implements IEntityComponentProvider, IDataProvider<IronGolemEntity> {

    INSTANCE;

    public static final String ANGRY_AT_KEY = "ReputationModAngryAt";

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        NbtCompound data = accessor.getData().raw();
        PlayerEntity player = accessor.getPlayer();
        IronGolemEntity golem = accessor.getEntity();

        IronGolemCache.Data golemData = IronGolemProvider.getIronGolemData(data, player, golem);

        @Nullable
        UUID angryAt = golemData.getAngryAt();

        if (player.getUuid().equals(angryAt)) {
            String angryTranslateKey = String.format("entity.%s.iron_golem.angry", ReputationMod.MOD_ID);
            MutableText text = Text.translatable(angryTranslateKey).formatted(Formatting.DARK_RED);
            tooltip.addLine(text);
        }
    }

    @Override
    public final void appendData(IDataWriter data, IServerAccessor<IronGolemEntity> accessor,
            IPluginConfig config) {
        IronGolemEntity golem = accessor.getTarget();

        @Nullable
        UUID angryAt = golem.getAngryAt();
        if (angryAt != null) {
            data.raw().putUuid(IronGolemProvider.ANGRY_AT_KEY, angryAt);
        }
    }

    private static IronGolemCache.Data getIronGolemData(NbtCompound data, PlayerEntity player, IronGolemEntity golem) {
        Cache<IronGolemEntity, IronGolemCache.Data> golemCache = IronGolemCache.getOrCreate(player);
        IronGolemCache.Data golemData = Optional
                .ofNullable(golemCache.getIfPresent(golem))
                .orElse(new IronGolemCache.Data());

        @Nullable
        UUID angryAt = data.contains(IronGolemProvider.ANGRY_AT_KEY)
                ? data.getUuid(IronGolemProvider.ANGRY_AT_KEY)
                : null;
        golemData.setAngryAt(angryAt);

        golemCache.put(golem, golemData);

        return golemData;
    }
}
