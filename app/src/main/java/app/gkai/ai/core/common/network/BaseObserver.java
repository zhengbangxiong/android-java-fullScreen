package app.gkai.ai.core.common.network;


import android.content.Context;
import android.util.Log;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 */

public abstract class BaseObserver<T> implements Observer<T> {
    protected Context mContext;

    public BaseObserver(Context cxt) {
        this.mContext = cxt;
    }

    public BaseObserver() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T response) {
        try {
//            Resp resp = (Resp)response;

//            if(10001 == resp.getStatus() || 20014 == resp.getStatus()){
//                //                    baseActivity.cleanLoginData();
//                Intent intent = new Intent(mContext, LoginActivity.class);
//                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            onSuccees(response);
        } catch (Exception e) {
            try {
                onFailure(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void onError(Throwable e) {
        Log.e("BaseObserver", e.getMessage());
        try {
            onFailure(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccees(T t) throws Exception;

//    /**
//     * 返回成功了,但是code错误
//     *
//     * @param t
//     * @throws Exception
//     */
//    protected void onCodeError(T t, Throwable e) throws Exception {
//
//    }

    /**
     * 返回失败
     *
     * @param e
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e) throws Exception;


}
