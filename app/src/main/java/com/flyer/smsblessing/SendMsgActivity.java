package com.flyer.smsblessing;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyer.bean.FastivalLab;
import com.flyer.bean.Msg;
import com.flyer.tools.SmsTools;
import com.flyer.view.FlowLayout;

import java.util.HashSet;
import java.util.Set;

public class SendMsgActivity extends AppCompatActivity {

    private static final String TAG = "send_msg";
    private static final String KEY_FASTIVAL_ID = "fastival_id";
    private static final String KEY_MSG_ID = "msg_id";
    private static final int CODE_REQUEST = 0x23;

    private static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    private static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";


    private PendingIntent sendPendingIntent;

    private PendingIntent resultPendingInent;
    private BroadcastReceiver sendReceiver;
    //短信被成功接收的广播
    private BroadcastReceiver resultReceiver;

    private int mFastivalId;
    private int mMsgId;

    /**
     * 定义两个容器，一个用来存放联系人显示昵称
     * 一个用来显示联系人号码
     */
    private Set<String> mContactsName = new HashSet<>();
    private Set<String> mContactsNum = new HashSet<>();

    private EditText et_content;
    private Button btn_add;
    private FlowLayout flow_layout;
    private FloatingActionButton fab_send;
    private LinearLayout ll_layout_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFastivalId = getIntent().getIntExtra(KEY_FASTIVAL_ID, -1);
        mMsgId = getIntent().getIntExtra(KEY_MSG_ID, -1);
        setContentView(R.layout.activity_send_msg);

        et_content = (EditText) findViewById(R.id.et_content);
        btn_add = (Button) findViewById(R.id.btn_add);
        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);
        fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        ll_layout_loading = (LinearLayout) findViewById(R.id.ll_layout_loading);

        if (mMsgId != -1) {
            Msg msg = FastivalLab.getInstance().getMsgById(mMsgId);
            et_content.setText(msg.getContent());
            setTitle(FastivalLab.getInstance().getFastivalById(mFastivalId).getName());
        }

        initEvents();

        initReceiver();
    }

    private void initReceiver() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        sendPendingIntent = PendingIntent.getBroadcast(this, 0, sendIntent, 0);

        Intent restultIntent = new Intent(ACTION_DELIVER_MSG);
        resultPendingInent = PendingIntent.getBroadcast(this, 0, restultIntent, 0);

        registerReceiver(sendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == RESULT_OK) {
                    Log.i(TAG, "短信发送成功");
                } else {
                    Log.i(TAG, "短信发送失败");
                }

            }
        }, new IntentFilter(ACTION_SEND_MSG));

        registerReceiver(resultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "联系人已经接收到我们的短信");
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(sendReceiver);
            unregisterReceiver(resultReceiver);
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    private void initEvents() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到联系人界面，也可以自定义联系人选择界面
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });

        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactsNum.size() < 1) {
                    Toast.makeText(SendMsgActivity.this , "请先选择联系人",Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_content.getText().toString())) {
                    Toast.makeText(SendMsgActivity.this , "发送内容不能为空",Toast.LENGTH_LONG).show();
                } else {
                    new SmsTools().sendMsg(mContactsNum, et_content.getText().toString(), sendPendingIntent, resultPendingInent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST) {
            if (resultCode == RESULT_OK) {
                //拿到联系人的uri
                Uri uri = data.getData();
                // Cursor cursor =  managedQuery(uri , null ,null, null , null);
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNum = getContactsNum(cursor);
                    if (!TextUtils.isEmpty(phoneNum)) {
                        mContactsName.add(contactName);
                        mContactsNum.add(phoneNum);
                        addTag(contactName);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 把所选的联系人显示出来
     *
     * @param contactName
     */
    private void addTag(String contactName) {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_tag, flow_layout, false);
        textView.setText(contactName);
        //因为inflate中传入了false为参数，所以要调用以下代码才能看到选择的联系人；
        flow_layout.addView(textView);
    }

    /**
     * 获取联系人的电话号码
     *
     * @param cursor
     */
    private String getContactsNum(Cursor cursor) {
        int numCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (numCount > 0) {
            //设置了一个或多个电话号码
            int contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);

            if (phoneCursor != null) {
                phoneCursor.moveToFirst();
                String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneCursor.close();
                return phoneNum;
            }
        }
        cursor.close();
        return null;
    }

    public static void toActivity(Context context, int fastivialId, int msgId) {
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(KEY_FASTIVAL_ID, fastivialId);
        intent.putExtra(KEY_MSG_ID, msgId);
        context.startActivity(intent);
    }
}
