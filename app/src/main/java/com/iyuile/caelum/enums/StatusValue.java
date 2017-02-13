package com.iyuile.caelum.enums;

/**
 * @Description 状态
 */
public enum StatusValue {

    NORMAL(11), // 正常
    FREEZE(12), // 冻结
    DELETE(13), // 删除
    AUDITING(21), // 审核中
    AUDITINGFAIL(22), // 审核中
    RESIGN(23), // 下架
    UNCONFIRMED(31), // 未确认
    CONFIRMED(32), // 已确认
    COMPLETE(33),// 已完成
    CANCELED(34);// 取消

    private int value;

    public int getValue() {
        return value;
    }

    /**
     * @param value
     */
    private StatusValue(int value) {
        this.value = value;
    }

    public static StatusValue getActionType(int v) {
        StatusValue[] values = values();
        if (values != null) {
            for (StatusValue each : values) {
                if (each.getValue() == v) {
                    return each;
                }
            }
        }
        return null;
    }

}
