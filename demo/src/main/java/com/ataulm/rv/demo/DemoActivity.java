package com.ataulm.rv.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.rv.SpanSizeLookup;

public class DemoActivity extends Activity {

    private static final int DUMMY_ITEM_COUNT = 25;
    private static final int SPAN_COUNT = 4;

    private static final SpanSizeLookup SPAN_SIZE_LOOKUP = new SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return 1;
        }

        @Override
        public int getSpanCount() {
            return SPAN_COUNT;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        RecyclerView listview = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return SPAN_SIZE_LOOKUP.getSpanSize(position);
                    }
                }
        );
        listview.setLayoutManager(layoutManager);

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        listview.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, SPAN_SIZE_LOOKUP));
        listview.setAdapter(new DummyRecyclerAdapter(getLayoutInflater()));
    }

    private static class DummyRecyclerAdapter extends RecyclerView.Adapter {

        private final LayoutInflater layoutInflater;

        DummyRecyclerAdapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.view_item, parent, false);
            return new DummyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ItemView) holder.itemView).update(position);
        }

        @Override
        public int getItemCount() {
            return DUMMY_ITEM_COUNT;
        }

        static class DummyViewHolder extends RecyclerView.ViewHolder {

            public DummyViewHolder(View itemView) {
                super(itemView);
            }

        }

    }

}
