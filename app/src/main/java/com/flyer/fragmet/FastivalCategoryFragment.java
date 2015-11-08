package com.flyer.fragmet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.flyer.bean.FastivalBean;
import com.flyer.bean.FastivalLab;
import com.flyer.smsblessing.ChooseActivity;
import com.flyer.smsblessing.R;

/**
 * Created by liangchuanfei on 15/11/8.
 */
public class FastivalCategoryFragment extends Fragment {

    public static final String ID_GESTIVAL_ID = "festival_id";
    private GridView mGridView;
    private ArrayAdapter<FastivalBean> mAdapter;
    private LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fastival_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        layoutInflater = layoutInflater.from(getActivity());
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter = new ArrayAdapter<FastivalBean>(getActivity(), -1, FastivalLab.getInstance().getFastivals()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_fastival, parent, false);
                }

                TextView textView = (TextView) convertView.findViewById(R.id.tv);
                textView.setText(getItem(position).getName());
                return convertView;
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChooseActivity.class);
                intent.putExtra(ID_GESTIVAL_ID, mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }
}
