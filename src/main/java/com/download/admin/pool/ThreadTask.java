package com.download.admin.pool;

import com.download.admin.service.documentService;

/**
 * @author dengchao
 * @date 2019/12/4 23:26
 */
public class ThreadTask implements Runnable {
    private Integer index = 0;

    public ThreadTask() {

    }

    public ThreadTask(Integer index) {
        this.index = index;
    }

    public void run() {
        documentService.getDocuments(this.index);
    }
}