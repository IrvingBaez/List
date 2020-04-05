package com.example.list.compoundComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.list.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

public class GenericRecyclerView<DATA> extends LinearLayout {
    private ArrayList<DATA> dataList;
    private RecyclerView recyclerView;
    private GenericAdapter adapter;
    private GenericallyRecyclableView instanceExample;
    private GenericRecyclerViewListener parent;

    public GenericRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public GenericRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GenericRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_generic_recycler, this);

        this.parent = (GenericRecyclerViewListener)context;
        this.instanceExample = null;
        this.recyclerView = findViewById(R.id.view_experimental_view);
        this.recyclerView.setNestedScrollingEnabled(true);
        //this.recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),VERTICAL));
    }

    public void setViewExample(GenericallyRecyclableView instance){
        this.instanceExample = instance;
    }

    public void setDataList(ArrayList<DATA> dataList) {
        this.dataList = dataList;
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        this.recyclerView.setLayoutManager(manager);
        this.adapter = new GenericAdapter();
        this.recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new GenericCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START |
                        ItemTouchHelper.END, ItemTouchHelper.LEFT));

        helper.attachToRecyclerView(this.recyclerView);
    }

    public void notifyDataChange() {
        this.adapter.notifyDataSetChanged();
    }

    private class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.Data>{

        @NonNull
        @Override
        public GenericAdapter.Data onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GenericallyRecyclableView view = null;
            try {
                view = instanceExample.getClass().
                        getDeclaredConstructor(Context.class).newInstance(parent.getContext());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                    NoSuchMethodException e) {
                e.printStackTrace();
            }

            return new Data(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GenericAdapter.Data holder, int position) {
            holder.assignData(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class Data extends RecyclerView.ViewHolder {
            private GenericallyRecyclableView view;

            public Data(@NonNull View itemView) {
                super(itemView);
                this.view = (GenericallyRecyclableView) itemView;
            }

            public void assignData(DATA data) {
                this.view.setData(data);
            }
        }
    }

    private class GenericCallback extends ItemTouchHelper.SimpleCallback {

        public GenericCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int position_dragged = viewHolder.getAdapterPosition();
            int position_target = target.getAdapterPosition();
            Collections.swap(dataList, position_dragged, position_target);
            adapter.notifyItemMoved(position_dragged, position_target);

            parent.onDataReordered();
            return false;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                viewHolder.itemView.setAlpha(0.5f);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1f);
            parent.onClearView();
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }

    public interface GenericRecyclerViewListener{
        void onDataReordered();
        void onClearView();
    }
}
