package project.zero.codescan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import project.zero.codescan.base.App;
import project.zero.codescan.bean.IdBean;

public class SplashScreenActivity extends Activity {

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
//        initMyDatas();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        };
        run();
    }

    private void initMyDatas() {
        boolean isFirst = App.sp.getBoolean("isFirst", true);
        if (isFirst) {
            App.sp.edit().putBoolean("isFirst", false).commit();
            ArrayList<IdBean> list = new ArrayList<>();

            IdBean bean = new IdBean();
            bean.setUserName("zero");
            bean.setPassWord("123456");
            list.add(bean);
            IdBean bean1 = new IdBean();
            bean1.setUserName("Ein");
            bean1.setPassWord("123456");
            list.add(bean1);
            IdBean bean2 = new IdBean();
            bean2.setUserName("Zwei");
            bean2.setPassWord("123456");
            list.add(bean2);
            IdBean bean3 = new IdBean();
            bean3.setUserName("Dreaw");
            bean3.setPassWord("123456");
            list.add(bean3);
            IdBean bean4 = new IdBean();
            bean4.setUserName("QianQian");
            bean4.setPassWord("123456");
            list.add(bean4);
            IdBean bean5 = new IdBean();
            bean5.setUserName("XiaoXiao");
            bean5.setPassWord("123456");
            list.add(bean5);

            SharedPreferences.Editor edt = App.sp.edit();
            Type type = new TypeToken<List<IdBean>>() {
            }.getType();
            String datas = new Gson().toJson(list, type);

            edt.putString("idBean", datas);
            edt.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        run();
    }

    private void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
