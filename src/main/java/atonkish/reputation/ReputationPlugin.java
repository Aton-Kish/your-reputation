package atonkish.reputation;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import atonkish.reputation.provider.IronGolemProvider;
import atonkish.reputation.provider.VillagerReputationProvider;
import atonkish.reputation.provider.VillagerSnitchProvider;

public class ReputationPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        // Client Side
        registrar.addComponent(IronGolemProvider.INSTANCE, TooltipPosition.BODY, IronGolemEntity.class);
        registrar.addComponent(VillagerReputationProvider.INSTANCE, TooltipPosition.BODY, VillagerEntity.class);
        registrar.addComponent(VillagerSnitchProvider.INSTANCE, TooltipPosition.BODY, VillagerEntity.class);

        // Server Side
        registrar.addEntityData(IronGolemProvider.INSTANCE, IronGolemEntity.class);
        registrar.addEntityData(VillagerReputationProvider.INSTANCE, VillagerEntity.class);
        registrar.addEntityData(VillagerSnitchProvider.INSTANCE, VillagerEntity.class);
    }
}
