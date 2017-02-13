package com.iyuile.caelum.api.download;

/**
 * 下载文件进度
 * <p>
 * Created by WangYao on 2016/12/20.
 */
public interface ProgressListener {
    /**
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
