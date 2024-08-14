package kotlin.time;

import kotlin.Metadata;
import kotlin.time.Duration;

@Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0000\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a\"\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0000ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\"\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002ø\u0001\u0000¢\u0006\u0004\b\f\u0010\n\u001a \u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0000ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a \u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a \u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u0001H\u0000ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a\r\u0010\u0016\u001a\u00020\u0017*\u00020\u0001H\b\u0002\u0004\n\u0002\b\u0019¨\u0006\u0018"}, d2 = {"checkInfiniteSumDefined", "", "longNs", "duration", "Lkotlin/time/Duration;", "durationNs", "checkInfiniteSumDefined-PjuGub4", "(JJJ)J", "saturatingAdd", "saturatingAdd-pTJri5U", "(JJ)J", "saturatingAddInHalves", "saturatingAddInHalves-pTJri5U", "saturatingDiff", "valueNs", "originNs", "saturatingFiniteDiff", "value1Ns", "value2Ns", "saturatingOriginsDiff", "origin1Ns", "origin2Ns", "isSaturated", "", "kotlin-stdlib"}, k = 2, mv = {1, 8, 0}, xi = 48)
/* compiled from: longSaturatedMath.kt */
public final class LongSaturatedMathKt {
    /* renamed from: saturatingAdd-pTJri5U  reason: not valid java name */
    public static final long m1644saturatingAddpTJri5U(long longNs, long duration) {
        long durationNs = Duration.m1536getInWholeNanosecondsimpl(duration);
        boolean z = true;
        if (((longNs - 1) | 1) == Long.MAX_VALUE) {
            return m1643checkInfiniteSumDefinedPjuGub4(longNs, duration, durationNs);
        }
        if ((1 | (durationNs - 1)) != Long.MAX_VALUE) {
            z = false;
        }
        if (z) {
            return m1645saturatingAddInHalvespTJri5U(longNs, duration);
        }
        long result = longNs + durationNs;
        if (((longNs ^ result) & (durationNs ^ result)) >= 0) {
            return result;
        }
        if (longNs < 0) {
            return Long.MIN_VALUE;
        }
        return Long.MAX_VALUE;
    }

    /* renamed from: checkInfiniteSumDefined-PjuGub4  reason: not valid java name */
    private static final long m1643checkInfiniteSumDefinedPjuGub4(long longNs, long duration, long durationNs) {
        if (!Duration.m1548isInfiniteimpl(duration) || (longNs ^ durationNs) >= 0) {
            return longNs;
        }
        throw new IllegalArgumentException("Summing infinities of different signs");
    }

    /* renamed from: saturatingAddInHalves-pTJri5U  reason: not valid java name */
    private static final long m1645saturatingAddInHalvespTJri5U(long longNs, long duration) {
        long half = Duration.m1519divUwyO8pc(duration, 2);
        if ((1 | (Duration.m1536getInWholeNanosecondsimpl(half) - 1)) == Long.MAX_VALUE) {
            return (long) (((double) longNs) + Duration.m1559toDoubleimpl(duration, DurationUnit.NANOSECONDS));
        }
        return m1644saturatingAddpTJri5U(m1644saturatingAddpTJri5U(longNs, half), Duration.m1551minusLRDsOJo(duration, half));
    }

    public static final long saturatingDiff(long valueNs, long originNs) {
        if ((1 | (originNs - 1)) == Long.MAX_VALUE) {
            return Duration.m1568unaryMinusUwyO8pc(DurationKt.toDuration(originNs, DurationUnit.DAYS));
        }
        return saturatingFiniteDiff(valueNs, originNs);
    }

    public static final long saturatingOriginsDiff(long origin1Ns, long origin2Ns) {
        boolean z = true;
        if (!(((origin2Ns - 1) | 1) == Long.MAX_VALUE)) {
            if ((1 | (origin1Ns - 1)) != Long.MAX_VALUE) {
                z = false;
            }
            if (z) {
                return DurationKt.toDuration(origin1Ns, DurationUnit.DAYS);
            }
            return saturatingFiniteDiff(origin1Ns, origin2Ns);
        } else if (origin1Ns == origin2Ns) {
            return Duration.Companion.m1618getZEROUwyO8pc();
        } else {
            return Duration.m1568unaryMinusUwyO8pc(DurationKt.toDuration(origin2Ns, DurationUnit.DAYS));
        }
    }

    private static final long saturatingFiniteDiff(long value1Ns, long value2Ns) {
        long result = value1Ns - value2Ns;
        if (((result ^ value1Ns) & (~(result ^ value2Ns))) < 0) {
            long j = (long) DurationKt.NANOS_IN_MILLIS;
            long resultMs = (value1Ns / j) - (value2Ns / j);
            long resultNs = (value1Ns % j) - (value2Ns % j);
            Duration.Companion companion = Duration.Companion;
            long duration = DurationKt.toDuration(resultMs, DurationUnit.MILLISECONDS);
            Duration.Companion companion2 = Duration.Companion;
            return Duration.m1552plusLRDsOJo(duration, DurationKt.toDuration(resultNs, DurationUnit.NANOSECONDS));
        }
        Duration.Companion companion3 = Duration.Companion;
        return DurationKt.toDuration(result, DurationUnit.NANOSECONDS);
    }

    private static final boolean isSaturated(long $this$isSaturated) {
        return (1 | ($this$isSaturated - 1)) == Long.MAX_VALUE;
    }
}
