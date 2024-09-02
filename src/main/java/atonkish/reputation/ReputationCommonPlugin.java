package atonkish.reputation;

import mcp.mobius.waila.api.ICommonRegistrar;
import mcp.mobius.waila.api.IWailaCommonPlugin;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import atonkish.reputation.provider.IronGolemProvider;
import atonkish.reputation.provider.VillagerReputationProvider;
import atonkish.reputation.provider.VillagerSnitchProvider;

public class ReputationCommonPlugin implements IWailaCommonPlugin {
    @Override
    public void register(ICommonRegistrar registrar) {
        registrar.entityData(IronGolemProvider.INSTANCE, IronGolemEntity.class);
        registrar.entityData(VillagerReputationProvider.INSTANCE, VillagerEntity.class);
        registrar.entityData(VillagerSnitchProvider.INSTANCE, VillagerEntity.class);
    }
}
