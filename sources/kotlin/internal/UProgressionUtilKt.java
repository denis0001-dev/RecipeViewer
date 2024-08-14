package kotlin.internal;

import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport1;
import kotlin.UByte$$ExternalSyntheticBackport2;
import kotlin.UInt;
import kotlin.ULong;

@Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, k = 2, mv = {1, 8, 0}, xi = 48)
/* compiled from: UProgressionUtil.kt */
public final class UProgressionUtilKt {
    /* renamed from: differenceModulo-WZ9TVnA  reason: not valid java name */
    private static final int m1397differenceModuloWZ9TVnA(int a, int b, int c) {
        int ac = UByte$$ExternalSyntheticBackport1.m(a, c);
        int bc = UByte$$ExternalSyntheticBackport1.m(b, c);
        return UInt.m279constructorimpl(Integer.compare(ac ^ Integer.MIN_VALUE, bc ^ Integer.MIN_VALUE) >= 0 ? ac - bc : UInt.m279constructorimpl(ac - bc) + c);
    }

    /* renamed from: differenceModulo-sambcqE  reason: not valid java name */
    private static final long m1398differenceModulosambcqE(long a, long b, long c) {
        long ac = UByte$$ExternalSyntheticBackport2.m(a, c);
        long bc = UByte$$ExternalSyntheticBackport2.m(b, c);
        return ULong.m358constructorimpl(Long.compare(ac ^ Long.MIN_VALUE, bc ^ Long.MIN_VALUE) >= 0 ? ac - bc : ULong.m358constructorimpl(ac - bc) + c);
    }

    /* renamed from: getProgressionLastElement-Nkh28Cs  reason: not valid java name */
    public static final int m1400getProgressionLastElementNkh28Cs(int start, int end, int step) {
        if (step > 0) {
            if (Integer.compare(start ^ Integer.MIN_VALUE, end ^ Integer.MIN_VALUE) < 0) {
                return UInt.m279constructorimpl(end - m1397differenceModuloWZ9TVnA(end, start, UInt.m279constructorimpl(step)));
            }
        } else if (step >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (Integer.compare(start ^ Integer.MIN_VALUE, end ^ Integer.MIN_VALUE) > 0) {
            return UInt.m279constructorimpl(m1397differenceModuloWZ9TVnA(start, end, UInt.m279constructorimpl(-step)) + end);
        }
        return end;
    }

    /* renamed from: getProgressionLastElement-7ftBX0g  reason: not valid java name */
    public static final long m1399getProgressionLastElement7ftBX0g(long start, long end, long step) {
        if (step > 0) {
            if (Long.compare(start ^ Long.MIN_VALUE, end ^ Long.MIN_VALUE) < 0) {
                return ULong.m358constructorimpl(end - m1398differenceModulosambcqE(end, start, ULong.m358constructorimpl(step)));
            }
        } else if (step >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (Long.compare(start ^ Long.MIN_VALUE, end ^ Long.MIN_VALUE) > 0) {
            return ULong.m358constructorimpl(m1398differenceModulosambcqE(start, end, ULong.m358constructorimpl(-step)) + end);
        }
        return end;
    }
}
