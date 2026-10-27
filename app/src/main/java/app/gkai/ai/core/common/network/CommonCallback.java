package app.gkai.ai.core.common.network;


import java.util.function.BiConsumer;

import app.gkai.ai.core.common.base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommonCallback<T> implements Callback<T> {

    BaseActivity  baseActivity;
    BiFunction<T> function;

    BiConsumer<Call<T>,Throwable> failFunction;

    public CommonCallback(BaseActivity context, BiFunction<T> function) {
        this.baseActivity = context;
        this.function = function;
    }


    public CommonCallback<T> setFailFunction(BiConsumer<Call<T>,Throwable>  function){
        this.failFunction=function;
        return this;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (!response.isSuccessful()) {

                baseActivity.dismissDialog();
                baseActivity.showToastFailure(String.valueOf(response.code()));
               // baseActivity.showToastFailure(baseActivity.getResources().getString(me.yuncun.one.R.string.network_error));
                return;
            }
            try{
//                Resp resp = (Resp)response.body();
//                if(10001 == resp.getStatus()){
////                    baseActivity.cleanLoginData();
//                    Intent intent = new Intent(baseActivity, LoginActivity.class);
//                    baseActivity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                }
            }catch (Exception e){
                e.printStackTrace();
            }



            this.function.apply(call, response);
        } catch (Exception e) {
            e.printStackTrace();
//            MyDirectToast.show(baseActivity, MyDirectToast.waring, baseActivity.getResources().getString(me.yuncun.one.R.string.network_error));
            baseActivity.dismissDialog();
            baseActivity.showToastFailure(baseActivity.getResources().getString(app.gkai.ai.R.string.service_busy));
        }
    }


//    abstract void doing(Call<T> call, Response<T> response) throws Exception;


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        //Toast.makeText(WalletActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
//        MyDirectToast.show(baseActivity, MyDirectToast.waring, baseActivity.getResources().getString(me.yuncun.one.R.string.network_error));

        if(failFunction==null){
//            Log.e("OKHttp-----", call.request().url().host());
            baseActivity.dismissDialog();
            baseActivity.showToastFailure(baseActivity.getResources().getString(app.gkai.ai.R.string.network_timeout));
            t.printStackTrace();
        }else{
//            failFunction.accept(call,t);
        }
    }


}
