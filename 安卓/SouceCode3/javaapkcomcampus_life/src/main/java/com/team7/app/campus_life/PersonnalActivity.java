package com.team7.app.campus_life;

import com.team7.app.campus_life.R.id;
import com.team7.app.common.Constants;
import com.team7.app.common.HttpHelper;
import com.team7.app.common.HttpPostUploadUtil;
import com.team7.app.entity.Users;
import com.team7.app.service.MyApplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class PersonnalActivity extends Activity implements OnClickListener {
	private TextView title, publish,message,collection,look;
	private Button btn_back, btn_right;
	private MyApplication myApplication;
	private View dialogView;
	private Dialog dialog;
	private Intent intent;
	private TextView camera,gallery;
	Bitmap bitmap;
	String picturePath="";
	String subpath="";
	ImageView my_icon;
	private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/systemCemer";
	SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		preferences = getSharedPreferences("shared,",MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center);
		myApplication = (MyApplication) this.getApplicationContext();
		myApplication.addActivity(this);
		Users users = (Users) myApplication.userMap.get("user");
		final TextView username = (TextView) findViewById(R.id.name);
		final TextView school = (TextView) findViewById(R.id.school);
		final TextView subject = (TextView) findViewById(R.id.subject);
		final TextView upload = (TextView)findViewById(R.id.icon_text);
		my_icon=(ImageView)findViewById(R.id.my_icon);
		my_icon.setImageResource(R.drawable.iboy);
		my_icon.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dialogView = getLayoutInflater().inflate(R.layout.my_dialog,
						null, false);
				dialog = new Dialog(PersonnalActivity.this, R.style.myDialogTheme);
				dialog.setContentView(dialogView);
				dialog.show();
				// 从相册选择图片
				gallery = (TextView) dialogView.findViewById(R.id.gallery);
				gallery.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

							Log.e("picturePath","before");
							Intent intent = new Intent(
									Intent.ACTION_PICK);
							intent.setType("image/*");
							intent.putExtra("return-data", true);
							startActivityForResult(intent, 2);
							dialog.dismiss();
					}
				});
				// 拍照
				camera = (TextView) dialogView.findViewById(R.id.camera);
				camera.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

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

						dialog.dismiss();

					}
				});
			}
		});
		upload.setOnClickListener(new View.OnClickListener(){

			//final Users	user = (Users)myApplication.userMap.get("user"); //!!!no!or set in the beginning!

			@Override
			public void onClick(View view) {
				final Users	user = (Users)myApplication.userMap.get("user");
				subpath = picturePath.substring(picturePath.lastIndexOf('/')+1,picturePath.length());
				Log.e("path","on click:"+picturePath);
				Log.e("user:",user==null?"null":user.toString());
				if(picturePath.equals(""))
				{
					Log.e("path","on click:"+picturePath);
					Toast.makeText(PersonnalActivity.this,"请点击上方图片选择图片上传",Toast.LENGTH_LONG).show();
				}
				else
				{
					HashMap<String,Object> params = new HashMap<String, Object>();
					params.put("username",user.getUserName());
					Log.e("path","username:"+user.getUserName());
					params.put("path",subpath);
					SharedPreferences.Editor editor =  preferences.edit();
					editor.putString("path",subpath);
					editor.putString("save-path",picturePath);
					editor.apply();
					Log.e("path","path:"+picturePath);
					HttpHelper.asyncPost(Constants.URL
							+ "/second-hand/user-icon.do", params, new HttpHelper.Callback() {
						@Override
						public void dataLoaded(Message msg) {
							String text="";
							if(msg.what!=200) {
								if (msg.what == 500) {
									text = "图片保存失败：服务器内部错误！";
								} else if (msg.what == 404) {
									text = "资源不存在!";
								}
							}
							Toast.makeText(PersonnalActivity.this,text,Toast.LENGTH_SHORT);
						}
					});
					loadImage(picturePath);
				}
			}
		});
		if (users != null) {
			username.setText(users.getUserName());
			school.setText(users.getSchool());
			subject.setText(users.getProfessional());
			Log.e("user",subject.getText().toString());
			downLoadImg();
		} else {
			SharedPreferences.Editor edt = preferences.edit();
			edt.putString("path","");
			edt.putString("save-path","");
			edt.apply();
			View dialogView = getLayoutInflater().inflate(R.layout.my_dialog,
					null, false);
			TextView title = (TextView) dialogView.findViewById(R.id.title);
			title.setText("温馨提示");
			TextView message = (TextView) dialogView.findViewById(R.id.message);
			message.setVisibility(View.VISIBLE);
			message.setText("你还没有登录，请先登录");
			TextView gallery = (TextView) dialogView.findViewById(R.id.gallery);
			gallery.setVisibility(View.GONE);
			TextView camera = (TextView) dialogView.findViewById(R.id.camera);
			camera.setVisibility(View.GONE);
			final Dialog dialog = new Dialog(PersonnalActivity.this,
					R.style.myDialogTheme);
			dialog.setContentView(dialogView);
			dialog.setCancelable(false);
			dialog.show();
			Button cancel = (Button) dialogView
					.findViewById(R.id.button_cancel);
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					PersonnalActivity.this.finish();
				}
			});

			Button ok = (Button) dialogView.findViewById(R.id.button_ok);
			ok.setVisibility(View.VISIBLE);
			final Handler handler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) {
					Users user = (Users) myApplication.userMap.get("user");
					username.setText("用户名:"+user.getUserName());
					school.setText("学校:"+user.getSchool());
					subject.setText("专业:"+user.getCourt()+"/"+user.getProfessional());
					Log.e("user",subject.getText().toString());
					downLoadImg();
				}
			};
			myApplication.userMap.put("handler",handler);
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Intent intent = new Intent(PersonnalActivity.this,
							LoginActivity.class);
					PersonnalActivity.this.startActivity(intent);
				}
			});
		}
		title = (TextView) this.findViewById(R.id.title_tv);
		title.setText("个人中心");
		//我的发布
		publish = (TextView) this.findViewById(R.id.publish);
		publish.setOnClickListener(this);
		//返回按钮
		btn_back = (Button) this.findViewById(R.id.button_back);
		btn_back.setOnClickListener(this);
		// 注册按钮
		btn_right = (Button) this.findViewById(R.id.button_right);
		btn_right.setOnClickListener(this);
		//我的留言
		message = (TextView) this.findViewById(R.id.message);
		message.setOnClickListener(this);
		collection = (TextView) this.findViewById(R.id.collection);
		collection.setOnClickListener(this);
		look = (TextView) this.findViewById(R.id.look);
		look.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.publish:
			intent = new Intent(PersonnalActivity.this,
					MyReleaseActivity.class);
			startActivity(intent);
			break;
		case id.button_back:
			PersonnalActivity.this.finish();
			break;
		case id.button_right:
			if (!myApplication.userMap.isEmpty()) {
				View view = getLayoutInflater().inflate(R.layout.my_dialog,
						null, false);
				TextView title = (TextView) view.findViewById(R.id.title);
				title.setText("温馨提示");
				TextView message = (TextView) view
						.findViewById(R.id.message);
				message.setVisibility(View.VISIBLE);
				message.setText("你确定要注销吗？");
				TextView gallery = (TextView) view
						.findViewById(R.id.gallery);
				gallery.setVisibility(View.GONE);
				TextView camera = (TextView) view.findViewById(R.id.camera);
				camera.setVisibility(View.GONE);
				final Dialog dialog = new Dialog(PersonnalActivity.this,
						R.style.myDialogTheme);
				dialog.setContentView(view);
				dialog.show();
				Button cancel = (Button) view
						.findViewById(R.id.button_cancel);
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
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("path","");
						editor.apply();
						dialog.dismiss();
						myApplication.userMap.clear();
						Intent intent = new Intent(PersonnalActivity.this,
								MainActivity.class);
						startActivity(intent);
						PersonnalActivity.this.finish();
					}
				});
			}
			break;
		case id.message:
			intent = new Intent(PersonnalActivity.this,
					MyMsgActivity.class);
			startActivity(intent); 
			break;
		case id.collection:
			intent = new Intent(PersonnalActivity.this,
					MyCollectionActivity.class);
			startActivity(intent); 
			break;
		case id.look :
			intent = new Intent(PersonnalActivity.this,
					MyLookActivity.class);
			startActivity(intent); 
			break;
		default:
			break;
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
			inp.read(box);
		}
		catch (Exception e)
		{
			Log.e("picture",Log.getStackTraceString(e));
		}
		HttpPostUploadUtil.formUpload(Constants.URL + "/second-hand/icon.do",picturePath.substring(picturePath.lastIndexOf("/")+1),box, new HttpPostUploadUtil.CallBack() {
			@Override
			public void loadData(Message msg) {
				if(msg.what!= HttpStatus.SC_OK)
				{
					Toast.makeText(PersonnalActivity.this,"上传图片失败",Toast.LENGTH_SHORT).show();
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
				Log.e("picturePath", MediaStore.Images.Media.DATA);
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
		my_icon.setImageBitmap(bitmap);

	}
	private void downLoadImg()
	{
		Users user = (Users)myApplication.userMap.get("user");
		if(preferences.getString("path","").equals("")) {
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("username",user.getUserName());
			params.put("userid",user.getUserId());
			HttpHelper.asyncPost(Constants.URL + "/second-hand/user-icon-get.do", params, new HttpHelper.Callback() {

				String path;
				@Override
				public void dataLoaded(Message msg) {
					if(msg.what==200)
					{
						path = ((String)msg.obj).trim();


						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("path",path);
						editor.putString("save-path",Constants.SD_SRC+path);
						picturePath = Constants.SD_SRC+path;
						editor.apply();

						Log.e("user","path:"+path);
						new MyTask().execute(Constants.URL + "/second-hand/img/icon/"
								+ path, 0 + "");
					}
					else
					{
						Toast.makeText(PersonnalActivity.this,"error post user icon:"+msg.what,Toast.LENGTH_LONG).show();
					}
				}
			});

		}
		else
			//TO DO bug here
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			String localPath = preferences.getString("save-path","");
			Bitmap bitmap = BitmapFactory.decodeFile(localPath,options);
			my_icon.setImageBitmap(bitmap);
		}
	}
	public class MyTask extends
			AsyncTask<String, Void, HashMap<String, Object>> {

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			try {
				Log.e("url","what !!!!");
				HttpClient httpClient = HttpHelper.getHttpClient();
				String uri = params[0];
				HttpResponse response = httpClient.execute(new HttpGet(uri));
				Log.e("url","路径:"+uri);
				if (response.getStatusLine().getStatusCode() == 200) {
					InputStream is = (InputStream) response.getEntity()
							.getContent();
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
					is.close();
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("bitmap", bitmap);
					//hashMap.put("int", params[1]);
					return hashMap;
				} else {
					Log.e("path","result-status:"+response.getStatusLine().getStatusCode());
					return null;
				}
			} catch (Exception e) {
				Log.e("path",Log.getStackTraceString(e));
				Log.e("url",Log.getStackTraceString(e));
				return null;
			}
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			Users user = (Users)myApplication.userMap.get("user");
			if (result == null&&user!=null) {
				Toast.makeText(PersonnalActivity.this, "加载图片失败", 1).show();
				return;
			}
			if(result!=null) {
				Bitmap bitmap = (Bitmap) result.get("bitmap");
				//String string = (String) result.get("int");

				my_icon.setImageBitmap(bitmap);
			}
		}

	}
	/*
	public void destoryBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	*/
	/*
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destoryBitmap(bitmap);
	}*/
}
