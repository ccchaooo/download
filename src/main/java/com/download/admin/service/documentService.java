package com.download.admin.service;

import com.download.admin.model.Doc;
import com.download.admin.utils.DownLoad;
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

import java.io.IOException;
import java.time.LocalDate;


/**
 * @author dengchao
 * @date 2019/11/25 0025 13:12
 */
public class documentService {


    public static void main(String[] args) throws IOException, InterruptedException {
        for (int index = 1; index < 18781; index++)
            getDocuments(index);
        Thread.sleep(10);
    }

    private static void getDocuments(int index) throws IOException, InterruptedException {
        String url = "http://qjgwxt.ltkc.net/index.php/Document/DocumentList/status/0/type/all/d_classfy/all/p/" + index + ".html";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieUtil.cookie);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        Document document = Jsoup.parse(resEntity.getBody());
        Elements elements = document.getElementsByTag("table").get(0).getElementsByTag("tr");
        elements.parallelStream()
                .filter(ele -> ele.getElementsByTag("td").size() > 0)
                .map(documentService::buildDoc)
                .forEach(DownLoad::downloadHtml);
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
        return doc;
    }


}
