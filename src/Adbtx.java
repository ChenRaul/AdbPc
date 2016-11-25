
/**
 * Server.java
 * @author wuzq
 * @create 2012-2-21
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Adbtx {
	public static final String adb_path= "D:\\360安全浏览器下载\\adt-bundle-windows-x86-20130522\\adt-bundle-windows-x86-20130522\\sdk\\platform-tools\\adb.exe";//adb所在路径
	private static final String HOST = "localhost";
	private static int PORT = 8080;
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;

	private Socket client;
    private FileInputStream fis;
    private DataOutputStream dos;
    private static int count = 0;
    
	public Adbtx() {
		try {
			socket = new Socket(HOST, PORT);
			System.out.println(socket.isConnected());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("login exception" + ex.getMessage());
		}
		//发送文件的 socket，与上面一个端口不一样
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					client =new Socket(HOST, 8001);
					//向服务端传送文件
	                File file =new File("c:/sources-19_r02.zip");
	                fis =new FileInputStream(file);
	                dos =new DataOutputStream(client.getOutputStream());
	                  
	                //文件名和长度
	                dos.writeUTF(file.getName());
	                dos.flush();
	                dos.writeLong(file.length());
	                dos.flush();
	                  
	                //传输文件
	                byte[] sendBytes =new byte[1024];
	                int length =0;
	                while((length = fis.read(sendBytes,0, sendBytes.length)) >0){
	                    dos.write(sendBytes,0, length);
	                    dos.flush();
	                }
	             //   System.out.println("文件发送完毕");
	                fis.close();
	                dos.close();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			}
		}).start();
	}

	public void openUrl(String msg) {
		if (socket.isConnected()) {
			if (!socket.isOutputShutdown()) {
				out.println(msg);//发送消息
				//System.out.println("send suceess!!");
			}
		}
	}
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length()/2; 
	   
	    char[] hexChars = hexString.toCharArray();  
	    System.out.println("length="+hexString.length()+"---"+hexChars.length);
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	    	  int pos = i * 2;   
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	} 
	private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}  
	public static String bytesToHexString(byte[] src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	}   
	public static String bytesToBitString(byte[] src){  //byte数组转换成16进制字符串，此处能够用得到应该就是端口才用得到了，包括上面  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        
        for (int i = 0; i < src.length; i++) {  
        		int z = src[i]; 
        		z |= 256;
        	  String str = Integer.toBinaryString(z);
        	  int len = str.length(); 
        	stringBuilder.append(str.substring(len-8, len));  
        }  
        return stringBuilder.toString();  
    }
	/*讲一个文件夹下的所有文佳的内容都读出来*/
	 public static void writeFile(String path) throws UnsupportedEncodingException {

	        File file = new File(path);
	        if (!file.exists()) {
	            return;
	        }
	        for (File f : file.listFiles()) {
	            if (f.isFile()) {
	                if(f.getName().endsWith(".java")){
	                	try {
						  
						    
						    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File("D:\\wer.txt"),true),"GBK");
						    BufferedWriter witer = new BufferedWriter(out);
						    
						    InputStreamReader is = new InputStreamReader(new FileInputStream(f), "UTF-8");
						    BufferedReader reader = new BufferedReader(is);
							try {
								witer.write("BUT "+f.getName()+"\n");
								String str;;
								while((str = reader.readLine()) != null){
									if(!str.startsWith("package")){
										witer.write("  "+str);
										witer.write('\n');
									}
								}
								witer.write('\n');
								witer.flush();
								witer.close();
								out.close();
								is.close();
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println("write IOException");
							}
							count++;
							System.out.println(f.getName());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("write FileNotFoundException");
						}
	                }
	            } else if (f.isDirectory()) {
	            	writeFile(f.getAbsolutePath());
	            }
	        }
//	        try {
//				outs.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	        
	    }
	public static void main(String[] args) throws UnsupportedEncodingException {
		String path ="C:\\Users\\ChenShuai\\Desktop\\main\\";
		
			writeFile (path);
		System.out.println(count);
	
		
		try {
			//把虚拟机的8090端口绑定到PC本机的8080端口，这样当PC向8080发送数据时实际上是发到虚拟机的8090端口
			//Runtime.getRuntime().exec(G3ExpPCclient.adb_path + " –s emulator-5554 forward tcp:8080 tcp:8090");这个方法不好用
			Runtime.getRuntime().exec(Adbtx.adb_path + " forward tcp:8080 tcp:8090");//这个好用
			Runtime.getRuntime().exec(Adbtx.adb_path + " forward tcp:8001 tcp:8000");
			System.out.println("已经将虚拟机端口8090绑定到PC端口8080 "+adb_path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Adbtx pc = new Adbtx();
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "GB2312"));//这是用来从键盘接受一行输入的代码
		
		 while (true) {
			String msg="";
			try {
				msg = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (msg.equals("exit")) {
				System.out.println("退出");
				System.exit(-1);
			} else {
				pc.openUrl(msg);
			}
		}
	}
	  
	public static String getMd5(String str) {
		StringBuilder sb = new StringBuilder(40);
		try {
			byte[] bs = MessageDigest.getInstance("MD5").digest(str.getBytes());
			for (byte x : bs) {
				if ((x & 0xff) >> 4 == 0) {
					sb.append("0").append(Integer.toHexString(x & 0xff));
				} else {
					sb.append(Integer.toHexString(x & 0xff));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}

