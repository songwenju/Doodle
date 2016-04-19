package com.zhangshuo.doodle.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtils {
	/**
	 * 把流转换成String，尾部不会有多余的空格，但在使用的过程中要判断空
	 * @param in
	 * @return
	 */
	public static String convertStreamToString(InputStream in) {
		//定义一个ByteArrayOutputStream:可以捕获内存缓冲区的数据，转换成字节数组
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int len;
			byte[]buffer = new byte[1024];
			while ((len = in.read(buffer))!= -1) {
				out.write(buffer, 0, len);
			}
			in.close();
			byte[] result = out.toByteArray();
			//在这里可以指定文件的编码
			return new String(result,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 该类会返回一个在尾部添加了很多空格的字符串
	 * @param is
	 * @return
	 */
	public static String convertStreamToString2(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}
