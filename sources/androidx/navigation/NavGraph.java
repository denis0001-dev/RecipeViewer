package androidx.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.collection.SparseArrayCompat;
import androidx.collection.SparseArrayKt;
import androidx.navigation.NavDestination;
import androidx.navigation.common.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

@Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 @2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00010\u0002:\u0001@B\u0015\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00000\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0000J\u000e\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0001J\u001f\u0010\"\u001a\u00020\u001e2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010#\"\u00020\u0001¢\u0006\u0002\u0010$J\u0016\u0010\"\u001a\u00020\u001e2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010%J\u0006\u0010&\u001a\u00020\u001eJ\u0013\u0010'\u001a\u00020(2\b\u0010\u001f\u001a\u0004\u0018\u00010)H\u0002J\u0012\u0010*\u001a\u0004\u0018\u00010\u00012\b\b\u0001\u0010+\u001a\u00020\u0011J\u001c\u0010*\u001a\u0004\u0018\u00010\u00012\b\b\u0001\u0010+\u001a\u00020\u00112\u0006\u0010,\u001a\u00020(H\u0007J\u001a\u0010*\u001a\u0004\u0018\u00010\u00012\u0006\u0010-\u001a\u00020\u00072\u0006\u0010,\u001a\u00020(H\u0007J\u0012\u0010*\u001a\u0004\u0018\u00010\u00012\b\u0010-\u001a\u0004\u0018\u00010\u0007J\b\u0010.\u001a\u00020\u0011H\u0007J\b\u0010/\u001a\u00020\u0011H\u0016J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000101H\u0002J\u0012\u00102\u001a\u0004\u0018\u0001032\u0006\u00104\u001a\u000205H\u0017J\u0012\u00106\u001a\u0004\u0018\u0001032\u0006\u00107\u001a\u000205H\u0007J\u0018\u00108\u001a\u00020\u001e2\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020<H\u0016J\u000e\u0010=\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0001J\u000e\u0010>\u001a\u00020\u001e2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010>\u001a\u00020\u001e2\u0006\u0010\u0018\u001a\u00020\u0007J\b\u0010?\u001a\u00020\u0007H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078WX\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0019\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b8G¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00078G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\tR\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u0002\n\u0000R$\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00118G@BX\u000e¢\u0006\f\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R(\u0010\u0019\u001a\u0004\u0018\u00010\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0007@BX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\t\"\u0004\b\u001b\u0010\u001c¨\u0006A"}, d2 = {"Landroidx/navigation/NavGraph;", "Landroidx/navigation/NavDestination;", "", "navGraphNavigator", "Landroidx/navigation/Navigator;", "(Landroidx/navigation/Navigator;)V", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "nodes", "Landroidx/collection/SparseArrayCompat;", "getNodes", "()Landroidx/collection/SparseArrayCompat;", "startDestDisplayName", "getStartDestDisplayName", "startDestId", "", "startDestIdName", "startDestinationId", "getStartDestinationId", "()I", "setStartDestinationId", "(I)V", "startDestRoute", "startDestinationRoute", "getStartDestinationRoute", "setStartDestinationRoute", "(Ljava/lang/String;)V", "addAll", "", "other", "addDestination", "node", "addDestinations", "", "([Landroidx/navigation/NavDestination;)V", "", "clear", "equals", "", "", "findNode", "resId", "searchParents", "route", "getStartDestination", "hashCode", "iterator", "", "matchDeepLink", "Landroidx/navigation/NavDestination$DeepLinkMatch;", "navDeepLinkRequest", "Landroidx/navigation/NavDeepLinkRequest;", "matchDeepLinkExcludingChildren", "request", "onInflate", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "remove", "setStartDestination", "toString", "Companion", "navigation-common_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
/* compiled from: NavGraph.kt */
public class NavGraph extends NavDestination implements Iterable<NavDestination>, KMappedMarker {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final SparseArrayCompat<NavDestination> nodes = new SparseArrayCompat<>();
    private int startDestId;
    private String startDestIdName;
    private String startDestinationRoute;

    @JvmStatic
    public static final NavDestination findStartDestination(NavGraph navGraph) {
        return Companion.findStartDestination(navGraph);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NavGraph(Navigator<? extends NavGraph> navGraphNavigator) {
        super((Navigator<? extends NavDestination>) navGraphNavigator);
        Intrinsics.checkNotNullParameter(navGraphNavigator, "navGraphNavigator");
    }

    public final SparseArrayCompat<NavDestination> getNodes() {
        return this.nodes;
    }

    public void onInflate(Context context, AttributeSet attrs) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
        super.onInflate(context, attrs);
        TypedArray $this$use$iv = context.getResources().obtainAttributes(attrs, R.styleable.NavGraphNavigator);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "context.resources.obtain…vGraphNavigator\n        )");
        setStartDestinationId($this$use$iv.getResourceId(R.styleable.NavGraphNavigator_startDestination, 0));
        this.startDestIdName = NavDestination.Companion.getDisplayName(context, this.startDestId);
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
    }

    public NavDestination.DeepLinkMatch matchDeepLink(NavDeepLinkRequest navDeepLinkRequest) {
        Intrinsics.checkNotNullParameter(navDeepLinkRequest, "navDeepLinkRequest");
        NavDestination.DeepLinkMatch bestMatch = super.matchDeepLink(navDeepLinkRequest);
        Collection destination$iv$iv = new ArrayList();
        Iterator it = iterator();
        while (it.hasNext()) {
            Object it$iv$iv = ((NavDestination) it.next()).matchDeepLink(navDeepLinkRequest);
            if (it$iv$iv != null) {
                destination$iv$iv.add(it$iv$iv);
            }
        }
        return (NavDestination.DeepLinkMatch) CollectionsKt.maxOrNull(CollectionsKt.listOfNotNull((T[]) new NavDestination.DeepLinkMatch[]{bestMatch, (NavDestination.DeepLinkMatch) CollectionsKt.maxOrNull((List) destination$iv$iv)}));
    }

    public final NavDestination.DeepLinkMatch matchDeepLinkExcludingChildren(NavDeepLinkRequest request) {
        Intrinsics.checkNotNullParameter(request, "request");
        return super.matchDeepLink(request);
    }

    public final void addDestination(NavDestination node) {
        Intrinsics.checkNotNullParameter(node, "node");
        int id = node.getId();
        String innerRoute = node.getRoute();
        boolean z = false;
        if (!((id == 0 && innerRoute == null) ? false : true)) {
            throw new IllegalArgumentException("Destinations must have an id or route. Call setId(), setRoute(), or include an android:id or app:route in your navigation XML.".toString());
        } else if (getRoute() == null || (!Intrinsics.areEqual((Object) innerRoute, (Object) getRoute()))) {
            if (id != getId()) {
                NavDestination existingDestination = this.nodes.get(id);
                if (existingDestination != node) {
                    if (node.getParent() == null) {
                        z = true;
                    }
                    if (z) {
                        if (existingDestination != null) {
                            existingDestination.setParent((NavGraph) null);
                        }
                        node.setParent(this);
                        this.nodes.put(node.getId(), node);
                        return;
                    }
                    throw new IllegalStateException("Destination already has a parent set. Call NavGraph.remove() to remove the previous parent.".toString());
                }
                return;
            }
            throw new IllegalArgumentException(("Destination " + node + " cannot have the same id as graph " + this).toString());
        } else {
            throw new IllegalArgumentException(("Destination " + node + " cannot have the same route as graph " + this).toString());
        }
    }

    public final void addDestinations(Collection<? extends NavDestination> nodes2) {
        Intrinsics.checkNotNullParameter(nodes2, "nodes");
        for (NavDestination node : nodes2) {
            if (node != null) {
                addDestination(node);
            }
        }
    }

    public final void addDestinations(NavDestination... nodes2) {
        Intrinsics.checkNotNullParameter(nodes2, "nodes");
        for (NavDestination node : nodes2) {
            addDestination(node);
        }
    }

    public final NavDestination findNode(int resId) {
        return findNode(resId, true);
    }

    public final NavDestination findNode(String route) {
        CharSequence charSequence = route;
        if (!(charSequence == null || StringsKt.isBlank(charSequence))) {
            return findNode(route, true);
        }
        return null;
    }

    public final NavDestination findNode(int resId, boolean searchParents) {
        NavDestination destination = this.nodes.get(resId);
        if (destination != null) {
            return destination;
        }
        if (!searchParents || getParent() == null) {
            return null;
        }
        NavGraph parent = getParent();
        Intrinsics.checkNotNull(parent);
        return parent.findNode(resId);
    }

    public final NavDestination findNode(String route, boolean searchParents) {
        Sequence element$iv;
        boolean z;
        Intrinsics.checkNotNullParameter(route, "route");
        NavDestination destination = this.nodes.get(NavDestination.Companion.createRoute(route).hashCode());
        if (destination == null) {
            Iterator<T> it = SequencesKt.asSequence(SparseArrayKt.valueIterator(this.nodes)).iterator();
            while (true) {
                if (!it.hasNext()) {
                    element$iv = null;
                    break;
                }
                element$iv = it.next();
                if (((NavDestination) element$iv).matchDeepLink(route) != null) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    break;
                }
            }
            destination = (NavDestination) element$iv;
        }
        if (destination != null) {
            return destination;
        }
        if (!searchParents || getParent() == null) {
            return null;
        }
        NavGraph parent = getParent();
        Intrinsics.checkNotNull(parent);
        return parent.findNode(route);
    }

    public final Iterator<NavDestination> iterator() {
        return new NavGraph$iterator$1(this);
    }

    public final void addAll(NavGraph other) {
        Intrinsics.checkNotNullParameter(other, "other");
        Iterator iterator = other.iterator();
        while (iterator.hasNext()) {
            iterator.remove();
            addDestination(iterator.next());
        }
    }

    public final void remove(NavDestination node) {
        Intrinsics.checkNotNullParameter(node, "node");
        int index = this.nodes.indexOfKey(node.getId());
        if (index >= 0) {
            this.nodes.valueAt(index).setParent((NavGraph) null);
            this.nodes.removeAt(index);
        }
    }

    public final void clear() {
        Iterator iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public String getDisplayName() {
        return getId() != 0 ? super.getDisplayName() : "the root navigation";
    }

    @Deprecated(message = "Use getStartDestinationId instead.", replaceWith = @ReplaceWith(expression = "startDestinationId", imports = {}))
    public final int getStartDestination() {
        return getStartDestinationId();
    }

    public final int getStartDestinationId() {
        return this.startDestId;
    }

    private final void setStartDestinationId(int startDestId2) {
        if (startDestId2 != getId()) {
            if (this.startDestinationRoute != null) {
                setStartDestinationRoute((String) null);
            }
            this.startDestId = startDestId2;
            this.startDestIdName = null;
            return;
        }
        throw new IllegalArgumentException(("Start destination " + startDestId2 + " cannot use the same id as the graph " + this).toString());
    }

    public final void setStartDestination(int startDestId2) {
        setStartDestinationId(startDestId2);
    }

    public final void setStartDestination(String startDestRoute) {
        Intrinsics.checkNotNullParameter(startDestRoute, "startDestRoute");
        setStartDestinationRoute(startDestRoute);
    }

    public final String getStartDestinationRoute() {
        return this.startDestinationRoute;
    }

    private final void setStartDestinationRoute(String startDestRoute) {
        int i;
        if (startDestRoute == null) {
            i = 0;
        } else if (!(!Intrinsics.areEqual((Object) startDestRoute, (Object) getRoute()))) {
            throw new IllegalArgumentException(("Start destination " + startDestRoute + " cannot use the same route as the graph " + this).toString());
        } else if (!StringsKt.isBlank(startDestRoute)) {
            i = NavDestination.Companion.createRoute(startDestRoute).hashCode();
        } else {
            throw new IllegalArgumentException("Cannot have an empty start destination route".toString());
        }
        this.startDestId = i;
        this.startDestinationRoute = startDestRoute;
    }

    public final String getStartDestDisplayName() {
        if (this.startDestIdName == null) {
            String str = this.startDestinationRoute;
            if (str == null) {
                str = String.valueOf(this.startDestId);
            }
            this.startDestIdName = str;
        }
        String str2 = this.startDestIdName;
        Intrinsics.checkNotNull(str2);
        return str2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        NavDestination startDestination = findNode(this.startDestinationRoute);
        if (startDestination == null) {
            startDestination = findNode(getStartDestinationId());
        }
        sb.append(" startDestination=");
        if (startDestination != null) {
            sb.append("{");
            sb.append(startDestination.toString());
            sb.append("}");
        } else if (this.startDestinationRoute != null) {
            sb.append(this.startDestinationRoute);
        } else if (this.startDestIdName != null) {
            sb.append(this.startDestIdName);
        } else {
            sb.append("0x" + Integer.toHexString(this.startDestId));
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    public boolean equals(Object other) {
        Sequence $this$all$iv;
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof NavGraph)) {
            return false;
        }
        if (super.equals(other) && this.nodes.size() == ((NavGraph) other).nodes.size() && getStartDestinationId() == ((NavGraph) other).getStartDestinationId()) {
            Iterator<T> it = SequencesKt.asSequence(SparseArrayKt.valueIterator(this.nodes)).iterator();
            while (true) {
                if (!it.hasNext()) {
                    $this$all$iv = 1;
                    break;
                }
                NavDestination it2 = (NavDestination) it.next();
                if (!Intrinsics.areEqual((Object) it2, (Object) ((NavGraph) other).nodes.get(it2.getId()))) {
                    $this$all$iv = null;
                    break;
                }
            }
            if ($this$all$iv != null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = getStartDestinationId();
        SparseArrayCompat $receiver$iv = this.nodes;
        int size = $receiver$iv.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            result = (((result * 31) + $receiver$iv.keyAt(index$iv)) * 31) + $receiver$iv.valueAt(index$iv).hashCode();
        }
        return result;
    }

    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\f\u0010\u0003\u001a\u00020\u0004*\u00020\u0005H\u0007¨\u0006\u0006"}, d2 = {"Landroidx/navigation/NavGraph$Companion;", "", "()V", "findStartDestination", "Landroidx/navigation/NavDestination;", "Landroidx/navigation/NavGraph;", "navigation-common_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    /* compiled from: NavGraph.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final NavDestination findStartDestination(NavGraph $this$findStartDestination) {
            Intrinsics.checkNotNullParameter($this$findStartDestination, "<this>");
            return (NavDestination) SequencesKt.last(SequencesKt.generateSequence($this$findStartDestination.findNode($this$findStartDestination.getStartDestinationId()), NavGraph$Companion$findStartDestination$1.INSTANCE));
        }
    }
}
