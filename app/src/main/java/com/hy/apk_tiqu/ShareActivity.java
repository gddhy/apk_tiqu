package com.hy.apk_tiqu;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;
import java.io.*;

public class ShareActivity extends Activity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		File file = new File(Environment.getExternalStorageDirectory(),MainActivity.work_dir);
		if(!file.exists()){
			file.mkdirs();
		}
		String str = "APK提取失败";
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		String mihome_uri = "content://com.miui.home.fileprovider/root/data/app/";
		if(Intent.ACTION_SEND.equals(action)&&type.equals("application/vnd.android.package-archive")){
			String uri = intent.getExtras().get(Intent.EXTRA_STREAM).toString();
			String packageName = uri.substring(mihome_uri.length(),uri.indexOf("-"));
			String type_path = MainActivity.copyApk(this,packageName);
			if(type_path != null){
				str = "APK已提取\n请分享";
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_STREAM,
									 Uri.fromFile(new File(type_path)));
				shareIntent.setType("*/*");//此处可发送多种文件
				startActivity(Intent.createChooser(shareIntent,"分享到："));
			}
		}
		Toast.makeText(this,str,Toast.LENGTH_LONG).show();
		finish();
    }
}
