package project.zero.codescan.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.zero.codescan.R;
import project.zero.codescan.adapter.FriendListAdapter;
import project.zero.codescan.base.App;
import project.zero.codescan.utils.debug.Debug;

public class FriendListActivity extends Activity {
    @Bind(R.id.friend_list)
    ListView fList;


    private List<String> usernames;
    private FriendListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        ButterKnife.bind(this);

        adapter = new FriendListAdapter(this);
        fList.setAdapter(adapter);
        //需异步执行
        getListDatas();

        initListener();
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
    private void initListener() {
        fList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String usename = (String) adapter.getItem(position);
                Intent intent = new Intent(FriendListActivity.this,ChatActivity.class);
                intent.putExtra("chatUsername", usename);
                startActivity(intent);
            }
        });
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                Debug.d("*********链接成功");
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
                            if (NetUtils.hasNetwork(FriendListActivity.this)) {
                                //连接不到聊天服务器
                                Debug.d("连接不到聊天服务器");

                            } else {
                                //当前网络不可用，请检查网络设置
                                Debug.d("前网络不可用，请检查网络设置");
                            }
                        }
                    }
                });

            }
        });
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                getListDatas();
                App.showToast("同意");
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
                getListDatas();
                App.showToast("拒绝");
            }

            @Override
            public void onContactInvited(final String username, String reason) {
                App.showToast("邀请");
                //收到好友邀请
                final AlertDialog.Builder dia = new AlertDialog.Builder(FriendListActivity.this);
                dia.setTitle("是否同意" + username + "加为好友");
                // dia.setCancelable(false);
                dia.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(username);
                            Debug.d("同意好友成功");
                        } catch (HyphenateException e) {
                            Debug.d("同意好友异常");
                        }
                        dialog.dismiss();
                    }
                });

                dia.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(username);
                            Debug.d("拒绝好友成功");
                        } catch (HyphenateException e) {
                            Debug.d("拒绝好友异常");
                        }
                    }
                });
                dia.show();

                getListDatas();

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                getListDatas();
                App.showToast("删除");
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                getListDatas();
                App.showToast("增加");
            }
        });
    }

    @OnClick({R.id.logout, R.id.add_friends})
    public void onEventClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                Logout();
                break;
            case R.id.add_friends:
//                Intent intent = new Intent(this, ChatActivity.class);
//                intent.putExtra("chatUsername", "zero");
//                startActivity(intent);
                addFriend();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logout();
    }

    private void Logout() {
        //此方法为同步方法，里面的参数 true 表示退出登录时解绑 GCM 或者小米推送的 token
//                EMClient.getInstance().logout(true);
        //此方法为异步方法
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                finish();
                Debug.d("退出成功");
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void addFriend() {
        final EditText edt = new EditText(FriendListActivity.this);
        edt.setHint("请输入好好友昵称");
        //参数为要添加的好友的username和添加理由

        AlertDialog.Builder dia = new AlertDialog.Builder(this);
        dia.setTitle("添加好友");
        dia.setMessage("请输入好友id");
        dia.setView(edt);
        // dia.setCancelable(false);
        dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (TextUtils.isEmpty(edt.getText())) {
//                    App.showToast("请输入昵称id");
//                    return;
//                }

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(edt.getText().toString().trim(), "这里是理由");
                            getListDatas();
                            Debug.d("2---添加好友成功");
                        } catch (HyphenateException e) {
                            Debug.d("2---添加好友异常");
                        }
                    }
                }).start();
                dialog.dismiss();
            }

        });

        dia.setNegativeButton("取消", null);
        dia.show();
    }

    private void getListDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setList(usernames);
                        }
                    });
                    Debug.d("1---获取数据成功");
                } catch (HyphenateException e) {
                    Debug.d("1---获取数据异常"+e.toString());
                }
            }
        }).start();

    }
}
