package com.wdh.mvvmdemo.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wdh.mvvmdemo.NewActivity;
import com.wdh.mvvmdemo.base.BaseViewModel;
import com.wdh.mvvmdemo.bean.WeXinData;

/**
 * View model for each item in the repositories RecyclerView
 */
public class ItemViewModel extends BaseObservable implements BaseViewModel{
    private WeXinData.ResultBean.ListBean data;
    private Context context;

    public ItemViewModel(Context context,WeXinData.ResultBean.ListBean data) {
        this.context = context;
        this.data = data;
    }

    public String getTitle(){
        return data.getTitle();
    }

    public String getSource(){
        return data.getSource();
    }

    public String getImageUrl(){
        return data.getFirstImg();
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    public void onItemClick(View view) {
        context.startActivity(NewActivity.newIntent(context, data));
    }

    // Allows recycling ItemRepoViewModels within the recyclerview adapter
    public void setRepository(WeXinData.ResultBean.ListBean  repository) {
        this.data = repository;
        notifyChange();
    }

    //In this case destroy doesn't need to do anything because there is not async calls
    @Override
    public void destroy() {

    }
}
