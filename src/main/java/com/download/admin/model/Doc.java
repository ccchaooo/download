package com.download.admin.model;

import org.jsoup.nodes.Element;

import java.time.LocalDate;

/**
 * @author dengchao
 * @date 2019/11/25 21:38
 */
public class Doc {
    Integer id;
    String htmlName;
    String docName;
    String dir;
    LocalDate time;
    String flowDetail = "http://qjgwxt.ltkc.net/index.php/Flow/FlowDetail?did=";
    String documentUrl = "http://qjgwxt.ltkc.net//index.php/Document/Download?did=";

    public String getHtmlName() {
        return htmlName;
    }

    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public Doc build(Element element) {
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFlowDetail() {
        return flowDetail;
    }

    public void setFlowDetail(String flowDetail) {
        this.flowDetail = flowDetail;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public void setDocumentUrl(Integer id) {
        this.documentUrl += id;
    }

    public void setFlowDetail(Integer id) {
        this.flowDetail += id;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", dir='" + dir + '\'' +
                ", time=" + time +
                ", flowDetail='" + flowDetail + '\'' +
                ", documentUrl='" + documentUrl + '\'' +
                '}';
    }
}
