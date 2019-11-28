package com.download.admin.utils;

import com.download.admin.model.Doc;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
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
import java.util.Objects;

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
    public static void downloadFile(Doc doc) {
        File file = null;
        // 指定文件名称(有需求可以自定义)
        String fileFullName = doc.getId() + " " + doc.getName();
        try {
            // 指定存放位置(有需求可以自定义)
            String path = doc.getDir() + File.separatorChar + fileFullName;
            file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                return;
            }

            // 统一资源
            URL url = new URL(doc.getDocumentUrl());
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);

            //设置登录信息
            httpURLConnection.setRequestProperty("Cookie", cookieUtil.cookie);
            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 建立链接从请求中获取数据
            URLConnection con = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());


            // 控制台打印文件大小
//            System.out.println(fileFullName + " 大小为:" + fileLength / (1024) + "kb");


            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
            }
            // 关闭资源
            bin.close();
            out.close();
        } catch (Exception e) {
            System.out.println("文件下载失败:" + fileFullName);
        }
    }


    public static void downloadHtml(Doc doc) {
//        // 指定文件名称(有需求可以自定义)
//        String fileFullName = doc.getId() + " " + doc.getName() + ".html";
//
//        // 指定存放位置(有需求可以自定义)
//        String path = doc.getDir() + File.separatorChar + fileFullName;
//        File file = new File(path);
//        // 校验文件夹目录是否存在，不存在就创建一个目录
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
//        if (file.exists()) {
//            return;
//        }
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("Cookie", cookieUtil.cookie);
//        RestTemplate template = new RestTemplate();
//        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
//        ResponseEntity<String> response = template.exchange(doc.getFlowDetail(), HttpMethod.GET, requestEntity, String.class);
//        String body = response.getBody();
//        body = body.replace("/public/css/style.css", "../../style.css");
//
//        // 控制台打印文件大小
////        System.out.println(fileFullName + " 大小为:" + body.length() / (1024) + "kb");
//
//        byte[] bytes = body.getBytes();
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            fos.write(bytes);
//            fos.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("文件下载失败:" + fileFullName);
//        }
//        resetName(doc, body);
//        downloadFile(doc);
    }

    private static void resetName(Doc doc, String body) {
        Elements a = Jsoup.parse(body).getElementsByTag("a");
        if (!Objects.isNull(a)) {
            doc.setName(a.text());
        }
    }

}