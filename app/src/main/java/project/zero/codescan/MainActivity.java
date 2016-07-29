package project.zero.codescan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tandong.sa.avatars.AvatarDrawableFactory;
import com.tandong.sa.slideMenu.SlidingMenu;
import com.tandong.sa.view.SmartListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.zero.codescan.adapter.MenuAdapter;
import project.zero.codescan.bean.MenuBean;
import project.zero.codescan.fragment.CodeScanFragment;
import project.zero.codescan.fragment.ShareFragment;
import project.zero.codescan.fragment.ChatFragment;
import project.zero.codescan.fragment.OldCodesFragment;

public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.main_left)
    TextView left;
    @Bind(R.id.main_middle)
    TextView middle;

    @Bind(R.id.main_frag)
    LinearLayout fragLaytout;

    private SlidingMenu menu;
    private SmartListView lv_menu;
    private ImageView icon;
    private MenuAdapter menuAdapter;
    private ArrayList<MenuBean> list;
    private Animation animationMenu;

    private Fragment frag;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initMenu();
        initView();
        initListener();
//        initAvatar();
    }

    private void initAvatar() {
        AvatarDrawableFactory adf = new AvatarDrawableFactory(getResources(), this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = false;

        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.mipmap.itachi, options);
        Drawable roundedAvatarDrawable = adf.getRoundedAvatarDrawable(avatar);
        icon.setImageDrawable(roundedAvatarDrawable);
    }

    private void initListener() {
    }

    @OnClick({R.id.main_left})
    public void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.main_left:
                menu.toggle();//菜单关闭和展开方法
                break;
            default:
                break;
        }
    }

    private void initView() {
        middle.setText("生成");
        frag = new ChatFragment();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.main_frag, frag);
        ft.commit();
    }

    private void initMenu() {

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);//左右都可滑出
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//滑出模式（全屏可滑出还是边界滑出）
        menu.setShadowWidthRes(R.dimen.shadow_width);//阴影宽度
        menu.setShadowDrawable(R.drawable.shadow);//阴影
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//菜单宽度
        menu.setBehindWidth(360);//主界面剩余宽度
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);//左侧菜单
//        menu.setSecondaryMenu(R.layout.layout_menu);// 设置右侧菜单
        menu.setSecondaryShadowDrawable(R.drawable.shadow);// 设置右侧菜单阴影的图片资源
        lv_menu = (SmartListView) menu.findViewById(R.id.lv_menu);
        icon = (ImageView) menu.findViewById(R.id.iv_emnu);
        lv_menu.setOnItemClickListener(this);
        menu.setOnOpenListener(ool);


    }

    SlidingMenu.OnOpenListener ool = new SlidingMenu.OnOpenListener() {

        @Override
        public void onOpen() {
            list = new ArrayList<>();

            String[] names = {"生成", "分享", "环信", "记录",};
            int[] imgs = {R.drawable.composer_sleep, R.drawable.composer_place, R.drawable.composer_music, R.drawable.composer_thought,};
            for (int i = 0; i < names.length; i++) {
                MenuBean bean = new MenuBean();
                bean.setName(names[i]);
                bean.setResId(imgs[i]);
                list.add(bean);
            }
            menuAdapter = new MenuAdapter(MainActivity.this, list);
            lv_menu.setAdapter(menuAdapter);
            animationMenu = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_menu_list);
            LayoutAnimationController lac = new LayoutAnimationController(animationMenu);
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lv_menu.setLayoutAnimation(lac);

        }
    };
    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
////        String menu = list.get(position).getName();
////        if (menu.contains("SmartTag")) {
////            gotoActivity(MainActivity.class, false);
////        } else if (menu.contains("zUImageLoader")) {
////            gotoActivity(MainActivity.class, false);
////        } else if (menu.contains("ZoomImageView")) {
////            gotoActivity(MainActivity.class, false);
////        } else if (menu.contains("Cropper")) {
////            gotoActivity(MainActivity.class, false);
////        } else if (menu.contains("动画特效")) {
////        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                index = 0;
                frag = new CodeScanFragment();
                middle.setText("生成");
                break;
            case 1:
                index = 1;
                frag = new ShareFragment();
                middle.setText("分享");
                break;
            case 2:
                index = 2;
                frag = new ChatFragment();
                middle.setText("环信");
                break;
            case 3://历史记录
                index = 3;
                frag = new OldCodesFragment();
                middle.setText("记录");
                break;
            default:
                break;
        }
        ft = fm.beginTransaction();
        ft.replace(R.id.main_frag, frag);
        ft.commit();
        menu.toggle();//菜单关闭和展开方法
    }
}
