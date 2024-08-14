package androidx.navigation.fragment;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00040\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Lkotlin/Pair;", "", "invoke"}, k = 3, mv = {1, 8, 0}, xi = 48)
/* compiled from: FragmentNavigator.kt */
final class FragmentNavigator$popBackStack$1$1 extends Lambda implements Function1<Pair<? extends String, ? extends Boolean>, String> {
    public static final FragmentNavigator$popBackStack$1$1 INSTANCE = new FragmentNavigator$popBackStack$1$1();

    FragmentNavigator$popBackStack$1$1() {
        super(1);
    }

    public final String invoke(Pair<String, Boolean> it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getFirst();
    }
}
