package com.hy.apk_tiqu;


import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity 
{
	public static String work_dir = "APK提取";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		File file = new File(Environment.getExternalStorageDirectory(),work_dir);
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
			String type_path = copyApk(this,packageName);
			if(type_path != null){
				str = "APK提取成功\n储存在"+type_path;
			}
		}
		Toast.makeText(this,str,Toast.LENGTH_LONG).show();
		finish();
    }
	
		public static String copyApk(Context context,String packageName){
			String[] info = getApkInfo(context,packageName); 
			if(info!=null){
				String path = info[0];
				String name = info[1]+"_"+info[2]+"_"+info[3]+"_"+info[4] + ".apk";
				String dest = new File(new File(Environment.getExternalStorageDirectory(),work_dir),name).getPath();
				//path:app程序源文件路径  dest:新的存储路径  name:app名称
				new Thread(new CopyRunnable(path, dest, name)).start();
				return dest;
			} else {
				return null;
			}
		}

		
		private static String[] getApkInfo(Context context,String packageName){
			//1 PackageDir 2 Name 3 PackageNme 4 VersionName 5 VersionCode
			String[] info = new String[5];
			
			try {
				ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
				//通过包名获取程序各项信息
				info[0] = applicationInfo.sourceDir;
				info[1] = context.getPackageManager().getApplicationLabel(applicationInfo).toString();
				info[2] = applicationInfo.packageName;
				
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName,PackageManager.GET_CONFIGURATIONS);
				info[3] = packageInfo.versionName;
				info[4] = String.valueOf( packageInfo.versionCode );
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
				info = null;
			}
			return info;
		}
}
