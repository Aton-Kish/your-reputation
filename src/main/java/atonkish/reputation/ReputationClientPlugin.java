package atonkish.reputation;

import mcp.mobius.waila.api.IClientRegistrar;
import mcp.mobius.waila.api.IWailaClientPlugin;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import atonkish.reputation.provider.IronGolemProvider;
import atonkish.reputation.provider.VillagerReputationProvider;
import atonkish.reputation.provider.VillagerSnitchProvider;

public class ReputationClientPlugin implements IWailaClientPlugin {
    @Override
    public void register(IClientRegistrar registrar) {
        registrar.body(IronGolemProvider.INSTANCE, IronGolemEntity.class);
        registrar.body(VillagerReputationProvider.INSTANCE,  VillagerEntity.class);
        registrar.body(VillagerSnitchProvider.INSTANCE,  VillagerEntity.class);
    }
}
