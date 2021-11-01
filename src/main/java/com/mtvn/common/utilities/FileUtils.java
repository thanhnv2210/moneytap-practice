package com.mtvn.common.utilities;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileUtils {

    public static File[] getFilesWithExtn(String dir, String extn) {
        return new File(dir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return StringUtils.hasText(name) && name.toLowerCase().endsWith("." + extn);
            }
        });
    }

    public static byte[] getFileBytes(File file) throws IOException {
        return FileCopyUtils.copyToByteArray(file);
    }
}
