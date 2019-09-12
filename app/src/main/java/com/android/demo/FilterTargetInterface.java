package com.android.demo;

import java.util.List;

public interface FilterTargetInterface {

        /** 让你的bean实现这个接口, 指定过滤String列表;.
         * @return
         */
        List<String> getFilterString();
    }