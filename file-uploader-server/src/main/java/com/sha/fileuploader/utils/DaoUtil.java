package com.sha.fileuploader.utils;

import java.util.List;

public class DaoUtil {

    public static Object firstOrNull(final List resultList) {
        if((resultList != null) && (!resultList.isEmpty())) {
            return resultList.iterator().next();
        }
        return null;
    }
}
