package atonkish.reputation.provider;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.WailaConstants;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.VillagerCache;
import atonkish.reputation.util.ReputationStatus;

public class VillagerComponentProvider implements IEntityComponentProvider {
    @Override
    public void appendHead(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();
        NbtCompound data = accessor.getServerData().getCompound(ReputationMod.REPUTATION_CUSTOM_DATA_KEY);

        VillagerCache.Data villagerData = VillagerComponentProvider.getVillagerData(player, villager, data);

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
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = accessor.getEntity();
        NbtCompound data = accessor.getServerData().getCompound(ReputationMod.REPUTATION_CUSTOM_DATA_KEY);

        VillagerCache.Data villagerData = VillagerComponentProvider.getVillagerData(player, villager, data);

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

    private static VillagerCache.Data getVillagerData(PlayerEntity player, VillagerEntity villager, NbtCompound data) {
        Cache<VillagerEntity, VillagerCache.Data> villagerCache = VillagerCache.getOrCreate(player);
        VillagerCache.Data villagerData = Optional
                .ofNullable(villagerCache.getIfPresent(villager))
                .orElse(new VillagerCache.Data());

        @Nullable
        Integer reputation = data.contains(ReputationMod.VILLAGER_REPUTATION_KEY)
                ? data.getInt(ReputationMod.VILLAGER_REPUTATION_KEY)
                : null;
        if (reputation != null) {
            villagerData.setReputation(reputation);
        }

        @Nullable
        Boolean isSnitch = data.contains(ReputationMod.VILLAGER_IS_SNITCH_KEY)
                ? data.getBoolean(ReputationMod.VILLAGER_IS_SNITCH_KEY)
                : null;
        if (isSnitch != null) {
            villagerData.setIsSnitch(isSnitch);
        }

        villagerCache.put(villager, villagerData);

        return villagerData;
    }
}
