package com.download.admin.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dengchao
 * @date 2019/11/28 0028 18:23
 */
public class FileUtil {

    private static Map<String, String> files = new HashMap<>();

    public static Map<String, String> getFiles() {
        if (files.size() !=0){
            return files;
        }
        String path = "D:/DocDownload/";
        File f = new File(path);
        FileUtil.getFile(f);
        return files;
    }


    private static void getFile(File file) {
        if (file != null) {
            File[] f = file.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    getFile(f[i]);
                }
            } else {
                System.out.println(file);
                files.put(file.getName().substring(0, 5), file.getName());
            }
        }
    }
}
