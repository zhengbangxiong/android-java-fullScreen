package app.gkai.ai.modules.splash.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import app.gkai.ai.R;
import app.gkai.ai.core.common.base.BaseActivity;
import app.gkai.ai.core.common.constant.Constants;
import app.gkai.ai.core.common.manager.ActivityManager;
import app.gkai.ai.core.common.util.ClientConfigDao;
import app.gkai.ai.core.common.view.MyDirectToast;
import app.gkai.ai.modules.home.ui.activity.AndroidJSWebViewActivity;
import app.gkai.ai.modules.home.ui.activity.HomeWebViewActivity;
import app.gkai.ai.modules.splash.service.GuideService;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 *  描述： 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "GuideActivity";

    ViewPager mViewPager;
    //容器
    private List<View> mList = new ArrayList<>();

    private View view1, view2, view3, view4;


    //小圆点
    @BindView(R.id.point1)
    ImageView point1;
    @BindView(R.id.point2)
    ImageView point2;
    @BindView(R.id.point3)
    ImageView point3;
    @BindView(R.id.point4)
    ImageView point4;
    TextView tv_agreement;

    CheckBox  checkbox;
    ImageView img_one, img_two, img_three;

    private GuideService                 mGuideService = new GuideService();
    private WeakReference<GuideActivity> mActivity     = new WeakReference(this);
//    private String path_one = "file:///android_asset/animation/guide_one_bg";
//    private String path_two = "file:///android_asset/animation/guide_two_bg";
//    private String path_three = "file:///android_asset/animation/guide_three_bg";



    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        ActivityManager.addActivity(this, TAG);


        initValue();

    }

    public void initValue() {
        //设置默认图片
        setPointImg(true, false, false, false);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        view1 = View.inflate(this, R.layout.pager_item_one, null);
        view2 = View.inflate(this, R.layout.pager_item_two, null);
        view3 = View.inflate(this, R.layout.pager_item_three, null);
        view4 = View.inflate(this, R.layout.pager_item_four, null);
        view4.findViewById(R.id.tv_ljty).setOnClickListener(this);
        view4.findViewById(R.id.tv_agreement).setOnClickListener(this);
        checkbox = view4.findViewById(R.id.checkbox3);
        tv_agreement = view4.findViewById(R.id.tv_agreement);
        img_one = (ImageView) view1.findViewById(R.id.img_one);
        img_two = (ImageView) view2.findViewById(R.id.img_two);
        img_three = (ImageView) view3.findViewById(R.id.img_three);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        mList.add(view4);


//        setPictrue(img_one, path_one);


        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());

        //监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //pager切换
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setPointImg(true, false, false, false);
//                        setPictrue(img_one, path_one);

                        //                        btn_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false, true, false, false);
//                        setPictrue(img_two, path_two);
                        //                        btn_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false, false, true, false);
//                        setPictrue(img_three, path_three);
                        //                        btn_back.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        setPointImg(false, false, false, true);
                        //                        btn_back.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setPictrue(ImageView imageView, String img_path){
        Glide.with(GuideActivity.this).load(img_path).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (drawable instanceof GifDrawable) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
                    gifDrawable.setLoopCount(1);
                    imageView.setImageDrawable(drawable);

                    gifDrawable.start();
                }
            }
        });
    }


    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mList.get(position));
            //super.destroyItem(container, position, object);
        }
    }

    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3, boolean isCheck4) {
        if (isCheck1) {
            point1.setBackgroundResource(R.drawable.point_on);
        } else {
            point1.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheck2) {
            point2.setBackgroundResource(R.drawable.point_on);
        } else {
            point2.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheck3) {
            point3.setBackgroundResource(R.drawable.point_on);
        } else {
            point3.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheck4) {
            point4.setBackgroundResource(R.drawable.point_on);
        } else {
            point4.setBackgroundResource(R.drawable.point_off);
        }
    }

    @Override
    protected void releaceSource() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ljty:
                if (!checkbox.isChecked()) {
                    MyDirectToast.show(this, MyDirectToast.waring, getResources().getString(app.gkai.ai.R.string.guide_agrees_one));
                    return;
                }
                /**
                 * 第一次进引导页 “1”：代表进去过了，其他代表没有
                 */
                ClientConfigDao.SetConfigString(this, Constants.ISFIRST, "1");
                jumpActivity(AndroidJSWebViewActivity.class);
                ActivityManager.removeAllActivity();
                break;
            case R.id.tv_agreement:
                showDialog();
                mGuideService.commonSetting(mActivity.get());
                break;
        }
    }

    public void showDialog() {
        showLoadingDialog(getResources().getString(R.string.loading));
    }

    /**
     * 跳转到webview
     */
    public void SetStartWebView(String content) {
        Intent intent = new Intent(this, HomeWebViewActivity.class);
        intent.putExtra("data", content);
        intent.putExtra("title", getResources().getString(R.string.guide_agrees_three));
        startActivity(intent);
    }
}
