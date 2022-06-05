package atonkish.reputation.provider;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IServerDataProvider;

import atonkish.reputation.ReputationMod;
import atonkish.reputation.entity.passive.VillagerEntityInterface;

public class VillagerDataProvider implements IServerDataProvider<VillagerEntity> {
    @Override
    public final void appendServerData(NbtCompound data, IServerAccessor<VillagerEntity> accessor, IPluginConfig config) {
        ServerPlayerEntity player = accessor.getPlayer();

        VillagerEntity villager = accessor.getTarget();
        NbtCompound villagerData = new NbtCompound();

        int reputation = villager.getReputation(player);
        villagerData.putInt(ReputationMod.VILLAGER_REPUTATION_KEY, reputation);

        boolean isSnitch = ((VillagerEntityInterface) villager).isSnitch(player);
        villagerData.putBoolean(ReputationMod.VILLAGER_IS_SNITCH_KEY, isSnitch);

        data.put(ReputationMod.REPUTATION_CUSTOM_DATA_KEY, villagerData);
    }
}
