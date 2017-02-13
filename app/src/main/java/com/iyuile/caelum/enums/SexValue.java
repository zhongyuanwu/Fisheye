package com.iyuile.caelum.enums;

/**
 * @Description 性别
 */
public enum SexValue {

    MAN(1), // 男
    GIRL(2), // 女
    SECRECY(3); // 保密

    private int value;

    public int getValue() {
        return value;
    }

    /**
     * @param value
     */
    private SexValue(int value) {
        this.value = value;
    }

    public static SexValue getActionType(int v) {
        SexValue[] values = values();
        if (values != null) {
            for (SexValue each : values) {
                if (each.getValue() == v) {
                    return each;
                }
            }
        }
        return null;
    }

}
