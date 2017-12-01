package com.wdh.mvvmdemo.viewmodel;

import android.widget.Toast;
import com.wdh.mvvmdemo.MyApplication;
import com.wdh.mvvmdemo.base.BaseViewModel;
import com.wdh.mvvmdemo.bean.WeXinData;
import com.wdh.mvvmdemo.http.RetrofitManager;
import com.wdh.mvvmdemo.rx.RxManager;
import com.wdh.mvvmdemo.rx.RxSubscriber;
import java.util.HashMap;
import java.util.Map;


public class MainViewModel implements BaseViewModel {
    private DataListener dataListener;
    // private ObservableField<String> titleText;

    public MainViewModel( DataListener dataListener) {
        this.dataListener = dataListener;
        // titleText = new ObservableField<>(context.getString(R.string.title_text));
        weiXinData();
    }

//    public void setDataListener(DataListener dataListener) {
//        this.dataListener = dataListener;
//    }

    private void weiXinData() {
        Map<String, String> map = new HashMap<>();
        map.put("key","f8e04c7c45fa8c41e1eb4682d516f1d1");
        RxManager.getInstance().doSubscribe(RetrofitManager.getApiInstance().weixinData(map),
                new RxSubscriber<WeXinData>(false) {
            @Override
            protected void _onNext(WeXinData weXinData) {
                if(weXinData.getError_code()==0){
                    if (dataListener != null) dataListener.onRepositoriesChanged(weXinData);
                }else {
                    Toast.makeText(MyApplication.getInstance(), "数据请求失败:"+weXinData.getReason(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void _onError() {

            }
        });
    }

    @Override
    public void destroy() {
        dataListener = null;
    }

    public interface DataListener {
        void onRepositoriesChanged(WeXinData repositories);
    }
}
