package androidx.navigation;

import android.os.Bundle;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B)\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010\u0016\u001a\u00020\u0003H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012¨\u0006\u0019"}, d2 = {"Landroidx/navigation/NavAction;", "", "destinationId", "", "navOptions", "Landroidx/navigation/NavOptions;", "defaultArguments", "Landroid/os/Bundle;", "(ILandroidx/navigation/NavOptions;Landroid/os/Bundle;)V", "getDefaultArguments", "()Landroid/os/Bundle;", "setDefaultArguments", "(Landroid/os/Bundle;)V", "getDestinationId", "()I", "getNavOptions", "()Landroidx/navigation/NavOptions;", "setNavOptions", "(Landroidx/navigation/NavOptions;)V", "equals", "", "other", "hashCode", "toString", "", "navigation-common_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
/* compiled from: NavAction.kt */
public final class NavAction {
    private Bundle defaultArguments;
    private final int destinationId;
    private NavOptions navOptions;

    public NavAction(int i) {
        this(i, (NavOptions) null, (Bundle) null, 6, (DefaultConstructorMarker) null);
    }

    public NavAction(int i, NavOptions navOptions2) {
        this(i, navOptions2, (Bundle) null, 4, (DefaultConstructorMarker) null);
    }

    public NavAction(int destinationId2, NavOptions navOptions2, Bundle defaultArguments2) {
        this.destinationId = destinationId2;
        this.navOptions = navOptions2;
        this.defaultArguments = defaultArguments2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ NavAction(int i, NavOptions navOptions2, Bundle bundle, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? null : navOptions2, (i2 & 4) != 0 ? null : bundle);
    }

    public final int getDestinationId() {
        return this.destinationId;
    }

    public final NavOptions getNavOptions() {
        return this.navOptions;
    }

    public final void setNavOptions(NavOptions navOptions2) {
        this.navOptions = navOptions2;
    }

    public final Bundle getDefaultArguments() {
        return this.defaultArguments;
    }

    public final void setDefaultArguments(Bundle bundle) {
        this.defaultArguments = bundle;
    }

    public boolean equals(Object other) {
        boolean z;
        Set keySet;
        Iterable $this$all$iv;
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof NavAction)) {
            return false;
        }
        if (this.destinationId == ((NavAction) other).destinationId && Intrinsics.areEqual((Object) this.navOptions, (Object) ((NavAction) other).navOptions)) {
            if (Intrinsics.areEqual((Object) this.defaultArguments, (Object) ((NavAction) other).defaultArguments)) {
                return true;
            }
            Bundle bundle = this.defaultArguments;
            if (bundle == null || (keySet = bundle.keySet()) == null) {
                z = false;
            } else {
                Iterable $this$all$iv2 = keySet;
                if (!($this$all$iv2 instanceof Collection) || !((Collection) $this$all$iv2).isEmpty()) {
                    Iterator it = $this$all$iv2.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            $this$all$iv = 1;
                            break;
                        }
                        String it2 = (String) it.next();
                        Bundle bundle2 = this.defaultArguments;
                        Object obj = null;
                        Object obj2 = bundle2 != null ? bundle2.get(it2) : null;
                        Bundle bundle3 = ((NavAction) other).defaultArguments;
                        if (bundle3 != null) {
                            obj = bundle3.get(it2);
                        }
                        if (!Intrinsics.areEqual(obj2, obj)) {
                            $this$all$iv = null;
                            break;
                        }
                    }
                } else {
                    $this$all$iv = 1;
                }
                z = $this$all$iv == 1;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        Set<String> $this$forEach$iv;
        int hashCode = Integer.hashCode(this.destinationId) * 31;
        NavOptions navOptions2 = this.navOptions;
        int result = hashCode + (navOptions2 != null ? navOptions2.hashCode() : 0);
        Bundle bundle = this.defaultArguments;
        if (!(bundle == null || ($this$forEach$iv = bundle.keySet()) == null)) {
            for (String it : $this$forEach$iv) {
                int i = result * 31;
                Bundle bundle2 = this.defaultArguments;
                Object obj = bundle2 != null ? bundle2.get(it) : null;
                result = i + (obj != null ? obj.hashCode() : 0);
            }
        }
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(0x");
        sb.append(Integer.toHexString(this.destinationId));
        sb.append(")");
        if (this.navOptions != null) {
            sb.append(" navOptions=");
            sb.append(this.navOptions);
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }
}
