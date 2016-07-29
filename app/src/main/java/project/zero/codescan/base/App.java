package project.zero.codescan.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import project.zero.codescan.utils.debug.Debug;

public class App extends Application {
    private static App instance;
    public static SharedPreferences sp;
    private boolean isMainActivityFinish = true;
    public static File dir;
    public Context applicationContext;

    private ArrayList<Activity> runActivity = new ArrayList<Activity>();

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        //严格模式
//        setStrictMode();

        applicationContext = this;
        Debug.setTag("Zero");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.setThreadPolicy(new StrictMode
//                .ThreadPolicy
//                .Builder()
//                .detectAll()
//                .penaltyDialog()
//                .build());
//            StrictMode.setVmPolicy(new StrictMode
//                .VmPolicy
//                .Builder()
//                .detectAll()
//                .penaltyDeath()
//                .build());
//        }
        super.onCreate();
        instance = this;
        sp = getSharedPreferences("CodeScan", MODE_PRIVATE);

        File sdCardDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        dir = new File(sdCardDir.getPath() + "/code_scan/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        initEase();//初始化环信
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()// API等级11，使用StrictMode.noteSlowCode
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyLog()
            .penaltyFlashScreen()// API等级11
            .build());

        // 其实和性能无关，但如果使用StrictMode，最好也定义VM策略
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
            .detectLeakedSqlLiteObjects()
            .detectLeakedClosableObjects()// API等级11
            .penaltyLog()
            .penaltyDeath()
            .build());
    }

    /**
     * 注：如果你的 APP 中有第三方的服务启动，
     * 请在初始化 SDK（EMClient.getInstance().init(applicationContext, options)）方法的
     * 前面添加以下相关代码（相应代码也可参考 Demo 的 application），使用 EaseUI 库的就不用理会这个。
     */
    private void initEase() {
        EMOptions options = new EMOptions();// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(applicationContext, options);//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        if (processAppName == null || !processAppName.equalsIgnoreCase(applicationContext.getPackageName())) {
            Debug.d("enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回return;
        }
    }
    //如何获取processAppName
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static App getInstance() {
        return instance;
    }

    public static void showToast(String s) {
        Toast.makeText(instance, s, Toast.LENGTH_SHORT).show();
    }

    public void addRunActivity(Activity _value) {
        if (this.runActivity == null) {
            this.runActivity = new ArrayList<Activity>();
        }
        if (!this.runActivity.contains(_value)) {
            this.runActivity.add(_value);
        }
    }

    public void removeRunActivity(Activity _value) {
        if (this.runActivity != null) {
            this.runActivity.remove(_value);
        }
    }

    public void exitApp() {
        if (this.runActivity != null) {
            for (Activity act : this.runActivity) {
                act.finish();
            }
        }
    }

    public ArrayList<Activity> getRunActivity() {
        if (this.runActivity == null) {
            this.runActivity = new ArrayList<Activity>();
        }
        return this.runActivity;
    }

    public void setIsMainActivityFinish(boolean b) {
        isMainActivityFinish = b;
    }

    public boolean getIsMainActivityFinish() {
        return isMainActivityFinish;
    }

    public void setMainActivityFinish(boolean isMainActivityFinish) {
        this.isMainActivityFinish = isMainActivityFinish;
    }

    public void setRunActivity(ArrayList<Activity> runActivity) {
        this.runActivity = runActivity;
    }

    public void releaseData() {
        if (this.runActivity != null) {
            this.runActivity.clear();
            this.runActivity = null;
        }

    }


}
