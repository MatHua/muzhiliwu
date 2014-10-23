package com.muzhiliwu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

@IocBean
public class FileUtils {
	private ServletContext sc;

	public String getPath(String path) {
		return sc.getRealPath(path);
	}

	/**
	 * 从properties文件查找属性，如果没有，则返回默认值,
	 * 
	 * @param path
	 * @param key
	 * @param defValue
	 * @param isIncrease
	 * @return
	 */
	public static synchronized String getProperties(String path, String key,
			String defValue) {
		String value = "";
		FileInputStream in;
		try {
			in = new FileInputStream(path);

			Properties p = new Properties();
			// 如果此处直接 使用 visitFile 会报错，找不到文件。
			p.load(in);
			in.close();
			value = p.getProperty(key);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Strings.isBlank(value)) {
			return defValue;
		}
		return value;
	}

	public static void readTxtFile(String filePath) {
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (lineTxt.length() > 0)
						for (String s : lineTxt.split(" ")) {
							System.out.println(s);
						}
					// System.out.println(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

	}

	public static long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	// 递归
	public long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;

	}

	public static void main(String argv[]) throws Exception {
		String filePath = "C:/Users/Administrator/Desktop/u=3333596883,574321280&fm=21&gp=0.jpg";

		System.out.println(getFileSizes(new File(filePath)));
		;
	}

}
