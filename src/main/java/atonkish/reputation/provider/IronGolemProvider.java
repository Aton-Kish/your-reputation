package atonkish.reputation.provider;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.cache.Cache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
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

import atonkish.reputation.ReputationMod;
import atonkish.reputation.util.cache.IronGolemCache;

public enum IronGolemProvider implements IEntityComponentProvider, IServerDataProvider<Entity> {

    INSTANCE;

    public static final Identifier IRON_GOLEM_IDENTIFIER = new Identifier(ReputationMod.MOD_ID, "iron_golem");
    public static final String ANGRY_AT_KEY = "ReputationModAngryAt";

    @Override
    public Identifier getUid() {
        return IronGolemProvider.IRON_GOLEM_IDENTIFIER;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.BODY + 100;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        NbtCompound data = accessor.getServerData();
        PlayerEntity player = accessor.getPlayer();
        IronGolemEntity golem = (IronGolemEntity) accessor.getEntity();

        IronGolemCache.Data golemData = IronGolemProvider.getIronGolemData(data, player, golem);

        @Nullable
        UUID angryAt = golemData.getAngryAt();

        if (player.getUuid().equals(angryAt)) {
            String angryTranslateKey = String.format("entity.%s.iron_golem.angry", ReputationMod.MOD_ID);
            MutableText text = Text.translatable(angryTranslateKey).formatted(Formatting.DARK_RED);
            tooltip.add(text);
        }
    }

    @Override
    public final void appendServerData(NbtCompound data, ServerPlayerEntity player, World world, Entity entity,
            boolean showDetails) {
        IronGolemEntity golem = (IronGolemEntity) entity;

        @Nullable
        UUID angryAt = golem.getAngryAt();
        if (angryAt != null) {
            data.putUuid(IronGolemProvider.ANGRY_AT_KEY, angryAt);
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
