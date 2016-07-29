//package project.zero.codescan.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//
//import com.tandong.sa.stagger.DynamicHeightTextView;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import project.zero.codescan.R;
//import project.zero.codescan.base.App;
//import project.zero.codescan.bean.CollBean;
//
//
//public class SGVAdapter extends BaseAdapter {
//
//    private Context c;
//    private Random mRandom;
//    private ArrayList<Integer> mBackgroundColors;
//    protected List<CollBean> list;
//    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
//
//    public SGVAdapter(Context context) {
//        this.c = context;
//        list = new ArrayList<>();
//        mBackgroundColors = new ArrayList<Integer>();
//        mBackgroundColors.add(R.color.orange);
//        mBackgroundColors.add(R.color.green);
//        mBackgroundColors.add(R.color.blue);
//        mBackgroundColors.add(R.color.yellow);
//        mBackgroundColors.add(R.color.grey);
//    }
//
//    public void addList(List<CollBean> lis) {
//        this.list.clear();
//
//        if (lis != null) {
//            this.list.addAll(lis);
//        }
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int arg0) {
//        // TODO Auto-generated method stub
//        return list.get(arg0);
//    }
//
//    @Override
//    public long getItemId(int arg0) {
//        // TODO Auto-generated method stub
//        return arg0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = View.inflate(c, R.layout.adapter_sgv, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        double positionHeight = getPositionRatio(position);
//        int backgroundIndex = position >= mBackgroundColors.size() ? position % mBackgroundColors.size() : position;
//
//        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
//
//        CollBean bean = list.get(position);
//        String url = App.dir + File.separator + bean.getUrl();
//
//        Bitmap bmp = convertToBitmap(url, 200, 300);
//        holder.show.setImageBitmap(bmp);
//        holder.name.setHeightRatio(positionHeight);
//        holder.name.setText(bean.getName());
//
//
//
//
//
//        return convertView;
//    }
//
//    private double getPositionRatio(final int position) {
//        double ratio = sPositionHeightRatios.get(position, 0.0);
//        // if not yet done generate and stash the columns height
//        // in our real world scenario this will be determined by
//        // some match based on the known height and width of the image
//        // and maybe a helpful way to get the column height!
//        if (ratio == 0) {
//            ratio = getRandomHeightRatio();
//            sPositionHeightRatios.append(position, ratio);
//        }
//        return ratio;
//    }
//
//    private double getRandomHeightRatio() {
//        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
//        // the width
//    }
//
//    public static Bitmap convertToBitmap(String path, int w, int h) {
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        // 设置为ture只获取图片大小
//        opts.inJustDecodeBounds = true;
//        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        // 返回为空
//        BitmapFactory.decodeFile(path, opts);
//        int width = opts.outWidth;
//        int height = opts.outHeight;
//        float scaleWidth = 0.f, scaleHeight = 0.f;
//        if (width > w || height > h) {
//            // 缩放
//            scaleWidth = ((float) width) / w;
//            scaleHeight = ((float) height) / h;
//        }
//        opts.inJustDecodeBounds = false;
//        float scale = Math.max(scaleWidth, scaleHeight);
//        opts.inSampleSize = (int) scale;
//        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
//        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
//    }
//
//    class ViewHolder {
//        ImageView show;
//        DynamicHeightTextView name;
//
//        public ViewHolder(View view) {
//            show = (ImageView) view.findViewById(R.id.sgv_iv);
//            name = (DynamicHeightTextView) view.findViewById(R.id.sgv_tv);
//        }
//    }
//}
