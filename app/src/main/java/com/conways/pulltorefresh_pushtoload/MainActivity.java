package com.conways.pulltorefresh_pushtoload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.conways.pulltorefresh_pushtoload.wegit.SwipeToLoadLayout;


public class MainActivity extends AppCompatActivity implements OnRefreshListener,OnLoadMoreListener{

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setLoadingMore(false);
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setRefreshing(false);
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                });
            }
        }).start();

    }
}
