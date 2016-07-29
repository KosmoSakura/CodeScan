package project.zero.codescan.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;
import com.tandong.sa.stagger.StaggeredGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.zero.codescan.R;
import project.zero.codescan.adapter.HistoryDatasAdapter;
import project.zero.codescan.base.App;
import project.zero.codescan.bean.CollBean;
import project.zero.codescan.utils.Util;
import project.zero.codescan.view.ZoomImageView;

/**
 * Created by ZeroProject on 2016/5/25 17:00
 */
public class OldCodesFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private View contentView;
    private ZoomImageView zoom;
    private LinearLayout zoom_out;

    private StaggeredGridView sGv;
    private boolean mHasRequestedMore;

//    private SGVAdapter mAdapter;
    private List<CollBean> list;
    private HistoryDatasAdapter adapter;
    private boolean isShow = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.frag_codes, container, false);

        initView();
        initDatas();
        return contentView;
    }

    private void initView() {
        zoom = (ZoomImageView) contentView.findViewById(R.id.zoom);
        zoom_out = (LinearLayout) contentView.findViewById(R.id.zoom_out);
        sGv = (StaggeredGridView) contentView.findViewById(R.id.frag_gv);
        list = new ArrayList<>();
//        mAdapter = new SGVAdapter(getActivity());
        adapter = new HistoryDatasAdapter(getActivity());
        sGv.setAdapter(adapter);
        sGv.setOnScrollListener(this);
        sGv.setOnItemClickListener(this);

        zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    isShow = false;
                    zoom_out.setVisibility(View.GONE);
                } else {
                    isShow = true;
                    zoom_out.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        initDatas();
    }

    private void initDatas() {
        String json = App.sp.getString("Coll", "");
        Gson gson = new Gson();
        list = gson.fromJson(json, new TypeToken<List<CollBean>>() {
        }.getType());


       /* for (int i = 0; i < 20; i++) {
            CollBean bean = new CollBean();
            bean.setUrl("/storage/sdcard/myscan1464243924533.png");
            list.add(bean);
        }*/
//        mAdapter.addList(list);
        adapter.addList(list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CollBean bean = (CollBean) adapter.getItem(position);
        String url = App.dir + File.separator + bean.getUrl();
        Bitmap bmp = Util.convertToBitmap(url, 200, 300);
        zoom.setImage(bmp);
        zoom_out.setVisibility(View.VISIBLE);
        isShow = true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                mHasRequestedMore = true;
            }
        }
    }
}
