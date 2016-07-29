package project.zero.codescan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tandong.sa.stagger.DynamicHeightTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.zero.codescan.R;
import project.zero.codescan.base.App;
import project.zero.codescan.bean.CollBean;
import project.zero.codescan.utils.Util;

/***
 * ADAPTER
 */

public class HistoryDatasAdapter extends BaseAdapter {

    private static final String TAG = "HistoryDatasAdapter";


    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;
    protected List<CollBean> list;
    private Context context;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public HistoryDatasAdapter(final Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        list = new ArrayList<>();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
    }
    public void addList(List<CollBean> lis) {
        this.list.clear();

        if (lis != null) {
            this.list.addAll(lis);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.adapter_sgv, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        CollBean bean = list.get(position);
        String url = App.dir + File.separator + bean.getUrl();

        Bitmap bmp = Util.convertToBitmap(url, 200, 300);
        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ? position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));


        vh.name.setHeightRatio(positionHeight);
        vh.name.setText(bean.getName());
//        vh.show.setImageBitmap(bmp);
        Drawable d = new BitmapDrawable(bmp);
//        vh.name.setCompoundDrawables(null, d, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            vh.name.setBackground(d);
        }

        return convertView;
    }



    class ViewHolder {
        ImageView show;
        DynamicHeightTextView name;

        public ViewHolder(View view) {
            show = (ImageView) view.findViewById(R.id.sgv_iv);
            name = (DynamicHeightTextView) view.findViewById(R.id.sgv_tv);
        }
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }
}