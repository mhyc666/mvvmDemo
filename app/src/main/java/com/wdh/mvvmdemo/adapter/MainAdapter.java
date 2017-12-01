package com.wdh.mvvmdemo.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.wdh.mvvmdemo.R;
import com.wdh.mvvmdemo.bean.WeXinData;
import com.wdh.mvvmdemo.databinding.ItemWeixinBinding;
import com.wdh.mvvmdemo.viewmodel.ItemViewModel;

import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private List<WeXinData.ResultBean.ListBean> mData;

    public  MainAdapter(){
        this.mData= Collections.emptyList();
    }

    public void setRepositories(List<WeXinData.ResultBean.ListBean> repositories) {
        this.mData = repositories;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemWeixinBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_weixin,
                parent,
                false);
        return new MainViewHolder(binding) ;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
       holder.bindRepository(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.size() <= 0)
            return 0;

        return mData.size();
    }

     class MainViewHolder extends RecyclerView.ViewHolder {
        ItemWeixinBinding binding;
         MainViewHolder( ItemWeixinBinding binding) {
            super(binding.linear);
            this.binding=binding;
        }
        void bindRepository(WeXinData.ResultBean.ListBean repository) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ItemViewModel(itemView.getContext(), repository));
            } else {
                binding.getViewModel().setRepository(repository);
            }
        }
    }
}
