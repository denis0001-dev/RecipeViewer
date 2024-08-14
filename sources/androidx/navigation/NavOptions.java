package androidx.navigation;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u001a\u0018\u00002\u00020\u0001:\u0001#BQ\b\u0010\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n\u0012\u0006\u0010\r\u001a\u00020\n¢\u0006\u0002\u0010\u000eBY\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\b\b\u0001\u0010\t\u001a\u00020\n\u0012\b\b\u0001\u0010\u000b\u001a\u00020\n\u0012\b\b\u0001\u0010\f\u001a\u00020\n\u0012\b\b\u0001\u0010\r\u001a\u00020\n¢\u0006\u0002\u0010\u0010J\u0013\u0010\u001a\u001a\u00020\u00032\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010\u001c\u001a\u00020\nH\u0007J\b\u0010\u001d\u001a\u00020\nH\u0016J\u0006\u0010\u001e\u001a\u00020\u0003J\u0006\u0010\u001f\u001a\u00020\u0003J\u0006\u0010 \u001a\u00020\u0003J\u0006\u0010!\u001a\u00020\u0003J\b\u0010\"\u001a\u00020\u0006H\u0016R\u0013\u0010\t\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\u000b\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0013\u0010\f\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0013\u0010\r\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0014\u0010\u000f\u001a\u00020\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u000e\u0010\u0007\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0017\u001a\u0004\u0018\u00010\u0006@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\b\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Landroidx/navigation/NavOptions;", "", "singleTop", "", "restoreState", "popUpToRoute", "", "popUpToInclusive", "popUpToSaveState", "enterAnim", "", "exitAnim", "popEnterAnim", "popExitAnim", "(ZZLjava/lang/String;ZZIIII)V", "popUpToId", "(ZZIZZIIII)V", "getEnterAnim", "()I", "getExitAnim", "getPopEnterAnim", "getPopExitAnim", "getPopUpToId", "<set-?>", "getPopUpToRoute", "()Ljava/lang/String;", "equals", "other", "getPopUpTo", "hashCode", "isPopUpToInclusive", "shouldLaunchSingleTop", "shouldPopUpToSaveState", "shouldRestoreState", "toString", "Builder", "navigation-common_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
/* compiled from: NavOptions.kt */
public final class NavOptions {
    private final int enterAnim;
    private final int exitAnim;
    private final int popEnterAnim;
    private final int popExitAnim;
    private final int popUpToId;
    private final boolean popUpToInclusive;
    private String popUpToRoute;
    private final boolean popUpToSaveState;
    private final boolean restoreState;
    private final boolean singleTop;

    public NavOptions(boolean singleTop2, boolean restoreState2, int popUpToId2, boolean popUpToInclusive2, boolean popUpToSaveState2, int enterAnim2, int exitAnim2, int popEnterAnim2, int popExitAnim2) {
        this.singleTop = singleTop2;
        this.restoreState = restoreState2;
        this.popUpToId = popUpToId2;
        this.popUpToInclusive = popUpToInclusive2;
        this.popUpToSaveState = popUpToSaveState2;
        this.enterAnim = enterAnim2;
        this.exitAnim = exitAnim2;
        this.popEnterAnim = popEnterAnim2;
        this.popExitAnim = popExitAnim2;
    }

    public final int getPopUpToId() {
        return this.popUpToId;
    }

    public final int getEnterAnim() {
        return this.enterAnim;
    }

    public final int getExitAnim() {
        return this.exitAnim;
    }

    public final int getPopEnterAnim() {
        return this.popEnterAnim;
    }

    public final int getPopExitAnim() {
        return this.popExitAnim;
    }

    @Deprecated(message = "Use popUpToId instead.", replaceWith = @ReplaceWith(expression = "popUpToId", imports = {}))
    public final int getPopUpTo() {
        return this.popUpToId;
    }

    public final String getPopUpToRoute() {
        return this.popUpToRoute;
    }

