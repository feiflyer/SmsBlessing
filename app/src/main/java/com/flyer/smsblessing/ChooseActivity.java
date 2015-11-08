package com.flyer.smsblessing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.flyer.bean.FastivalLab;
import com.flyer.bean.Msg;
import com.flyer.fragmet.FastivalCategoryFragment;

import java.util.List;

public class ChooseActivity extends AppCompatActivity {
    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private ArrayAdapter<Msg> mAdapter;
    private int mFastivalId;
    private LayoutInflater layoutInflater;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFastivalId = getIntent().getIntExtra(FastivalCategoryFragment.ID_GESTIVAL_ID, -1);
        setContentView(R.layout.activity_choose);
        setTitle(FastivalLab.getInstance().getFastivalById(mFastivalId).getName());
        layoutInflater = layoutInflater.from(this);
        listView = (ListView) findViewById(R.id.lv_choose);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsgActivity.toActivity(ChooseActivity.this, mFastivalId, -1);
            }
        });

        listView.setAdapter(mAdapter = new ArrayAdapter<Msg>(this, -1, FastivalLab.getInstance().getMsgByFastivalId(mFastivalId)) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_msg, parent, false);
                }

                TextView textView = (TextView) convertView.findViewById(R.id.tv_msg_content);
                Button button = (Button) convertView.findViewById(R.id.btn_to_send);

                textView.setText(getItem(position).getContent());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMsgActivity.toActivity(ChooseActivity.this, mFastivalId, getItem(position).getId());
                    }
                });
                return convertView;
            }
        });
    }

}
