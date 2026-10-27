package app.gkai.ai.core.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @ author FX
 * @ date  2018/11/19  17:18
 * @ fuction
 */
public class ScrollGridView extends GridView {


    public ScrollGridView(Context context) {
        super(context);
    }

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }

}
