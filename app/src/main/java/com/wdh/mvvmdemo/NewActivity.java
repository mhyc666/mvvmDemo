package com.wdh.mvvmdemo;

import android.content.Context;
import android.content.Intent;

import com.wdh.mvvmdemo.base.BaseActivity;
import com.wdh.mvvmdemo.bean.WeXinData;




public class NewActivity extends BaseActivity{

    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";

    public static Intent newIntent(Context context,WeXinData.ResultBean.ListBean repository) {
        Intent intent = new Intent(context, NewActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
