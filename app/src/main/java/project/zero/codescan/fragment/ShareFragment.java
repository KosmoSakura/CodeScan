package project.zero.codescan.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.windward.sharelibrary.qq.QQUtil;
import com.windward.sharelibrary.weibo.WBUtil;
import com.windward.sharelibrary.wxapi.WXUtils;

import java.util.ArrayList;

import project.zero.codescan.R;

/**
 * Created by ZeroProject on 2016/5/25 17:00
 */
public class ShareFragment extends Fragment {

    View contentView;
    private Button sina;
    private Button weiCircle;
    private Button weiFri;
    private Button QF;
    private Button QZone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.frag_codes3, container, false);
        sina = (Button) contentView.findViewById(R.id.sina);
        weiCircle = (Button) contentView.findViewById(R.id.win);
        weiFri = (Button) contentView.findViewById(R.id.win2);
        QF = (Button) contentView.findViewById(R.id.win3);
        QZone = (Button) contentView.findViewById(R.id.win4);

        initListiner();
        return contentView;
    }

    private void initListiner() {
        sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSina();
            }
        });

        weiCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWCircle(1);
            }
        });
        weiFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWCircle(0);
            }
        });

        QF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQFriend();
            }
        });
        QZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQZone();
            }
        });
    }




    //1--微信朋友圈,,0--分享到微信
    private void toWCircle(int x) {
        WXUtils u = new WXUtils(getActivity());
        int type = 1;//1--微信朋友圈,0--微信好友
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.caijian);
        String title = "微信好友分享标题";
        String description = "微信好友分享描述";
        u.sendWebMessageToWeiXin(getActivity(),type,title,description,"www.baidu.com",bitmap);
//      u.sendImageMessageToWeiXin(UserMoreActivity.this,type,bitmap,title,description);
    }
    private void toQZone() {
        QQUtil util = new QQUtil(getActivity());
        String title = "标题";// 必填
        String summary = "摘要";// 选填
        String target_url = null;// 乱填会崩
        ArrayList<String> list = new ArrayList<String>();
        list.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");

        util.sendTextMessageToQzone(getActivity(), title, summary, target_url, list);
    }
    private void toQFriend() {
        QQUtil u = new QQUtil(getActivity());
        String title = "标题";// 分享的标题, 最长30个字符。
        String summary = "摘要";// 分享的消息摘要，最长40个字。
        // 分享图片的URL或者本地路径
        String bitmapPath = "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif";
        // 这条分享消息被好友点击后的跳转URL。"http://www.qq.com/news/1.html"
        String target_url = null;
        u.sendImageTextMessageToQQ(getActivity(), title, summary, bitmapPath, target_url);
    }
    private void toSina() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), WBUtil.class);
        intent.putExtra("bmp", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        intent.putExtra("title", "这里是title");
        intent.putExtra("msg", "这个是信息主体");
        intent.putExtra("pageUrl", "www.baidu.com");
        startActivity(intent);
    }
}
