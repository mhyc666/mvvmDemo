package com.wdh.mvvmdemo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.wdh.mvvmdemo.adapter.MainAdapter;
import com.wdh.mvvmdemo.base.BaseActivity;
import com.wdh.mvvmdemo.bean.WeXinData;
import com.wdh.mvvmdemo.databinding.ActivityMainBinding;
import com.wdh.mvvmdemo.view.DividerItemDecoration;
import com.wdh.mvvmdemo.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity implements MainViewModel.DataListener{
    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this);
        binding.setViewModel(mainViewModel);
        setSupportActionBar(binding.toolbar);
        setupRecyclerView(binding.recycler);
    }


    @Override
    protected void initData() {

    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        MainAdapter adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onRepositoriesChanged(WeXinData repositories) {
        MainAdapter adapter= (MainAdapter) binding.recycler.getAdapter();
        adapter.setRepositories(repositories.getResult().getList());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.destroy();
    }

}
