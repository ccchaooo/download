package com.download.admin.service;

import com.download.admin.model.Doc;
import com.download.admin.utils.DownLoad;
import com.download.admin.utils.FileUtil;
import com.download.admin.utils.cookieUtil;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;


/**
 * @author dengchao
 * @date 2019/11/25 0025 13:12
 */
public class documentService {

    private static Map<String, String> savedFiles = FileUtil.getFiles();

    public static void main(String[] args) {
        for (int index = 19472; index > 1; index--) {
            if (savedFiles.containsKey(String.valueOf(index))) {
                System.out.println("已存在:" + savedFiles.get(String.valueOf(index)));
                continue;
            }
            getDocuments(index);
//            Thread.sleep(10);
        }
    }

    public static void getDocuments(int index) {
        String flowDetail = "http://qjgwxt.ltkc.net/index.php/Flow/FlowDetail?did=" + index;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", cookieUtil.cookie);
        RestTemplate template = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = template.exchange(flowDetail, HttpMethod.GET, requestEntity, String.class);
        String body = response.getBody();
        body = body.replace("/public/css/style.css", "../../style.css");

        Doc doc = buildDoc(index, body);
        if (Objects.isNull(doc)) {
            return;
        }

        // 指定存放位置(有需求可以自定义)
        String path = doc.getDir() + File.separatorChar + doc.getHtmlName();
        File file = new File(path);
        // 校验文件夹目录是否存在，不存在就创建一个目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        byte[] bytes = body.getBytes();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
            fos.flush();

            DownLoad.downloadFile(doc);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("页面下载失败:" + doc.getHtmlName());
        }
    }

    private static Doc buildDoc(int index, String body) {
        Elements tds = Jsoup.parse(body).getElementsByTag("td");
        Doc doc = new Doc();
        LocalDate time;
        try {
            time = LocalDate.parse(tds.get(9).text());
        } catch (Exception e) {
            return new Doc();
        }
        doc.setId(index);
        doc.setTime(time);
        doc.setHtmlName(index + " " + tds.get(13).text() + ".html");
        doc.setDocName(index + " " + tds.get(15).text());
        doc.setDir("D:/DocDownload/" + time.getYear() + "/" + time.getMonth() + "/");
        System.out.println(doc.getId() + ": " + doc.getDocName());
        return doc;
    }
}
