package app.gkai.ai.core.common.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import app.gkai.ai.core.beans.Page;
import app.gkai.ai.modules.home.beans.resp.ArticleResp;


/**
 * 滚动的TextView
 */
public class ScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private Context mContext;

    //mInUp,mOutUp分别构成向下翻页的进出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    private boolean toRun;

    private static int RUN = 9527;

    /**
     * 滚动间隔
     */
    private int mScrollInterval = 6000;


    /**
     * 轮播文字数组
     */
    private Page<ArticleResp> articlePage;

    /**
     * 当前显示的位置
     */
    private int mPosition;
    
    private Handler upHandler;

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        init();

    }

    private void init() {

        setFactory(this);

        mInUp = createAnim(true, true);
        mOutUp = createAnim(false, true);

        setInAnimation(mInUp);//当View显示时动画资源ID
        setOutAnimation(mOutUp);//当View隐藏是动画资源ID。
        
    }

    private Rotate3dAnimation createAnim( boolean turnIn, boolean turnUp){

        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        rotation.setDuration(2000);//执行动画的时间
        rotation.setFillAfter(false);//是否保持动画完毕之后的状态
//        rotation.setInterpolator(new AccelerateInterpolator());//设置加速模式
        return rotation;
    }


    //这里返回的TextView，就是我们看到的View,可以设置自己想要的效果
    public View makeView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(14);
//        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.rgb(57, 56, 56));
        textView.setSingleLine();
        return textView;

    }

    /**
     * 设置轮播文字数组
     */
    public void setItems(Page<ArticleResp> articlePage, Handler upHandler){

        if(articlePage == null){
            throw new IllegalArgumentException("轮播文字数组不能为null！");
        }

        this.articlePage = articlePage;
        this.upHandler = upHandler;
        mPosition = 0;

        if(articlePage.getContent().size() > 0){
            setText(articlePage.getContent().get(mPosition).getTitle().toString());
//            setText(articlePage.get(mPosition).get("home_name").toString());
        }else{
            throw new IllegalArgumentException("轮播文字数组没有内容！");
        }
    }

    /**
     * 开始滚动
     */
    public void startScroll(){

        checkArray();
        handler.removeMessages(RUN);
        toRun = true;
        new Thread(){
        	@Override
			public void run() {
        		handler.sendEmptyMessageDelayed(RUN,mScrollInterval);
        	}
        }.start();
        
    }

    /**
     * 开始滚动
     * @param mScrollInterval 再次滚动间隔，默认3000ms
     */
    public void startScroll(final int mScrollInterval){

        checkArray();

        this.mScrollInterval = mScrollInterval;

        toRun = true;
        new Thread(){
        	@Override
			public void run() {
        		handler.sendEmptyMessageDelayed(RUN,mScrollInterval);
        	}
        }.start();
    }


    /**
     * 设置再次滚动间隔
     * @param mScrollInterval 滚动持续时间，默认300ms
     */
    public void setScrollInterval(int mScrollInterval){

        this.mScrollInterval = mScrollInterval;

    }

    /**
     * 获取当前位置
     * @return
     */
    public int getCurrentPosition(){
        return mPosition;
    }


    /**
     * 停止滚动
     */
    public void stopScroll(){
        toRun = false;
    }


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == RUN) {

                if(toRun){

                    mPosition++;
                    if(mPosition == articlePage.getContent().size()){
                        mPosition = 0;
                        toRun = false;
                        new Thread(){
                        	@Override
                			public void run() {
                        		upHandler.obtainMessage(1000, "").sendToTarget();
                        	}
                        }.start();
                    }
                    
                    if(toRun){
	                    setText(articlePage.getContent().get(mPosition).getTitle().toString());
	                    new Thread(){
	                    	@Override
	            			public void run() {
	                    		handler.sendEmptyMessageDelayed(RUN,mScrollInterval);
	                    	}
	                    }.start();
                    }
                }
            }
        }
    };

    /**
     * 校验数组是否为空
     */
    private void checkArray(){

        if(articlePage == null){
            throw new IllegalArgumentException("没有设置轮播文字数组或轮播文字数组不能为null！");
        }

        if(articlePage.getContent().size() < 1){
            throw new IllegalArgumentException("轮播文字数组没有内容！");
        }

    }

    class Rotate3dAnimation extends Animation {
        private       float   mCenterX;
        private       float   mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private       Camera  mCamera;

        public Rotate3dAnimation(boolean turnIn, boolean turnUp) {
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() ;
            mCenterX = getWidth() ;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final float centerX = mCenterX ;
            final float centerY = mCenterY ;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1: -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
            if(!toRun){
            	matrix.reset();
            }
        }
    }

}
