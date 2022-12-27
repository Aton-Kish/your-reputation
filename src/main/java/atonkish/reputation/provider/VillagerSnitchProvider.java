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
import mcp.mobius.waila.api.IWailaConfig;
import mcp.mobius.waila.api.WailaConstants;
import mcp.mobius.waila.mixin.EntityAccess;

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

        @Nullable
        Text customName = villager.getCustomName();
        String name = customName != null
                ? String.format("%s (%s)", customName.getString(),
                        ((EntityAccess) villager).wthit_getTypeName().getString())
                : villager.getName().getString();

        IWailaConfig.Formatter formatter = IWailaConfig.get().getFormatter();
        Text text = formatter.entityName(name);
        if (villagerData.isSnitch()) {
            String snitchTranslateKey = String.format("entity.%s.villager.snitch", ReputationMod.MOD_ID);
            MutableText mText = Text.empty();
            mText = mText.append(Text.literal(name).formatted(Formatting.WHITE, Formatting.STRIKETHROUGH));
            mText = mText.append(" ");
            mText = mText.append(Text.translatable(snitchTranslateKey).formatted(Formatting.DARK_RED));

            text = mText;
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
