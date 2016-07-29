package project.zero.codescan.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagePick {
	static final String TAG = ("ImageInfoAction");

	public static final int INTENT_REQUEST_CODE_ALBUM = 101; // 本地相册
	public static final int INTENT_REQUEST_CODE_CAMERA = 102; // 相机
	public static final int INTENT_REQUEST_CODE_CROP = 103; // 剪切
	public static final int INTENT_REQUEST_CODE_VIDEO = 104; // 选择视频

	private static String imgPath; // 图片文件缓存的目录
	private String mCurrImgFile; // 当前图片的文件路径

	protected Activity activity;
	// 图片的宽高
	private int outputX = 200;
	private int outputY = 200;
	// 图片的比例
	private int aspectX = 1;
	private int aspectY = 1;
	// 是否需要剪切
	private boolean mCrop = true;

	private OnBitmapListener onBitmapListener;
	private Fragment fragment;

	private Handler handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			if (null == onBitmapListener)
				return;

			String resPath = (String) msg.obj;
			if (!mCrop) {
				String imagePath = getImgFile();
				Bitmap bitmap = ImageUtil.getBitmapFromFile(resPath, 720, 1280);
				// 压缩...
				PhotoUtils.saveBmpToSdCard(bitmap, new File(imagePath));
				onBitmapListener.onSelectBitmap(imagePath, bitmap);
			} else {
				Bitmap bitmap = ImageUtil.getBitmapFromFile(resPath, 720, 1280);
				onBitmapListener.onSelectBitmap(resPath, bitmap);
			}

		}

	};

	public ImagePick(Activity activity, OnBitmapListener bListener) {
		this(activity, bListener, true, 200, 200, null);
	}

	public ImagePick(Fragment fragment, OnBitmapListener listener) {
		this(fragment.getActivity(), listener, true, 200, 200, fragment);
	}

	public void setOnBitmapListener(OnBitmapListener onBitmapListener) {
		if (onBitmapListener == null) {
			return;
		}
		this.onBitmapListener = onBitmapListener;
	}

	/**
	 * @param activity
	 * @param bListener
	 * @param crop
	 *            是否需要剪切
	 * @param outX
	 *            剪切的宽
	 * @param outY
	 *            剪切的高
	 */
	private ImagePick(Activity activity, OnBitmapListener bListener,
			boolean crop, int outX, int outY, Fragment fragment) {
		this.activity = activity;
		imgPath = getCachePath(activity).getAbsolutePath();
		this.mCrop = crop;
		this.onBitmapListener = bListener;
		this.outputX = outX;
		this.outputY = outY;
		this.fragment = fragment;
	}

	public void setOutputX(int outputX) {
		this.outputX = outputX;
	}

	public void setOutputY(int outputY) {
		this.outputY = outputY;
	}

	public void setAspectX(int aspectX) {
		this.aspectX = aspectX;
	}

	public void setAspectY(int aspectY) {
		this.aspectY = aspectY;
	}

	public void setCrop(boolean crop) {
		this.mCrop = crop;
	}

	/**
	 * 获取本地图片
	 */
	public void getLocalPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/jpeg");
		if (fragment != null) {
			fragment.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
			return;
		}

		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
	}

	/**
	 * 获取相机的图片
	 *
	 * @throws IOException
	 */
	public void getCameraPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = getImgFile();
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		mCurrImgFile = path;
		if (this.fragment != null) {
			fragment.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
			return;
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);

	}

	/**
	 * @param resPath
	 *            剪切图片的地址
	 * @param outPath
	 *            剪切成功后的保存的目标地址
	 */
	public void cropPhoto(String resPath, String outPath) {
		Uri uri = FileUtils.getUriFromFile(resPath);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// 比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				FileUtils.getUriFromFile(outPath));
		intent.putExtra("return-data", false);
		intent.putExtra("noFaceDetection", true); // no face detection
		if (fragment != null) {
			fragment.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
			return;
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
	}

	private String getImgPath(Intent data) {
		Uri uri = data.getData();
		Scheme scheme = Scheme.ofUri(uri.toString());
		switch (scheme) {
		case CONTENT: {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.getContentResolver().query(uri, proj,
					null, null, null);
			try {
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						return path;
					}
				}
				return null;
			} catch (Exception e) {
			} finally {
				if (cursor != null) {
					try {
						cursor.close();
					} catch (Exception e2) {
					}
				}
			}
		}
			break;
		case FILE:
			return scheme.crop(uri.toString());
		case ASSETS:
			break;
		case DRAWABLE:
			break;
		case HTTP:
			break;
		case HTTPS:
			break;
		case UNKNOWN:
			break;
		default:
			break;
		}

		return null;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (!FileUtils.isSdcardExist()) {
				Toast.makeText(activity, "SD卡不可用!", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		switch (requestCode) {
		// 选择相册返回
		case INTENT_REQUEST_CODE_ALBUM:
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				String resPath = getImgPath(data);
				if (!mCrop) {
					handler.sendMessage(handler.obtainMessage(
							INTENT_REQUEST_CODE_ALBUM, resPath));
					return;
				}
				mCurrImgFile = getImgFile();
				if (TextUtils.isEmpty(resPath)) {
					return;
				}
				cropPhoto(resPath, mCurrImgFile);
			}
			break;

		case INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				if (!TextUtils.isEmpty(mCurrImgFile)) {
					if (!mCrop) {
						handler.sendMessage(handler.obtainMessage(
								INTENT_REQUEST_CODE_CAMERA, mCurrImgFile));
						return;
					}
					String resPath = mCurrImgFile;
					mCurrImgFile = getImgFile();
					cropPhoto(resPath, mCurrImgFile);
				}
			}
			break;

		case INTENT_REQUEST_CODE_CROP:
			if (resultCode == Activity.RESULT_OK) {
				handler.sendMessage(handler.obtainMessage(
						INTENT_REQUEST_CODE_CROP, mCurrImgFile));
			}
			break;
		}

	}

	public static void clearImgCache(Context context) {
		if (!FileUtils.isSdcardExist()) {
			return;
		}
		try {
			FileUtils.delFolder(getCachePath(context).getAbsolutePath());
		} catch (Exception e) {

		}

	}

	public static File getCachePath(Context context) {
		return new File(context.getExternalCacheDir().getAbsolutePath(),
				"imgpick");
	}

	private String getImgFile() {
		if (TextUtils.isEmpty(imgPath)) {
			throw new NullPointerException();
		}
		String pName = getPictureName();
		FileUtils.createDir(imgPath);
		return new File(imgPath, pName).getAbsolutePath();
	}

	@SuppressLint("SimpleDateFormat")
	public static String getPictureName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		String path = dateFormat.format(date) + ".jpg";
		return path;
	}

	public interface OnBitmapListener {
		void onSelectBitmap(String path, Bitmap bm);
	}

	public enum Scheme {
		HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"), ASSETS(
				"assets"), DRAWABLE("drawable"), UNKNOWN("");

		private String scheme;
		private String uriPrefix;

		Scheme(String scheme) {
			this.scheme = scheme;
			uriPrefix = scheme + "://";
		}

		/**
		 * Defines scheme of incoming URI
		 *
		 * @param uri
		 *            URI for scheme detection
		 * @return Scheme of incoming URI
		 */
		public static Scheme ofUri(String uri) {
			if (uri != null) {
				for (Scheme s : values()) {
					if (s.belongsTo(uri)) {
						return s;
					}
				}
			}
			return UNKNOWN;
		}

		private boolean belongsTo(String uri) {
			return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
		}

		/**
		 * Appends scheme to incoming path
		 */
		public String wrap(String path) {
			return uriPrefix + path;
		}

		/**
		 * Removed scheme part ("scheme://") from incoming URI
		 */
		public String crop(String uri) {
			if (!belongsTo(uri)) {
				throw new IllegalArgumentException(String.format(
						"URI [%1$s] doesn't have expected scheme [%2$s]", uri,
						scheme));
			}
			return uri.substring(uriPrefix.length());
		}
	}

}
