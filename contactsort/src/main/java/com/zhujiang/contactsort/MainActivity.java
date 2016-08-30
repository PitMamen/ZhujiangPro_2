package com.zhujiang.contactsort;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AlphabetIndexer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener {

    /**
     * 分组布局
     */
    private LinearLayout titleLayout;

    /**
     * 分组上显示的字母
     */
    private TextView title;

    /**
     * 联系人列表
     */
    private ListView mContactListView;

    /**
     * 联系人适配器
     */
    private ContactAdapter mAdapter;

    /**
     * 用于字母表排序分组
     */
    private AlphabetIndexer mIndexer;

    /**
     * 保存所有联系人列表
     */
    private List<Contact> mContacts = new ArrayList<Contact>();

    /**
     * 定义字母表的排序规则
     */
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 最后一个可见的item
     */
    private int lastFirstVisibleItem = -1;

    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
    }

    private void requestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int state = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (state != PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this
                        , Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, "Give this app contact permisson!"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    // 请求申请权限
                    ActivityCompat.requestPermissions(this
                            , new String[]{Manifest.permission.READ_CONTACTS}, 0);
                }
            } else {
                initialization();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialization();
            } else {
                Toast.makeText(this, "Failure to apply for permission!"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (!cursor.isClosed()) {
            cursor.close();
            Log.i("TAG", "cursor closed!");
        }
        super.onDestroy();
    }

    private void initialization() {
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        title = (TextView) findViewById(R.id.title1);
        mContactListView = (ListView) findViewById(R.id.contacts_list_view);
        mAdapter = new ContactAdapter(this, R.layout.contact_item, mContacts);

        /**
         * 由于startManagingCursor(Cursor c)方法失效，因为此方法的数据库操作在UI线程中
         * 如果有大量数据会导致阻塞
         *
         * 使用LoaderManager来实现一个LoaderCallbacks接口，创建一个类继承CursorLoader类
         * 在onLoadInBackground方法中查询数据库，
         * 在LoaderCallbacks的onCreateLoader方法返回CursorLoader实例
         *
         */
        getLoaderManager().initLoader(1, null, this);
    }

    /**
     * 设置联系人ListView监听事件,根据当前状态进行分组显示，从而实现挤压动画效果
     */
    private void setupContactListView() {
        mContactListView.setAdapter(mAdapter);
        mContactListView.setOnScrollListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCurorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String sortKey = cursor.getString(1);

            Contact contact = new Contact();
            contact.setName(name);
            contact.setSortKey(sortKey);
            mContacts.add(contact);
        }

        mIndexer = new AlphabetIndexer(cursor, 1, alphabet);
        mAdapter.setIndexer(mIndexer);
        if (mContacts.size() > 0) {
            setupContactListView();
        }

        this.cursor = cursor;
    }

//    /**
//     * 获取sortKey的首个字符，如果是英文字符直接返回，否则返回#
//     *
//     * @param sortKeyStr
//     * @return
//     */
//    private String getSortKey(String sortKeyStr) {
//        return hanZiTopingYin(sortKeyStr.substring(0, 1));
//    }

//    /**
//     * 将汉字转换为拼音首字母
//     *
//     * @param firstName
//     * @return
//     */
//    private String hanZiTopingYin(char firstName) {
//        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
//        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
//        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//
//
//        String[] pinyinArray = null;
//        try {
//            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstName, format);
//        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//            badHanyuPinyinOutputFormatCombination.printStackTrace();
//        }
//        if (pinyinArray != null && pinyinArray.length > 0) {
//            return pinyinArray[0].toUpperCase();
//        }
//        return "#";
//    }


//    /**
//     * 获取汉字串拼音首字母，英文字符不变
//     *
//     * @param chinese 汉字串
//     * @return 汉语拼音首字母
//     */
//    public static String hanZiTopingYin(String chinese) {
//        StringBuffer pybf = new StringBuffer();
//        char[] arr = chinese.toCharArray();
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] > 128) {
//                try {
//                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
//                    if (temp != null) {
//                        pybf.append(temp[0].charAt(0));
//                    }
//                } catch (BadHanyuPinyinOutputFormatCombination e) {
//                    e.printStackTrace();
//                }
//            } else {
//                pybf.append(arr[i]);
//            }
//        }
//        return pybf.toString().replaceAll("\\W", "").trim();
//    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // getSectionForPosition返回匹配规则字符串所在的位置
        int section = mIndexer.getSectionForPosition(firstVisibleItem);
        // getPositionForSection通过匹配的规则字符所在位置获取字符串相匹配的索引
        int nextSecPosition = mIndexer.getPositionForSection(section + 1);
        if (firstVisibleItem != lastFirstVisibleItem) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
            params.topMargin = 0;
            titleLayout.setLayoutParams(params);
            title.setText(String.valueOf(alphabet.charAt(section)));
        }

        if (firstVisibleItem + 1 == nextSecPosition) {
            View childView = view.getChildAt(0);
            if (childView != null) {
                int titleHeight = titleLayout.getHeight();
                int bottom = view.getBottom();
                ViewGroup.MarginLayoutParams params =
                        (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                // 如果一个item的高度小于titleLayout的高度,就开始挤压titleLayout
                if (bottom < titleHeight) {
                    float pushedDistance = bottom - titleHeight;
                    params.topMargin = (int) pushedDistance;
                    titleLayout.setLayoutParams(params);
                }
            }
        }

        lastFirstVisibleItem = firstVisibleItem;
    }


    static class MyCurorLoader extends CursorLoader {

        public MyCurorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor onLoadInBackground() {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = getContext().getContentResolver().query(uri
                    , new String[]{"display_name", "phonebook_label"}
                    , null, null, "phonebook_label");
            return cursor;
        }
    }


}
