package atonkish.reputation.provider;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.WailaConstants;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.entity.passive.VillagerEntityInterface;
import atonkish.reputation.util.cache.VillagerCache;

public enum VillagerSnitchProvider implements IEntityComponentProvider, IServerDataProvider<VillagerEntity> {

    INSTANCE;

    public static final String IS_SNITCH_KEY = "ReputationModIsSnitch";

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        NbtCompound data = accessor.getServerData();
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();

        VillagerCache.Data villagerData = VillagerSnitchProvider.getVillagerData(data, player, villager);

        MutableText text = Text.literal("");
        if (villagerData.isSnitch()) {
            text = text.append(Text.literal(villager.getDisplayName().getString())
                    .formatted(Formatting.WHITE, Formatting.STRIKETHROUGH));
            text = text.append(" ");
            text = text.append(Text.translatable("entity." + ReputationMod.MOD_ID + ".villager.snitch")
                    .formatted(Formatting.DARK_RED));
        } else {
            text = text.append(Text.literal(villager.getDisplayName().getString()).formatted(Formatting.WHITE));
        }

        tooltip.setLine(WailaConstants.OBJECT_NAME_TAG, text);
    }

    @Override
    public final void appendServerData(NbtCompound data, IServerAccessor<VillagerEntity> accessor,
            IPluginConfig config) {
        ServerPlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getTarget();

        boolean isSnitch = ((VillagerEntityInterface) villager).isSnitch(player);
        data.putBoolean(VillagerSnitchProvider.IS_SNITCH_KEY, isSnitch);
    }

    private static VillagerCache.Data getVillagerData(NbtCompound data, PlayerEntity player, VillagerEntity villager) {
        Cache<VillagerEntity, VillagerCache.Data> villagerCache = VillagerCache.getOrCreate(player);
        VillagerCache.Data villagerData = Optional
                .ofNullable(villagerCache.getIfPresent(villager))
                .orElse(new VillagerCache.Data());

        @Nullable
        Boolean isSnitch = data.contains(VillagerSnitchProvider.IS_SNITCH_KEY)
                ? data.getBoolean(VillagerSnitchProvider.IS_SNITCH_KEY)
                : null;
        if (isSnitch != null) {
            villagerData.setIsSnitch(isSnitch);
        }

        villagerCache.put(villager, villagerData);

        return villagerData;
    }
}
