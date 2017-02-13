package com.iyuile.caelum.entity.response;

import java.io.Serializable;

/**
 * @author WangYao
 * @version 1
 * @Description 返回中附带的对象, 其中有 {@link PaginationResponse}
 * @ProjectName Apus
 * @ClassName {@link MetaResponse}
 * @Date 2016-4-9 下午6:33:13
 */
public class MetaResponse implements Serializable {
    private PaginationResponse pagination;

    public PaginationResponse getPagination() {
        return pagination;
    }

    public void setPagination(PaginationResponse pagination) {
        this.pagination = pagination;
    }
}
