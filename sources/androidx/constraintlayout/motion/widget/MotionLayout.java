package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Flow;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.core.widgets.Placeholder;
import androidx.constraintlayout.core.widgets.VirtualLayout;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.motion.utils.StopLogic;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MotionLayout extends ConstraintLayout implements NestedScrollingParent3 {
    private static final boolean DEBUG = false;
    public static final int DEBUG_SHOW_NONE = 0;
    public static final int DEBUG_SHOW_PATH = 2;
    public static final int DEBUG_SHOW_PROGRESS = 1;
    private static final float EPSILON = 1.0E-5f;
    public static boolean IS_IN_EDIT_MODE = false;
    static final int MAX_KEY_FRAMES = 50;
    static final String TAG = "MotionLayout";
    public static final int TOUCH_UP_COMPLETE = 0;
    public static final int TOUCH_UP_COMPLETE_TO_END = 2;
    public static final int TOUCH_UP_COMPLETE_TO_START = 1;
    public static final int TOUCH_UP_DECELERATE = 4;
    public static final int TOUCH_UP_DECELERATE_AND_COMPLETE = 5;
    public static final int TOUCH_UP_NEVER_TO_END = 7;
    public static final int TOUCH_UP_NEVER_TO_START = 6;
    public static final int TOUCH_UP_STOP = 3;
    public static final int VELOCITY_LAYOUT = 1;
    public static final int VELOCITY_POST_LAYOUT = 0;
    public static final int VELOCITY_STATIC_LAYOUT = 3;
    public static final int VELOCITY_STATIC_POST_LAYOUT = 2;
    boolean firstDown = true;
    private float lastPos;
    private float lastY;
    private long mAnimationStartTime = 0;
    /* access modifiers changed from: private */
    public int mBeginState = -1;
    private RectF mBoundsCheck = new RectF();
    int mCurrentState = -1;
    int mDebugPath = 0;
    private DecelerateInterpolator mDecelerateLogic = new DecelerateInterpolator();
    private ArrayList<MotionHelper> mDecoratorsHelpers = null;
    private boolean mDelayedApply = false;
    private DesignTool mDesignTool;
    DevModeDraw mDevModeDraw;
    /* access modifiers changed from: private */
    public int mEndState = -1;
    int mEndWrapHeight;
    int mEndWrapWidth;
    HashMap<View, MotionController> mFrameArrayList = new HashMap<>();
    private int mFrames = 0;
    int mHeightMeasureMode;
    private boolean mInLayout = false;
    /* access modifiers changed from: private */
    public boolean mInRotation = false;
    boolean mInTransition = false;
    boolean mIndirectTransition = false;
    private boolean mInteractionEnabled = true;
    Interpolator mInterpolator;
    private Matrix mInverseMatrix = null;
    boolean mIsAnimating = false;
    private boolean mKeepAnimating = false;
    private KeyCache mKeyCache = new KeyCache();
    private long mLastDrawTime = -1;
    private float mLastFps = 0.0f;
    /* access modifiers changed from: private */
    public int mLastHeightMeasureSpec = 0;
    int mLastLayoutHeight;
    int mLastLayoutWidth;
    float mLastVelocity = 0.0f;
    /* access modifiers changed from: private */
    public int mLastWidthMeasureSpec = 0;
    private float mListenerPosition = 0.0f;
    private int mListenerState = 0;
    protected boolean mMeasureDuringTransition = false;
    Model mModel = new Model();
    private boolean mNeedsFireTransitionCompleted = false;
    int mOldHeight;
    int mOldWidth;
    private Runnable mOnComplete = null;
    private ArrayList<MotionHelper> mOnHideHelpers = null;
    private ArrayList<MotionHelper> mOnShowHelpers = null;
    float mPostInterpolationPosition;
    HashMap<View, ViewState> mPreRotate = new HashMap<>();
    /* access modifiers changed from: private */
    public int mPreRotateHeight;
    /* access modifiers changed from: private */
    public int mPreRotateWidth;
    private int mPreviouseRotation;
    Interpolator mProgressInterpolator = null;
    private View mRegionView = null;
    int mRotatMode = 0;
    MotionScene mScene;
    private int[] mScheduledTransitionTo = null;
    int mScheduledTransitions = 0;
    float mScrollTargetDT;
    float mScrollTargetDX;
    float mScrollTargetDY;
    long mScrollTargetTime;
    int mStartWrapHeight;
    int mStartWrapWidth;
    /* access modifiers changed from: private */
    public StateCache mStateCache;
    private StopLogic mStopLogic = new StopLogic();
    Rect mTempRect = new Rect();
    private boolean mTemporalInterpolator = false;
    ArrayList<Integer> mTransitionCompleted = new ArrayList<>();
    private float mTransitionDuration = 1.0f;
    float mTransitionGoalPosition = 0.0f;
    private boolean mTransitionInstantly;
    float mTransitionLastPosition = 0.0f;
    private long mTransitionLastTime;
    private TransitionListener mTransitionListener;
    private CopyOnWriteArrayList<TransitionListener> mTransitionListeners = null;
    float mTransitionPosition = 0.0f;
    TransitionState mTransitionState = TransitionState.UNDEFINED;
    boolean mUndergoingMotion = false;
    int mWidthMeasureMode;

    protected interface MotionTracker {
        void addMovement(MotionEvent motionEvent);

        void clear();

        void computeCurrentVelocity(int i);

        void computeCurrentVelocity(int i, float f);

        float getXVelocity();

        float getXVelocity(int i);

        float getYVelocity();

        float getYVelocity(int i);

        void recycle();
    }

    public interface TransitionListener {
        void onTransitionChange(MotionLayout motionLayout, int i, int i2, float f);

        void onTransitionCompleted(MotionLayout motionLayout, int i);

        void onTransitionStarted(MotionLayout motionLayout, int i, int i2);

        void onTransitionTrigger(MotionLayout motionLayout, int i, boolean z, float f);
    }

    enum TransitionState {
        UNDEFINED,
        SETUP,
        MOVING,
        FINISHED
    }

    /* access modifiers changed from: package-private */
    public MotionController getMotionController(int mTouchAnchorId) {
        return this.mFrameArrayList.get(findViewById(mTouchAnchorId));
    }

    public MotionLayout(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public MotionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MotionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /* access modifiers changed from: protected */
    public long getNanoTime() {
        return System.nanoTime();
    }

    /* access modifiers changed from: protected */
    public MotionTracker obtainVelocityTracker() {
        return MyTracker.obtain();
    }

    public void enableTransition(int transitionID, boolean enable) {
        MotionScene.Transition t = getTransition(transitionID);
        if (enable) {
            t.setEnabled(true);
            return;
        }
        if (t == this.mScene.mCurrentTransition) {
            Iterator<MotionScene.Transition> it = this.mScene.getTransitionsWithState(this.mCurrentState).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MotionScene.Transition transition = it.next();
                if (transition.isEnabled()) {
                    this.mScene.mCurrentTransition = transition;
                    break;
                }
            }
        }
        t.setEnabled(false);
    }

    /* access modifiers changed from: package-private */
    public void setState(TransitionState newState) {
        if (newState != TransitionState.FINISHED || this.mCurrentState != -1) {
            TransitionState oldState = this.mTransitionState;
            this.mTransitionState = newState;
            if (oldState == TransitionState.MOVING && newState == TransitionState.MOVING) {
                fireTransitionChange();
            }
            switch (oldState) {
                case UNDEFINED:
                case SETUP:
                    if (newState == TransitionState.MOVING) {
                        fireTransitionChange();
                    }
                    if (newState == TransitionState.FINISHED) {
                        fireTransitionCompleted();
                        return;
                    }
                    return;
                case MOVING:
                    if (newState == TransitionState.FINISHED) {
                        fireTransitionCompleted();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private static class MyTracker implements MotionTracker {
        private static MyTracker me = new MyTracker();
        VelocityTracker tracker;

        private MyTracker() {
        }

        public static MyTracker obtain() {
            me.tracker = VelocityTracker.obtain();
            return me;
        }

        public void recycle() {
            if (this.tracker != null) {
                this.tracker.recycle();
                this.tracker = null;
            }
        }

        public void clear() {
            if (this.tracker != null) {
                this.tracker.clear();
            }
        }

        public void addMovement(MotionEvent event) {
            if (this.tracker != null) {
                this.tracker.addMovement(event);
            }
        }

        public void computeCurrentVelocity(int units) {
            if (this.tracker != null) {
                this.tracker.computeCurrentVelocity(units);
            }
        }

        public void computeCurrentVelocity(int units, float maxVelocity) {
            if (this.tracker != null) {
                this.tracker.computeCurrentVelocity(units, maxVelocity);
            }
        }

        public float getXVelocity() {
            if (this.tracker != null) {
                return this.tracker.getXVelocity();
            }
            return 0.0f;
        }

        public float getYVelocity() {
            if (this.tracker != null) {
                return this.tracker.getYVelocity();
            }
            return 0.0f;
        }

        public float getXVelocity(int id) {
            if (this.tracker != null) {
                return this.tracker.getXVelocity(id);
            }
            return 0.0f;
        }

        public float getYVelocity(int id) {
            if (this.tracker != null) {
                return getYVelocity(id);
            }
            return 0.0f;
        }
    }

    /* access modifiers changed from: package-private */
    public void setStartState(int beginId) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setStartState(beginId);
            this.mStateCache.setEndState(beginId);
            return;
        }
        this.mCurrentState = beginId;
    }

    public void setTransition(int beginId, int endId) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setStartState(beginId);
            this.mStateCache.setEndState(endId);
        } else if (this.mScene != null) {
            this.mBeginState = beginId;
            this.mEndState = endId;
            this.mScene.setTransition(beginId, endId);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(beginId), this.mScene.getConstraintSet(endId));
            rebuildScene();
            this.mTransitionLastPosition = 0.0f;
            transitionToStart();
        }
    }

    public void setTransition(int transitionId) {
        if (this.mScene != null) {
            MotionScene.Transition transition = getTransition(transitionId);
            int i = this.mCurrentState;
            this.mBeginState = transition.getStartConstraintSetId();
            this.mEndState = transition.getEndConstraintSetId();
            if (!isAttachedToWindow()) {
                if (this.mStateCache == null) {
                    this.mStateCache = new StateCache();
                }
                this.mStateCache.setStartState(this.mBeginState);
                this.mStateCache.setEndState(this.mEndState);
                return;
            }
            float pos = Float.NaN;
            if (this.mCurrentState == this.mBeginState) {
                pos = 0.0f;
            } else if (this.mCurrentState == this.mEndState) {
                pos = 1.0f;
            }
            this.mScene.setTransition(transition);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            rebuildScene();
            float f = 0.0f;
            if (this.mTransitionLastPosition != pos) {
                if (pos == 0.0f) {
                    endTrigger(true);
                    this.mScene.getConstraintSet(this.mBeginState).applyTo(this);
                } else if (pos == 1.0f) {
                    endTrigger(false);
                    this.mScene.getConstraintSet(this.mEndState).applyTo(this);
                }
            }
            if (!Float.isNaN(pos)) {
                f = pos;
            }
            this.mTransitionLastPosition = f;
            if (Float.isNaN(pos)) {
                Log.v(TAG, Debug.getLocation() + " transitionToStart ");
                transitionToStart();
                return;
            }
            setProgress(pos);
        }
    }

    /* access modifiers changed from: protected */
    public void setTransition(MotionScene.Transition transition) {
        this.mScene.setTransition(transition);
        setState(TransitionState.SETUP);
        if (this.mCurrentState == this.mScene.getEndId()) {
            this.mTransitionLastPosition = 1.0f;
            this.mTransitionPosition = 1.0f;
            this.mTransitionGoalPosition = 1.0f;
        } else {
            this.mTransitionLastPosition = 0.0f;
            this.mTransitionPosition = 0.0f;
            this.mTransitionGoalPosition = 0.0f;
        }
        this.mTransitionLastTime = transition.isTransitionFlag(1) ? -1 : getNanoTime();
        int newBeginState = this.mScene.getStartId();
        int newEndState = this.mScene.getEndId();
        if (newBeginState != this.mBeginState || newEndState != this.mEndState) {
            this.mBeginState = newBeginState;
            this.mEndState = newEndState;
            this.mScene.setTransition(this.mBeginState, this.mEndState);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            this.mModel.setMeasuredId(this.mBeginState, this.mEndState);
            this.mModel.reEvaluateState();
            rebuildScene();
        }
    }

    public void loadLayoutDescription(int motionScene) {
        if (motionScene != 0) {
            try {
                this.mScene = new MotionScene(getContext(), this, motionScene);
                if (this.mCurrentState == -1 && this.mScene != null) {
                    this.mCurrentState = this.mScene.getStartId();
                    this.mBeginState = this.mScene.getStartId();
                    this.mEndState = this.mScene.getEndId();
                }
                if (isAttachedToWindow()) {
                    Display display = getDisplay();
                    this.mPreviouseRotation = display == null ? 0 : display.getRotation();
                    if (this.mScene != null) {
                        ConstraintSet cSet = this.mScene.getConstraintSet(this.mCurrentState);
                        this.mScene.readFallback(this);
                        if (this.mDecoratorsHelpers != null) {
                            Iterator<MotionHelper> it = this.mDecoratorsHelpers.iterator();
                            while (it.hasNext()) {
                                it.next().onFinishedMotionScene(this);
                            }
                        }
                        if (cSet != null) {
                            cSet.applyTo(this);
                        }
                        this.mBeginState = this.mCurrentState;
                    }
                    onNewStateAttachHandlers();
                    if (this.mStateCache != null) {
                        if (this.mDelayedApply) {
                            post(new Runnable() {
                                public void run() {
                                    MotionLayout.this.mStateCache.apply();
                                }
                            });
                        } else {
                            this.mStateCache.apply();
                        }
                    } else if (this.mScene != null && this.mScene.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
                        transitionToEnd();
                        setState(TransitionState.SETUP);
                        setState(TransitionState.MOVING);
                    }
                } else {
                    this.mScene = null;
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("unable to parse MotionScene file", ex);
            } catch (Exception ex2) {
                throw new IllegalArgumentException("unable to parse MotionScene file", ex2);
            }
        } else {
            this.mScene = null;
        }
    }

    public boolean isAttachedToWindow() {
        return super.isAttachedToWindow();
    }

    public void setState(int id, int screenWidth, int screenHeight) {
        setState(TransitionState.SETUP);
        this.mCurrentState = id;
        this.mBeginState = -1;
        this.mEndState = -1;
        if (this.mConstraintLayoutSpec != null) {
            this.mConstraintLayoutSpec.updateConstraints(id, (float) screenWidth, (float) screenHeight);
        } else if (this.mScene != null) {
            this.mScene.getConstraintSet(id).applyTo(this);
        }
    }

    public void setInterpolatedProgress(float pos) {
        if (this.mScene != null) {
            setState(TransitionState.MOVING);
            Interpolator interpolator = this.mScene.getInterpolator();
            if (interpolator != null) {
                setProgress(interpolator.getInterpolation(pos));
                return;
            }
        }
        setProgress(pos);
    }

    public void setProgress(float pos, float velocity) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setProgress(pos);
            this.mStateCache.setVelocity(velocity);
            return;
        }
        setProgress(pos);
        setState(TransitionState.MOVING);
        this.mLastVelocity = velocity;
        float f = 0.0f;
        if (velocity != 0.0f) {
            if (velocity > 0.0f) {
                f = 1.0f;
            }
            animateTo(f);
        } else if (pos != 0.0f && pos != 1.0f) {
            if (pos > 0.5f) {
                f = 1.0f;
            }
            animateTo(f);
        }
    }

    class StateCache {
        final String KeyEndState = "motion.EndState";
        final String KeyProgress = "motion.progress";
        final String KeyStartState = "motion.StartState";
        final String KeyVelocity = "motion.velocity";
        int endState = -1;
        float mProgress = Float.NaN;
        float mVelocity = Float.NaN;
        int startState = -1;

        StateCache() {
        }

        /* access modifiers changed from: package-private */
        public void apply() {
            if (!(this.startState == -1 && this.endState == -1)) {
                if (this.startState == -1) {
                    MotionLayout.this.transitionToState(this.endState);
                } else if (this.endState == -1) {
                    MotionLayout.this.setState(this.startState, -1, -1);
                } else {
                    MotionLayout.this.setTransition(this.startState, this.endState);
                }
                MotionLayout.this.setState(TransitionState.SETUP);
            }
            if (!Float.isNaN(this.mVelocity)) {
                MotionLayout.this.setProgress(this.mProgress, this.mVelocity);
                this.mProgress = Float.NaN;
                this.mVelocity = Float.NaN;
                this.startState = -1;
                this.endState = -1;
            } else if (!Float.isNaN(this.mProgress)) {
                MotionLayout.this.setProgress(this.mProgress);
            }
        }

        public Bundle getTransitionState() {
            Bundle bundle = new Bundle();
            bundle.putFloat("motion.progress", this.mProgress);
            bundle.putFloat("motion.velocity", this.mVelocity);
            bundle.putInt("motion.StartState", this.startState);
            bundle.putInt("motion.EndState", this.endState);
            return bundle;
        }

        public void setTransitionState(Bundle bundle) {
            this.mProgress = bundle.getFloat("motion.progress");
            this.mVelocity = bundle.getFloat("motion.velocity");
            this.startState = bundle.getInt("motion.StartState");
            this.endState = bundle.getInt("motion.EndState");
        }

        public void setProgress(float progress) {
            this.mProgress = progress;
        }

        public void setEndState(int endState2) {
            this.endState = endState2;
        }

        public void setVelocity(float mVelocity2) {
            this.mVelocity = mVelocity2;
        }

        public void setStartState(int startState2) {
            this.startState = startState2;
        }

        public void recordState() {
            this.endState = MotionLayout.this.mEndState;
            this.startState = MotionLayout.this.mBeginState;
            this.mVelocity = MotionLayout.this.getVelocity();
            this.mProgress = MotionLayout.this.getProgress();
        }
    }

    public void setTransitionState(Bundle bundle) {
        if (this.mStateCache == null) {
            this.mStateCache = new StateCache();
        }
        this.mStateCache.setTransitionState(bundle);
        if (isAttachedToWindow()) {
            this.mStateCache.apply();
        }
    }

    public Bundle getTransitionState() {
        if (this.mStateCache == null) {
            this.mStateCache = new StateCache();
        }
        this.mStateCache.recordState();
        return this.mStateCache.getTransitionState();
    }

    public void setProgress(float pos) {
        if (pos < 0.0f || pos > 1.0f) {
            Log.w(TAG, "Warning! Progress is defined for values between 0.0 and 1.0 inclusive");
        }
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setProgress(pos);
            return;
        }
        if (pos <= 0.0f) {
            if (this.mTransitionLastPosition == 1.0f && this.mCurrentState == this.mEndState) {
                setState(TransitionState.MOVING);
            }
            this.mCurrentState = this.mBeginState;
            if (this.mTransitionLastPosition == 0.0f) {
                setState(TransitionState.FINISHED);
            }
        } else if (pos >= 1.0f) {
            if (this.mTransitionLastPosition == 0.0f && this.mCurrentState == this.mBeginState) {
                setState(TransitionState.MOVING);
            }
            this.mCurrentState = this.mEndState;
            if (this.mTransitionLastPosition == 1.0f) {
                setState(TransitionState.FINISHED);
            }
        } else {
            this.mCurrentState = -1;
            setState(TransitionState.MOVING);
        }
        if (this.mScene != null) {
            this.mTransitionInstantly = true;
            this.mTransitionGoalPosition = pos;
            this.mTransitionPosition = pos;
            this.mTransitionLastTime = -1;
            this.mAnimationStartTime = -1;
            this.mInterpolator = null;
            this.mInTransition = true;
            invalidate();
        }
    }

    /* access modifiers changed from: private */
    public void setupMotionViews() {
        int layoutWidth;
        int i;
        int i2;
        int i3;
        MotionLayout motionLayout = this;
        int n = getChildCount();
        motionLayout.mModel.build();
        boolean flip = true;
        motionLayout.mInTransition = true;
        SparseArray<MotionController> controllers = new SparseArray<>();
        for (int i4 = 0; i4 < n; i4++) {
            View child = motionLayout.getChildAt(i4);
            controllers.put(child.getId(), motionLayout.mFrameArrayList.get(child));
        }
        int layoutWidth2 = getWidth();
        int layoutHeight = getHeight();
        int arc = motionLayout.mScene.gatPathMotionArc();
        if (arc != -1) {
            for (int i5 = 0; i5 < n; i5++) {
                MotionController motionController = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i5));
                if (motionController != null) {
                    motionController.setPathMotionArc(arc);
                }
            }
        }
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        int[] depends = new int[motionLayout.mFrameArrayList.size()];
        int count = 0;
        for (int i6 = 0; i6 < n; i6++) {
            MotionController motionController2 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i6));
            if (motionController2.getAnimateRelativeTo() != -1) {
                sparseBooleanArray.put(motionController2.getAnimateRelativeTo(), true);
                depends[count] = motionController2.getAnimateRelativeTo();
                count++;
            }
        }
        if (motionLayout.mDecoratorsHelpers != null) {
            for (int i7 = 0; i7 < count; i7++) {
                MotionController motionController3 = motionLayout.mFrameArrayList.get(motionLayout.findViewById(depends[i7]));
                if (motionController3 != null) {
                    motionLayout.mScene.getKeyFrames(motionController3);
                }
            }
            Iterator<MotionHelper> it = motionLayout.mDecoratorsHelpers.iterator();
            while (it.hasNext()) {
                it.next().onPreSetup(motionLayout, motionLayout.mFrameArrayList);
            }
            int i8 = 0;
            while (i8 < count) {
                MotionController motionController4 = motionLayout.mFrameArrayList.get(motionLayout.findViewById(depends[i8]));
                if (motionController4 == null) {
                    i3 = i8;
                } else {
                    i3 = i8;
                    motionController4.setup(layoutWidth2, layoutHeight, motionLayout.mTransitionDuration, getNanoTime());
                }
                i8 = i3 + 1;
            }
            int i9 = i8;
        } else {
            int i10 = 0;
            while (i10 < count) {
                MotionController motionController5 = motionLayout.mFrameArrayList.get(motionLayout.findViewById(depends[i10]));
                if (motionController5 == null) {
                    i2 = i10;
                } else {
                    motionLayout.mScene.getKeyFrames(motionController5);
                    i2 = i10;
                    MotionController motionController6 = motionController5;
                    motionController5.setup(layoutWidth2, layoutHeight, motionLayout.mTransitionDuration, getNanoTime());
                }
                i10 = i2 + 1;
            }
            int i11 = i10;
        }
        int i12 = 0;
        while (i12 < n) {
            View v = motionLayout.getChildAt(i12);
            MotionController motionController7 = motionLayout.mFrameArrayList.get(v);
            if (sparseBooleanArray.get(v.getId())) {
                i = i12;
            } else if (motionController7 != null) {
                motionLayout.mScene.getKeyFrames(motionController7);
                MotionController motionController8 = motionController7;
                i = i12;
                View view = v;
                motionController7.setup(layoutWidth2, layoutHeight, motionLayout.mTransitionDuration, getNanoTime());
            } else {
                i = i12;
                View view2 = v;
            }
            i12 = i + 1;
        }
        int i13 = i12;
        float stagger = motionLayout.mScene.getStaggered();
        if (stagger != 0.0f) {
            if (((double) stagger) >= 0.0d) {
                flip = false;
            }
            boolean useMotionStagger = false;
            float stagger2 = Math.abs(stagger);
            float min = Float.MAX_VALUE;
            float max = -3.4028235E38f;
            int i14 = 0;
            while (true) {
                if (i14 >= n) {
                    break;
                }
                SparseArray<MotionController> controllers2 = controllers;
                MotionController f = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i14));
                if (!Float.isNaN(f.mMotionStagger)) {
                    useMotionStagger = true;
                    break;
                }
                float x = f.getFinalX();
                float y = f.getFinalY();
                MotionController motionController9 = f;
                float mdist = flip ? y - x : y + x;
                min = Math.min(min, mdist);
                max = Math.max(max, mdist);
                i14++;
                controllers = controllers2;
            }
            if (useMotionStagger) {
                float min2 = Float.MAX_VALUE;
                float max2 = -3.4028235E38f;
                for (int i15 = 0; i15 < n; i15++) {
                    MotionController f2 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i15));
                    if (!Float.isNaN(f2.mMotionStagger)) {
                        min2 = Math.min(min2, f2.mMotionStagger);
                        max2 = Math.max(max2, f2.mMotionStagger);
                    }
                }
                int i16 = 0;
                while (i16 < n) {
                    MotionController f3 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i16));
                    if (!Float.isNaN(f3.mMotionStagger)) {
                        layoutWidth = layoutWidth2;
                        f3.mStaggerScale = 1.0f / (1.0f - stagger2);
                        if (flip) {
                            f3.mStaggerOffset = stagger2 - (((max2 - f3.mMotionStagger) / (max2 - min2)) * stagger2);
                        } else {
                            f3.mStaggerOffset = stagger2 - (((f3.mMotionStagger - min2) * stagger2) / (max2 - min2));
                        }
                    } else {
                        layoutWidth = layoutWidth2;
                    }
                    i16++;
                    layoutWidth2 = layoutWidth;
                }
                return;
            }
            int i17 = 0;
            while (i17 < n) {
                MotionController f4 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i17));
                float x2 = f4.getFinalX();
                float y2 = f4.getFinalY();
                float mdist2 = flip ? y2 - x2 : y2 + x2;
                f4.mStaggerScale = 1.0f / (1.0f - stagger2);
                f4.mStaggerOffset = stagger2 - (((mdist2 - min) * stagger2) / (max - min));
                i17++;
                motionLayout = this;
            }
            return;
        }
        int i18 = layoutWidth2;
    }

    public void touchAnimateTo(int touchUpMode, float position, float currentVelocity) {
        if (this.mScene != null && this.mTransitionLastPosition != position) {
            this.mTemporalInterpolator = true;
            this.mAnimationStartTime = getNanoTime();
            this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
            this.mTransitionGoalPosition = position;
            this.mInTransition = true;
            switch (touchUpMode) {
                case 0:
                case 1:
                case 2:
                case 6:
                case 7:
                    if (touchUpMode == 1 || touchUpMode == 7) {
                        position = 0.0f;
                    } else if (touchUpMode == 2 || touchUpMode == 6) {
                        position = 1.0f;
                    }
                    if (this.mScene.getAutoCompleteMode() == 0) {
                        this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                    } else {
                        this.mStopLogic.springConfig(this.mTransitionLastPosition, position, currentVelocity, this.mScene.getSpringMass(), this.mScene.getSpringStiffiness(), this.mScene.getSpringDamping(), this.mScene.getSpringStopThreshold(), this.mScene.getSpringBoundary());
                    }
                    int currentState = this.mCurrentState;
                    this.mTransitionGoalPosition = position;
                    this.mCurrentState = currentState;
                    this.mInterpolator = this.mStopLogic;
                    break;
                case 4:
                    this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                    this.mInterpolator = this.mDecelerateLogic;
                    break;
                case 5:
                    if (!willJump(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration())) {
                        this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                        this.mLastVelocity = 0.0f;
                        int currentState2 = this.mCurrentState;
                        this.mTransitionGoalPosition = position;
                        this.mCurrentState = currentState2;
                        this.mInterpolator = this.mStopLogic;
                        break;
                    } else {
                        this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                        this.mInterpolator = this.mDecelerateLogic;
                        break;
                    }
            }
            this.mTransitionInstantly = false;
            this.mAnimationStartTime = getNanoTime();
            invalidate();
        }
    }

    public void touchSpringTo(float position, float currentVelocity) {
        if (this.mScene != null && this.mTransitionLastPosition != position) {
            this.mTemporalInterpolator = true;
            this.mAnimationStartTime = getNanoTime();
            this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
            this.mTransitionGoalPosition = position;
            this.mInTransition = true;
            this.mStopLogic.springConfig(this.mTransitionLastPosition, position, currentVelocity, this.mScene.getSpringMass(), this.mScene.getSpringStiffiness(), this.mScene.getSpringDamping(), this.mScene.getSpringStopThreshold(), this.mScene.getSpringBoundary());
            int currentState = this.mCurrentState;
            this.mTransitionGoalPosition = position;
            this.mCurrentState = currentState;
            this.mInterpolator = this.mStopLogic;
            this.mTransitionInstantly = false;
            this.mAnimationStartTime = getNanoTime();
            invalidate();
        }
    }

    private static boolean willJump(float velocity, float position, float maxAcceleration) {
        if (velocity > 0.0f) {
            float time = velocity / maxAcceleration;
            if (position + ((velocity * time) - (((maxAcceleration * time) * time) / 2.0f)) > 1.0f) {
                return true;
            }
            return false;
        }
        float time2 = (-velocity) / maxAcceleration;
        if (position + (velocity * time2) + (((maxAcceleration * time2) * time2) / 2.0f) < 0.0f) {
            return true;
        }
        return false;
    }

    class DecelerateInterpolator extends MotionInterpolator {
        float currentP = 0.0f;
        float initalV = 0.0f;
        float maxA;

        DecelerateInterpolator() {
        }

        public void config(float velocity, float position, float maxAcceleration) {
            this.initalV = velocity;
            this.currentP = position;
            this.maxA = maxAcceleration;
        }

        public float getInterpolation(float time) {
            if (this.initalV > 0.0f) {
                if (this.initalV / this.maxA < time) {
                    time = this.initalV / this.maxA;
                }
                MotionLayout.this.mLastVelocity = this.initalV - (this.maxA * time);
                return this.currentP + ((this.initalV * time) - (((this.maxA * time) * time) / 2.0f));
            }
            if ((-this.initalV) / this.maxA < time) {
                time = (-this.initalV) / this.maxA;
            }
            MotionLayout.this.mLastVelocity = this.initalV + (this.maxA * time);
            return this.currentP + (this.initalV * time) + (((this.maxA * time) * time) / 2.0f);
        }

        public float getVelocity() {
            return MotionLayout.this.mLastVelocity;
        }
    }

    /* access modifiers changed from: package-private */
    public void animateTo(float position) {
        if (this.mScene != null) {
            if (this.mTransitionLastPosition != this.mTransitionPosition && this.mTransitionInstantly) {
                this.mTransitionLastPosition = this.mTransitionPosition;
            }
            if (this.mTransitionLastPosition != position) {
                this.mTemporalInterpolator = false;
                float currentPosition = this.mTransitionLastPosition;
                this.mTransitionGoalPosition = position;
                this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
                setProgress(this.mTransitionGoalPosition);
                this.mInterpolator = null;
                this.mProgressInterpolator = this.mScene.getInterpolator();
                this.mTransitionInstantly = false;
                this.mAnimationStartTime = getNanoTime();
                this.mInTransition = true;
                this.mTransitionPosition = currentPosition;
                this.mTransitionLastPosition = currentPosition;
                invalidate();
            }
        }
    }

    private void computeCurrentPositions() {
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            View v = getChildAt(i);
            MotionController frame = this.mFrameArrayList.get(v);
            if (frame != null) {
                frame.setStartCurrentState(v);
            }
        }
    }

    public void transitionToStart() {
        animateTo(0.0f);
    }

    public void transitionToEnd() {
        animateTo(1.0f);
        this.mOnComplete = null;
    }

    public void transitionToEnd(Runnable onComplete) {
        animateTo(1.0f);
        this.mOnComplete = onComplete;
    }

    public void transitionToState(int id) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setEndState(id);
            return;
        }
        transitionToState(id, -1, -1);
    }

    public void transitionToState(int id, int duration) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setEndState(id);
            return;
        }
        transitionToState(id, -1, -1, duration);
    }

    public void transitionToState(int id, int screenWidth, int screenHeight) {
        transitionToState(id, screenWidth, screenHeight, -1);
    }

    public void rotateTo(int id, int duration) {
        int i = 1;
        this.mInRotation = true;
        this.mPreRotateWidth = getWidth();
        this.mPreRotateHeight = getHeight();
        int currentRotation = getDisplay().getRotation();
        if ((currentRotation + 1) % 4 <= (this.mPreviouseRotation + 1) % 4) {
            i = 2;
        }
        this.mRotatMode = i;
        this.mPreviouseRotation = currentRotation;
        int n = getChildCount();
        for (int i2 = 0; i2 < n; i2++) {
            View v = getChildAt(i2);
            ViewState bounds = this.mPreRotate.get(v);
            if (bounds == null) {
                bounds = new ViewState();
                this.mPreRotate.put(v, bounds);
            }
            bounds.getState(v);
        }
        this.mBeginState = -1;
        this.mEndState = id;
        this.mScene.setTransition(-1, this.mEndState);
        this.mModel.initFrom(this.mLayoutWidget, (ConstraintSet) null, this.mScene.getConstraintSet(this.mEndState));
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        invalidate();
        transitionToEnd(new Runnable() {
            public void run() {
                boolean unused = MotionLayout.this.mInRotation = false;
            }
        });
        if (duration > 0) {
            this.mTransitionDuration = ((float) duration) / 1000.0f;
        }
    }

    public boolean isInRotation() {
        return this.mInRotation;
    }

    public void jumpToState(int id) {
        if (!isAttachedToWindow()) {
            this.mCurrentState = id;
        }
        if (this.mBeginState == id) {
            setProgress(0.0f);
        } else if (this.mEndState == id) {
            setProgress(1.0f);
        } else {
            setTransition(id, id);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0030 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0031  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void transitionToState(int r23, int r24, int r25, int r26) {
        /*
            r22 = this;
            r0 = r22
            r1 = r26
            androidx.constraintlayout.motion.widget.MotionScene r2 = r0.mScene
            r3 = -1
            if (r2 == 0) goto L_0x0025
            androidx.constraintlayout.motion.widget.MotionScene r2 = r0.mScene
            androidx.constraintlayout.widget.StateSet r2 = r2.mStateSet
            if (r2 == 0) goto L_0x0025
            androidx.constraintlayout.motion.widget.MotionScene r2 = r0.mScene
            androidx.constraintlayout.widget.StateSet r2 = r2.mStateSet
            int r4 = r0.mCurrentState
            r5 = r24
            float r6 = (float) r5
            r7 = r25
            float r8 = (float) r7
            r9 = r23
            int r2 = r2.convertToConstraintSet(r4, r9, r6, r8)
            if (r2 == r3) goto L_0x002b
            r4 = r2
            goto L_0x002c
        L_0x0025:
            r9 = r23
            r5 = r24
            r7 = r25
        L_0x002b:
            r4 = r9
        L_0x002c:
            int r2 = r0.mCurrentState
            if (r2 != r4) goto L_0x0031
            return
        L_0x0031:
            int r2 = r0.mBeginState
            r6 = 1148846080(0x447a0000, float:1000.0)
            r8 = 0
            if (r2 != r4) goto L_0x0042
            r0.animateTo(r8)
            if (r1 <= 0) goto L_0x0041
            float r2 = (float) r1
            float r2 = r2 / r6
            r0.mTransitionDuration = r2
        L_0x0041:
            return
        L_0x0042:
            int r2 = r0.mEndState
            r9 = 1065353216(0x3f800000, float:1.0)
            if (r2 != r4) goto L_0x0052
            r0.animateTo(r9)
            if (r1 <= 0) goto L_0x0051
            float r2 = (float) r1
            float r2 = r2 / r6
            r0.mTransitionDuration = r2
        L_0x0051:
            return
        L_0x0052:
            r0.mEndState = r4
            int r2 = r0.mCurrentState
            if (r2 == r3) goto L_0x006c
            int r2 = r0.mCurrentState
            r0.setTransition(r2, r4)
            r0.animateTo(r9)
            r0.mTransitionLastPosition = r8
            r22.transitionToEnd()
            if (r1 <= 0) goto L_0x006b
            float r2 = (float) r1
            float r2 = r2 / r6
            r0.mTransitionDuration = r2
        L_0x006b:
            return
        L_0x006c:
            r2 = 0
            r0.mTemporalInterpolator = r2
            r0.mTransitionGoalPosition = r9
            r0.mTransitionPosition = r8
            r0.mTransitionLastPosition = r8
            long r10 = r22.getNanoTime()
            r0.mTransitionLastTime = r10
            long r10 = r22.getNanoTime()
            r0.mAnimationStartTime = r10
            r0.mTransitionInstantly = r2
            r2 = 0
            r0.mInterpolator = r2
            if (r1 != r3) goto L_0x0092
            androidx.constraintlayout.motion.widget.MotionScene r10 = r0.mScene
            int r10 = r10.getDuration()
            float r10 = (float) r10
            float r10 = r10 / r6
            r0.mTransitionDuration = r10
        L_0x0092:
            r0.mBeginState = r3
            androidx.constraintlayout.motion.widget.MotionScene r3 = r0.mScene
            int r10 = r0.mBeginState
            int r11 = r0.mEndState
            r3.setTransition(r10, r11)
            android.util.SparseArray r3 = new android.util.SparseArray
            r3.<init>()
            if (r1 != 0) goto L_0x00af
            androidx.constraintlayout.motion.widget.MotionScene r10 = r0.mScene
            int r10 = r10.getDuration()
            float r10 = (float) r10
            float r10 = r10 / r6
            r0.mTransitionDuration = r10
            goto L_0x00b5
        L_0x00af:
            if (r1 <= 0) goto L_0x00b5
            float r10 = (float) r1
            float r10 = r10 / r6
            r0.mTransitionDuration = r10
        L_0x00b5:
            int r6 = r22.getChildCount()
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r10 = r0.mFrameArrayList
            r10.clear()
            r10 = 0
        L_0x00bf:
            if (r10 >= r6) goto L_0x00e1
            android.view.View r11 = r0.getChildAt(r10)
            androidx.constraintlayout.motion.widget.MotionController r12 = new androidx.constraintlayout.motion.widget.MotionController
            r12.<init>(r11)
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r13 = r0.mFrameArrayList
            r13.put(r11, r12)
            int r13 = r11.getId()
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r14 = r0.mFrameArrayList
            java.lang.Object r14 = r14.get(r11)
            androidx.constraintlayout.motion.widget.MotionController r14 = (androidx.constraintlayout.motion.widget.MotionController) r14
            r3.put(r13, r14)
            int r10 = r10 + 1
            goto L_0x00bf
        L_0x00e1:
            r10 = 1
            r0.mInTransition = r10
            androidx.constraintlayout.motion.widget.MotionLayout$Model r11 = r0.mModel
            androidx.constraintlayout.core.widgets.ConstraintWidgetContainer r12 = r0.mLayoutWidget
            androidx.constraintlayout.motion.widget.MotionScene r13 = r0.mScene
            androidx.constraintlayout.widget.ConstraintSet r13 = r13.getConstraintSet(r4)
            r11.initFrom(r12, r2, r13)
            r22.rebuildScene()
            androidx.constraintlayout.motion.widget.MotionLayout$Model r2 = r0.mModel
            r2.build()
            r22.computeCurrentPositions()
            int r2 = r22.getWidth()
            int r17 = r22.getHeight()
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionHelper> r11 = r0.mDecoratorsHelpers
            if (r11 == 0) goto L_0x0169
            r11 = 0
        L_0x0109:
            if (r11 >= r6) goto L_0x0122
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r12 = r0.mFrameArrayList
            android.view.View r13 = r0.getChildAt(r11)
            java.lang.Object r12 = r12.get(r13)
            androidx.constraintlayout.motion.widget.MotionController r12 = (androidx.constraintlayout.motion.widget.MotionController) r12
            if (r12 != 0) goto L_0x011a
            goto L_0x011f
        L_0x011a:
            androidx.constraintlayout.motion.widget.MotionScene r13 = r0.mScene
            r13.getKeyFrames(r12)
        L_0x011f:
            int r11 = r11 + 1
            goto L_0x0109
        L_0x0122:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionHelper> r11 = r0.mDecoratorsHelpers
            java.util.Iterator r11 = r11.iterator()
        L_0x0128:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L_0x013a
            java.lang.Object r12 = r11.next()
            androidx.constraintlayout.motion.widget.MotionHelper r12 = (androidx.constraintlayout.motion.widget.MotionHelper) r12
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r13 = r0.mFrameArrayList
            r12.onPreSetup(r0, r13)
            goto L_0x0128
        L_0x013a:
            r11 = 0
            r15 = r11
        L_0x013c:
            if (r15 >= r6) goto L_0x0166
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r11 = r0.mFrameArrayList
            android.view.View r12 = r0.getChildAt(r15)
            java.lang.Object r11 = r11.get(r12)
            r18 = r11
            androidx.constraintlayout.motion.widget.MotionController r18 = (androidx.constraintlayout.motion.widget.MotionController) r18
            if (r18 != 0) goto L_0x0151
            r21 = r15
            goto L_0x0163
        L_0x0151:
            float r14 = r0.mTransitionDuration
            long r19 = r22.getNanoTime()
            r11 = r18
            r12 = r2
            r13 = r17
            r21 = r15
            r15 = r19
            r11.setup(r12, r13, r14, r15)
        L_0x0163:
            int r15 = r21 + 1
            goto L_0x013c
        L_0x0166:
            r21 = r15
            goto L_0x01a0
        L_0x0169:
            r11 = 0
            r15 = r11
        L_0x016b:
            if (r15 >= r6) goto L_0x019e
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r11 = r0.mFrameArrayList
            android.view.View r12 = r0.getChildAt(r15)
            java.lang.Object r11 = r11.get(r12)
            r14 = r11
            androidx.constraintlayout.motion.widget.MotionController r14 = (androidx.constraintlayout.motion.widget.MotionController) r14
            if (r14 != 0) goto L_0x017f
            r21 = r15
            goto L_0x019b
        L_0x017f:
            androidx.constraintlayout.motion.widget.MotionScene r11 = r0.mScene
            r11.getKeyFrames(r14)
            float r13 = r0.mTransitionDuration
            long r18 = r22.getNanoTime()
            r11 = r14
            r12 = r2
            r16 = r13
            r13 = r17
            r20 = r14
            r14 = r16
            r21 = r15
            r15 = r18
            r11.setup(r12, r13, r14, r15)
        L_0x019b:
            int r15 = r21 + 1
            goto L_0x016b
        L_0x019e:
            r21 = r15
        L_0x01a0:
            androidx.constraintlayout.motion.widget.MotionScene r11 = r0.mScene
            float r11 = r11.getStaggered()
            int r12 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1))
            if (r12 == 0) goto L_0x0206
            r12 = 2139095039(0x7f7fffff, float:3.4028235E38)
            r13 = -8388609(0xffffffffff7fffff, float:-3.4028235E38)
            r14 = 0
        L_0x01b1:
            if (r14 >= r6) goto L_0x01d8
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r15 = r0.mFrameArrayList
            android.view.View r10 = r0.getChildAt(r14)
            java.lang.Object r10 = r15.get(r10)
            androidx.constraintlayout.motion.widget.MotionController r10 = (androidx.constraintlayout.motion.widget.MotionController) r10
            float r15 = r10.getFinalX()
            float r16 = r10.getFinalY()
            float r8 = r16 + r15
            float r12 = java.lang.Math.min(r12, r8)
            float r8 = r16 + r15
            float r13 = java.lang.Math.max(r13, r8)
            int r14 = r14 + 1
            r8 = 0
            r10 = 1
            goto L_0x01b1
        L_0x01d8:
            r8 = 0
        L_0x01d9:
            if (r8 >= r6) goto L_0x0206
            java.util.HashMap<android.view.View, androidx.constraintlayout.motion.widget.MotionController> r10 = r0.mFrameArrayList
            android.view.View r14 = r0.getChildAt(r8)
            java.lang.Object r10 = r10.get(r14)
            androidx.constraintlayout.motion.widget.MotionController r10 = (androidx.constraintlayout.motion.widget.MotionController) r10
            float r14 = r10.getFinalX()
            float r15 = r10.getFinalY()
            float r16 = r9 - r11
            float r1 = r9 / r16
            r10.mStaggerScale = r1
            float r1 = r14 + r15
            float r1 = r1 - r12
            float r1 = r1 * r11
            float r16 = r13 - r12
            float r1 = r1 / r16
            float r1 = r11 - r1
            r10.mStaggerOffset = r1
            int r8 = r8 + 1
            r1 = r26
            goto L_0x01d9
        L_0x0206:
            r1 = 0
            r0.mTransitionPosition = r1
            r0.mTransitionLastPosition = r1
            r1 = 1
            r0.mInTransition = r1
            r22.invalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.transitionToState(int, int, int, int):void");
    }

    public float getVelocity() {
        return this.mLastVelocity;
    }

    public void getViewVelocity(View view, float posOnViewX, float posOnViewY, float[] returnVelocity, int type) {
        float position;
        float v = this.mLastVelocity;
        float position2 = this.mTransitionLastPosition;
        if (this.mInterpolator != null) {
            float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            float interpolatedPosition = this.mInterpolator.getInterpolation(this.mTransitionLastPosition + EPSILON);
            float position3 = this.mInterpolator.getInterpolation(this.mTransitionLastPosition);
            v = (dir * ((interpolatedPosition - position3) / EPSILON)) / this.mTransitionDuration;
            position = position3;
        } else {
            position = position2;
        }
        if (this.mInterpolator instanceof MotionInterpolator) {
            v = ((MotionInterpolator) this.mInterpolator).getVelocity();
        }
        MotionController f = this.mFrameArrayList.get(view);
        if ((type & 1) == 0) {
            f.getPostLayoutDvDp(position, view.getWidth(), view.getHeight(), posOnViewX, posOnViewY, returnVelocity);
        } else {
            f.getDpDt(position, posOnViewX, posOnViewY, returnVelocity);
        }
        if (type < 2) {
            returnVelocity[0] = returnVelocity[0] * v;
            returnVelocity[1] = returnVelocity[1] * v;
        }
    }

    class Model {
        ConstraintSet mEnd = null;
        int mEndId;
        ConstraintWidgetContainer mLayoutEnd = new ConstraintWidgetContainer();
        ConstraintWidgetContainer mLayoutStart = new ConstraintWidgetContainer();
        ConstraintSet mStart = null;
        int mStartId;

        Model() {
        }

        /* access modifiers changed from: package-private */
        public void copy(ConstraintWidgetContainer src, ConstraintWidgetContainer dest) {
            ConstraintWidget child_d;
            ArrayList<ConstraintWidget> children = src.getChildren();
            HashMap<ConstraintWidget, ConstraintWidget> map = new HashMap<>();
            map.put(src, dest);
            dest.getChildren().clear();
            dest.copy(src, map);
            Iterator<ConstraintWidget> it = children.iterator();
            while (it.hasNext()) {
                ConstraintWidget child_s = it.next();
                if (child_s instanceof Barrier) {
                    child_d = new Barrier();
                } else if (child_s instanceof Guideline) {
                    child_d = new Guideline();
                } else if (child_s instanceof Flow) {
                    child_d = new Flow();
                } else if (child_s instanceof Placeholder) {
                    child_d = new Placeholder();
                } else if (child_s instanceof Helper) {
                    child_d = new HelperWidget();
                } else {
                    child_d = new ConstraintWidget();
                }
                dest.add(child_d);
                map.put(child_s, child_d);
            }
            Iterator<ConstraintWidget> it2 = children.iterator();
            while (it2.hasNext()) {
                ConstraintWidget child_s2 = it2.next();
                map.get(child_s2).copy(child_s2, map);
            }
        }

        /* access modifiers changed from: package-private */
        public void initFrom(ConstraintWidgetContainer baseLayout, ConstraintSet start, ConstraintSet end) {
            this.mStart = start;
            this.mEnd = end;
            this.mLayoutStart = new ConstraintWidgetContainer();
            this.mLayoutEnd = new ConstraintWidgetContainer();
            this.mLayoutStart.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutEnd.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutStart.removeAllChildren();
            this.mLayoutEnd.removeAllChildren();
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutStart);
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutEnd);
            if (((double) MotionLayout.this.mTransitionLastPosition) > 0.5d) {
                if (start != null) {
                    setupConstraintWidget(this.mLayoutStart, start);
                }
                setupConstraintWidget(this.mLayoutEnd, end);
            } else {
                setupConstraintWidget(this.mLayoutEnd, end);
                if (start != null) {
                    setupConstraintWidget(this.mLayoutStart, start);
                }
            }
            this.mLayoutStart.setRtl(MotionLayout.this.isRtl());
            this.mLayoutStart.updateHierarchy();
            this.mLayoutEnd.setRtl(MotionLayout.this.isRtl());
            this.mLayoutEnd.updateHierarchy();
            ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    this.mLayoutStart.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
                if (layoutParams.height == -2) {
                    this.mLayoutStart.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
        }

        private void setupConstraintWidget(ConstraintWidgetContainer base, ConstraintSet cSet) {
            SparseArray<ConstraintWidget> mapIdToWidget = new SparseArray<>();
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            mapIdToWidget.clear();
            mapIdToWidget.put(0, base);
            mapIdToWidget.put(MotionLayout.this.getId(), base);
            if (!(cSet == null || cSet.mRotate == 0)) {
                MotionLayout.this.resolveSystem(this.mLayoutEnd, MotionLayout.this.getOptimizationLevel(), View.MeasureSpec.makeMeasureSpec(MotionLayout.this.getHeight(), BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(MotionLayout.this.getWidth(), BasicMeasure.EXACTLY));
            }
            Iterator<ConstraintWidget> it = base.getChildren().iterator();
            while (it.hasNext()) {
                ConstraintWidget child = it.next();
                child.setAnimated(true);
                mapIdToWidget.put(((View) child.getCompanionWidget()).getId(), child);
            }
            Iterator<ConstraintWidget> it2 = base.getChildren().iterator();
            while (it2.hasNext()) {
                ConstraintWidget child2 = it2.next();
                View view = (View) child2.getCompanionWidget();
                cSet.applyToLayoutParams(view.getId(), layoutParams);
                child2.setWidth(cSet.getWidth(view.getId()));
                child2.setHeight(cSet.getHeight(view.getId()));
                if (view instanceof ConstraintHelper) {
                    cSet.applyToHelper((ConstraintHelper) view, child2, layoutParams, mapIdToWidget);
                    if (view instanceof androidx.constraintlayout.widget.Barrier) {
                        ((androidx.constraintlayout.widget.Barrier) view).validateParams();
                    }
                }
                layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                MotionLayout.this.applyConstraintsFromLayoutParams(false, view, child2, layoutParams, mapIdToWidget);
                if (cSet.getVisibilityMode(view.getId()) == 1) {
                    child2.setVisibility(view.getVisibility());
                } else {
                    child2.setVisibility(cSet.getVisibility(view.getId()));
                }
            }
            Iterator<ConstraintWidget> it3 = base.getChildren().iterator();
            while (it3.hasNext()) {
                ConstraintWidget child3 = it3.next();
                if (child3 instanceof VirtualLayout) {
                    Helper helper = (Helper) child3;
                    ((ConstraintHelper) child3.getCompanionWidget()).updatePreLayout(base, helper, mapIdToWidget);
                    ((VirtualLayout) helper).captureWidgets();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public ConstraintWidget getWidget(ConstraintWidgetContainer container, View view) {
            if (container.getCompanionWidget() == view) {
                return container;
            }
            ArrayList<ConstraintWidget> children = container.getChildren();
            int count = children.size();
            for (int i = 0; i < count; i++) {
                ConstraintWidget widget = children.get(i);
                if (widget.getCompanionWidget() == view) {
                    return widget;
                }
            }
            return null;
        }

        private void debugLayoutParam(String str, ConstraintLayout.LayoutParams params) {
            String str2 = "|__";
            StringBuilder append = new StringBuilder().append(((((((((((" " + (params.startToStart != -1 ? "SS" : "__")) + (params.startToEnd != -1 ? "|SE" : str2)) + (params.endToStart != -1 ? "|ES" : str2)) + (params.endToEnd != -1 ? "|EE" : str2)) + (params.leftToLeft != -1 ? "|LL" : str2)) + (params.leftToRight != -1 ? "|LR" : str2)) + (params.rightToLeft != -1 ? "|RL" : str2)) + (params.rightToRight != -1 ? "|RR" : str2)) + (params.topToTop != -1 ? "|TT" : str2)) + (params.topToBottom != -1 ? "|TB" : str2)) + (params.bottomToTop != -1 ? "|BT" : str2));
            if (params.bottomToBottom != -1) {
                str2 = "|BB";
            }
            Log.v(MotionLayout.TAG, str + append.append(str2).toString());
        }

        private void debugWidget(String str, ConstraintWidget child) {
            String str2;
            String str3;
            String str4;
            StringBuilder append = new StringBuilder().append(" ");
            String str5 = "B";
            String str6 = "__";
            if (child.mTop.mTarget != null) {
                str2 = "T" + (child.mTop.mTarget.mType == ConstraintAnchor.Type.TOP ? "T" : str5);
            } else {
                str2 = str6;
            }
            StringBuilder append2 = new StringBuilder().append(append.append(str2).toString());
            if (child.mBottom.mTarget != null) {
                StringBuilder append3 = new StringBuilder().append(str5);
                if (child.mBottom.mTarget.mType == ConstraintAnchor.Type.TOP) {
                    str5 = "T";
                }
                str3 = append3.append(str5).toString();
            } else {
                str3 = str6;
            }
            StringBuilder append4 = new StringBuilder().append(append2.append(str3).toString());
            String str7 = "R";
            if (child.mLeft.mTarget != null) {
                str4 = "L" + (child.mLeft.mTarget.mType == ConstraintAnchor.Type.LEFT ? "L" : str7);
            } else {
                str4 = str6;
            }
            StringBuilder append5 = new StringBuilder().append(append4.append(str4).toString());
            if (child.mRight.mTarget != null) {
                StringBuilder append6 = new StringBuilder().append(str7);
                if (child.mRight.mTarget.mType == ConstraintAnchor.Type.LEFT) {
                    str7 = "L";
                }
                str6 = append6.append(str7).toString();
            }
            Log.v(MotionLayout.TAG, str + append5.append(str6).toString() + " ---  " + child);
        }

        private void debugLayout(String title, ConstraintWidgetContainer c) {
            String cName = title + " " + Debug.getName((View) c.getCompanionWidget());
            Log.v(MotionLayout.TAG, cName + "  ========= " + c);
            int count = c.getChildren().size();
            for (int i = 0; i < count; i++) {
                String str = cName + "[" + i + "] ";
                ConstraintWidget child = c.getChildren().get(i);
                String str2 = "_";
                StringBuilder append = new StringBuilder().append((("" + (child.mTop.mTarget != null ? "T" : str2)) + (child.mBottom.mTarget != null ? "B" : str2)) + (child.mLeft.mTarget != null ? "L" : str2));
                if (child.mRight.mTarget != null) {
                    str2 = "R";
                }
                String a = append.append(str2).toString();
                View v = (View) child.getCompanionWidget();
                String name = Debug.getName(v);
                if (v instanceof TextView) {
                    name = name + "(" + ((TextView) v).getText() + ")";
                }
                Log.v(MotionLayout.TAG, str + "  " + name + " " + child + " " + a);
            }
            Log.v(MotionLayout.TAG, cName + " done. ");
        }

        public void reEvaluateState() {
            measure(MotionLayout.this.mLastWidthMeasureSpec, MotionLayout.this.mLastHeightMeasureSpec);
            MotionLayout.this.setupMotionViews();
        }

        public void measure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            MotionLayout.this.mWidthMeasureMode = widthMode;
            MotionLayout.this.mHeightMeasureMode = heightMode;
            int optimizationLevel = MotionLayout.this.getOptimizationLevel();
            computeStartEndSize(widthMeasureSpec, heightMeasureSpec);
            boolean recompute_start_end_size = true;
            if ((MotionLayout.this.getParent() instanceof MotionLayout) && widthMode == 1073741824 && heightMode == 1073741824) {
                recompute_start_end_size = false;
            }
            if (recompute_start_end_size) {
                computeStartEndSize(widthMeasureSpec, heightMeasureSpec);
                MotionLayout.this.mStartWrapWidth = this.mLayoutStart.getWidth();
                MotionLayout.this.mStartWrapHeight = this.mLayoutStart.getHeight();
                MotionLayout.this.mEndWrapWidth = this.mLayoutEnd.getWidth();
                MotionLayout.this.mEndWrapHeight = this.mLayoutEnd.getHeight();
                MotionLayout.this.mMeasureDuringTransition = (MotionLayout.this.mStartWrapWidth == MotionLayout.this.mEndWrapWidth && MotionLayout.this.mStartWrapHeight == MotionLayout.this.mEndWrapHeight) ? false : true;
            }
            int width = MotionLayout.this.mStartWrapWidth;
            int height = MotionLayout.this.mStartWrapHeight;
            if (MotionLayout.this.mWidthMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mWidthMeasureMode == 0) {
                width = (int) (((float) MotionLayout.this.mStartWrapWidth) + (MotionLayout.this.mPostInterpolationPosition * ((float) (MotionLayout.this.mEndWrapWidth - MotionLayout.this.mStartWrapWidth))));
            }
            if (MotionLayout.this.mHeightMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mHeightMeasureMode == 0) {
                height = (int) (((float) MotionLayout.this.mStartWrapHeight) + (MotionLayout.this.mPostInterpolationPosition * ((float) (MotionLayout.this.mEndWrapHeight - MotionLayout.this.mStartWrapHeight))));
            }
            MotionLayout.this.resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, width, height, this.mLayoutStart.isWidthMeasuredTooSmall() || this.mLayoutEnd.isWidthMeasuredTooSmall(), this.mLayoutStart.isHeightMeasuredTooSmall() || this.mLayoutEnd.isHeightMeasuredTooSmall());
        }

        private void computeStartEndSize(int widthMeasureSpec, int heightMeasureSpec) {
            int optimisationLevel = MotionLayout.this.getOptimizationLevel();
            if (MotionLayout.this.mCurrentState == MotionLayout.this.getStartState()) {
                MotionLayout.this.resolveSystem(this.mLayoutEnd, optimisationLevel, (this.mEnd == null || this.mEnd.mRotate == 0) ? widthMeasureSpec : heightMeasureSpec, (this.mEnd == null || this.mEnd.mRotate == 0) ? heightMeasureSpec : widthMeasureSpec);
                if (this.mStart != null) {
                    MotionLayout.this.resolveSystem(this.mLayoutStart, optimisationLevel, this.mStart.mRotate == 0 ? widthMeasureSpec : heightMeasureSpec, this.mStart.mRotate == 0 ? heightMeasureSpec : widthMeasureSpec);
                    return;
                }
                return;
            }
            if (this.mStart != null) {
                MotionLayout.this.resolveSystem(this.mLayoutStart, optimisationLevel, this.mStart.mRotate == 0 ? widthMeasureSpec : heightMeasureSpec, this.mStart.mRotate == 0 ? heightMeasureSpec : widthMeasureSpec);
            }
            MotionLayout.this.resolveSystem(this.mLayoutEnd, optimisationLevel, (this.mEnd == null || this.mEnd.mRotate == 0) ? widthMeasureSpec : heightMeasureSpec, (this.mEnd == null || this.mEnd.mRotate == 0) ? heightMeasureSpec : widthMeasureSpec);
        }

        public void build() {
            SparseArray<MotionController> controllers;
            String str;
            int n = MotionLayout.this.getChildCount();
            MotionLayout.this.mFrameArrayList.clear();
            SparseArray<MotionController> controllers2 = new SparseArray<>();
            int[] ids = new int[n];
            for (int i = 0; i < n; i++) {
                View v = MotionLayout.this.getChildAt(i);
                MotionController motionController = new MotionController(v);
                int id = v.getId();
                ids[i] = id;
                controllers2.put(id, motionController);
                MotionLayout.this.mFrameArrayList.put(v, motionController);
            }
            int i2 = 0;
            while (i2 < n) {
                View v2 = MotionLayout.this.getChildAt(i2);
                MotionController motionController2 = MotionLayout.this.mFrameArrayList.get(v2);
                if (motionController2 == null) {
                    controllers = controllers2;
                } else {
                    if (this.mStart != null) {
                        ConstraintWidget startWidget = getWidget(this.mLayoutStart, v2);
                        if (startWidget != null) {
                            motionController2.setStartState(MotionLayout.this.toRect(startWidget), this.mStart, MotionLayout.this.getWidth(), MotionLayout.this.getHeight());
                        } else if (MotionLayout.this.mDebugPath != 0) {
                            Log.e(MotionLayout.TAG, Debug.getLocation() + "no widget for  " + Debug.getName(v2) + " (" + v2.getClass().getName() + ")");
                        }
                        controllers = controllers2;
                        str = MotionLayout.TAG;
                    } else if (MotionLayout.this.mInRotation) {
                        int i3 = MotionLayout.this.mRotatMode;
                        int access$2100 = MotionLayout.this.mPreRotateWidth;
                        int access$2200 = MotionLayout.this.mPreRotateHeight;
                        controllers = controllers2;
                        str = MotionLayout.TAG;
                        motionController2.setStartState(MotionLayout.this.mPreRotate.get(v2), v2, i3, access$2100, access$2200);
                    } else {
                        controllers = controllers2;
                        str = MotionLayout.TAG;
                    }
                    if (this.mEnd != null) {
                        ConstraintWidget endWidget = getWidget(this.mLayoutEnd, v2);
                        if (endWidget != null) {
                            motionController2.setEndState(MotionLayout.this.toRect(endWidget), this.mEnd, MotionLayout.this.getWidth(), MotionLayout.this.getHeight());
                        } else if (MotionLayout.this.mDebugPath != 0) {
                            Log.e(str, Debug.getLocation() + "no widget for  " + Debug.getName(v2) + " (" + v2.getClass().getName() + ")");
                        }
                    }
                }
                i2++;
                controllers2 = controllers;
            }
            SparseArray<MotionController> controllers3 = controllers2;
            int i4 = 0;
            while (i4 < n) {
                SparseArray<MotionController> controllers4 = controllers3;
                MotionController controller = controllers4.get(ids[i4]);
                int relativeToId = controller.getAnimateRelativeTo();
                if (relativeToId != -1) {
                    controller.setupRelative(controllers4.get(relativeToId));
                }
                i4++;
                controllers3 = controllers4;
            }
        }

        public void setMeasuredId(int startId, int endId) {
            this.mStartId = startId;
            this.mEndId = endId;
        }

        public boolean isNotConfiguredWith(int startId, int endId) {
            return (startId == this.mStartId && endId == this.mEndId) ? false : true;
        }
    }

    /* access modifiers changed from: private */
    public Rect toRect(ConstraintWidget cw) {
        this.mTempRect.top = cw.getY();
        this.mTempRect.left = cw.getX();
        this.mTempRect.right = cw.getWidth() + this.mTempRect.left;
        this.mTempRect.bottom = cw.getHeight() + this.mTempRect.top;
        return this.mTempRect;
    }

    public void requestLayout() {
        if (!this.mMeasureDuringTransition && this.mCurrentState == -1 && this.mScene != null && this.mScene.mCurrentTransition != null) {
            int mode = this.mScene.mCurrentTransition.getLayoutDuringTransition();
            if (mode != 0) {
                if (mode == 2) {
                    int n = getChildCount();
                    for (int i = 0; i < n; i++) {
                        this.mFrameArrayList.get(getChildAt(i)).remeasure();
                    }
                    return;
                }
            } else {
                return;
            }
        }
        super.requestLayout();
    }

    public String toString() {
        Context context = getContext();
        return Debug.getName(context, this.mBeginState) + "->" + Debug.getName(context, this.mEndState) + " (pos:" + this.mTransitionLastPosition + " Dpos/Dt:" + this.mLastVelocity;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mScene == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        boolean recalc = (this.mLastWidthMeasureSpec == widthMeasureSpec && this.mLastHeightMeasureSpec == heightMeasureSpec) ? false : true;
        if (this.mNeedsFireTransitionCompleted) {
            this.mNeedsFireTransitionCompleted = false;
            onNewStateAttachHandlers();
            processTransitionCompleted();
            recalc = true;
        }
        if (this.mDirtyHierarchy) {
            recalc = true;
        }
        this.mLastWidthMeasureSpec = widthMeasureSpec;
        this.mLastHeightMeasureSpec = heightMeasureSpec;
        int startId = this.mScene.getStartId();
        int endId = this.mScene.getEndId();
        boolean setMeasure = true;
        if ((recalc || this.mModel.isNotConfiguredWith(startId, endId)) && this.mBeginState != -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(startId), this.mScene.getConstraintSet(endId));
            this.mModel.reEvaluateState();
            this.mModel.setMeasuredId(startId, endId);
            setMeasure = false;
        } else if (recalc) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        if (this.mMeasureDuringTransition || setMeasure) {
            int heightPadding = getPaddingTop() + getPaddingBottom();
            int androidLayoutWidth = this.mLayoutWidget.getWidth() + getPaddingLeft() + getPaddingRight();
            int androidLayoutHeight = this.mLayoutWidget.getHeight() + heightPadding;
            if (this.mWidthMeasureMode == Integer.MIN_VALUE || this.mWidthMeasureMode == 0) {
                androidLayoutWidth = (int) (((float) this.mStartWrapWidth) + (this.mPostInterpolationPosition * ((float) (this.mEndWrapWidth - this.mStartWrapWidth))));
                requestLayout();
            }
            if (this.mHeightMeasureMode == Integer.MIN_VALUE || this.mHeightMeasureMode == 0) {
                androidLayoutHeight = (int) (((float) this.mStartWrapHeight) + (this.mPostInterpolationPosition * ((float) (this.mEndWrapHeight - this.mStartWrapHeight))));
                requestLayout();
            }
            setMeasuredDimension(androidLayoutWidth, androidLayoutHeight);
        }
        evaluateLayout();
    }

    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        if (this.mScene == null || this.mScene.mCurrentTransition == null || this.mScene.mCurrentTransition.getTouchResponse() == null || (this.mScene.mCurrentTransition.getTouchResponse().getFlags() & 2) != 0) {
            return false;
        }
        return true;
    }

    public void onNestedScrollAccepted(View child, View target, int axes, int type) {
        this.mScrollTargetTime = getNanoTime();
        this.mScrollTargetDT = 0.0f;
        this.mScrollTargetDX = 0.0f;
        this.mScrollTargetDY = 0.0f;
    }

    public void onStopNestedScroll(View target, int type) {
        if (this.mScene != null && this.mScrollTargetDT != 0.0f) {
            this.mScene.processScrollUp(this.mScrollTargetDX / this.mScrollTargetDT, this.mScrollTargetDY / this.mScrollTargetDT);
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        if (!(!this.mUndergoingMotion && dxConsumed == 0 && dyConsumed == 0)) {
            consumed[0] = consumed[0] + dxUnconsumed;
            consumed[1] = consumed[1] + dyUnconsumed;
        }
        this.mUndergoingMotion = false;
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        MotionScene.Transition currentTransition;
        TouchResponse touchResponse;
        int regionId;
        final View view = target;
        int i = dx;
        int i2 = dy;
        MotionScene scene = this.mScene;
        if (scene != null && (currentTransition = scene.mCurrentTransition) != null && currentTransition.isEnabled()) {
            if (!currentTransition.isEnabled() || (touchResponse = currentTransition.getTouchResponse()) == null || (regionId = touchResponse.getTouchRegionId()) == -1 || target.getId() == regionId) {
                if (scene.getMoveWhenScrollAtTop()) {
                    TouchResponse touchResponse2 = currentTransition.getTouchResponse();
                    int vert = -1;
                    if (!(touchResponse2 == null || (touchResponse2.getFlags() & 4) == 0)) {
                        vert = dy;
                    }
                    if ((this.mTransitionPosition == 1.0f || this.mTransitionPosition == 0.0f) && view.canScrollVertically(vert)) {
                        return;
                    }
                }
                if (!(currentTransition.getTouchResponse() == null || (currentTransition.getTouchResponse().getFlags() & 1) == 0)) {
                    float dir = scene.getProgressDirection((float) i, (float) i2);
                    if ((this.mTransitionLastPosition <= 0.0f && dir < 0.0f) || (this.mTransitionLastPosition >= 1.0f && dir > 0.0f)) {
                        view.setNestedScrollingEnabled(false);
                        view.post(new Runnable(this) {
                            public void run() {
                                view.setNestedScrollingEnabled(true);
                            }
                        });
                        return;
                    }
                }
                float progress = this.mTransitionPosition;
                long time = getNanoTime();
                this.mScrollTargetDX = (float) i;
                this.mScrollTargetDY = (float) i2;
                this.mScrollTargetDT = (float) (((double) (time - this.mScrollTargetTime)) * 1.0E-9d);
                this.mScrollTargetTime = time;
                scene.processScrollMove((float) i, (float) i2);
                if (progress != this.mTransitionPosition) {
                    consumed[0] = i;
                    consumed[1] = i2;
                }
                evaluate(false);
                if (consumed[0] != 0 || consumed[1] != 0) {
                    this.mUndergoingMotion = true;
                }
            }
        }
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    private class DevModeDraw {
        private static final int DEBUG_PATH_TICKS_PER_MS = 16;
        final int DIAMOND_SIZE = 10;
        final int GRAPH_COLOR = -13391360;
        final int KEYFRAME_COLOR = -2067046;
        final int RED_COLOR = -21965;
        final int SHADOW_COLOR = 1996488704;
        Rect mBounds = new Rect();
        DashPathEffect mDashPathEffect;
        Paint mFillPaint;
        int mKeyFrameCount;
        float[] mKeyFramePoints;
        Paint mPaint = new Paint();
        Paint mPaintGraph;
        Paint mPaintKeyframes;
        Path mPath;
        int[] mPathMode;
        float[] mPoints;
        boolean mPresentationMode = false;
        private float[] mRectangle;
        int mShadowTranslate = 1;
        Paint mTextPaint;

        public DevModeDraw() {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-21965);
            this.mPaint.setStrokeWidth(2.0f);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaintKeyframes = new Paint();
            this.mPaintKeyframes.setAntiAlias(true);
            this.mPaintKeyframes.setColor(-2067046);
            this.mPaintKeyframes.setStrokeWidth(2.0f);
            this.mPaintKeyframes.setStyle(Paint.Style.STROKE);
            this.mPaintGraph = new Paint();
            this.mPaintGraph.setAntiAlias(true);
            this.mPaintGraph.setColor(-13391360);
            this.mPaintGraph.setStrokeWidth(2.0f);
            this.mPaintGraph.setStyle(Paint.Style.STROKE);
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setColor(-13391360);
            this.mTextPaint.setTextSize(MotionLayout.this.getContext().getResources().getDisplayMetrics().density * 12.0f);
            this.mRectangle = new float[8];
            this.mFillPaint = new Paint();
            this.mFillPaint.setAntiAlias(true);
            this.mDashPathEffect = new DashPathEffect(new float[]{4.0f, 8.0f}, 0.0f);
            this.mPaintGraph.setPathEffect(this.mDashPathEffect);
            this.mKeyFramePoints = new float[100];
            this.mPathMode = new int[50];
            if (this.mPresentationMode) {
                this.mPaint.setStrokeWidth(8.0f);
                this.mFillPaint.setStrokeWidth(8.0f);
                this.mPaintKeyframes.setStrokeWidth(8.0f);
                this.mShadowTranslate = 4;
            }
        }

        public void draw(Canvas canvas, HashMap<View, MotionController> frameArrayList, int duration, int debugPath) {
            if (frameArrayList != null && frameArrayList.size() != 0) {
                canvas.save();
                if (!MotionLayout.this.isInEditMode() && (debugPath & 1) == 2) {
                    String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.mEndState) + ":" + MotionLayout.this.getProgress();
                    canvas.drawText(str, 10.0f, (float) (MotionLayout.this.getHeight() - 30), this.mTextPaint);
                    canvas.drawText(str, 11.0f, (float) (MotionLayout.this.getHeight() - 29), this.mPaint);
                }
                for (MotionController motionController : frameArrayList.values()) {
                    int mode = motionController.getDrawPath();
                    if (debugPath > 0 && mode == 0) {
                        mode = 1;
                    }
                    if (mode != 0) {
                        this.mKeyFrameCount = motionController.buildKeyFrames(this.mKeyFramePoints, this.mPathMode);
                        if (mode >= 1) {
                            int frames = duration / 16;
                            if (this.mPoints == null || this.mPoints.length != frames * 2) {
                                this.mPoints = new float[(frames * 2)];
                                this.mPath = new Path();
                            }
                            canvas.translate((float) this.mShadowTranslate, (float) this.mShadowTranslate);
                            this.mPaint.setColor(1996488704);
                            this.mFillPaint.setColor(1996488704);
                            this.mPaintKeyframes.setColor(1996488704);
                            this.mPaintGraph.setColor(1996488704);
                            motionController.buildPath(this.mPoints, frames);
                            drawAll(canvas, mode, this.mKeyFrameCount, motionController);
                            this.mPaint.setColor(-21965);
                            this.mPaintKeyframes.setColor(-2067046);
                            this.mFillPaint.setColor(-2067046);
                            this.mPaintGraph.setColor(-13391360);
                            canvas.translate((float) (-this.mShadowTranslate), (float) (-this.mShadowTranslate));
                            drawAll(canvas, mode, this.mKeyFrameCount, motionController);
                            if (mode == 5) {
                                drawRectangle(canvas, motionController);
                            }
                        }
                    }
                }
                canvas.restore();
            }
        }

        public void drawAll(Canvas canvas, int mode, int keyFrames, MotionController motionController) {
            if (mode == 4) {
                drawPathAsConfigured(canvas);
            }
            if (mode == 2) {
                drawPathRelative(canvas);
            }
            if (mode == 3) {
                drawPathCartesian(canvas);
            }
            drawBasicPath(canvas);
            drawTicks(canvas, mode, keyFrames, motionController);
        }

        private void drawBasicPath(Canvas canvas) {
            canvas.drawLines(this.mPoints, this.mPaint);
        }

        private void drawTicks(Canvas canvas, int mode, int keyFrames, MotionController motionController) {
            int viewHeight;
            int viewWidth;
            Canvas canvas2 = canvas;
            int i = mode;
            MotionController motionController2 = motionController;
            if (motionController2.mView != null) {
                viewWidth = motionController2.mView.getWidth();
                viewHeight = motionController2.mView.getHeight();
            } else {
                viewWidth = 0;
                viewHeight = 0;
            }
            for (int i2 = 1; i2 < keyFrames - 1; i2++) {
                if (i != 4 || this.mPathMode[i2 - 1] != 0) {
                    float x = this.mKeyFramePoints[i2 * 2];
                    float y = this.mKeyFramePoints[(i2 * 2) + 1];
                    this.mPath.reset();
                    this.mPath.moveTo(x, y + 10.0f);
                    this.mPath.lineTo(x + 10.0f, y);
                    this.mPath.lineTo(x, y - 10.0f);
                    this.mPath.lineTo(x - 10.0f, y);
                    this.mPath.close();
                    MotionPaths keyFrame = motionController2.getKeyFrame(i2 - 1);
                    if (i == 4) {
                        if (this.mPathMode[i2 - 1] == 1) {
                            drawPathRelativeTicks(canvas2, x - 0.0f, y - 0.0f);
                        } else if (this.mPathMode[i2 - 1] == 0) {
                            drawPathCartesianTicks(canvas2, x - 0.0f, y - 0.0f);
                        } else if (this.mPathMode[i2 - 1] == 2) {
                            drawPathScreenTicks(canvas, x - 0.0f, y - 0.0f, viewWidth, viewHeight);
                        }
                        canvas2.drawPath(this.mPath, this.mFillPaint);
                    }
                    if (i == 2) {
                        drawPathRelativeTicks(canvas2, x - 0.0f, y - 0.0f);
                    }
                    if (i == 3) {
                        drawPathCartesianTicks(canvas2, x - 0.0f, y - 0.0f);
                    }
                    if (i == 6) {
                        drawPathScreenTicks(canvas, x - 0.0f, y - 0.0f, viewWidth, viewHeight);
                    }
                    if (0.0f == 0.0f && 0.0f == 0.0f) {
                        canvas2.drawPath(this.mPath, this.mFillPaint);
                    } else {
                        drawTranslation(canvas, x - 0.0f, y - 0.0f, x, y);
                    }
                }
            }
            if (this.mPoints.length > 1) {
                canvas2.drawCircle(this.mPoints[0], this.mPoints[1], 8.0f, this.mPaintKeyframes);
                canvas2.drawCircle(this.mPoints[this.mPoints.length - 2], this.mPoints[this.mPoints.length - 1], 8.0f, this.mPaintKeyframes);
            }
        }

        private void drawTranslation(Canvas canvas, float x1, float y1, float x2, float y2) {
            canvas.drawRect(x1, y1, x2, y2, this.mPaintGraph);
            canvas.drawLine(x1, y1, x2, y2, this.mPaintGraph);
        }

        private void drawPathRelative(Canvas canvas) {
            canvas.drawLine(this.mPoints[0], this.mPoints[1], this.mPoints[this.mPoints.length - 2], this.mPoints[this.mPoints.length - 1], this.mPaintGraph);
        }

        private void drawPathAsConfigured(Canvas canvas) {
            boolean path = false;
            boolean cart = false;
            for (int i = 0; i < this.mKeyFrameCount; i++) {
                if (this.mPathMode[i] == 1) {
                    path = true;
                }
                if (this.mPathMode[i] == 0) {
                    cart = true;
                }
            }
            if (path) {
                drawPathRelative(canvas);
            }
            if (cart) {
                drawPathCartesian(canvas);
            }
        }

        private void drawPathRelativeTicks(Canvas canvas, float x, float y) {
            float f = x;
            float f2 = y;
            float x1 = this.mPoints[0];
            float y1 = this.mPoints[1];
            float x2 = this.mPoints[this.mPoints.length - 2];
            float y2 = this.mPoints[this.mPoints.length - 1];
            float dist = (float) Math.hypot((double) (x1 - x2), (double) (y1 - y2));
            float t = (((f - x1) * (x2 - x1)) + ((f2 - y1) * (y2 - y1))) / (dist * dist);
            float xp = x1 + ((x2 - x1) * t);
            float yp = y1 + ((y2 - y1) * t);
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(xp, yp);
            float len = (float) Math.hypot((double) (xp - f), (double) (yp - f2));
            String text = "" + (((float) ((int) ((len * 100.0f) / dist))) / 100.0f);
            getTextBounds(text, this.mTextPaint);
            canvas.drawTextOnPath(text, path, (len / 2.0f) - ((float) (this.mBounds.width() / 2)), -20.0f, this.mTextPaint);
            float f3 = len;
            Path path2 = path;
            float f4 = yp;
            canvas.drawLine(x, y, xp, yp, this.mPaintGraph);
        }

        /* access modifiers changed from: package-private */
        public void getTextBounds(String text, Paint paint) {
            paint.getTextBounds(text, 0, text.length(), this.mBounds);
        }

        private void drawPathCartesian(Canvas canvas) {
            float x1 = this.mPoints[0];
            float y1 = this.mPoints[1];
            float x2 = this.mPoints[this.mPoints.length - 2];
            float y2 = this.mPoints[this.mPoints.length - 1];
            canvas.drawLine(Math.min(x1, x2), Math.max(y1, y2), Math.max(x1, x2), Math.max(y1, y2), this.mPaintGraph);
            canvas.drawLine(Math.min(x1, x2), Math.min(y1, y2), Math.min(x1, x2), Math.max(y1, y2), this.mPaintGraph);
        }

        private void drawPathCartesianTicks(Canvas canvas, float x, float y) {
            Canvas canvas2 = canvas;
            float x1 = this.mPoints[0];
            float y1 = this.mPoints[1];
            float x2 = this.mPoints[this.mPoints.length - 2];
            float y2 = this.mPoints[this.mPoints.length - 1];
            float minx = Math.min(x1, x2);
            float maxy = Math.max(y1, y2);
            float xgap = x - Math.min(x1, x2);
            float ygap = Math.max(y1, y2) - y;
            String text = "" + (((float) ((int) (((double) ((xgap * 100.0f) / Math.abs(x2 - x1))) + 0.5d))) / 100.0f);
            getTextBounds(text, this.mTextPaint);
            canvas2.drawText(text, ((xgap / 2.0f) - ((float) (this.mBounds.width() / 2))) + minx, y - 20.0f, this.mTextPaint);
            String str = text;
            float f = x1;
            canvas.drawLine(x, y, Math.min(x1, x2), y, this.mPaintGraph);
            String text2 = "" + (((float) ((int) (((double) ((ygap * 100.0f) / Math.abs(y2 - y1))) + 0.5d))) / 100.0f);
            getTextBounds(text2, this.mTextPaint);
            canvas2.drawText(text2, x + 5.0f, maxy - ((ygap / 2.0f) - ((float) (this.mBounds.height() / 2))), this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(y1, y2), this.mPaintGraph);
        }

        private void drawPathScreenTicks(Canvas canvas, float x, float y, int viewWidth, int viewHeight) {
            Canvas canvas2 = canvas;
            float xgap = x;
            float ygap = y;
            String text = "" + (((float) ((int) (((double) (((xgap - ((float) (viewWidth / 2))) * 100.0f) / ((float) (MotionLayout.this.getWidth() - viewWidth)))) + 0.5d))) / 100.0f);
            getTextBounds(text, this.mTextPaint);
            canvas2.drawText(text, ((xgap / 2.0f) - ((float) (this.mBounds.width() / 2))) + 0.0f, y - 20.0f, this.mTextPaint);
            String str = text;
            canvas.drawLine(x, y, Math.min(0.0f, 1.0f), y, this.mPaintGraph);
            String text2 = "" + (((float) ((int) (((double) (((ygap - ((float) (viewHeight / 2))) * 100.0f) / ((float) (MotionLayout.this.getHeight() - viewHeight)))) + 0.5d))) / 100.0f);
            getTextBounds(text2, this.mTextPaint);
            canvas2.drawText(text2, x + 5.0f, 0.0f - ((ygap / 2.0f) - ((float) (this.mBounds.height() / 2))), this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(0.0f, 1.0f), this.mPaintGraph);
        }

        private void drawRectangle(Canvas canvas, MotionController motionController) {
            this.mPath.reset();
            for (int i = 0; i <= 50; i++) {
                motionController.buildRect(((float) i) / ((float) 50), this.mRectangle, 0);
                this.mPath.moveTo(this.mRectangle[0], this.mRectangle[1]);
                this.mPath.lineTo(this.mRectangle[2], this.mRectangle[3]);
                this.mPath.lineTo(this.mRectangle[4], this.mRectangle[5]);
                this.mPath.lineTo(this.mRectangle[6], this.mRectangle[7]);
                this.mPath.close();
            }
            this.mPaint.setColor(1140850688);
            canvas.translate(2.0f, 2.0f);
            canvas.drawPath(this.mPath, this.mPaint);
            canvas.translate(-2.0f, -2.0f);
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    private void debugPos() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Log.v(TAG, " " + Debug.getLocation() + " " + Debug.getName(this) + " " + Debug.getName(getContext(), this.mCurrentState) + " " + Debug.getName(child) + child.getLeft() + " " + child.getTop());
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mDecoratorsHelpers != null) {
            Iterator<MotionHelper> it = this.mDecoratorsHelpers.iterator();
            while (it.hasNext()) {
                it.next().onPreDraw(canvas);
            }
        }
        evaluate(false);
        if (!(this.mScene == null || this.mScene.mViewTransitionController == null)) {
            this.mScene.mViewTransitionController.animate();
        }
        super.dispatchDraw(canvas);
        if (this.mScene != null) {
            if ((this.mDebugPath & 1) == 1 && !isInEditMode()) {
                this.mFrames++;
                long currentDrawTime = getNanoTime();
                if (this.mLastDrawTime != -1) {
                    long delay = currentDrawTime - this.mLastDrawTime;
                    if (delay > 200000000) {
                        this.mLastFps = ((float) ((int) ((((float) this.mFrames) / (((float) delay) * 1.0E-9f)) * 100.0f))) / 100.0f;
                        this.mFrames = 0;
                        this.mLastDrawTime = currentDrawTime;
                    }
                } else {
                    this.mLastDrawTime = currentDrawTime;
                }
                Paint paint = new Paint();
                paint.setTextSize(42.0f);
                String str = (this.mLastFps + " fps " + Debug.getState(this, this.mBeginState) + " -> ") + Debug.getState(this, this.mEndState) + " (progress: " + (((float) ((int) (getProgress() * 1000.0f))) / 10.0f) + " ) state=" + (this.mCurrentState == -1 ? "undefined" : Debug.getState(this, this.mCurrentState));
                paint.setColor(ViewCompat.MEASURED_STATE_MASK);
                canvas.drawText(str, 11.0f, (float) (getHeight() - 29), paint);
                paint.setColor(-7864184);
                canvas.drawText(str, 10.0f, (float) (getHeight() - 30), paint);
            }
            if (this.mDebugPath > 1) {
                if (this.mDevModeDraw == null) {
                    this.mDevModeDraw = new DevModeDraw();
                }
                this.mDevModeDraw.draw(canvas, this.mFrameArrayList, this.mScene.getDuration(), this.mDebugPath);
            }
            if (this.mDecoratorsHelpers != null) {
                Iterator<MotionHelper> it2 = this.mDecoratorsHelpers.iterator();
                while (it2.hasNext()) {
                    it2.next().onPostDraw(canvas);
                }
            }
        }
    }

    private void evaluateLayout() {
        int i;
        float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
        long currentTime = getNanoTime();
        float deltaPos = 0.0f;
        if (!(this.mInterpolator instanceof StopLogic)) {
            deltaPos = ((((float) (currentTime - this.mTransitionLastTime)) * dir) * 1.0E-9f) / this.mTransitionDuration;
        }
        float position = this.mTransitionLastPosition + deltaPos;
        boolean done = false;
        if (this.mTransitionInstantly) {
            position = this.mTransitionGoalPosition;
        }
        if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
            position = this.mTransitionGoalPosition;
            done = true;
        }
        if (this.mInterpolator != null && !done) {
            if (this.mTemporalInterpolator) {
                position = this.mInterpolator.getInterpolation(((float) (currentTime - this.mAnimationStartTime)) * 1.0E-9f);
            } else {
                position = this.mInterpolator.getInterpolation(position);
            }
        }
        if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
            position = this.mTransitionGoalPosition;
        }
        this.mPostInterpolationPosition = position;
        int n = getChildCount();
        long time = getNanoTime();
        float interPos = this.mProgressInterpolator == null ? position : this.mProgressInterpolator.getInterpolation(position);
        int i2 = 0;
        while (i2 < n) {
            View child = getChildAt(i2);
            MotionController frame = this.mFrameArrayList.get(child);
            if (frame != null) {
                View view = child;
                i = i2;
                frame.interpolate(child, interPos, time, this.mKeyCache);
            } else {
                i = i2;
            }
            i2 = i + 1;
        }
        int i3 = i2;
        if (this.mMeasureDuringTransition) {
            requestLayout();
        }
    }

    /* access modifiers changed from: package-private */
    public void endTrigger(boolean start) {
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            MotionController frame = this.mFrameArrayList.get(getChildAt(i));
            if (frame != null) {
                frame.endTrigger(start);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void evaluate(boolean force) {
        float dir;
        int stopLogicDone;
        boolean newState;
        boolean newState2;
        if (this.mTransitionLastTime == -1) {
            this.mTransitionLastTime = getNanoTime();
        }
        if (this.mTransitionLastPosition > 0.0f && this.mTransitionLastPosition < 1.0f) {
            this.mCurrentState = -1;
        }
        boolean newState3 = false;
        if (this.mKeepAnimating || (this.mInTransition && (force || this.mTransitionGoalPosition != this.mTransitionLastPosition))) {
            float dir2 = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            long currentTime = getNanoTime();
            float deltaPos = 0.0f;
            if (!(this.mInterpolator instanceof MotionInterpolator)) {
                deltaPos = ((((float) (currentTime - this.mTransitionLastTime)) * dir2) * 1.0E-9f) / this.mTransitionDuration;
            }
            float position = this.mTransitionLastPosition + deltaPos;
            boolean done = false;
            if (this.mTransitionInstantly) {
                position = this.mTransitionGoalPosition;
            }
            if ((dir2 > 0.0f && position >= this.mTransitionGoalPosition) || (dir2 <= 0.0f && position <= this.mTransitionGoalPosition)) {
                position = this.mTransitionGoalPosition;
                this.mInTransition = false;
                done = true;
            }
            this.mTransitionLastPosition = position;
            this.mTransitionPosition = position;
            this.mTransitionLastTime = currentTime;
            if (this.mInterpolator == null || done) {
                dir = dir2;
                this.mLastVelocity = deltaPos;
                stopLogicDone = 0;
            } else if (this.mTemporalInterpolator) {
                dir = dir2;
                float position2 = this.mInterpolator.getInterpolation(((float) (currentTime - this.mAnimationStartTime)) * 1.0E-9f);
                if (this.mInterpolator == this.mStopLogic) {
                    stopLogicDone = this.mStopLogic.isStopped() ? 2 : 1;
                } else {
                    stopLogicDone = 0;
                }
                this.mTransitionLastPosition = position2;
                this.mTransitionLastTime = currentTime;
                if (this.mInterpolator instanceof MotionInterpolator) {
                    float lastVelocity = ((MotionInterpolator) this.mInterpolator).getVelocity();
                    this.mLastVelocity = lastVelocity;
                    if (Math.abs(lastVelocity) * this.mTransitionDuration <= EPSILON && stopLogicDone == 2) {
                        this.mInTransition = false;
                    }
                    if (lastVelocity > 0.0f && position2 >= 1.0f) {
                        position2 = 1.0f;
                        this.mTransitionLastPosition = 1.0f;
                        this.mInTransition = false;
                    }
                    if (lastVelocity >= 0.0f || position2 > 0.0f) {
                        position = position2;
                    } else {
                        this.mTransitionLastPosition = 0.0f;
                        this.mInTransition = false;
                        position = 0.0f;
                    }
                } else {
                    position = position2;
                }
            } else {
                dir = dir2;
                float p2 = position;
                position = this.mInterpolator.getInterpolation(position);
                if (this.mInterpolator instanceof MotionInterpolator) {
                    this.mLastVelocity = ((MotionInterpolator) this.mInterpolator).getVelocity();
                } else {
                    this.mLastVelocity = ((this.mInterpolator.getInterpolation(p2 + deltaPos) - position) * dir) / deltaPos;
                }
                stopLogicDone = 0;
            }
            if (Math.abs(this.mLastVelocity) > EPSILON) {
                setState(TransitionState.MOVING);
            }
            if (stopLogicDone != 1) {
                if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
                    position = this.mTransitionGoalPosition;
                    this.mInTransition = false;
                }
                if (position >= 1.0f || position <= 0.0f) {
                    this.mInTransition = false;
                    setState(TransitionState.FINISHED);
                }
            }
            int n = getChildCount();
            this.mKeepAnimating = false;
            long time = getNanoTime();
            this.mPostInterpolationPosition = position;
            float interPos = this.mProgressInterpolator == null ? position : this.mProgressInterpolator.getInterpolation(position);
            if (this.mProgressInterpolator != null) {
                this.mLastVelocity = this.mProgressInterpolator.getInterpolation((dir / this.mTransitionDuration) + position);
                this.mLastVelocity -= this.mProgressInterpolator.getInterpolation(position);
            }
            int i = 0;
            while (i < n) {
                View child = getChildAt(i);
                MotionController frame = this.mFrameArrayList.get(child);
                if (frame != null) {
                    newState2 = newState3;
                    this.mKeepAnimating = frame.interpolate(child, interPos, time, this.mKeyCache) | this.mKeepAnimating;
                } else {
                    newState2 = newState3;
                }
                i++;
                newState3 = newState2;
            }
            boolean newState4 = newState3;
            boolean end = (dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition);
            if (!this.mKeepAnimating && !this.mInTransition && end) {
                setState(TransitionState.FINISHED);
            }
            if (this.mMeasureDuringTransition) {
                requestLayout();
            }
            this.mKeepAnimating |= !end;
            if (position > 0.0f || this.mBeginState == -1 || this.mCurrentState == this.mBeginState) {
                newState = newState4;
            } else {
                newState = true;
                this.mCurrentState = this.mBeginState;
                this.mScene.getConstraintSet(this.mBeginState).applyCustomAttributes(this);
                setState(TransitionState.FINISHED);
            }
            boolean newState5 = newState;
            boolean z = end;
            if (((double) position) < 1.0d || this.mCurrentState == this.mEndState) {
                newState3 = newState5;
            } else {
                newState3 = true;
                this.mCurrentState = this.mEndState;
                this.mScene.getConstraintSet(this.mEndState).applyCustomAttributes(this);
                setState(TransitionState.FINISHED);
            }
            if (this.mKeepAnimating || this.mInTransition) {
                invalidate();
            } else if ((dir > 0.0f && position == 1.0f) || (dir < 0.0f && position == 0.0f)) {
                setState(TransitionState.FINISHED);
            }
            if (!this.mKeepAnimating && !this.mInTransition && ((dir > 0.0f && position == 1.0f) || (dir < 0.0f && position == 0.0f))) {
                onNewStateAttachHandlers();
            }
        }
        if (this.mTransitionLastPosition >= 1.0f) {
            if (this.mCurrentState != this.mEndState) {
                newState3 = true;
            }
            this.mCurrentState = this.mEndState;
        } else if (this.mTransitionLastPosition <= 0.0f) {
            if (this.mCurrentState != this.mBeginState) {
                newState3 = true;
            }
            this.mCurrentState = this.mBeginState;
        }
        this.mNeedsFireTransitionCompleted |= newState3;
        if (newState3 && !this.mInLayout) {
            requestLayout();
        }
        this.mTransitionPosition = this.mTransitionLastPosition;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mInLayout = true;
        try {
            if (this.mScene == null) {
                super.onLayout(changed, left, top, right, bottom);
                return;
            }
            int w = right - left;
            int h = bottom - top;
            if (!(this.mLastLayoutWidth == w && this.mLastLayoutHeight == h)) {
                rebuildScene();
                evaluate(true);
            }
            this.mLastLayoutWidth = w;
            this.mLastLayoutHeight = h;
            this.mOldWidth = w;
            this.mOldHeight = h;
            this.mInLayout = false;
        } finally {
            this.mInLayout = false;
        }
    }

    /* access modifiers changed from: protected */
    public void parseLayoutDescription(int id) {
        this.mConstraintLayoutSpec = null;
    }

    private void init(AttributeSet attrs) {
        IS_IN_EDIT_MODE = isInEditMode();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MotionLayout);
            int N = a.getIndexCount();
            boolean apply = true;
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.MotionLayout_layoutDescription) {
                    this.mScene = new MotionScene(getContext(), this, a.getResourceId(attr, -1));
                } else if (attr == R.styleable.MotionLayout_currentState) {
                    this.mCurrentState = a.getResourceId(attr, -1);
                } else if (attr == R.styleable.MotionLayout_motionProgress) {
                    this.mTransitionGoalPosition = a.getFloat(attr, 0.0f);
                    this.mInTransition = true;
                } else if (attr == R.styleable.MotionLayout_applyMotionScene) {
                    apply = a.getBoolean(attr, apply);
                } else {
                    int i2 = 0;
                    if (attr == R.styleable.MotionLayout_showPaths) {
                        if (this.mDebugPath == 0) {
                            if (a.getBoolean(attr, false)) {
                                i2 = 2;
                            }
                            this.mDebugPath = i2;
                        }
                    } else if (attr == R.styleable.MotionLayout_motionDebug) {
                        this.mDebugPath = a.getInt(attr, 0);
                    }
                }
            }
            a.recycle();
            if (this.mScene == null) {
                Log.e(TAG, "WARNING NO app:layoutDescription tag");
            }
            if (!apply) {
                this.mScene = null;
            }
        }
        if (this.mDebugPath != 0) {
            checkStructure();
        }
        if (this.mCurrentState == -1 && this.mScene != null) {
            this.mCurrentState = this.mScene.getStartId();
            this.mBeginState = this.mScene.getStartId();
            this.mEndState = this.mScene.getEndId();
        }
    }

    public void setScene(MotionScene scene) {
        this.mScene = scene;
        this.mScene.setRtl(isRtl());
        rebuildScene();
    }

    public MotionScene getScene() {
        return this.mScene;
    }

    private void checkStructure() {
        if (this.mScene == null) {
            Log.e(TAG, "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            return;
        }
        checkStructure(this.mScene.getStartId(), this.mScene.getConstraintSet(this.mScene.getStartId()));
        SparseIntArray startToEnd = new SparseIntArray();
        SparseIntArray endToStart = new SparseIntArray();
        Iterator<MotionScene.Transition> it = this.mScene.getDefinedTransitions().iterator();
        while (it.hasNext()) {
            MotionScene.Transition definedTransition = it.next();
            if (definedTransition == this.mScene.mCurrentTransition) {
                Log.v(TAG, "CHECK: CURRENT");
            }
            checkStructure(definedTransition);
            int startId = definedTransition.getStartConstraintSetId();
            int endId = definedTransition.getEndConstraintSetId();
            String startString = Debug.getName(getContext(), startId);
            String endString = Debug.getName(getContext(), endId);
            if (startToEnd.get(startId) == endId) {
                Log.e(TAG, "CHECK: two transitions with the same start and end " + startString + "->" + endString);
            }
            if (endToStart.get(endId) == startId) {
                Log.e(TAG, "CHECK: you can't have reverse transitions" + startString + "->" + endString);
            }
            startToEnd.put(startId, endId);
            endToStart.put(endId, startId);
            if (this.mScene.getConstraintSet(startId) == null) {
                Log.e(TAG, " no such constraintSetStart " + startString);
            }
            if (this.mScene.getConstraintSet(endId) == null) {
                Log.e(TAG, " no such constraintSetEnd " + startString);
            }
        }
    }

    private void checkStructure(int csetId, ConstraintSet set) {
        String setName = Debug.getName(getContext(), csetId);
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View v = getChildAt(i);
            int id = v.getId();
            if (id == -1) {
                Log.w(TAG, "CHECK: " + setName + " ALL VIEWS SHOULD HAVE ID's " + v.getClass().getName() + " does not!");
            }
            if (set.getConstraint(id) == null) {
                Log.w(TAG, "CHECK: " + setName + " NO CONSTRAINTS for " + Debug.getName(v));
            }
        }
        int[] ids = set.getKnownIds();
        for (int i2 = 0; i2 < ids.length; i2++) {
            int id2 = ids[i2];
            String idString = Debug.getName(getContext(), id2);
            if (findViewById(ids[i2]) == null) {
                Log.w(TAG, "CHECK: " + setName + " NO View matches id " + idString);
            }
            if (set.getHeight(id2) == -1) {
                Log.w(TAG, "CHECK: " + setName + "(" + idString + ") no LAYOUT_HEIGHT");
            }
            if (set.getWidth(id2) == -1) {
                Log.w(TAG, "CHECK: " + setName + "(" + idString + ") no LAYOUT_HEIGHT");
            }
        }
    }

    private void checkStructure(MotionScene.Transition transition) {
        if (transition.getStartConstraintSetId() == transition.getEndConstraintSetId()) {
            Log.e(TAG, "CHECK: start and end constraint set should not be the same!");
        }
    }

    public void setDebugMode(int debugMode) {
        this.mDebugPath = debugMode;
        invalidate();
    }

    public void getDebugMode(boolean showPaths) {
        this.mDebugPath = showPaths ? 2 : 1;
        invalidate();
    }

    private boolean callTransformedTouchEvent(View view, MotionEvent event, float offsetX, float offsetY) {
        Matrix viewMatrix = view.getMatrix();
        if (viewMatrix.isIdentity()) {
            event.offsetLocation(offsetX, offsetY);
            boolean handled = view.onTouchEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
            return handled;
        }
        MotionEvent transformedEvent = MotionEvent.obtain(event);
        transformedEvent.offsetLocation(offsetX, offsetY);
        if (this.mInverseMatrix == null) {
            this.mInverseMatrix = new Matrix();
        }
        viewMatrix.invert(this.mInverseMatrix);
        transformedEvent.transform(this.mInverseMatrix);
        boolean handled2 = view.onTouchEvent(transformedEvent);
        transformedEvent.recycle();
        return handled2;
    }

    private boolean handlesTouchEvent(float x, float y, View view, MotionEvent event) {
        boolean handled = false;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int i = group.getChildCount() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                View child = group.getChildAt(i);
                if (handlesTouchEvent((((float) child.getLeft()) + x) - ((float) view.getScrollX()), (((float) child.getTop()) + y) - ((float) view.getScrollY()), child, event)) {
                    handled = true;
                    break;
                }
                i--;
            }
        }
        if (handled) {
            return handled;
        }
        this.mBoundsCheck.set(x, y, (((float) view.getRight()) + x) - ((float) view.getLeft()), (((float) view.getBottom()) + y) - ((float) view.getTop()));
        if ((event.getAction() != 0 || this.mBoundsCheck.contains(event.getX(), event.getY())) && callTransformedTouchEvent(view, event, -x, -y)) {
            return true;
        }
        return handled;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        TouchResponse touchResponse;
        int regionId;
        RectF region;
        if (this.mScene == null || !this.mInteractionEnabled) {
            return false;
        }
        if (this.mScene.mViewTransitionController != null) {
            this.mScene.mViewTransitionController.touchEvent(event);
        }
        MotionScene.Transition currentTransition = this.mScene.mCurrentTransition;
        if (currentTransition != null && currentTransition.isEnabled() && (touchResponse = currentTransition.getTouchResponse()) != null && ((event.getAction() != 0 || (region = touchResponse.getTouchRegion(this, new RectF())) == null || region.contains(event.getX(), event.getY())) && (regionId = touchResponse.getTouchRegionId()) != -1)) {
            if (this.mRegionView == null || this.mRegionView.getId() != regionId) {
                this.mRegionView = findViewById(regionId);
            }
            if (this.mRegionView != null) {
                this.mBoundsCheck.set((float) this.mRegionView.getLeft(), (float) this.mRegionView.getTop(), (float) this.mRegionView.getRight(), (float) this.mRegionView.getBottom());
                if (this.mBoundsCheck.contains(event.getX(), event.getY()) && !handlesTouchEvent((float) this.mRegionView.getLeft(), (float) this.mRegionView.getTop(), this.mRegionView, event)) {
                    return onTouchEvent(event);
                }
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mScene == null || !this.mInteractionEnabled || !this.mScene.supportTouch()) {
            return super.onTouchEvent(event);
        }
        MotionScene.Transition currentTransition = this.mScene.mCurrentTransition;
        if (currentTransition != null && !currentTransition.isEnabled()) {
            return super.onTouchEvent(event);
        }
        this.mScene.processTouchEvent(event, getCurrentState(), this);
        if (this.mScene.mCurrentTransition.isTransitionFlag(4)) {
            return this.mScene.mCurrentTransition.getTouchResponse().isDragStarted();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Display display = getDisplay();
        if (display != null) {
            this.mPreviouseRotation = display.getRotation();
        }
        if (!(this.mScene == null || this.mCurrentState == -1)) {
            ConstraintSet cSet = this.mScene.getConstraintSet(this.mCurrentState);
            this.mScene.readFallback(this);
            if (this.mDecoratorsHelpers != null) {
                Iterator<MotionHelper> it = this.mDecoratorsHelpers.iterator();
                while (it.hasNext()) {
                    it.next().onFinishedMotionScene(this);
                }
            }
            if (cSet != null) {
                cSet.applyTo(this);
            }
            this.mBeginState = this.mCurrentState;
        }
        onNewStateAttachHandlers();
        if (this.mStateCache != null) {
            if (this.mDelayedApply) {
                post(new Runnable() {
                    public void run() {
                        MotionLayout.this.mStateCache.apply();
                    }
                });
            } else {
                this.mStateCache.apply();
            }
        } else if (this.mScene != null && this.mScene.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
            transitionToEnd();
            setState(TransitionState.SETUP);
            setState(TransitionState.MOVING);
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (this.mScene != null) {
            this.mScene.setRtl(isRtl());
        }
    }

    /* access modifiers changed from: package-private */
    public void onNewStateAttachHandlers() {
        if (this.mScene != null) {
            if (this.mScene.autoTransition(this, this.mCurrentState)) {
                requestLayout();
                return;
            }
            if (this.mCurrentState != -1) {
                this.mScene.addOnClickListeners(this, this.mCurrentState);
            }
            if (this.mScene.supportTouch()) {
                this.mScene.setupTouch();
            }
        }
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public float getProgress() {
        return this.mTransitionLastPosition;
    }

    /* access modifiers changed from: package-private */
    public void getAnchorDpDt(int mTouchAnchorId, float pos, float locationX, float locationY, float[] mAnchorDpDt) {
        String idName;
        HashMap<View, MotionController> hashMap = this.mFrameArrayList;
        View viewById = getViewById(mTouchAnchorId);
        View v = viewById;
        MotionController f = hashMap.get(viewById);
        if (f != null) {
            f.getDpDt(pos, locationX, locationY, mAnchorDpDt);
            float y = v.getY();
            float deltaPos = pos - this.lastPos;
            float deltaY = y - this.lastY;
            if (deltaPos != 0.0f) {
                float f2 = deltaY / deltaPos;
            }
            this.lastPos = pos;
            this.lastY = y;
            return;
        }
        if (v == null) {
            idName = "" + mTouchAnchorId;
        } else {
            idName = v.getContext().getResources().getResourceName(mTouchAnchorId);
        }
        Log.w(TAG, "WARNING could not find view id " + idName);
    }

    public long getTransitionTimeMs() {
        if (this.mScene != null) {
            this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
        }
        return (long) (this.mTransitionDuration * 1000.0f);
    }

    public void setTransitionListener(TransitionListener listener) {
        this.mTransitionListener = listener;
    }

    public void addTransitionListener(TransitionListener listener) {
        if (this.mTransitionListeners == null) {
            this.mTransitionListeners = new CopyOnWriteArrayList<>();
        }
        this.mTransitionListeners.add(listener);
    }

    public boolean removeTransitionListener(TransitionListener listener) {
        if (this.mTransitionListeners == null) {
            return false;
        }
        return this.mTransitionListeners.remove(listener);
    }

    public void fireTrigger(int triggerId, boolean positive, float progress) {
        if (this.mTransitionListener != null) {
            this.mTransitionListener.onTransitionTrigger(this, triggerId, positive, progress);
        }
        if (this.mTransitionListeners != null) {
            Iterator<TransitionListener> it = this.mTransitionListeners.iterator();
            while (it.hasNext()) {
                it.next().onTransitionTrigger(this, triggerId, positive, progress);
            }
        }
    }

    private void fireTransitionChange() {
        if ((this.mTransitionListener != null || (this.mTransitionListeners != null && !this.mTransitionListeners.isEmpty())) && this.mListenerPosition != this.mTransitionPosition) {
            if (this.mListenerState != -1) {
                if (this.mTransitionListener != null) {
                    this.mTransitionListener.onTransitionStarted(this, this.mBeginState, this.mEndState);
                }
                if (this.mTransitionListeners != null) {
                    Iterator<TransitionListener> it = this.mTransitionListeners.iterator();
                    while (it.hasNext()) {
                        it.next().onTransitionStarted(this, this.mBeginState, this.mEndState);
                    }
                }
                this.mIsAnimating = true;
            }
            this.mListenerState = -1;
            this.mListenerPosition = this.mTransitionPosition;
            if (this.mTransitionListener != null) {
                this.mTransitionListener.onTransitionChange(this, this.mBeginState, this.mEndState, this.mTransitionPosition);
            }
            if (this.mTransitionListeners != null) {
                Iterator<TransitionListener> it2 = this.mTransitionListeners.iterator();
                while (it2.hasNext()) {
                    it2.next().onTransitionChange(this, this.mBeginState, this.mEndState, this.mTransitionPosition);
                }
            }
            this.mIsAnimating = true;
        }
    }

    /* access modifiers changed from: protected */
    public void fireTransitionCompleted() {
        if ((this.mTransitionListener != null || (this.mTransitionListeners != null && !this.mTransitionListeners.isEmpty())) && this.mListenerState == -1) {
            this.mListenerState = this.mCurrentState;
            int lastState = -1;
            if (!this.mTransitionCompleted.isEmpty()) {
                lastState = this.mTransitionCompleted.get(this.mTransitionCompleted.size() - 1).intValue();
            }
            if (!(lastState == this.mCurrentState || this.mCurrentState == -1)) {
                this.mTransitionCompleted.add(Integer.valueOf(this.mCurrentState));
            }
        }
        processTransitionCompleted();
        if (this.mOnComplete != null) {
            this.mOnComplete.run();
        }
        if (this.mScheduledTransitionTo != null && this.mScheduledTransitions > 0) {
            transitionToState(this.mScheduledTransitionTo[0]);
            System.arraycopy(this.mScheduledTransitionTo, 1, this.mScheduledTransitionTo, 0, this.mScheduledTransitionTo.length - 1);
            this.mScheduledTransitions--;
        }
    }

    private void processTransitionCompleted() {
        if (this.mTransitionListener != null || (this.mTransitionListeners != null && !this.mTransitionListeners.isEmpty())) {
            this.mIsAnimating = false;
            Iterator<Integer> it = this.mTransitionCompleted.iterator();
            while (it.hasNext()) {
                Integer state = it.next();
                if (this.mTransitionListener != null) {
                    this.mTransitionListener.onTransitionCompleted(this, state.intValue());
                }
                if (this.mTransitionListeners != null) {
                    Iterator<TransitionListener> it2 = this.mTransitionListeners.iterator();
                    while (it2.hasNext()) {
                        it2.next().onTransitionCompleted(this, state.intValue());
                    }
                }
            }
            this.mTransitionCompleted.clear();
        }
    }

    public DesignTool getDesignTool() {
        if (this.mDesignTool == null) {
            this.mDesignTool = new DesignTool(this);
        }
        return this.mDesignTool;
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            MotionHelper helper = (MotionHelper) view;
            if (this.mTransitionListeners == null) {
                this.mTransitionListeners = new CopyOnWriteArrayList<>();
            }
            this.mTransitionListeners.add(helper);
            if (helper.isUsedOnShow()) {
                if (this.mOnShowHelpers == null) {
                    this.mOnShowHelpers = new ArrayList<>();
                }
                this.mOnShowHelpers.add(helper);
            }
            if (helper.isUseOnHide()) {
                if (this.mOnHideHelpers == null) {
                    this.mOnHideHelpers = new ArrayList<>();
                }
                this.mOnHideHelpers.add(helper);
            }
            if (helper.isDecorator()) {
                if (this.mDecoratorsHelpers == null) {
                    this.mDecoratorsHelpers = new ArrayList<>();
                }
                this.mDecoratorsHelpers.add(helper);
            }
        }
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (this.mOnShowHelpers != null) {
            this.mOnShowHelpers.remove(view);
        }
        if (this.mOnHideHelpers != null) {
            this.mOnHideHelpers.remove(view);
        }
    }

    public void setOnShow(float progress) {
        if (this.mOnShowHelpers != null) {
            int count = this.mOnShowHelpers.size();
            for (int i = 0; i < count; i++) {
                this.mOnShowHelpers.get(i).setProgress(progress);
            }
        }
    }

    public void setOnHide(float progress) {
        if (this.mOnHideHelpers != null) {
            int count = this.mOnHideHelpers.size();
            for (int i = 0; i < count; i++) {
                this.mOnHideHelpers.get(i).setProgress(progress);
            }
        }
    }

    public int[] getConstraintSetIds() {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.getConstraintSetIds();
    }

    public ConstraintSet getConstraintSet(int id) {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.getConstraintSet(id);
    }

    public ConstraintSet cloneConstraintSet(int id) {
        if (this.mScene == null) {
            return null;
        }
        ConstraintSet orig = this.mScene.getConstraintSet(id);
        ConstraintSet ret = new ConstraintSet();
        ret.clone(orig);
        return ret;
    }

    @Deprecated
    public void rebuildMotion() {
        Log.e(TAG, "This method is deprecated. Please call rebuildScene() instead.");
        rebuildScene();
    }

    public void rebuildScene() {
        this.mModel.reEvaluateState();
        invalidate();
    }

    public void updateState(int stateId, ConstraintSet set) {
        if (this.mScene != null) {
            this.mScene.setConstraintSet(stateId, set);
        }
        updateState();
        if (this.mCurrentState == stateId) {
            set.applyTo(this);
        }
    }

    public void updateStateAnimate(int stateId, ConstraintSet set, int duration) {
        if (this.mScene != null && this.mCurrentState == stateId) {
            updateState(R.id.view_transition, getConstraintSet(stateId));
            setState(R.id.view_transition, -1, -1);
            updateState(stateId, set);
            MotionScene.Transition tmpTransition = new MotionScene.Transition(-1, this.mScene, R.id.view_transition, stateId);
            tmpTransition.setDuration(duration);
            setTransition(tmpTransition);
            transitionToEnd();
        }
    }

    public void scheduleTransitionTo(int id) {
        if (getCurrentState() == -1) {
            transitionToState(id);
            return;
        }
        if (this.mScheduledTransitionTo == null) {
            this.mScheduledTransitionTo = new int[4];
        } else if (this.mScheduledTransitionTo.length <= this.mScheduledTransitions) {
            this.mScheduledTransitionTo = Arrays.copyOf(this.mScheduledTransitionTo, this.mScheduledTransitionTo.length * 2);
        }
        int[] iArr = this.mScheduledTransitionTo;
        int i = this.mScheduledTransitions;
        this.mScheduledTransitions = i + 1;
        iArr[i] = id;
    }

    public void updateState() {
        this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
        rebuildScene();
    }

    public ArrayList<MotionScene.Transition> getDefinedTransitions() {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.getDefinedTransitions();
    }

    public int getStartState() {
        return this.mBeginState;
    }

    public int getEndState() {
        return this.mEndState;
    }

    public float getTargetPosition() {
        return this.mTransitionGoalPosition;
    }

    public void setTransitionDuration(int milliseconds) {
        if (this.mScene == null) {
            Log.e(TAG, "MotionScene not defined");
        } else {
            this.mScene.setDuration(milliseconds);
        }
    }

    public MotionScene.Transition getTransition(int id) {
        return this.mScene.getTransitionById(id);
    }

    /* access modifiers changed from: package-private */
    public int lookUpConstraintId(String id) {
        if (this.mScene == null) {
            return 0;
        }
        return this.mScene.lookUpConstraintId(id);
    }

    /* access modifiers changed from: package-private */
    public String getConstraintSetNames(int id) {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.lookUpConstraintName(id);
    }

    /* access modifiers changed from: package-private */
    public void disableAutoTransition(boolean disable) {
        if (this.mScene != null) {
            this.mScene.disableAutoTransition(disable);
        }
    }

    public void setInteractionEnabled(boolean enabled) {
        this.mInteractionEnabled = enabled;
    }

    public boolean isInteractionEnabled() {
        return this.mInteractionEnabled;
    }

    private void fireTransitionStarted(MotionLayout motionLayout, int mBeginState2, int mEndState2) {
        if (this.mTransitionListener != null) {
            this.mTransitionListener.onTransitionStarted(this, mBeginState2, mEndState2);
        }
        if (this.mTransitionListeners != null) {
            Iterator<TransitionListener> it = this.mTransitionListeners.iterator();
            while (it.hasNext()) {
                it.next().onTransitionStarted(motionLayout, mBeginState2, mEndState2);
            }
        }
    }

    public void viewTransition(int viewTransitionId, View... view) {
        if (this.mScene != null) {
            this.mScene.viewTransition(viewTransitionId, view);
        } else {
            Log.e(TAG, " no motionScene");
        }
    }

    public void enableViewTransition(int viewTransitionId, boolean enable) {
        if (this.mScene != null) {
            this.mScene.enableViewTransition(viewTransitionId, enable);
        }
    }

    public boolean isViewTransitionEnabled(int viewTransitionId) {
        if (this.mScene != null) {
            return this.mScene.isViewTransitionEnabled(viewTransitionId);
        }
        return false;
    }

    public boolean applyViewTransition(int viewTransitionId, MotionController motionController) {
        if (this.mScene != null) {
            return this.mScene.applyViewTransition(viewTransitionId, motionController);
        }
        return false;
    }

    public boolean isDelayedApplicationOfInitialState() {
        return this.mDelayedApply;
    }

    public void setDelayedApplicationOfInitialState(boolean delayedApply) {
        this.mDelayedApply = delayedApply;
    }
}
