package com.example.zhujiang.zhujiangpro_2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhujiang.zhujiangpro_2.R;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.Node;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.TreeHelper;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.adapter.TreeListViewAdapter;

import java.util.List;

/**
 * Created by zhujiang on 16-5-25.
 */
public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {


    public SimpleTreeListViewAdapter(ListView tree, Context context
            , List<T> datas, int defaultExpandLevel)
            throws IllegalAccessException {

        super(tree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            holder.iv.setVisibility(View.INVISIBLE);
        } else {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv.setImageResource(node.getIcon());
        }

        holder.tv.setText(node.getName());

        return convertView;
    }

    /**
     * 动态插入节点
     *
     * @param position
     * @param text
     */
    public void addExtraNode(int position, String text) {
        Node node = mVisibleNodes.get(position);
        int indexOf = mAllNodes.indexOf(node);

        // Node
        Node extraNode = new Node(-1,node.getId(),text);
        node.setExpand(!node.isExpand());  // 添加新的node后，当前默认节点打开
        extraNode.setParent(node);
        node.getChilds().add(extraNode);
        mAllNodes.add(indexOf+1,extraNode);
        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView iv;
        TextView tv;

        public ViewHolder(View itemView) {
            iv = (ImageView) itemView.findViewById(R.id.item_arrow);
            tv = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
