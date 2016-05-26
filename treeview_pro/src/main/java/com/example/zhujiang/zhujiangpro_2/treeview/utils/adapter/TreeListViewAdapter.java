package com.example.zhujiang.zhujiangpro_2.treeview.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.zhujiang.zhujiangpro_2.treeview.utils.Node;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.TreeHelper;

import java.util.List;

/**
 * Created by zhujiang on 16-5-25.
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;

    protected LayoutInflater mInflater;
    protected ListView mTree;

    protected OnTreeNodeClickListener mListener;


    public void setOnTreeNodeClickListener(OnTreeNodeClickListener listener) {
        this.mListener = listener;
    }

    public TreeListViewAdapter(ListView tree, Context context, List<T> datas
            , int defaultExpandLevel) throws IllegalAccessException {

        this.mContext = context;
        this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        this.mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

        mInflater = LayoutInflater.from(mContext);

        this.mTree = tree;

        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);

                if (mListener != null) {
                    mListener.onClick(mVisibleNodes.get(position), position);
                }
            }
        });
    }

    /**
     * 点击收缩或者展开
     *
     * @param position
     */
    private void expandOrCollapse(int position) {

        Node n = mVisibleNodes.get(position);

        if (n != null) {
            if (n.isLeaf())
                return;
            n.setExpand(!n.isExpand());
            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }



    /**
     * 设置node的点击回调
     */
    public interface OnTreeNodeClickListener {

        void onClick(Node node, int position);
    }

    public abstract View getConvertView(Node node, int position
            , View convertView, ViewGroup parent);
}
