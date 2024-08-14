package androidx.navigation;

import android.os.Bundle;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001J\u001b\u0010\u0007\u001a\u0004\u0018\u00010\u00022\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004H\u0002J\u0010\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u0004H\u0016J\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\f\u001a\u00020\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u0002H\u0016J\"\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00042\b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0016R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, d2 = {"androidx/navigation/NavType$Companion$FloatArrayType$1", "Landroidx/navigation/NavType;", "", "name", "", "getName", "()Ljava/lang/String;", "get", "bundle", "Landroid/os/Bundle;", "key", "parseValue", "value", "previousValue", "put", "", "navigation-common_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
/* compiled from: NavType.kt */
public final class NavType$Companion$FloatArrayType$1 extends NavType<float[]> {
    NavType$Companion$FloatArrayType$1() {
        super(true);
    }

    public String getName() {
        return "float[]";
    }

    public void put(Bundle bundle, String key, float[] value) {
        Intrinsics.checkNotNullParameter(bundle, "bundle");
        Intrinsics.checkNotNullParameter(key, "key");
        bundle.putFloatArray(key, value);
    }

    public float[] get(Bundle bundle, String key) {
        Intrinsics.checkNotNullParameter(bundle, "bundle");
        Intrinsics.checkNotNullParameter(key, "key");
        return (float[]) bundle.get(key);
    }

    public float[] parseValue(String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        return new float[]{NavType.FloatType.parseValue(value).floatValue()};
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r0 = kotlin.collections.ArraysKt.plus(r3, parseValue(r2));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float[] parseValue(java.lang.String r2, float[] r3) {
        /*
            r1 = this;
            java.lang.String r0 = "value"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            if (r3 == 0) goto L_0x0012
            float[] r0 = r1.parseValue((java.lang.String) r2)
            float[] r0 = kotlin.collections.ArraysKt.plus((float[]) r3, (float[]) r0)
            if (r0 != 0) goto L_0x0016
        L_0x0012:
            float[] r0 = r1.parseValue((java.lang.String) r2)
        L_0x0016:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.navigation.NavType$Companion$FloatArrayType$1.parseValue(java.lang.String, float[]):float[]");
    }
}
