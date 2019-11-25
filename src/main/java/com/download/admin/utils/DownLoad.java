package com.download.admin.utils;

import com.download.admin.model.Doc;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author dengchao
 * @date 2019/11/25 0025 12:57
 */
public class DownLoad {

    /**
     * 说明：根据指定URL将文件下载到指定目标位置
     *
     * @return 返回下载文件
     */
    public static File downloadFile(Doc doc) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(doc.getDocumentUrl());
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);

            //设置登录信息
            httpURLConnection.setRequestProperty("Cookie", doc.getCookie());
            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 控制台打印文件大小
            System.out.println("您要下载的文件大小为:" + fileLength / (1024 * 1024) + "MB");

            // 建立链接从请求中获取数据
            URLConnection con = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 指定文件名称(有需求可以自定义)
            String fileFullName = doc.getName();
            // 指定存放位置(有需求可以自定义)
            String path = doc.getDir() + File.separatorChar + fileFullName;
            file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 控制台打印文件下载的百分比情况
                System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
            }
            // 关闭资源
            bin.close();
            out.close();
            System.out.println("文件下载成功！");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("文件下载失败！");
        } finally {
            return file;
        }

    }


    public static void downloadHtml(Doc doc) throws IOException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", doc.getCookie());
        RestTemplate template = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = template.exchange(doc.getFlowDetail(), HttpMethod.GET, requestEntity, String.class);
        String body = response.getBody();
        body = body.replace("/public/css/style.css", "../../style.css");

        // 指定文件名称(有需求可以自定义)
        String fileFullName = doc.getName()+".html";
        // 指定存放位置(有需求可以自定义)
        String path = doc.getDir() + File.separatorChar + fileFullName;
        File file = new File(path);
        // 校验文件夹目录是否存在，不存在就创建一个目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 校验文件夹目录是否存在，不存在就创建一个目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        byte bytes[] = new byte[512];
        bytes = body.getBytes();
        int b = bytes.length; // 是字节的长度，不是字符串的长度
        FileOutputStream fos = new FileOutputStream(file);
//		fos.write(bytes, 0, b);
        fos.write(bytes);
        fos.flush();
        fos.close();

    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        // 指定资源地址，下载文件测试
//        downloadFile("http://qjgwxt.ltkc.net/index.php/Document/Download?did=19364", "D:/myFiles/");
//        downloadHtml("http://qjgwxt.ltkc.net/index.php/Flow/FlowDetail?did=19364", "D:/myFiles/");

    }
}