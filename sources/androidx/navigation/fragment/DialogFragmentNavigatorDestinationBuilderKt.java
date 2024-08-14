package androidx.navigation.fragment;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavGraphBuilder;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

@Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a#\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a?\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b¢\u0006\u0002\b\nH\bø\u0001\u0000\u001a!\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fH\b\u001a=\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f2\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b¢\u0006\u0002\b\nH\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\r"}, d2 = {"dialog", "", "F", "Landroidx/fragment/app/DialogFragment;", "Landroidx/navigation/NavGraphBuilder;", "id", "", "builder", "Lkotlin/Function1;", "Landroidx/navigation/fragment/DialogFragmentNavigatorDestinationBuilder;", "Lkotlin/ExtensionFunctionType;", "route", "", "navigation-fragment_release"}, k = 2, mv = {1, 8, 0}, xi = 48)
/* compiled from: DialogFragmentNavigatorDestinationBuilder.kt */
public final class DialogFragmentNavigatorDestinationBuilderKt {
    @Deprecated(message = "Use routes to create your DialogFragmentDestination instead", replaceWith = @ReplaceWith(expression = "dialog<F>(route = id.toString())", imports = {}))
    public static final /* synthetic */ <F extends DialogFragment> void dialog(NavGraphBuilder $this$dialog, int id) {
        Intrinsics.checkNotNullParameter($this$dialog, "<this>");
        NavGraphBuilder $this$dialog$iv = $this$dialog;
        Intrinsics.reifiedOperationMarker(4, "F");
        DialogFragmentNavigatorDestinationBuilder dialogFragmentNavigatorDestinationBuilder = new DialogFragmentNavigatorDestinationBuilder((DialogFragmentNavigator) $this$dialog$iv.getProvider().getNavigator(DialogFragmentNavigator.class), id, (KClass<? extends DialogFragment>) Reflection.getOrCreateKotlinClass(DialogFragment.class));
        DialogFragmentNavigatorDestinationBuilder dialogFragmentNavigatorDestinationBuilder2 = dialogFragmentNavigatorDestinationBuilder;
        $this$dialog$iv.destination(dialogFragmentNavigatorDestinationBuilder);
    }

    @Deprecated(message = "Use routes to create your DialogFragmentDestination instead", replaceWith = @ReplaceWith(expression = "dialog<F>(route = id.toString()) { builder.invoke() }", imports = {}))
    public static final /* synthetic */ <F extends DialogFragment> void dialog(NavGraphBuilder $this$dialog, int id, Function1<? super DialogFragmentNavigatorDestinationBuilder, Unit> builder) {
        Intrinsics.checkNotNullParameter($this$dialog, "<this>");
        Intrinsics.checkNotNullParameter(builder, "builder");
        Intrinsics.reifiedOperationMarker(4, "F");
        DialogFragmentNavigatorDestinationBuilder dialogFragmentNavigatorDestinationBuilder = new DialogFragmentNavigatorDestinationBuilder((DialogFragmentNavigator) $this$dialog.getProvider().getNavigator(DialogFragmentNavigator.class), id, (KClass<? extends DialogFragment>) Reflection.getOrCreateKotlinClass(DialogFragment.class));
        builder.invoke(dialogFragmentNavigatorDestinationBuilder);
        $this$dialog.destination(dialogFragmentNavigatorDestinationBuilder);
    }

    public static final /* synthetic */ <F extends DialogFragment> void dialog(NavGraphBuilder $this$dialog, String route) {
        Intrinsics.checkNotNullParameter($this$dialog, "<this>");
        Intrinsics.checkNotNullParameter(route, "route");
        NavGraphBuilder $this$dialog$iv = $this$dialog;
        Intrinsics.reifiedOperationMarker(4, "F");
        DialogFragmentNavigatorDestinationBuilder dialogFragmentNavigatorDestinationBuilder = new DialogFragmentNavigatorDestinationBuilder((DialogFragmentNavigator) $this$dialog$iv.getProvider().getNavigator(DialogFragmentNavigator.class), route, (KClass<? extends DialogFragment>) Reflection.getOrCreateKotlinClass(DialogFragment.class));
        DialogFragmentNavigatorDestinationBuilder dialogFragmentNavigatorDestinationBuilder2 = dialogFragmentNavigatorDestinationBuilder;
        $this$dialog$iv.destination(dialogFragmentNavigatorDestinationBuilder);
    }

    public static final /* synthetic */ <F extends DialogFragment> void dialog(NavGraphBuilder $this$dialog, String route, Function1<? super DialogFragmentNavigatorDestinationBuilder, Unit> builder) {
        Intrinsics.checkNotNullParameter($this$dialog, "<this>");
        Intrinsics.checkNotNullParameter(route, "route");
        Intrinsics.checkNotNullParameter(builder, "builder");
        Intrinsics.reifiedOperationMarker(4, "F");
        DialogFragmentNavigatorDestinationBuilder dialogFragmentNavigatorDestinationBuilder = new DialogFragmentNavigatorDestinationBuilder((DialogFragmentNavigator) $this$dialog.getProvider().getNavigator(DialogFragmentNavigator.class), route, (KClass<? extends DialogFragment>) Reflection.getOrCreateKotlinClass(DialogFragment.class));
        builder.invoke(dialogFragmentNavigatorDestinationBuilder);
        $this$dialog.destination(dialogFragmentNavigatorDestinationBuilder);
    }
}
