package com.download.admin.service;

import com.download.admin.model.Doc;
import com.download.admin.utils.DownLoad;
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

    private static void getDocuments(int index) throws IOException {
        String url = "http://qjgwxt.ltkc.net/index.php/Document/DocumentList/status/0/type/all/d_classfy/all/p/" + index + ".html";
        String cookie = "PHPSESSID=pnivhokicnnnh53rm5l6jj7102";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        Document document = Jsoup.parse(resEntity.getBody());
        Elements elements = document.getElementsByTag("table").get(0).getElementsByTag("tr");
        for (Element ele : elements) {
            if (ele.getElementsByTag("td").size() == 0) {
                continue;
            }
            Element title = ele.getElementsByTag("td").get(7).getElementsByTag("a").get(0);
            Element date = ele.getElementsByTag("td").get(13).getElementsByTag("td").get(0);
            DownLoad.downloadHtml(buildDoc(title, date));
        }
    }

    private static Doc buildDoc(Element title, Element date) {
//        <a href="/index.php/Flow/FlowDetail?did=19364">邛崃市十八届人大常委会第二十八次常委会通知</a>
        Doc doc = new Doc();
        int begin = title.toString().indexOf("?did") + 5;
        int end = title.toString().indexOf("\">");
        doc.setId(Integer.parseInt(title.toString().substring(begin,end)));
        LocalDate time = LocalDate.parse(date.text());
        doc.setTime(time);
        doc.setName(title.text());
        doc.setDir("D:/DocDownload/"+time.getYear()+"/"+time.getMonth()+"/");
        doc.setDocumentUrl(doc.getId());
        doc.setFlowDetail(doc.getId());
        return doc;
    }


}
