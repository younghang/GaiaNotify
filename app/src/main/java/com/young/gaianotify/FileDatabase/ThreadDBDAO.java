package com.young.gaianotify.FileDatabase;

import com.young.gaianotify.FileDeal.ThreadInfo;

import java.util.List;

/**
 * 数据访问接口
 * Created by young on 2016/5/8 0008.
 */
public interface ThreadDBDAO {
//    插入线程信息
void insertThreadInfo(ThreadInfo threadInfo);

//    删除线程
void deleteThreadInfo(String url, int threadId);
//更新线程信息
void updateThreadInfo(String url, int thread_id, int progerss);
//查找文件的线程信息以集合形式返回
List<ThreadInfo> getThreads(String url);
    //判断线程信息是否存在
    boolean isExist(String url, int thread_id);

}