    public NavOptions(boolean singleTop2, boolean restoreState2, String popUpToRoute2, boolean popUpToInclusive2, boolean popUpToSaveState2, int enterAnim2, int exitAnim2, int popEnterAnim2, int popExitAnim2) {
        this(singleTop2, restoreState2, NavDestination.Companion.createRoute(popUpToRoute2).hashCode(), popUpToInclusive2, popUpToSaveState2, enterAnim2, exitAnim2, popEnterAnim2, popExitAnim2);
        this.popUpToRoute = popUpToRoute2;
    }

    public final boolean shouldLaunchSingleTop() {
        return this.singleTop;
    }

    public final boolean shouldRestoreState() {
        return this.restoreState;
    }

    public final boolean isPopUpToInclusive() {
        return this.popUpToInclusive;
    }

    public final boolean shouldPopUpToSaveState() {
        return this.popUpToSaveState;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof NavOptions)) {
            return false;
        }
        if (this.singleTop == ((NavOptions) other).singleTop && this.restoreState == ((NavOptions) other).restoreState && this.popUpToId == ((NavOptions) other).popUpToId && Intrinsics.areEqual((Object) this.popUpToRoute, (Object) ((NavOptions) other).popUpToRoute) && this.popUpToInclusive == ((NavOptions) other).popUpToInclusive && this.popUpToSaveState == ((NavOptions) other).popUpToSaveState && this.enterAnim == ((NavOptions) other).enterAnim && this.exitAnim == ((NavOptions) other).exitAnim && this.popEnterAnim == ((NavOptions) other).popEnterAnim && this.popExitAnim == ((NavOptions) other).popExitAnim) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = ((((((int) shouldLaunchSingleTop()) * true) + (shouldRestoreState() ? 1 : 0)) * 31) + this.popUpToId) * 31;
        String str = this.popUpToRoute;
        return ((((((((((((result + (str != null ? str.hashCode() : 0)) * 31) + (isPopUpToInclusive() ? 1 : 0)) * 31) + (shouldPopUpToSaveState() ? 1 : 0)) * 31) + this.enterAnim) * 31) + this.exitAnim) * 31) + this.popEnterAnim) * 31) + this.popExitAnim;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(");
        if (this.singleTop) {
            sb.append("launchSingleTop ");
        }
        if (this.restoreState) {
            sb.append("restoreState ");
        }
        if (!((this.popUpToRoute == null && this.popUpToId == -1) || this.popUpToRoute == null)) {
            sb.append("popUpTo(");
            if (this.popUpToRoute != null) {
                sb.append(this.popUpToRoute);
            } else {
                sb.append("0x");
                sb.append(Integer.toHexString(this.popUpToId));
            }
            if (this.popUpToInclusive) {
                sb.append(" inclusive");
            }
            if (this.popUpToSaveState) {
                sb.append(" saveState");
            }
            sb.append(")");
        }
        if (!(this.enterAnim == -1 && this.exitAnim == -1 && this.popEnterAnim == -1 && this.popExitAnim == -1)) {
            sb.append("anim(enterAnim=0x");
            sb.append(Integer.toHexString(this.enterAnim));
            sb.append(" exitAnim=0x");
            sb.append(Integer.toHexString(this.exitAnim));
            sb.append(" popEnterAnim=0x");
            sb.append(Integer.toHexString(this.popEnterAnim));
            sb.append(" popExitAnim=0x");
            sb.append(Integer.toHexString(this.popExitAnim));
            sb.append(")");
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u0012\u001a\u00020\u00002\b\b\u0001\u0010\u0003\u001a\u00020\u0004J\u0010\u0010\u0013\u001a\u00020\u00002\b\b\u0001\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\nJ\u0010\u0010\u0015\u001a\u00020\u00002\b\b\u0001\u0010\u0006\u001a\u00020\u0004J\u0010\u0010\u0016\u001a\u00020\u00002\b\b\u0001\u0010\u0007\u001a\u00020\u0004J$\u0010\u0017\u001a\u00020\u00002\b\b\u0001\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\nH\u0007J$\u0010\u0017\u001a\u00020\u00002\b\u0010\u001b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0019\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\nH\u0007J\u000e\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\nR\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Landroidx/navigation/NavOptions$Builder;", "", "()V", "enterAnim", "", "exitAnim", "popEnterAnim", "popExitAnim", "popUpToId", "popUpToInclusive", "", "popUpToRoute", "", "popUpToSaveState", "restoreState", "singleTop", "build", "Landroidx/navigation/NavOptions;", "setEnterAnim", "setExitAnim", "setLaunchSingleTop", "setPopEnterAnim", "setPopExitAnim", "setPopUpTo", "destinationId", "inclusive", "saveState", "route", "setRestoreState", "navigation-common_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    /* compiled from: NavOptions.kt */
    public static final class Builder {
        private int enterAnim = -1;
        private int exitAnim = -1;
        private int popEnterAnim = -1;
        private int popExitAnim = -1;
        private int popUpToId = -1;
        private boolean popUpToInclusive;
        private String popUpToRoute;
        private boolean popUpToSaveState;
        private boolean restoreState;
        private boolean singleTop;

        public final Builder setPopUpTo(int i, boolean z) {
            return setPopUpTo$default(this, i, z, false, 4, (Object) null);
        }

        public final Builder setPopUpTo(String str, boolean z) {
            return setPopUpTo$default(this, str, z, false, 4, (Object) null);
        }

        public final Builder setLaunchSingleTop(boolean singleTop2) {
            this.singleTop = singleTop2;
            return this;
        }

        public final Builder setRestoreState(boolean restoreState2) {
            this.restoreState = restoreState2;
            return this;
        }

        public static /* synthetic */ Builder setPopUpTo$default(Builder builder, int i, boolean z, boolean z2, int i2, Object obj) {
            if ((i2 & 4) != 0) {
                z2 = false;
            }
            return builder.setPopUpTo(i, z, z2);
        }

        public final Builder setPopUpTo(int destinationId, boolean inclusive, boolean saveState) {
            this.popUpToId = destinationId;
            this.popUpToRoute = null;
            this.popUpToInclusive = inclusive;
            this.popUpToSaveState = saveState;
            return this;
        }

        public static /* synthetic */ Builder setPopUpTo$default(Builder builder, String str, boolean z, boolean z2, int i, Object obj) {
            if ((i & 4) != 0) {
                z2 = false;
            }
            return builder.setPopUpTo(str, z, z2);
        }

        public final Builder setPopUpTo(String route, boolean inclusive, boolean saveState) {
            this.popUpToRoute = route;
            this.popUpToId = -1;
            this.popUpToInclusive = inclusive;
            this.popUpToSaveState = saveState;
            return this;
        }

        public final Builder setEnterAnim(int enterAnim2) {
            this.enterAnim = enterAnim2;
            return this;
        }

        public final Builder setExitAnim(int exitAnim2) {
            this.exitAnim = exitAnim2;
            return this;
        }

        public final Builder setPopEnterAnim(int popEnterAnim2) {
            this.popEnterAnim = popEnterAnim2;
            return this;
        }

        public final Builder setPopExitAnim(int popExitAnim2) {
            this.popExitAnim = popExitAnim2;
            return this;
        }

        public final NavOptions build() {
            if (this.popUpToRoute != null) {
                return new NavOptions(this.singleTop, this.restoreState, this.popUpToRoute, this.popUpToInclusive, this.popUpToSaveState, this.enterAnim, this.exitAnim, this.popEnterAnim, this.popExitAnim);
            }
            return new NavOptions(this.singleTop, this.restoreState, this.popUpToId, this.popUpToInclusive, this.popUpToSaveState, this.enterAnim, this.exitAnim, this.popEnterAnim, this.popExitAnim);
        }
    }
}
