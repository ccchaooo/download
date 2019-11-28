package com.download.admin.service;

import com.download.admin.model.Doc;
import com.download.admin.utils.DownLoad;
import com.download.admin.utils.FileUtil;
import com.download.admin.utils.cookieUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;


/**
 * @author dengchao
 * @date 2019/11/25 0025 13:12
 */
public class documentService {

    private static Map<String, String> savedFiles = FileUtil.getFiles();

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int index = 19416; index > 1; index--
        )
            getDocuments(index);
        Thread.sleep(10);
    }

    private static void getDocuments(int index) throws IOException, InterruptedException {
        String flowDetail = "http://qjgwxt.ltkc.net/index.php/Flow/FlowDetail?did=" + index;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", cookieUtil.cookie);
        RestTemplate template = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = template.exchange(flowDetail, HttpMethod.GET, requestEntity, String.class);
        String body = response.getBody();
        body = body.replace("/public/css/style.css", "../../style.css");


        // 指定文件名称(有需求可以自定义)
        String fileFullName = index + ".html";

        // 指定存放位置(有需求可以自定义)
        String path = File.separatorChar + fileFullName;
        File file = new File(path);
        // 校验文件夹目录是否存在，不存在就创建一个目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        byte[] bytes = body.getBytes();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件下载失败:" + fileFullName);
        }
//        resetName(doc, body);
//        downloadFile(doc);

    }

    private static Doc buildDoc(Element ele) {

        Element title = ele.getElementsByTag("td").get(7).getElementsByTag("a").get(0);
        Element date = ele.getElementsByTag("td").get(13).getElementsByTag("td").get(0);

        Doc doc = new Doc();
        int begin = title.toString().indexOf("?did") + 5;
        int end = title.toString().indexOf("\">");
        doc.setId(Integer.parseInt(title.toString().substring(begin, end)));
        LocalDate time = LocalDate.parse(date.text());
        doc.setTime(time);
        doc.setName(title.text());
        doc.setDir("D:/DocDownload/" + time.getYear() + "/" + time.getMonth() + "/");
        doc.setDocumentUrl(doc.getId());
        doc.setFlowDetail(doc.getId());
        System.out.println(doc.getId() + ": " + doc.getName());
        return doc;
    }


}
