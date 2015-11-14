package com.flyer.fragmet;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyer.bean.Msg;
import com.flyer.db.SmsProvider;
import com.flyer.ibs.Constant;
import com.flyer.smsblessing.R;
import com.flyer.view.FlowLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by liangchuanfei on 15/11/14.
 */
public class SmsHistoryFragment extends ListFragment {

    private static final int LOADER_ID = 1;

    private LayoutInflater mLayoutInflater;
    private CursorAdapter mCursorAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutInflater = LayoutInflater.from(getActivity());
        initLoader();
        setUpListAdapter();
    }

    private void setUpListAdapter() {
        mCursorAdapter = new CursorAdapter(getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                //初始化布局
                return mLayoutInflater.inflate(R.layout.item_sended_msg, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textViewMsg = (TextView) view.findViewById(R.id.tv_msg);
                FlowLayout fl = (FlowLayout) view.findViewById(R.id.flow_msg);
                TextView tv_fl = (TextView) view.findViewById(R.id.tv_fastival);
                TextView tv_date = (TextView) view.findViewById(R.id.tv_date);

                textViewMsg.setText(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_MSG)));
                tv_fl.setText(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_FASTIVAL)));
                tv_date.setText(parseDate(cursor.getLong(cursor.getColumnIndex(Constant.COLUMN_DATE))));

                String names = cursor.getString(cursor.getColumnIndex(Constant.COLUMN_NAME));
                fl.removeAllViews();
                if (!TextUtils.isEmpty(names)) {
                    for (String name : names.split(":")) {
                        addtag(name, fl);
                    }
                }
            }
        };

        setListAdapter(mCursorAdapter);

    }

    private String parseDate(long date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }

    private void addtag(String name, FlowLayout fl) {
        TextView textView = (TextView) mLayoutInflater.inflate(R.layout.layout_tag, fl, false);
        textView.setText(name);
        fl.addView(textView);
    }

    private void initLoader() {
        /**
         * 如果有需要可以同过继承 AsyncTaskLoader实现自己的Loader
         *
         * 参考谷歌官方文档
         * http://developer.android.com/reference/android/content/AsyncTaskLoader.html
         *
         * 如果标志loader的ID已经存在，则最后创建的loader被复用。
         如果标志loader的ID不存在，initLoader()会激发LoaderManager.LoaderCallbacks的方法onCreateLoader()。

         */
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursoeLoader = new CursorLoader(getActivity(), SmsProvider.URI_SMS_ALL, null, null, null, null);
                return cursoeLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID) {
                    mCursorAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mCursorAdapter.swapCursor(null);
            }
        });
    }

}
