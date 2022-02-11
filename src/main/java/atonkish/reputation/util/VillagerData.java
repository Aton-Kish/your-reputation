package atonkish.reputation.util;

import javax.annotation.Nullable;

public class VillagerData {
    @Nullable
    private Integer reputation;
    private boolean snitch;

    public VillagerData(@Nullable Integer reputation, boolean snitch) {
        this.reputation = reputation;
        this.snitch = snitch;
    }

    public VillagerData() {
        this(null, false);
    }

    @Nullable
    public Integer getReputation() {
        return this.reputation;
    }

    public boolean isSnitch() {
        return this.snitch;
    }

    public void setReputation(@Nullable Integer reputation) {
        this.reputation = reputation;
    }

    public void setSnitch(boolean snitch) {
        this.snitch = snitch;
    }
}
