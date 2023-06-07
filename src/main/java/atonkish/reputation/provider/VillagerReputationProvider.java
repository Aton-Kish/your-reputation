package atonkish.reputation.provider;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import atonkish.reputation.util.ReputationStatus;
import atonkish.reputation.util.cache.VillagerCache;

public enum VillagerReputationProvider implements IEntityComponentProvider, IDataProvider<VillagerEntity> {

    INSTANCE;

    public static final String REPUTATION_KEY = "ReputationModReputation";

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        NbtCompound data = accessor.getData().raw();
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();

        VillagerCache.Data villagerData = VillagerReputationProvider.getVillagerData(data, player, villager);

        @Nullable
        Integer reputation = villagerData.getReputation();
        ReputationStatus status = ReputationStatus.getStatus(reputation);

        MutableText text = Text.translatable(status.getTranslateKey());

        if (reputation != null) {
            text = text.append(String.format(" (%d)", reputation));
        }

        text = text.formatted(status.getFormatting());

        tooltip.addLine(text);
    }

    @Override
    public final void appendData(IDataWriter data, IServerAccessor<VillagerEntity> accessor,
            IPluginConfig config) {
        ServerPlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getTarget();

        int reputation = villager.getReputation(player);
        data.raw().putInt(VillagerReputationProvider.REPUTATION_KEY, reputation);
    }

    private static VillagerCache.Data getVillagerData(NbtCompound data, PlayerEntity player, VillagerEntity villager) {
        Cache<VillagerEntity, VillagerCache.Data> villagerCache = VillagerCache.getOrCreate(player);
        VillagerCache.Data villagerData = Optional
                .ofNullable(villagerCache.getIfPresent(villager))
                .orElse(new VillagerCache.Data());

        @Nullable
        Integer reputation = data.contains(VillagerReputationProvider.REPUTATION_KEY)
                ? data.getInt(VillagerReputationProvider.REPUTATION_KEY)
                : null;
        if (reputation != null) {
            villagerData.setReputation(reputation);
        }

        villagerCache.put(villager, villagerData);

        return villagerData;
    }
}
