package app.gkai.ai.modules.home.ui.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class AndroidBug5497Workaround {

    private int statusBar = 0;

    /**
     * 关联要监听的视图
     *
     * @param viewObserving
     */
    public static void assistActivity(View viewObserving, Activity activity) {
        new AndroidBug5497Workaround(viewObserving, activity);
    }

    private View mViewObserved;//被监听的视图
    private int                    usableHeightPrevious;//视图变化前的可用高度
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(View viewObserving, Activity activity) {
        statusBar = getStatusBarHeight(activity);

        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                resetLayoutByUsableHeight(computeUsableHeight());
            }
        });
        frameLayoutParams = mViewObserved.getLayoutParams();
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            int fullHeight = mViewObserved.getRootView().getHeight();
            int heightDifference = fullHeight - usableHeightNow;

            if (heightDifference > fullHeight / 4) {
                // The keyboard is visible: keep the original workaround and resize above it.
                frameLayoutParams.height = usableHeightNow;
            } else {
                // A small difference is the status/navigation inset, not the keyboard. Keeping
                // usableHeightNow here leaves the gesture-navigation area showing the black
                // parent background instead of WebView content.
                frameLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top + statusBar);
    }


    public int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
