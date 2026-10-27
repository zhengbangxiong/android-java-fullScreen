package app.gkai.ai.modules.home.ui.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround3 {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497

    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    private int statusBar = 0;

    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround3(activity);
    }


    private View mChildOfContent;


    private int usableHeightPrevious;


    private FrameLayout.LayoutParams frameLayoutParams;


    private AndroidBug5497Workaround3(Activity activity) {
        statusBar = getStatusBarHeight(activity);

        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);

        mChildOfContent = content.getChildAt(0);


        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            public void onGlobalLayout() {

                possiblyResizeChildOfContent();

            }


        });

        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();

    }


    private void possiblyResizeChildOfContent() {

        int usableHeightNow = computeUsableHeight();

        if (usableHeightNow != usableHeightPrevious) {

            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();

            int heightDifference = usableHeightSansKeyboard - usableHeightNow;

            if (heightDifference > (usableHeightSansKeyboard / 4)) {

                // keyboard probably just became visible

                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;

            } else {

                // keyboard probably just became hidden

                frameLayoutParams.height = usableHeightSansKeyboard;

            }

            mChildOfContent.requestLayout();

            usableHeightPrevious = usableHeightNow;

        }

    }


    private int computeUsableHeight() {

        Rect r = new Rect();

        mChildOfContent.getWindowVisibleDisplayFrame(r);

        return (r.bottom - r.top  + statusBar);// 全屏模式下： return r.bottom

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
