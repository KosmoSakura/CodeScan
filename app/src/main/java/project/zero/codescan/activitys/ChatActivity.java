package project.zero.codescan.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.zero.codescan.R;
import project.zero.codescan.bean.ChatBean;
import project.zero.codescan.core.ImagePick;
import project.zero.codescan.core.ScreenUtil;
import project.zero.codescan.utils.debug.Debug;

public class ChatActivity extends Activity {

    @Bind(R.id.refresh_list_view)
    ListView listView;


    RelativeLayout titleBar;
    @Bind(R.id.imv_camera)
    ImageView imvCamera;
    @Bind(R.id.et_input_comment)
    EditText etInputComment;
    @Bind(R.id.bt_send)
    Button btSend;
    @Bind(R.id.rl_comment_layout)
    LinearLayout rlCommentLayout;

    //    private StewardCustonAdapter mAdapter;
    private ImagePick pick;
    private View vPop;
    private PopupWindow pop;
    private EMMessageListener msgListener;
    private Context context;
    private String chatUsername;//聊天对象
    private EMMessage message;//发送消息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        chatUsername = getIntent().getStringExtra("chatUsername");
        getMsg();
    }

    @OnClick({R.id.imv_camera})
    public void onEventClick(View view) {
        switch (view.getId()) {
            case R.id.imv_camera:

                break;
        }
    }

    private void getMsg() {
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWordsMsg(etInputComment.getText().toString());
            }
        });
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                for (EMMessage msg : messages) {
                    String s = msg.toString();
                    etInputComment.setText(s);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                for (EMMessage msg : messages) {
                    String s = msg.toString();
                    etInputComment.setText(s);
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
                for (EMMessage msg : messages) {
                    String s = msg.toString();
                    etInputComment.setText(s);
                }
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
                for (EMMessage msg : message) {
                    String s = msg.toString();
                    etInputComment.setText(s);
                }
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                String s = message.toString();
                etInputComment.setText(s);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(msgListener);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    private void sendWordsMsg(String text) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        message = EMMessage.createTxtSendMessage(text, chatUsername);
//    //如果是群聊，设置chattype，默认是单聊
//        if (chatType == CHATTYPE_GROUP)
//            message.setChatType(ChatType.GroupChat);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                etInputComment.setText("发送成功");
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    protected void initData() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(final int error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error == EMError.USER_REMOVED) {
                            // 显示帐号已经被移除
                            Debug.d("显示帐号已经被移除");
                        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            // 显示帐号在其他设备登录
                            Debug.d("显示帐号在其他设备登录");
                        } else {
                            if (NetUtils.hasNetwork(ChatActivity.this)) {
                                //连接不到聊天服务器
                                Debug.d("连接不到聊天服务器");
                            } else {
                                //当前网络不可用，请检查网络设置
                                Debug.d("当前网络不可用");
                            }
                        }
                    }
                });

            }
        });


        List<ChatBean> dataSorce = new ArrayList<>();
//        mAdapter = new StewardCustonAdapter(this, dataSorce);
//        listView.setAdapter(mAdapter);
    }


    private void selectPhotos() {
        vPop = getLayoutInflater().inflate(R.layout.view_popwindow_photo_select, null);
        pop = new PopupWindow(vPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ScreenUtil.scale(vPop);
        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        if (pop != null && !pop.isShowing()) {
            pop.showAtLocation(titleBar, 0, 0, 0);
        }
        TextView t2 = (TextView) pop.getContentView().findViewById(R.id.tv_pop_1);
        TextView t1 = (TextView) pop.getContentView().findViewById(R.id.tv_pop_2);
        TextView tCancel = (TextView) pop.getContentView().findViewById(R.id.tv_pop_cancel);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相册
                pick.getLocalPhoto();
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相机
                pick.getCameraPhoto();
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
        tCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pick != null) {
            pick.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean checkParams() {
        String msg = etInputComment.getText().toString();
        if (TextUtils.isEmpty(msg)) {

            return false;
        }

        return true;
    }

//    private void refreshUI(ChatBean bean) {
//
//        if (bean != null) {
//
//            //消息类型: 1=文字消息 2=图片消息 3=视频消息 4=管家的回复 5=管家分享的理财产品
//            // 6=沟通结束 7=用户评价管家
//            if (userEntity != null) {
//                mAdapter.setUserInfo(userEntity);
//            }
//            if (bankerEntity != null) {
//                mAdapter.setBankerInfo(bankerEntity);
//                mTel = bankerEntity.getMobile();
//            }
//
//            if (items != null && items.size() > 0) {
//                mAdapter.addList(items, true);
//                if (mPageIndex == 1)
//                    listView.setSelection(mAdapter.getCount() - 1);
//                else
//                    listView.setSelection(items.size());
////                if (items.size() == mPageRange)
//                mPageIndex++;
//            } else {
//                if (mPageIndex != 1)
//                    ToastUtil.showShortMessage("已加载完毕");
//            }
//        }
//
//    }

}
