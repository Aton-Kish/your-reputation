package atonkish.reputation.nbt;

import java.util.UUID;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.util.Uuids;

public final class ModNbtHelper {
    public static NbtIntArray fromUuid(UUID uuid) {
        return new NbtIntArray(Uuids.toIntArray(uuid));
    }

    public static UUID toUuid(NbtElement element) {
        if (element.getNbtType() != NbtIntArray.TYPE) {
            throw new IllegalArgumentException(
                    "Expected UUID-Tag to be of type " + NbtIntArray.TYPE.getCrashReportName() + ", but found "
                            + element.getNbtType().getCrashReportName() + ".");
        } else {
            int[] is = ((NbtIntArray) element).getIntArray();
            if (is.length != 4) {
                throw new IllegalArgumentException(
                        "Expected UUID-Array to be of length 4, but found " + is.length + ".");
            } else {
                return Uuids.toUuid(is);
            }
        }
    }
}
