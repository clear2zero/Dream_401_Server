package sto.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleUpload {
	public static boolean uploadFile(InputStream is,String uploadpath,String filename) {
		boolean isSuccess = false;
		File f = new File(uploadpath);
		if(!f.exists()){
			f.mkdirs();
		}
		File destFile = new File(uploadpath+filename);
		if(!destFile.exists()){
			try {
				if(!destFile.createNewFile()){
					isSuccess =  false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				isSuccess =  false;
			}
		}
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new FileWriter(destFile));
			String line = null;
			while((line = br.readLine())!=null){
				bw.write(line);
			}
			bw.flush();
			isSuccess =  true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isSuccess =  false;
		} catch (IOException e) {
			e.printStackTrace();
			isSuccess =  false;
		} finally{
			if(bw != null){
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(br != null){
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}
	
	public static boolean uploadByteFile(InputStream is,String uploadpath,String filename) {
		boolean isSuccess = false;
		File f = new File(uploadpath);
		if(!f.exists()){
			f.mkdirs();
		}
		File destFile = new File(uploadpath+filename);
		if(!destFile.exists()){
			try {
				if(!destFile.createNewFile()){
					isSuccess =  false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				isSuccess =  false;
			}
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] bt = new byte[1024*8];
			int bytesRead = 0;
			while(-1 != (bytesRead = bis.read(bt, 0, bt.length))){
				bos.write(bt,0,bytesRead);//注意Buffered对偏移量的修改 使用bos.write(bt)可能会造成对字节数组读写重复
			}
			bos.flush();
			isSuccess =  true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isSuccess =  false;
		} catch (IOException e) {
			e.printStackTrace();
			isSuccess =  false;
		} finally{
			if(bos != null){
				try {
					bos.close();
					bos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis != null){
				try {
					bis.close();
					bis = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}
}
