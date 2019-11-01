package com.liuzi.util.baidu;


/**
 * 共享变量
 * @author zsy
 */
public class BaiduConsts {
    
    /**
     * 连接默认参数
     */
    public static final int DEFAULT_CTIM = 2000;
    /**
     * 连接默认参数
     */
    public static final int DEFAULT_STIM = 60000;
    
    /**
     * 长文本文字最大字数限制
     */
    protected static final int PURELAND_TEXT_MAX_LENGTH = 20000;
    /**
     * 长文本审核
     */
    protected static final String PURELAND_URL = "https://aip.baidubce.com/rpc/2.0/nlp/v1/pureland";
}
