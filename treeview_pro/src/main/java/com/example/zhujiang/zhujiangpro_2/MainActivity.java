package com.example.zhujiang.zhujiangpro_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zhujiang.zhujiangpro_2.adapter.SimpleTreeListViewAdapter;
import com.example.zhujiang.zhujiangpro_2.bean.FileBean;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.Node;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.adapter.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;
    private SimpleTreeListViewAdapter<FileBean> mAdapter;
    private List<FileBean> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listview);

        initData();
        try {
            mAdapter = new SimpleTreeListViewAdapter<FileBean>(mListView, this, mDatas, 1);
            mListView.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(MainActivity.this, node.getName()
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                // google推荐使用DialogFragment,为了方便使用AlertDialog.Builder
                final EditText et = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this).setTitle("Add Node")
                        .setView(et).setPositiveButton("OK"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.addExtraNode(position, et.getText().toString());
                                dialog.dismiss();
                            }
                        }).setNegativeButton("CANCEL"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true; // 如果为true则长按和短按item同时触发，false反之只触发长按
            }
        });


    }

    private void initData() {
        mDatas = new ArrayList<FileBean>();
        FileBean bean = new FileBean(1, 0, "根目录1");
        mDatas.add(bean);
        bean = new FileBean(2, 0, "根目录2");
        mDatas.add(bean);
        bean = new FileBean(3, 0, "根目录3");
        mDatas.add(bean);
        bean = new FileBean(4, 1, "根目录1-1");
        mDatas.add(bean);
        bean = new FileBean(5, 1, "根目录1-2");
        mDatas.add(bean);
        bean = new FileBean(6, 1, "根目录1-3");
        mDatas.add(bean);
        bean = new FileBean(7, 2, "根目录2-1");
        mDatas.add(bean);
        bean = new FileBean(8, 2, "根目录2-2");
        mDatas.add(bean);
        bean = new FileBean(9, 3, "根目录3-1");
        mDatas.add(bean);
        bean = new FileBean(10, 5, "根目录1-2-1");
        mDatas.add(bean);

    }
}
