package com.hy.apk_tiqu;

/**
 * 将程序源文件Copy到指定目录
 */
import android.annotation.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.nio.channels.*;

public class CopyRunnable implements Runnable
 {
	private String source;
	private String dest;
	private String key;

	public CopyRunnable(String source, String dest, String key) {
		this.source = source;
		this.dest = dest;
		this.key = key;
	}
	@SuppressLint("StringFormatInvalid")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			int length = 1024 * 1024;
			File file = new File(Environment.getExternalStorageDirectory(),MainActivity.work_dir);
			if (!file.exists()) {
				boolean mk = file.mkdirs();
				if(mk){
					System.out.println("true");
				}
			}

			File fDest = new File(dest);
			if (fDest.exists()) {
				fDest.delete();
			}
			fDest.createNewFile();
			FileInputStream in = new FileInputStream(new File(source));
			FileOutputStream out = new FileOutputStream(fDest);
			FileChannel inC = in.getChannel();
			FileChannel outC = out.getChannel();
			int i = 0;
			while (true) {
				if (inC.position() == inC.size()) {
					inC.close();
					outC.close();
					//成功
					break;
				}
				if ((inC.size() - inC.position()) < 1024 * 1024) {
					length = (int) (inC.size() - inC.position());
				} else {
					length = 1024 * 1024;
				}
				inC.transferTo(inC.position(), length, outC);
				inC.position(inC.position() + length);
				i++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("TAG", e.toString());
		}
	}
}

