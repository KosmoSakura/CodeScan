package project.zero.codescan.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import project.zero.codescan.R;
import project.zero.codescan.activitys.FriendListActivity;
import project.zero.codescan.base.App;
import project.zero.codescan.bean.IdBean;
import project.zero.codescan.utils.Util;
import project.zero.codescan.utils.debug.Debug;

/**
 * Created by ZeroProject on 2016/5/25 17:00
 */
public class ChatFragment extends Fragment {

    View contentView;
    private AutoCompleteTextView ac_edt;
    private EditText password;
    private Button login;
    private Context c;
    private ArrayAdapter adapter;
    private ArrayList<IdBean> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        c = getContext();
        init(inflater, container);
        initDatas();
        return contentView;
    }

    private void initDatas() {
        //取数据
        String json = App.sp.getString("idBean", "");
        Gson gson = new Gson();
        list = gson.fromJson(json, new TypeToken<List<IdBean>>() {
        }.getType());

        if (list == null) {
            list = new ArrayList<>();
        }
        List<String> list1 = new ArrayList<>();

        for (IdBean bean : list) {
            list1.add(bean.getUserName());
        }
        adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, android.R.id.text1, list1);
        ac_edt.setAdapter(adapter);
        // 下拉列表高度
        ac_edt.setDropDownHeight(350);
        // 最短2个字符开始提示自动补全
        ac_edt.setThreshold(1);
        // 列表中的提示性语句
        ac_edt.setCompletionHint("登陆历史");
        // 该控件ac_edt获取或者失去焦点时触发该监听
        ac_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    // 展示下拉选项
                    view.showDropDown();
                }
            }
        });
        ac_edt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userName = ac_edt.getText().toString().trim();
                for (IdBean bean : list) {
                    if (bean.getUserName().equalsIgnoreCase(userName)) {
                        password.setText(bean.getPassWord().trim());
                    }
                }
            }
        });
    }

    private void init(LayoutInflater inflater, @Nullable ViewGroup container) {
        contentView = inflater.inflate(R.layout.frag_codes4, container, false);
        ac_edt = (AutoCompleteTextView) contentView.findViewById(R.id.tv_chat_num);
        password = (EditText) contentView.findViewById(R.id.tv_chat_password);
        login = (Button) contentView.findViewById(R.id.tv_chat_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
                Util.showProgress(getActivity(), "登陆中...");
            }
        });
    }

    private void Login() {
        EMClient.getInstance().login(ac_edt.getText().toString().trim(), password.getText().toString().trim(), new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
//                Toast.makeText(c, "登陆成功", Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.endProgress();
                    }
                });

                //判断数据库内是否有相同账号
                boolean has = true;
                OK:
                for (IdBean bean : list) {
                    if (bean.getUserName().equalsIgnoreCase(ac_edt.getText().toString().trim())) {
                        has = false;
                        break OK;
                    }
                }

                if (has) {//没有相同账号，则添加账号
                    IdBean bean1 = new IdBean();
                    bean1.setUserName(ac_edt.getText().toString().trim());
                    bean1.setPassWord(password.getText().toString().trim());
                    list.add(bean1);
                }

                SharedPreferences.Editor edt = App.sp.edit();
                Type type = new TypeToken<List<IdBean>>() {
                }.getType();
                String datas = new Gson().toJson(list, type);

                edt.putString("idBean", datas);
                edt.commit();


                Looper.prepare();
                Debug.d("登陆成功");
                App.showToast("登陆成功");
                startActivity(new Intent(c, FriendListActivity.class));
                Looper.loop();
            }

            @Override
            public void onError(int i, String s) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.endProgress();
                    }
                });
                Looper.prepare();
                Toast.makeText(c, "登陆失败" + s, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onProgress(int i, String s) {
                Looper.prepare();
                Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }


}
