package atonkish.reputation;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;

import atonkish.reputation.provider.IronGolemProvider;
import atonkish.reputation.provider.VillagerProvider;

public class ReputationPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        // Client Side
        registrar.addComponent(IronGolemProvider.INSTANCE, TooltipPosition.BODY, IronGolemEntity.class);
        registrar.addComponent(VillagerProvider.INSTANCE, TooltipPosition.HEAD, VillagerEntity.class);
        registrar.addComponent(VillagerProvider.INSTANCE, TooltipPosition.BODY, VillagerEntity.class);

        // Server Side
        registrar.addEntityData(IronGolemProvider.INSTANCE, IronGolemEntity.class);
        registrar.addEntityData(VillagerProvider.INSTANCE, VillagerEntity.class);
    }
}
