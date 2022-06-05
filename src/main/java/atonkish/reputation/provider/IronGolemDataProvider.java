package atonkish.reputation.provider;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.nbt.NbtCompound;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IServerDataProvider;

import atonkish.reputation.ReputationMod;

public class IronGolemDataProvider implements IServerDataProvider<IronGolemEntity> {
    @Override
    public final void appendServerData(NbtCompound data, IServerAccessor<IronGolemEntity> accessor, IPluginConfig config) {
        IronGolemEntity golem = accessor.getTarget();
        NbtCompound golemData = new NbtCompound();

        @Nullable
        UUID angryAt = golem.getAngryAt();
        golemData.putString(ReputationMod.IRON_GOLEM_ANGRY_AT_DATA, angryAt != null ? angryAt.toString() : "");

        data.put(ReputationMod.REPUTATION_CUSTOM_DATA_KEY, golemData);
    }
}
