package atonkish.reputation;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;

import atonkish.reputation.provider.IronGolemComponentProvider;
import atonkish.reputation.provider.IronGolemDataProvider;
import atonkish.reputation.provider.VillagerComponentProvider;
import atonkish.reputation.provider.VillagerDataProvider;

public class ReputationPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new VillagerComponentProvider(), TooltipPosition.HEAD, VillagerEntity.class);
        registrar.addComponent(new VillagerComponentProvider(), TooltipPosition.BODY, VillagerEntity.class);
        registrar.addEntityData(new VillagerDataProvider(), VillagerEntity.class);

        registrar.addComponent(new IronGolemComponentProvider(), TooltipPosition.BODY, IronGolemEntity.class);
        registrar.addEntityData(new IronGolemDataProvider(), IronGolemEntity.class);
    }
}
