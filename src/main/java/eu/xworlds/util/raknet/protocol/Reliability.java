package eu.xworlds.util.raknet.protocol;

public enum Reliability {
    UNRELIABLE((byte) 0, false, false, false, false),
    UNRELIABLE_SEQUENCED((byte) 1, false, false, true, false),
    RELIABLE((byte) 2, true, false, false, false),
    RELIABLE_ORDERED((byte) 3, true, true, false, false),
    RELIABLE_SEQUENCED((byte) 4, true, false, true, false),
    UNRELIABLE_WITH_ACK_RECEIPT((byte) 5, false, false, false, true),
    UNRELIABLE_SEQUENCED_WITH_ACK_RECEIPT((byte) 6, false, false, true, true),
    RELIABLE_WITH_ACK_RECEIPT((byte) 7, true, false, false, true),
    RELIABLE_ORDERED_WITH_ACK_RECEIPT((byte) 8, true, true, false, true),
    RELIABLE_SEQUENCED_WITH_ACK_RECEIPT((byte) 9, true, false, true, true);

    private final byte reliability;
    private final boolean reliable;
    private final boolean ordered;
    private final boolean sequenced;
    private final boolean requiresAck;

    Reliability(byte reliability, boolean reliable, boolean ordered, boolean sequenced,
                boolean requiresAck) {
        this.reliability = reliability;
        this.reliable = reliable;
        this.ordered = ordered;
        this.sequenced = sequenced;
        this.requiresAck = requiresAck;
    }

    public static Reliability of(byte reliability) {
        if (reliability < 0 || reliability >= Reliability.values().length) {
            return null;
        }
        return Reliability.values()[reliability];
    }

    public byte asByte() {
        return this.reliability;
    }

    public boolean isReliable() {
        return this.reliable;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public boolean isSequenced() {
        return this.sequenced;
    }

    public boolean requiresAck() {
        return this.requiresAck;
    }
}
