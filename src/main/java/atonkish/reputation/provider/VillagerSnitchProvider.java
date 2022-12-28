package atonkish.reputation.provider;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.entity.passive.VillagerEntityInterface;
import atonkish.reputation.util.cache.VillagerCache;

public enum VillagerSnitchProvider implements IEntityComponentProvider, IServerDataProvider<Entity> {

    INSTANCE;

    public static final Identifier VILLAGER_SNITCH_IDENTIFIER = new Identifier(ReputationMod.MOD_ID, "villager_snitch");
    public static final String IS_SNITCH_KEY = "ReputationModIsSnitch";

    @Override
    public Identifier getUid() {
        return VillagerSnitchProvider.VILLAGER_SNITCH_IDENTIFIER;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD - 1;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        NbtCompound data = accessor.getServerData();
        PlayerEntity player = accessor.getPlayer();
        VillagerEntity villager = (VillagerEntity) accessor.getEntity();

        VillagerCache.Data villagerData = VillagerSnitchProvider.getVillagerData(data, player, villager);

        IWailaConfig wailaConfig = config.getWailaConfig();

        Text name = Optional
                .ofNullable(villager.getCustomName())
                .orElse(villager.getType().getName());

        Text text = wailaConfig.getFormatting().title(name);
        if (villagerData.isSnitch()) {
            String snitchTranslateKey = String.format("entity.%s.villager.snitch",
                    ReputationMod.MOD_ID);
            MutableText mText = Text.empty();
            mText = mText.append(text.copy().formatted(Formatting.STRIKETHROUGH));
            mText = mText.append(" ");
            mText = mText.append(Text.translatable(snitchTranslateKey).formatted(Formatting.DARK_RED));

            text = mText;
        }

        tooltip.add(text);
    }

    @Override
    public final void appendServerData(NbtCompound data, ServerPlayerEntity player, World world, Entity entity,
            boolean showDetails) {
        VillagerEntity villager = (VillagerEntity) entity;

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
