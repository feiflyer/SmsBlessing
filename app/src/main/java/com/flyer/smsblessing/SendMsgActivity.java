package com.flyer.smsblessing;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyer.bean.FastivalLab;
import com.flyer.bean.Msg;
import com.flyer.view.FlowLayout;

public class SendMsgActivity extends AppCompatActivity {

    private static final String KEY_FASTIVAL_ID = "fastival_id";
    private static final String KEY_MSG_ID = "msg_id";
    private int mFastivalId;
    private int mMsgId;

    private EditText et_content;
    private Button btn_send;
    private FlowLayout flow_layout;
    private FloatingActionButton fab_send;
    private LinearLayout ll_layout_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFastivalId = getIntent().getIntExtra(KEY_FASTIVAL_ID , -1);
        mMsgId = getIntent().getIntExtra(KEY_MSG_ID , -1);
        setContentView(R.layout.activity_send_msg);

        et_content = (EditText) findViewById(R.id.et_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);
        fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        ll_layout_loading = (LinearLayout) findViewById(R.id.ll_layout_loading);

        if (mMsgId != -1){
            Msg msg = FastivalLab.getInstance().getMsgById(mMsgId);
            et_content.setText(msg.getContent());
            setTitle(FastivalLab.getInstance().getFastivalById(mFastivalId).getName());
        }
    }

    public static void toActivity(Context context ,int fastivialId, int msgId){
        Intent intent = new Intent(context ,SendMsgActivity.class);
        intent.putExtra(KEY_FASTIVAL_ID , fastivialId);
        intent.putExtra(KEY_MSG_ID , msgId);
        context.startActivity(intent);
    }
}
