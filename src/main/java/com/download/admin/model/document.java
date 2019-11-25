package com.download.admin.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;

/**
 * @author dengchao
 * @date 2019/11/25 0025 13:12
 */
public class document {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        for (int index = 1; index < 100; index++)
            getDocuments(index);
        Thread.sleep(100);
    }

    private static void getDocuments(int index) {
        String url = "http://qjgwxt.ltkc.net/index.php/Document/DocumentList/status/0/type/all/d_classfy/all/p/" + index + ".html";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "PHPSESSID=bb9352c6rvfqc90560og0800r4");
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
        Document doc = Jsoup.parse(resEntity.getBody());
        Elements elements = doc.getElementsByTag("table").get(0).getElementsByTag("tr");
        for (Element ele : elements) {
            if (ele.getElementsByTag("td").size() == 0) {
                continue;
            }
            Element element = ele.getElementsByTag("td").get(7).getElementsByTag("a").get(0);
            System.out.println(element.text());
        }
    }
}
