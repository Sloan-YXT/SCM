package com.team7.app.campus_life;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.team7.app.common.AppException;
import com.team7.app.common.Constants;
import com.team7.app.common.HttpHelper;
import com.team7.app.common.HttpHelper.Callback;
import com.team7.app.common.HttpPostUploadUtil;
import com.team7.app.entity.Shop;
import com.team7.app.entity.Users;
import com.team7.app.service.MyApplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReleaseActivity extends Activity {
	private Button btn_back,btn_right;
	private Spinner spinner_type;
	private String[] titles = {"体育用品", "办公用品","生活用品", "自行车", "电子产品", "图书", "电脑配件" ,"自习邀约" };
	private View view, dialogView;
	private TextView title,lable, gallery, camera;// Spinner的选项，对话框的相册和相机
	private ImageView addImageView;
	private Dialog dialog;
	private LinearLayout addPicLayout;
	private Bitmap bitmap;
	private ArrayList<Bitmap> bitmap_list = new ArrayList<Bitmap>();
	private ArrayList<String> photo_list = new ArrayList<String>();// 图片的路径集合
	private String picturePath = null;// 图片的存放路径
	private Shop shop;// 发布信息实体
	private EditText shopname, price, userPhone, description;
	private Button release;
	private String type = "fffff";
	private MyApplication myApplication;
	private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/systemCemer";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release);
		myApplication = (MyApplication) this
				.getApplicationContext();
		myApplication.addActivity(this);
		title = (TextView) this.findViewById(R.id.title_tv);
		title.setText("发布商品");
		shopname = (EditText) this.findViewById(R.id.releaseTitle);
		price = (EditText) this.findViewById(R.id.releasePrice);
		userPhone = (EditText) this.findViewById(R.id.link);
		description = (EditText) this.findViewById(R.id.describe);
		addPicLayout = (LinearLayout) this.findViewById(R.id.addPicLayout);
		btn_back = (Button) this.findViewById(R.id.button_back);
		final LinearLayout alter = (LinearLayout)findViewById(R.id.alter);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				View view = getLayoutInflater().inflate(R.layout.my_dialog,
						null, false);
				TextView title = (TextView) view.findViewById(R.id.title);
				title.setText("温馨提示");
				TextView message = (TextView) view
						.findViewById(R.id.message);
				message.setVisibility(View.VISIBLE);
				message.setText("你确定要取消发布吗？你填写的内容将丢失");
				TextView gallery = (TextView) view
						.findViewById(R.id.gallery);
				gallery.setVisibility(View.GONE);
				TextView camera = (TextView) view.findViewById(R.id.camera);
				camera.setVisibility(View.GONE);
				final Dialog dialog = new Dialog(ReleaseActivity.this,
						R.style.myDialogTheme);
				dialog.setContentView(view);
				dialog.show();
				Button cancel = (Button) view.findViewById(R.id.button_cancel);
				cancel.setVisibility(View.VISIBLE);
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button ok = (Button) view.findViewById(R.id.button_ok);
				ok.setVisibility(View.VISIBLE);
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent =  new Intent(ReleaseActivity.this,MainActivity.class);
						ReleaseActivity.this.startActivity(intent);
						ReleaseActivity.this.finish();
					}
				});
			}
		});
		// 注册按钮事件监听
		btn_right = (Button) this.findViewById(R.id.button_right);
		btn_right.setVisibility(View.GONE);
		spinner_type = (Spinner) this.findViewById(R.id.spinner_type);
		// 二手类型的spinner的适配器
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, titles) {
			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				view = getLayoutInflater().inflate(R.layout.spinner_item,
						parent, false);
				lable = (TextView) view.findViewById(R.id.label);
				lable.setText(getItem(position));

				if (spinner_type.getSelectedItemPosition() == position) {
					lable.setTextColor(getResources().getColor(
							R.color.selected_fg));
					view.setBackgroundColor(getResources().getColor(
							R.color.selected_bg));
					view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
				}
//				if(position==titles.length-1)
//				{
//					alter.setVisibility(View.GONE);
//				}
				return view;
			}
		};
		spinner_type.setAdapter(adapter);
		spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				type = titles[position];
				Log.v("dddddddddddddddddddddd", type);
				if(type.equals("自习邀约"))
				{
					TextView text =(TextView) findViewById(R.id.tx_releasePrice);
					EditText price = (EditText)findViewById(R.id.releasePrice);
					text.setText("地 点");
					price.setHint("请填写自习地点");
					price.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		addImageView = (ImageView) this.findViewById(R.id.addImageView);
		addImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogView = getLayoutInflater().inflate(R.layout.my_dialog,
						null, false);
				dialog = new Dialog(ReleaseActivity.this, R.style.myDialogTheme);
				dialog.setContentView(dialogView);
				dialog.show();
				// 从相册选择图片
				gallery = (TextView) dialogView.findViewById(R.id.gallery);
				gallery.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (photo_list.size() == 3) {
							Toast.makeText(ReleaseActivity.this,
									"对不起，内存限制不能再添加图片了～～", Toast.LENGTH_LONG)
									.show();
						} else {
							Log.e("picturePath","before");
							Intent intent = new Intent(
									Intent.ACTION_PICK);
							intent.setType("image/*");
							intent.putExtra("return-data", true);
							startActivityForResult(intent, 2);
						}
						dialog.dismiss();
					}
				});
				// 拍照
				camera = (TextView) dialogView.findViewById(R.id.camera);
				camera.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (photo_list.size() == 3) {
							Toast.makeText(ReleaseActivity.this,
									"对不起，内存限制只能添加少于等于三张图片", Toast.LENGTH_LONG)
									.show();
						} else {
							mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/systemCemer";

							//存储文件夹操作

							File outFilePath = new File(mFilePath);

							if (!outFilePath.exists()) {

								outFilePath.mkdirs();

							}

							//设置自定义照片的名字

							String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
							mFilePath = mFilePath + "/" + fileName + ".jpg";



							File outFile = new File(mFilePath);

							Uri uri = Uri.fromFile(outFile);
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
							startActivityForResult(intent, 1);
						}
						dialog.dismiss();

					}
				});
			}
		});
		// 发布信息
		release = (Button) this.findViewById(R.id.release);
		release.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checked()) {
					View view = getLayoutInflater().inflate(
							R.layout.progress_dialog, null, false);
					dialog = new Dialog(ReleaseActivity.this,
							R.style.myDialogTheme);
					dialog.setContentView(view);
					dialog.show();
					Users users = (Users) myApplication.userMap.get("user");
					String picture = "";
					for (int i = 0; i < photo_list.size(); i++) {
						picture += photo_list.get(i).substring(
								photo_list.get(i).lastIndexOf("/") + 1)
								+ ";";
						loadImage(photo_list.get(i));
					}
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("shopname", shopname.getText().toString());
					params.put("price", price.getText().toString());
					params.put("userName", users.getUserName());
					params.put("userPhone", userPhone.getText().toString());
					params.put("description", description.getText().toString());
					params.put("category", type);
					params.put("picture", picture);
					Log.v("dddddddddddddddddddddd", params.toString());
					HashMap<String, File> fileMap = new HashMap<String, File>();

					Log.v("ddddddddddddddddddfor (int i = 0; i < photo_list.size(); i++) {\n" +
							"\t\t\t\t\t\tfileMap.put(\"fileMap\" + i, new File(photo_list.get(i)));\n" +
							"\t\t\t\t\t}dddd", fileMap.toString());
					HttpHelper.asyncMultipartPost(Constants.URL
							+ "/second-hand/shop_add.do", params, fileMap,
							new Callback() {
								@Override
								public void dataLoaded(Message msg) {
									if (HttpStatus.SC_OK != msg.what) {
										dialog.dismiss();
										AppException.http(msg.what).makeToast(
												ReleaseActivity.this);
										return;
									}
									dialog.dismiss();
									String message = (String) msg.obj;
									Toast.makeText(ReleaseActivity.this,
											message, Toast.LENGTH_LONG).show();
									ReleaseActivity.this.finish();
								}
							});
				}
			}
		});
	}

	// 图片的处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		bitmap = null;
		if (requestCode == 1 && resultCode == RESULT_OK) {

			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			/*
			Bundle bundle = data.getExtras();
			bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			*/
			bitmap = loadingImageBitmap(mFilePath);
			Log.e("path","SystemCemerActivity" + mFilePath);
			FileOutputStream b = null;
			File file = new File("/sdcard/campus_life/myImage/");
			file.mkdirs();// 创建文件夹
			picturePath = "/sdcard/campus_life/myImage/"
					+ System.currentTimeMillis() + ".jpg";
			try {
				b = new FileOutputStream(picturePath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			Log.e("picturePath",data.getData().toString());
			try {
				Log.e("picturePath",MediaStore.Images.Media.DATA);
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Log.e("picturePath", Arrays.toString(filePathColumn));
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				Log.e("picturePath","column:"+columnIndex);
				picturePath = cursor.getString(columnIndex);
				FileOutputStream b = null;
				File file = new File("/sdcard/campus_life/myImage/");
				file.mkdirs();// 创建文件夹
				if(picturePath==null)
				{
					Log.e("picturePath","null here!");
				}
				Log.e("picturePath",picturePath);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				bitmap = BitmapFactory.decodeFile(picturePath,options);
				picturePath = "/sdcard/campus_life/myImage/"
						+ System.currentTimeMillis() + ".jpg";
				try {
					b = new FileOutputStream(picturePath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					//缺陷：压缩会损失画质！
					//如果是本身分辨率就低的照片，这么压缩后就更看不清了
					//把文件拉大来适应尺寸，清晰度又会变低
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				Log.e("本地图片",Log.getStackTraceString(e));
			}
		}
		if (bitmap != null && picturePath != null) {
			Log.v("dddddddddddddddddddddd", picturePath);
			photo_list.add(picturePath);
			Log.e("path","pic path:"+picturePath);
			bitmap_list.add(bitmap);
			showPhoto(bitmap);
		}
	}

	// 图片的显示
	public void showPhoto(final Bitmap bitmap) {
		final View item = LayoutInflater.from(this).inflate(
				R.layout.photo_item, null);
		ImageView photo = (ImageView) item.findViewById(R.id.photo);
		photo.setImageBitmap(bitmap);
		ImageView delete = (ImageView) item.findViewById(R.id.photo_delete);
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				photo_list.remove(picturePath);
				addPicLayout.removeView(item);
				destoryBitmap(bitmap);
			}
		});
		addPicLayout.addView(item);
	}

	// 发布信息验证
	private boolean checked() {
		if (shopname.getText().toString() == null
				|| shopname.getText().toString().equals("")) {
			Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (price.getText().toString() == null
				|| price.getText().toString().equals("")) {
			Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (userPhone.getText().toString() == null
				|| userPhone.getText().toString().equals("")
				|| userPhone.length() != 11) {
			Toast.makeText(this, "电话是十一位", Toast.LENGTH_SHORT).show();
			return false;
		} else {

			return true;
		}
	}
	// 销毁bitmap 释放内存
	public void destoryBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			View view = getLayoutInflater().inflate(R.layout.my_dialog,
					null, false);
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText("温馨提示");
			TextView message = (TextView) view
					.findViewById(R.id.message);
			message.setVisibility(View.VISIBLE);
			message.setText("你确定要取消发布吗？你填写的内容将丢失");
			TextView gallery = (TextView) view
					.findViewById(R.id.gallery);
			gallery.setVisibility(View.GONE);
			TextView camera = (TextView) view.findViewById(R.id.camera);
			camera.setVisibility(View.GONE);
			final Dialog dialog = new Dialog(ReleaseActivity.this,
					R.style.myDialogTheme);
			dialog.setContentView(view);
			dialog.show();
			Button cancel = (Button) view.findViewById(R.id.button_cancel);
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			Button ok = (Button) view.findViewById(R.id.button_ok);
			ok.setVisibility(View.VISIBLE);
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					ReleaseActivity.this.finish();
				}
			});
		}
		return true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap_list != null){
			for(int i = 0;i < bitmap_list.size();i++){
				destoryBitmap(bitmap_list.get(i));
			}
		}
	}
	private void loadImage(String picturePath)
	{
		StringBuilder content=new StringBuilder("");
		String r;
		int f;
		File file = new File(picturePath);
		int length=0;
		{
			try {
				FileInputStream inp = new FileInputStream(picturePath);
				while(inp.read()!=-1)
				{
					length++;
				}
				inp.close();
			}
			catch (Exception e)
			{
				Log.e("bugl",Log.getStackTraceString(e));
			}
		}
		Log.e("bugl","len1="+length+";len2="+file.length());
		byte[] box = new byte[(int)file.length()];
		Log.e("length","len=="+file.length());
		try {
			FileInputStream inp = new FileInputStream(picturePath);
			BufferedReader in = new BufferedReader(new InputStreamReader(inp,"utf-8"));
			DataInputStream ind = new DataInputStream(inp);
//				while((r=in.readLine())!=null)
//				{
//					content.append(r);
//				}
//				in.close();
//				inp.close();
//				ind.close();
			inp.read(box);
		}
		catch (Exception e)
		{
			Log.e("picture",Log.getStackTraceString(e));
		}
		//Log.e("picture",content.substring(0,100));
		Log.e("path",picturePath.substring(picturePath.lastIndexOf("/")+1));
		Log.e("length:",""+content.length());
		Log.e("length",""+new String(box).length());
		//params.put("content",content.substring(0,100));
//			File picture = new File(picturePath);
//			HashMap<String,File> param2 = new HashMap<String, File>();
//			param2.put("content",picture);
//
//			HttpHelper.asyncMultipartPost(Constants.URL + "/second-hand/user-images.do",params, param2,new Callback() {
//				@Override
//				public void dataLoaded(Message msg) {
//					if(msg.what!=HttpStatus.SC_OK)
//					{
//						Toast.makeText(getApplicationContext(),"图片上传失败:"+msg.what,Toast.LENGTH_SHORT).show();
//						Log.e("picture",""+msg.what);
//					}
//				}
//			});
//			try {
//				Log.e("path",params.toString());
//			}
//			catch (Exception e)
//			{
//				Log.e("picture",Log.getStackTraceString(e));
//			}
		HttpPostUploadUtil.formUpload(Constants.URL + "/second-hand/user-images.do",picturePath.substring(picturePath.lastIndexOf("/")+1),box, new HttpPostUploadUtil.CallBack() {
			@Override
			public void loadData(Message msg) {
				if(msg.what!=HttpStatus.SC_OK)
				{
					Toast.makeText(getApplicationContext(),"上传图片失败",Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	public Bitmap loadingImageBitmap(String imagePath) {

		/**

		 * 获取屏幕的宽与高

		 */

		final int width = getWindowManager().getDefaultDisplay().getWidth();

		final int height = getWindowManager().getDefaultDisplay().getHeight();

		/**

		 * 通过设置optios来只加载大图的尺寸

		 */

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		Bitmap bitmap = null;

		try {

			bitmap = BitmapFactory.decodeFile(imagePath, options);

			/**

			 * 计算手机宽高与显示大图的宽高，然后确定缩放有比例

			 */

			int widthRaio = (int) Math.ceil(options.outWidth/(float)width);

			int heightRaio = (int) Math.ceil(options.outHeight/(float)height);

			if (widthRaio>1&&heightRaio>1){

				if (widthRaio>heightRaio){

					options.inSampleSize = widthRaio;

				}else {

					options.inSampleSize = heightRaio;

				}

			}

			/**

			 * 设置加载缩放后的图片

			 */

			options.inJustDecodeBounds = false;

			bitmap = BitmapFactory.decodeFile(imagePath, options);

		} catch (Exception e) {

			e.printStackTrace();

		}



		return bitmap;

	}
}
