package com.centit.framework.common;

import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;

import com.centit.support.file.FileSystemOpt;

public class SysRollingFileAppender extends DailyRollingFileAppender {

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        String directory = SysParametersUtils.getLogHome();
        FileSystemOpt.createDirect(directory);
        String newFileName = fileName.startsWith(directory) ? fileName : directory + fileName;
        super.setFile(newFileName, append, bufferedIO, bufferSize);

    }

}
