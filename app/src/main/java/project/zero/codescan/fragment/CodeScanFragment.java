package project.zero.codescan.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import project.zero.codescan.R;
import project.zero.codescan.base.App;
import project.zero.codescan.bean.CollBean;
import project.zero.codescan.core.ImagePick;
import project.zero.codescan.utils.debug.Debug;
import project.zero.codescan.utils.QRCodeUtil;
import project.zero.codescan.view.ArcMenu;

/**
 * Created by ZeroProject on 2016/5/25 17:00
 */
public class CodeScanFragment extends Fragment implements ArcMenu.OnMenuItemClickListener {

    View contentView;
    private ArcMenu arcMenu;

    private EditText text;

    private ImagePick pick;//获取logo
    private ImageView show;
    private Handler han;

    //二维码扫描
    private int QR_WIDTH = 300;
    private int QR_HEIGHT = 300;
    private BufferedOutputStream outStream;
    private long time = Calendar.getInstance().getTimeInMillis();
    private Bitmap codeScan;

    private List<CollBean> lis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.frag_codes2, container, false);
        initView();
        initListener();

        return contentView;
    }

    private void initView() {
        String json = App.sp.getString("Coll", "");
        Gson gson = new Gson();
        lis = gson.fromJson(json, new TypeToken<List<CollBean>>() {
        }.getType());
        if (lis == null) {
            lis = new ArrayList<>();
        }
        han = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 5) {
                    show.setImageBitmap(codeScan);
                }
            }
        };
        arcMenu = (ArcMenu) contentView.findViewById(R.id.id_menu);
        text = (EditText) contentView.findViewById(R.id.code_text);
        show = (ImageView) contentView.findViewById(R.id.iv_show);
        pick = new ImagePick(this, new ImagePick.OnBitmapListener() {
            @Override
            public void onSelectBitmap(String path, final Bitmap bmp) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap scanbitmap = createQRImage(text.getText().toString());
                        codeScan = QRCodeUtil.addLogo(scanbitmap, bmp);
                        han.sendEmptyMessage(5);
                    }
                }).start();


            }
        });
    }

    private Bitmap getLogobitmap(Bitmap scanbitmap) {
        return Bitmap.createScaledBitmap(scanbitmap, 32, 32, true);
    }

    private void initListener() {
        arcMenu.setOnMenuItemClickListener(this);
    }

    //生成普通二维码
    private void creatCodeScan() {
        if (text.getText().equals("")) {
            Toast.makeText(getActivity(), "不可输入空的内容", Toast.LENGTH_SHORT).show();
        } else {
            final String url = text.getText().toString();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    codeScan = createQRImage(url);
                    han.sendEmptyMessage(5);
                }
            }).start();

        }
    }

    //保存本地
    private void saveLocal(final Bitmap scanbitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.bitmapdialog, null);
                ImageView img = (ImageView) view.findViewById(R.id.dialog_bitmap);
                img.setImageBitmap(scanbitmap);
                new AlertDialog.Builder(getActivity())
                    .setTitle("二维码")
                    .setView(view)
                    .setPositiveButton("存至本地",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                        /*
                                         * 将图片转换成一个byte[]；
                                         * */
                                    byte[] bitmaps = getbitmaptobytes(scanbitmap);

                                        /*
                                         * 将Byte[]转换成long类型
                                         * */
                                    long longbitmaps = bytes2long(bitmaps);
                                        /*
                                         * 判断SD卡是否有足够的空间供下载使用
                                         * */
//											boolean iscapacity = isEnaleforDownload(longbitmaps);
                                    if (true) {

                                        try {
                                            //防止出现重复名字
                                            String fileName = time + ".png";
                                            File cacheFile = new File(App.dir, fileName);

                                            CollBean collBean = new CollBean();
                                            collBean.setName(text.getText().toString());
                                            collBean.setUrl(fileName);
                                            Debug.d(" dir + fileName" + App.dir + File.separator + fileName);
                                            lis.add(collBean);
                                            collDatas();
                                            FileOutputStream fstream = new FileOutputStream(cacheFile);
                                            outStream = new BufferedOutputStream(fstream);
                                            outStream.write(bitmaps);


                                            Toast.makeText(getActivity(), "图片成功存至myscan目录下", Toast.LENGTH_SHORT).show();

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } finally {

                                            if (outStream != null) {
                                                try {
                                                    outStream.close();
                                                } catch (IOException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                            //TODO
                                            dialog.dismiss();
                                            dialog = null;
                                        }

                                    }
                                } else {
                                    Toast.makeText(getActivity(), "SDCard存储空间不足", Toast.LENGTH_SHORT).show();
                                }

                            }

                            private boolean isEnaleforDownload(long longbitmaps) {
                                //Statfs : 获取系统文件的类
                                //@.getAbsolutePath()给一个抽象路径名的绝对路径字符串
                                StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());

                                //获得你的手机共有几个存储，即获得块的总量
                                int blockCount = statfs.getBlockCount();

                                //该手机里可用的块的数量，即可用的存储。也可以说是剩余内存容量
                                int availableBlocks = statfs.getAvailableBlocks();

                                    /*
                                     * 获得每一个块的大小， 返回值用long接受，int可能达到上限
                                     * */
                                long blockSize = statfs.getBlockSize();
                                //获得可用的存储空间

                                long asavespace = availableBlocks * blockSize;

                                if (asavespace > longbitmaps) {
                                    return true;
                                }
                                return false;
                            }

                            // 将Byte[]转换成long类型
                            private long bytes2long(byte[] bitmaps) {
                                int num = 0;
                                for (int ix = 0; ix < 8; ++ix) {
                                    num <<= 8;
                                    num |= (bitmaps[ix] & 0xff);
                                }
                                return num;
                            }

                            //将图片转换成Byte[]
                            private byte[] getbitmaptobytes(Bitmap bitmap) {
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                return out.toByteArray();
                            }
                        }).show();
            }
        });

    }


    private void addLogo() {

    }


    //生成二维码
    public Bitmap createQRImage(String url) {

        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(url.getBytes("GBK"), "ISO-8859-1"), BarcodeFormat.QR_CODE, 300, 300);
//            BitMatrix matrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, 300, 300,hints);
//            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View view, final int pos) {
        if (TextUtils.isEmpty(text.getText())) {
            Toast.makeText(getActivity(), "未输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //从上往下数
                switch (pos) {
                    case 1://生成普通二维码
                        creatCodeScan();
                        break;
                    case 2://从相机生成logo二维码
                        pick.getCameraPhoto();
                        break;
                    case 3://从相册生成logo二维码
                        pick.getLocalPhoto();
                        break;
                    case 4://本地保存+收藏:
                        if (codeScan == null) {
                            Toast.makeText(getActivity(), "请先生成二维码再保存", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    saveLocal(codeScan);
                                }
                            }).start();
                        }
                        break;
                    case 5://发信息
                        sendMsg();
                        break;
                }
            }
        });
        Toast.makeText(getActivity(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
    }

    private void collDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor edt = App.sp.edit();
                Type type = new TypeToken<List<CollBean>>() {
                }.getType();
                String datas = new Gson().toJson(lis, type);
                edt.putString("Coll", datas);
                edt.commit();
            }
        }).start();
    }

    private void sendMsg() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("18581889095", null, text.getText().toString(), null, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pick != null) {
            pick.onActivityResult(requestCode, resultCode, data);
        }
    }
}
