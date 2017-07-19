package com.conways.pulltorefresh_pushtoload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.conways.pulltorefresh_pushtoload.wegit.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SwipeToLoadLayout.OnRefreshListener,SwipeToLoadLayout.OnLoadMoreListener {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    MyAdapter myAdapter;
    private List<Integer> list=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshing(true);
        recyclerView=(RecyclerView)this.findViewById(R.id.swipe_target);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.clear();
        for (int i = 0; i < 8; i++) {
            list.add(i);
        }
        myAdapter=new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)){
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000l);
                    list.clear();
                    for (int i = 0; i < 8; i++) {
                        list.add(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        swipeToLoadLayout.setLoadingMore(false);
                        myAdapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();

    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000l);
                    list.clear();
                    for (int i = 0; i < 8; i++) {
                        list.add(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setRefreshing(false);
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    @Override
    public void onLoadMore() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000l);
                    list.add(list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setLoadingMore(false);
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }
}
