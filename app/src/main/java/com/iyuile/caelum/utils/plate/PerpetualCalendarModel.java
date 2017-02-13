package com.iyuile.caelum.utils.plate;

import com.iyuile.caelum.utils.TimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by WangYao on 2017/1/17.
 */
public class PerpetualCalendarModel {

    public String[] tianGan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    public String[] diZhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    private String[] shuXing = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    private String[] wuXing = new String[]{"木", "火", "土", "金", "水"};
    private String[] fangXiang = new String[]{"东", "南", "中", "西", "北"};
    private String[] riQi = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};

    private int[] m0 = new int[]{0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1,//1901
            0, 1, 0, 1, 2, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 3, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 4, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 3632};
    private int[] m1 = new int[]{1, 0, 1, 0, 0, 4, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1,//1911
            1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 3, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 2, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 3, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 7294};
    private int[] m2 = new int[]{1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 1, 0, 1, 1,//1921
            0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 3, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 4, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 4, 0, 1, 0, 1, 1, 0, 10955};
    ;
    private int[] m3 = new int[]{1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1,//1931
            0, 1, 1, 0, 5, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 3, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 3, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 14587};
    private int[] m4 = new int[]{1, 1, 0, 1, 1, 3, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1,//1941
            0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 3, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 4, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 2, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 18249};
    private int[] m5 = new int[]{1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 3, 0, 1, 1, 0, 1, 0, 1,//1951
            0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 3, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 4, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 4, 1, 0, 1, 0, 1, 0, 21911};
    private int[] m6 = new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0,//1961
            1, 0, 1, 2, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 4, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 4, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 25544};
    private int[] m7 = new int[]{0, 1, 0, 0, 4, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1,//1971
            1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 4, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 4, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 5, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 29206};
    private int[] m8 = new int[]{0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 2, 1, 0, 0, 1, 0, 1, 1, 1,//1981
            1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 4, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 4, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 4, 0, 1, 0, 1, 1, 1, 1, 32868};
    private int[] m9 = new int[]{0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1,//1991
            0, 1, 4, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 4, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 2, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 36499};
    private int[] m10 = new int[]{1, 1, 0, 4, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0,//2001
            1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 4, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 4, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 4, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 40161};
    private int[] m11 = new int[]{1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 4, 1, 0, 1, 0, 1, 0, 1, 0,//2011
            1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 4, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 3, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 4, 1, 0, 0, 1, 0, 1, 0, 1, 43823};
    private int[] m12 = new int[]{0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1,//2021
            0, 4, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 4, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 4, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 47455};
    private int[] m13 = new int[]{0, 1, 4, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1,//2031
            0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 4, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 3, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 4, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 51117};
    private int[] m14 = new int[]{0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 4, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1,//2041
            0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 2, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 3, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 3, 0, 1, 0, 1, 1, 0, 1, 1, 0, 54779};

    private int[][] ms = new int[][]{m0, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14};
    private int[] ly = new int[]{29, 30, 58, 59, 59, 60};
    private int[] tw = new int[]{0, 0, 1, 1, 2, 2, 3, 3, 4, 4};
    private int[] dw = new int[]{4, 2, 0, 0, 2, 1, 1, 2, 3, 3, 2, 4};

    // ----

    //年
    private int nianIndex;
    private String nianString;
    //月
    private int yueIndex;
    private String yueString;
    //日
    private int riIndex;
    private String riString;
    //时辰
    private int shiChenIndex;
    private String shiChenString;
    //春节
    private int chuiJieYueIndex;
    private String chuiJieYueString;
    private int chuiJieRiIndex;
    private String chuiJieRiString;
    //春节前年干
    private int chuiJieQianTianGanIndex;
    private String chuiJieQianTianGanString;
    //春节前年支
    private int chuiJieQianDiZhiIndex;
    private String chuiJieQianDiZhiString;
    //春节后干
    private int chuiJieHouTianGanIndex;
    private String chuiJieHouTianGanString;
    //春节后支
    private int chuiJieHouDiZhiIndex;
    private String chuiJieHouDiZhiString;
    //生肖
    private int shengXiaoIndex;
    private String shengXiaoString;

    //八字
    //年干
    private int nianTianGanIndex;
    private String nianTianGanString;
    //年支
    private int nianDiZhiIndex;
    private String nianDiZhiString;
    //月干
    private int yueTianGanIndex;
    private String yueTianGanString;
    //月支
    private int yueDiZhiIndex;
    private String yueDiZhiString;
    //日干
    private int riTianGanIndex;
    private String riTianGanString;
    //日支
    private int riDiZhiIndex;
    private String riDiZhiString;
    //时干
    private int shiTianGanIndex;
    private String shiTianGanString;
    //时支
    private int shiDiZhiIndex;
    private String shiDiZhiString;

    //五行
    //年干
    private int nianTianGanWuXingIndex;
    private String nianTianGanWuXingString;
    //年支
    private int nianDiZhiWuXingIndex;
    private String nianDiZhiWuXingString;
    //月干
    private int yueTianGanWuXingIndex;
    private String yueTianGanWuXingString;
    //月支
    private int yueDiZhiWuXingIndex;
    private String yueDiZhiWuXingString;
    //日干
    private int riTianGanWuXingIndex;
    private String riTianGanWuXingString;
    //日支
    private int riDiZhiWuXingIndex;
    private String riDiZhiWuXingString;
    //时干
    private int shiTianGanWuXingIndex;
    private String shiTianGanWuXingString;
    //时支
    private int shiDiZhiWuXingIndex;
    private String shiDiZhiWuXingString;

    //方位
    //年干
    private int nianTianGanFangWeiIndex;
    private String nianTianGanFangWeiString;
    //年支
    private int nianDiZhiFangWeiIndex;
    private String nianDiZhiFangWeiString;
    //月干
    private int yueTianGanFangWeiIndex;
    private String yueTianGanFangWeiString;
    //月支
    private int yueDiZhiFangWeiIndex;
    private String yueDiZhiFangWeiString;
    //日干
    private int riTianGanFangWeiIndex;
    private String riTianGanFangWeiString;
    //日支
    private int riDiZhiFangWeiIndex;
    private String riDiZhiFangWeiString;
    //时干
    private int shiTianGanFangWeiIndex;
    private String shiTianGanFangWeiString;
    //时支
    private int shiDiZhiFangWeiIndex;
    private String shiDiZhiFangWeiString;

    public int getNianIndex() {
        return nianIndex;
    }

    public void setNianIndex(int nianIndex) {
        this.nianIndex = nianIndex;
    }

    public String getNianString() {
        return nianString;
    }

    public void setNianString(String nianString) {
        this.nianString = nianString;
    }

    public int getYueIndex() {
        return yueIndex;
    }

    public void setYueIndex(int yueIndex) {
        this.yueIndex = yueIndex;
    }

    public String getYueString() {
        return yueString;
    }

    public void setYueString(String yueString) {
        this.yueString = yueString;
    }

    public int getRiIndex() {
        return riIndex;
    }

    public void setRiIndex(int riIndex) {
        this.riIndex = riIndex;
    }

    public String getRiString() {
        return riString;
    }

    public void setRiString(String riString) {
        this.riString = riString;
    }

    public int getShiChenIndex() {
        return shiChenIndex;
    }

    public void setShiChenIndex(int shiChenIndex) {
        this.shiChenIndex = shiChenIndex;
    }

    public String getShiChenString() {
        return shiChenString;
    }

    public void setShiChenString(String shiChenString) {
        this.shiChenString = shiChenString;
    }

    public int getChuiJieYueIndex() {
        return chuiJieYueIndex;
    }

    public void setChuiJieYueIndex(int chuiJieYueIndex) {
        this.chuiJieYueIndex = chuiJieYueIndex;
    }

    public String getChuiJieYueString() {
        return chuiJieYueString;
    }

    public void setChuiJieYueString(String chuiJieYueString) {
        this.chuiJieYueString = chuiJieYueString;
    }

    public int getChuiJieRiIndex() {
        return chuiJieRiIndex;
    }

    public void setChuiJieRiIndex(int chuiJieRiIndex) {
        this.chuiJieRiIndex = chuiJieRiIndex;
    }

    public String getChuiJieRiString() {
        return chuiJieRiString;
    }

    public void setChuiJieRiString(String chuiJieRiString) {
        this.chuiJieRiString = chuiJieRiString;
    }

    public int getChuiJieQianTianGanIndex() {
        return chuiJieQianTianGanIndex;
    }

    public void setChuiJieQianTianGanIndex(int chuiJieQianTianGanIndex) {
        this.chuiJieQianTianGanIndex = chuiJieQianTianGanIndex;
    }

    public String getChuiJieQianTianGanString() {
        return chuiJieQianTianGanString;
    }

    public void setChuiJieQianTianGanString(String chuiJieQianTianGanString) {
        this.chuiJieQianTianGanString = chuiJieQianTianGanString;
    }

    public int getChuiJieQianDiZhiIndex() {
        return chuiJieQianDiZhiIndex;
    }

    public void setChuiJieQianDiZhiIndex(int chuiJieQianDiZhiIndex) {
        this.chuiJieQianDiZhiIndex = chuiJieQianDiZhiIndex;
    }

    public String getChuiJieQianDiZhiString() {
        return chuiJieQianDiZhiString;
    }

    public void setChuiJieQianDiZhiString(String chuiJieQianDiZhiString) {
        this.chuiJieQianDiZhiString = chuiJieQianDiZhiString;
    }

    public int getChuiJieHouTianGanIndex() {
        return chuiJieHouTianGanIndex;
    }

    public void setChuiJieHouTianGanIndex(int chuiJieHouTianGanIndex) {
        this.chuiJieHouTianGanIndex = chuiJieHouTianGanIndex;
    }

    public String getChuiJieHouTianGanString() {
        return chuiJieHouTianGanString;
    }

    public void setChuiJieHouTianGanString(String chuiJieHouTianGanString) {
        this.chuiJieHouTianGanString = chuiJieHouTianGanString;
    }

    public int getChuiJieHouDiZhiIndex() {
        return chuiJieHouDiZhiIndex;
    }

    public void setChuiJieHouDiZhiIndex(int chuiJieHouDiZhiIndex) {
        this.chuiJieHouDiZhiIndex = chuiJieHouDiZhiIndex;
    }

    public String getChuiJieHouDiZhiString() {
        return chuiJieHouDiZhiString;
    }

    public void setChuiJieHouDiZhiString(String chuiJieHouDiZhiString) {
        this.chuiJieHouDiZhiString = chuiJieHouDiZhiString;
    }

    public int getShengXiaoIndex() {
        return shengXiaoIndex;
    }

    public void setShengXiaoIndex(int shengXiaoIndex) {
        this.shengXiaoIndex = shengXiaoIndex;
    }

    public String getShengXiaoString() {
        return shengXiaoString;
    }

    public void setShengXiaoString(String shengXiaoString) {
        this.shengXiaoString = shengXiaoString;
    }

    public int getNianTianGanIndex() {
        return nianTianGanIndex;
    }

    public void setNianTianGanIndex(int nianTianGanIndex) {
        this.nianTianGanIndex = nianTianGanIndex;
    }

    public String getNianTianGanString() {
        return nianTianGanString;
    }

    public void setNianTianGanString(String nianTianGanString) {
        this.nianTianGanString = nianTianGanString;
    }

    public int getNianDiZhiIndex() {
        return nianDiZhiIndex;
    }

    public void setNianDiZhiIndex(int nianDiZhiIndex) {
        this.nianDiZhiIndex = nianDiZhiIndex;
    }

    public String getNianDiZhiString() {
        return nianDiZhiString;
    }

    public void setNianDiZhiString(String nianDiZhiString) {
        this.nianDiZhiString = nianDiZhiString;
    }

    public int getYueTianGanIndex() {
        return yueTianGanIndex;
    }

    public void setYueTianGanIndex(int yueTianGanIndex) {
        this.yueTianGanIndex = yueTianGanIndex;
    }

    public String getYueTianGanString() {
        return yueTianGanString;
    }

    public void setYueTianGanString(String yueTianGanString) {
        this.yueTianGanString = yueTianGanString;
    }

    public int getYueDiZhiIndex() {
        return yueDiZhiIndex;
    }

    public void setYueDiZhiIndex(int yueDiZhiIndex) {
        this.yueDiZhiIndex = yueDiZhiIndex;
    }

    public String getYueDiZhiString() {
        return yueDiZhiString;
    }

    public void setYueDiZhiString(String yueDiZhiString) {
        this.yueDiZhiString = yueDiZhiString;
    }

    public int getRiTianGanIndex() {
        return riTianGanIndex;
    }

    public void setRiTianGanIndex(int riTianGanIndex) {
        this.riTianGanIndex = riTianGanIndex;
    }

    public String getRiTianGanString() {
        return riTianGanString;
    }

    public void setRiTianGanString(String riTianGanString) {
        this.riTianGanString = riTianGanString;
    }

    public int getRiDiZhiIndex() {
        return riDiZhiIndex;
    }

    public void setRiDiZhiIndex(int riDiZhiIndex) {
        this.riDiZhiIndex = riDiZhiIndex;
    }

    public String getRiDiZhiString() {
        return riDiZhiString;
    }

    public void setRiDiZhiString(String riDiZhiString) {
        this.riDiZhiString = riDiZhiString;
    }

    public int getShiTianGanIndex() {
        return shiTianGanIndex;
    }

    public void setShiTianGanIndex(int shiTianGanIndex) {
        this.shiTianGanIndex = shiTianGanIndex;
    }

    public String getShiTianGanString() {
        return shiTianGanString;
    }

    public void setShiTianGanString(String shiTianGanString) {
        this.shiTianGanString = shiTianGanString;
    }

    public int getShiDiZhiIndex() {
        return shiDiZhiIndex;
    }

    public void setShiDiZhiIndex(int shiDiZhiIndex) {
        this.shiDiZhiIndex = shiDiZhiIndex;
    }

    public String getShiDiZhiString() {
        return shiDiZhiString;
    }

    public void setShiDiZhiString(String shiDiZhiString) {
        this.shiDiZhiString = shiDiZhiString;
    }

    public int getNianTianGanWuXingIndex() {
        return nianTianGanWuXingIndex;
    }

    public void setNianTianGanWuXingIndex(int nianTianGanWuXingIndex) {
        this.nianTianGanWuXingIndex = nianTianGanWuXingIndex;
    }

    public String getNianTianGanWuXingString() {
        return nianTianGanWuXingString;
    }

    public void setNianTianGanWuXingString(String nianTianGanWuXingString) {
        this.nianTianGanWuXingString = nianTianGanWuXingString;
    }

    public int getNianDiZhiWuXingIndex() {
        return nianDiZhiWuXingIndex;
    }

    public void setNianDiZhiWuXingIndex(int nianDiZhiWuXingIndex) {
        this.nianDiZhiWuXingIndex = nianDiZhiWuXingIndex;
    }

    public String getNianDiZhiWuXingString() {
        return nianDiZhiWuXingString;
    }

    public void setNianDiZhiWuXingString(String nianDiZhiWuXingString) {
        this.nianDiZhiWuXingString = nianDiZhiWuXingString;
    }

    public int getYueTianGanWuXingIndex() {
        return yueTianGanWuXingIndex;
    }

    public void setYueTianGanWuXingIndex(int yueTianGanWuXingIndex) {
        this.yueTianGanWuXingIndex = yueTianGanWuXingIndex;
    }

    public String getYueTianGanWuXingString() {
        return yueTianGanWuXingString;
    }

    public void setYueTianGanWuXingString(String yueTianGanWuXingString) {
        this.yueTianGanWuXingString = yueTianGanWuXingString;
    }

    public int getYueDiZhiWuXingIndex() {
        return yueDiZhiWuXingIndex;
    }

    public void setYueDiZhiWuXingIndex(int yueDiZhiWuXingIndex) {
        this.yueDiZhiWuXingIndex = yueDiZhiWuXingIndex;
    }

    public String getYueDiZhiWuXingString() {
        return yueDiZhiWuXingString;
    }

    public void setYueDiZhiWuXingString(String yueDiZhiWuXingString) {
        this.yueDiZhiWuXingString = yueDiZhiWuXingString;
    }

    public int getRiTianGanWuXingIndex() {
        return riTianGanWuXingIndex;
    }

    public void setRiTianGanWuXingIndex(int riTianGanWuXingIndex) {
        this.riTianGanWuXingIndex = riTianGanWuXingIndex;
    }

    public String getRiTianGanWuXingString() {
        return riTianGanWuXingString;
    }

    public void setRiTianGanWuXingString(String riTianGanWuXingString) {
        this.riTianGanWuXingString = riTianGanWuXingString;
    }

    public int getRiDiZhiWuXingIndex() {
        return riDiZhiWuXingIndex;
    }

    public void setRiDiZhiWuXingIndex(int riDiZhiWuXingIndex) {
        this.riDiZhiWuXingIndex = riDiZhiWuXingIndex;
    }

    public String getRiDiZhiWuXingString() {
        return riDiZhiWuXingString;
    }

    public void setRiDiZhiWuXingString(String riDiZhiWuXingString) {
        this.riDiZhiWuXingString = riDiZhiWuXingString;
    }

    public int getShiTianGanWuXingIndex() {
        return shiTianGanWuXingIndex;
    }

    public void setShiTianGanWuXingIndex(int shiTianGanWuXingIndex) {
        this.shiTianGanWuXingIndex = shiTianGanWuXingIndex;
    }

    public String getShiTianGanWuXingString() {
        return shiTianGanWuXingString;
    }

    public void setShiTianGanWuXingString(String shiTianGanWuXingString) {
        this.shiTianGanWuXingString = shiTianGanWuXingString;
    }

    public int getShiDiZhiWuXingIndex() {
        return shiDiZhiWuXingIndex;
    }

    public void setShiDiZhiWuXingIndex(int shiDiZhiWuXingIndex) {
        this.shiDiZhiWuXingIndex = shiDiZhiWuXingIndex;
    }

    public String getShiDiZhiWuXingString() {
        return shiDiZhiWuXingString;
    }

    public void setShiDiZhiWuXingString(String shiDiZhiWuXingString) {
        this.shiDiZhiWuXingString = shiDiZhiWuXingString;
    }

    public int getNianTianGanFangWeiIndex() {
        return nianTianGanFangWeiIndex;
    }

    public void setNianTianGanFangWeiIndex(int nianTianGanFangWeiIndex) {
        this.nianTianGanFangWeiIndex = nianTianGanFangWeiIndex;
    }

    public String getNianTianGanFangWeiString() {
        return nianTianGanFangWeiString;
    }

    public void setNianTianGanFangWeiString(String nianTianGanFangWeiString) {
        this.nianTianGanFangWeiString = nianTianGanFangWeiString;
    }

    public int getNianDiZhiFangWeiIndex() {
        return nianDiZhiFangWeiIndex;
    }

    public void setNianDiZhiFangWeiIndex(int nianDiZhiFangWeiIndex) {
        this.nianDiZhiFangWeiIndex = nianDiZhiFangWeiIndex;
    }

    public String getNianDiZhiFangWeiString() {
        return nianDiZhiFangWeiString;
    }

    public void setNianDiZhiFangWeiString(String nianDiZhiFangWeiString) {
        this.nianDiZhiFangWeiString = nianDiZhiFangWeiString;
    }

    public int getYueTianGanFangWeiIndex() {
        return yueTianGanFangWeiIndex;
    }

    public void setYueTianGanFangWeiIndex(int yueTianGanFangWeiIndex) {
        this.yueTianGanFangWeiIndex = yueTianGanFangWeiIndex;
    }

    public String getYueTianGanFangWeiString() {
        return yueTianGanFangWeiString;
    }

    public void setYueTianGanFangWeiString(String yueTianGanFangWeiString) {
        this.yueTianGanFangWeiString = yueTianGanFangWeiString;
    }

    public int getYueDiZhiFangWeiIndex() {
        return yueDiZhiFangWeiIndex;
    }

    public void setYueDiZhiFangWeiIndex(int yueDiZhiFangWeiIndex) {
        this.yueDiZhiFangWeiIndex = yueDiZhiFangWeiIndex;
    }

    public String getYueDiZhiFangWeiString() {
        return yueDiZhiFangWeiString;
    }

    public void setYueDiZhiFangWeiString(String yueDiZhiFangWeiString) {
        this.yueDiZhiFangWeiString = yueDiZhiFangWeiString;
    }

    public int getRiTianGanFangWeiIndex() {
        return riTianGanFangWeiIndex;
    }

    public void setRiTianGanFangWeiIndex(int riTianGanFangWeiIndex) {
        this.riTianGanFangWeiIndex = riTianGanFangWeiIndex;
    }

    public String getRiTianGanFangWeiString() {
        return riTianGanFangWeiString;
    }

    public void setRiTianGanFangWeiString(String riTianGanFangWeiString) {
        this.riTianGanFangWeiString = riTianGanFangWeiString;
    }

    public int getRiDiZhiFangWeiIndex() {
        return riDiZhiFangWeiIndex;
    }

    public void setRiDiZhiFangWeiIndex(int riDiZhiFangWeiIndex) {
        this.riDiZhiFangWeiIndex = riDiZhiFangWeiIndex;
    }

    public String getRiDiZhiFangWeiString() {
        return riDiZhiFangWeiString;
    }

    public void setRiDiZhiFangWeiString(String riDiZhiFangWeiString) {
        this.riDiZhiFangWeiString = riDiZhiFangWeiString;
    }

    public int getShiTianGanFangWeiIndex() {
        return shiTianGanFangWeiIndex;
    }

    public void setShiTianGanFangWeiIndex(int shiTianGanFangWeiIndex) {
        this.shiTianGanFangWeiIndex = shiTianGanFangWeiIndex;
    }

    public String getShiTianGanFangWeiString() {
        return shiTianGanFangWeiString;
    }

    public void setShiTianGanFangWeiString(String shiTianGanFangWeiString) {
        this.shiTianGanFangWeiString = shiTianGanFangWeiString;
    }

    public int getShiDiZhiFangWeiIndex() {
        return shiDiZhiFangWeiIndex;
    }

    public void setShiDiZhiFangWeiIndex(int shiDiZhiFangWeiIndex) {
        this.shiDiZhiFangWeiIndex = shiDiZhiFangWeiIndex;
    }

    public String getShiDiZhiFangWeiString() {
        return shiDiZhiFangWeiString;
    }

    public void setShiDiZhiFangWeiString(String shiDiZhiFangWeiString) {
        this.shiDiZhiFangWeiString = shiDiZhiFangWeiString;
    }

    @Override
    public String toString() {
        return "PerpetualCalendarModel{" +
                "nianIndex=" + nianIndex +
                ", nianString='" + nianString + '\'' +
                ", yueIndex=" + yueIndex +
                ", yueString='" + yueString + '\'' +
                ", riIndex=" + riIndex +
                ", riString='" + riString + '\'' +
                ", shiChenIndex=" + shiChenIndex +
                ", shiChenString='" + shiChenString + '\'' +
                ", chuiJieYueIndex=" + chuiJieYueIndex +
                ", chuiJieYueString='" + chuiJieYueString + '\'' +
                ", chuiJieRiIndex=" + chuiJieRiIndex +
                ", chuiJieRiString='" + chuiJieRiString + '\'' +
                ", chuiJieQianTianGanIndex=" + chuiJieQianTianGanIndex +
                ", chuiJieQianTianGanString='" + chuiJieQianTianGanString + '\'' +
                ", chuiJieQianDiZhiIndex=" + chuiJieQianDiZhiIndex +
                ", chuiJieQianDiZhiString='" + chuiJieQianDiZhiString + '\'' +
                ", chuiJieHouTianGanIndex=" + chuiJieHouTianGanIndex +
                ", chuiJieHouTianGanString='" + chuiJieHouTianGanString + '\'' +
                ", chuiJieHouDiZhiIndex=" + chuiJieHouDiZhiIndex +
                ", chuiJieHouDiZhiString='" + chuiJieHouDiZhiString + '\'' +
                ", shengXiaoIndex=" + shengXiaoIndex +
                ", shengXiaoString='" + shengXiaoString + '\'' +
                ", nianTianGanIndex=" + nianTianGanIndex +
                ", nianTianGanString='" + nianTianGanString + '\'' +
                ", nianDiZhiIndex=" + nianDiZhiIndex +
                ", nianDiZhiString='" + nianDiZhiString + '\'' +
                ", yueTianGanIndex=" + yueTianGanIndex +
                ", yueTianGanString='" + yueTianGanString + '\'' +
                ", yueDiZhiIndex=" + yueDiZhiIndex +
                ", yueDiZhiString='" + yueDiZhiString + '\'' +
                ", riTianGanIndex=" + riTianGanIndex +
                ", riTianGanString='" + riTianGanString + '\'' +
                ", riDiZhiIndex=" + riDiZhiIndex +
                ", riDiZhiString='" + riDiZhiString + '\'' +
                ", shiTianGanIndex=" + shiTianGanIndex +
                ", shiTianGanString='" + shiTianGanString + '\'' +
                ", shiDiZhiIndex=" + shiDiZhiIndex +
                ", shiDiZhiString='" + shiDiZhiString + '\'' +
                ", nianTianGanWuXingIndex=" + nianTianGanWuXingIndex +
                ", nianTianGanWuXingString='" + nianTianGanWuXingString + '\'' +
                ", nianDiZhiWuXingIndex=" + nianDiZhiWuXingIndex +
                ", nianDiZhiWuXingString='" + nianDiZhiWuXingString + '\'' +
                ", yueTianGanWuXingIndex=" + yueTianGanWuXingIndex +
                ", yueTianGanWuXingString='" + yueTianGanWuXingString + '\'' +
                ", yueDiZhiWuXingIndex=" + yueDiZhiWuXingIndex +
                ", yueDiZhiWuXingString='" + yueDiZhiWuXingString + '\'' +
                ", riTianGanWuXingIndex=" + riTianGanWuXingIndex +
                ", riTianGanWuXingString='" + riTianGanWuXingString + '\'' +
                ", riDiZhiWuXingIndex=" + riDiZhiWuXingIndex +
                ", riDiZhiWuXingString='" + riDiZhiWuXingString + '\'' +
                ", shiTianGanWuXingIndex=" + shiTianGanWuXingIndex +
                ", shiTianGanWuXingString='" + shiTianGanWuXingString + '\'' +
                ", shiDiZhiWuXingIndex=" + shiDiZhiWuXingIndex +
                ", shiDiZhiWuXingString='" + shiDiZhiWuXingString + '\'' +
                ", nianTianGanFangWeiIndex=" + nianTianGanFangWeiIndex +
                ", nianTianGanFangWeiString='" + nianTianGanFangWeiString + '\'' +
                ", nianDiZhiFangWeiIndex=" + nianDiZhiFangWeiIndex +
                ", nianDiZhiFangWeiString='" + nianDiZhiFangWeiString + '\'' +
                ", yueTianGanFangWeiIndex=" + yueTianGanFangWeiIndex +
                ", yueTianGanFangWeiString='" + yueTianGanFangWeiString + '\'' +
                ", yueDiZhiFangWeiIndex=" + yueDiZhiFangWeiIndex +
                ", yueDiZhiFangWeiString='" + yueDiZhiFangWeiString + '\'' +
                ", riTianGanFangWeiIndex=" + riTianGanFangWeiIndex +
                ", riTianGanFangWeiString='" + riTianGanFangWeiString + '\'' +
                ", riDiZhiFangWeiIndex=" + riDiZhiFangWeiIndex +
                ", riDiZhiFangWeiString='" + riDiZhiFangWeiString + '\'' +
                ", shiTianGanFangWeiIndex=" + shiTianGanFangWeiIndex +
                ", shiTianGanFangWeiString='" + shiTianGanFangWeiString + '\'' +
                ", shiDiZhiFangWeiIndex=" + shiDiZhiFangWeiIndex +
                ", shiDiZhiFangWeiString='" + shiDiZhiFangWeiString + '\'' +
                '}';
    }


    public PerpetualCalendarModel(Date date) throws Exception {
        int year = Integer.parseInt(TimeUtil.dateToString(date, "yyyy"));
        int month = Integer.parseInt(TimeUtil.dateToString(date, "MM"));
        int day = Integer.parseInt(TimeUtil.dateToString(date, "dd"));
        int hour = Integer.parseInt(TimeUtil.dateToString(date, "HH"));

        //年的合法性判断
        if (year == 0 || year < 1901 || year > 2050)
            throw new Exception("年应在1901和2050之间");

        int yueZhiIndex = year - 1901;
        int i = (int) Math.floor(yueZhiIndex / 10);
        int nl0 = (i == 0) ? 0 : ms[i - 1][120];
        int n = i * 120;
        for (int j = 0; j < 120; j++) {
            n++;
            if (n > yueZhiIndex * 12) break;
            nl0 += ly[ms[i][j]];
        }
        //春节计算
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.set(Calendar.MILLISECOND, 0);

        c.set(year, 0, 1, 0, 0, 0);
        long now = c.getTimeInMillis();
        c.set(1901, 1, 19, 0, 0, 0);
        long start = c.getTimeInMillis();

        long gl0 = ((now - start) / 86400000l);

        chuiJieYueIndex = (nl0 - gl0 > 30) ? 2 : 1;
        chuiJieYueString = chuiJieYueIndex + "";
        chuiJieRiIndex = (int) ((nl0 - gl0) % 31 + 1);
        chuiJieRiString = chuiJieRiIndex + "";
        //节前什么年
        chuiJieQianTianGanIndex = (yueZhiIndex + 6) % 10;
        chuiJieQianTianGanString = tianGan[(yueZhiIndex + 6) % 10];
        chuiJieQianDiZhiIndex = yueZhiIndex % 12;
        chuiJieQianDiZhiString = diZhi[yueZhiIndex % 12];
        //节后后什么年
        chuiJieHouTianGanIndex = (yueZhiIndex + 7) % 10;
        chuiJieHouTianGanString = tianGan[(yueZhiIndex + 7) % 10];
        chuiJieHouDiZhiIndex = (yueZhiIndex + 1) % 12;
        chuiJieHouDiZhiString = diZhi[(yueZhiIndex + 1) % 12];

        //计算四柱
        c.set(year, month - 1, day, hour, 0, 0);
        long nowTimestamp = c.getTimeInMillis();
        c.set(1901, 1, 18, 23, 0, 0);
        long startTimestamp = c.getTimeInMillis();

        int sum = (int) Math.floor(((nowTimestamp - startTimestamp) / 86400000l));

        //计算日的天干和地支
        riTianGanIndex = (sum + 54) % 10;
        riTianGanString = tianGan[riTianGanIndex];
        riDiZhiIndex = (sum + 52) % 12;
        riDiZhiString = diZhi[riDiZhiIndex];

        //计算时辰的天干和地支
        shiDiZhiIndex = (int) Math.floor((hour * 1 + 1) / 2) % 12;
        shiDiZhiString = diZhi[shiDiZhiIndex];
        shiTianGanIndex = ((riTianGanIndex % 5) * 2 + shiDiZhiIndex) % 10;
        shiTianGanString = tianGan[shiTianGanIndex];

        //计算时辰
        shiChenIndex = (int) Math.floor((hour * 1 + 1) / 2) % 12;
        shiChenString = diZhi[shiChenIndex];
        //计算农历日期
        for (i = 0; ms[i][120] <= sum; i++) ;

        int k = (i == 0) ? 0 : ms[i - 1][120];
        int p = i * 120;
        int j = 0;
        for (; j < 120; j++) {
            k += ly[ms[i][j]];
            p++;
            if (k > sum) break;
        }
        int ri;
        if (sum + 30 < 0) {
            ri = 59 + sum;
            p = -1;
        } else if (sum < 0) {
            ri = 30 + sum;
            p = 0;
        } else {
            ri = sum + ly[ms[i][j]] - k;
        }

        //计算农历月份
        yueIndex = (p + 11) % 12;
        yueString = (yueIndex == 0) ? "正" : riQi[yueIndex];
        int mij = ms[i][j];
        if ((mij == 2 || mij == 3) && ri > 28) {
            ri -= 29;
            yueString = "闰" + yueString;
        } else if ((mij == 4 || mij == 5) && ri > 29) {
            ri -= 30;
            yueString = "闰" + yueString;
        }
        //计算农历日
        riIndex = ri;
        riString = ((ri < 10) ? "初" : "") + riQi[ri];

        //计算年的天干地支
        nianTianGanIndex = (int) (Math.floor((p - 1) / 12 + 7) % 10);
        nianTianGanString = tianGan[nianTianGanIndex];
        nianDiZhiIndex = (int) (Math.floor((p - 1) / 12 + 1) % 12);
        nianDiZhiString = diZhi[nianDiZhiIndex];


        //计算农历年
        nianIndex = year;
        nianString = nianTianGanString + nianDiZhiString;


        //计算月的天干和地支
        yueTianGanIndex = (p + 5) % 10;
        yueTianGanString = tianGan[yueTianGanIndex];
        yueDiZhiIndex = (p + 1) % 12;
        yueDiZhiString = diZhi[yueDiZhiIndex];

        //生肖
        shengXiaoIndex = nianDiZhiIndex;
        shengXiaoString = shuXing[shengXiaoIndex];

        int nianGanIndex = tw[nianTianGanIndex];
        int nianZhiIndex = dw[nianDiZhiIndex];
        int yueGanIndex = tw[yueTianGanIndex];
        yueZhiIndex = dw[yueDiZhiIndex];
        int riGanIndex = tw[riTianGanIndex];
        int riZhiIndex = dw[riDiZhiIndex];
        int shiGanIndex = tw[shiTianGanIndex];
        int shiZhiIndex = dw[shiDiZhiIndex];

        //计算五行
        //年
        nianTianGanWuXingIndex = nianGanIndex;
        nianTianGanWuXingString = wuXing[nianGanIndex];
        nianDiZhiWuXingIndex = nianZhiIndex;
        nianDiZhiWuXingString = wuXing[nianZhiIndex];
        //月
        yueTianGanWuXingIndex = yueGanIndex;
        yueTianGanWuXingString = wuXing[yueGanIndex];
        yueDiZhiWuXingIndex = yueZhiIndex;
        yueDiZhiWuXingString = wuXing[yueZhiIndex];
        //日
        riTianGanWuXingIndex = riGanIndex;
        riTianGanWuXingString = wuXing[riGanIndex];
        riDiZhiWuXingIndex = riZhiIndex;
        riDiZhiWuXingString = wuXing[riZhiIndex];
        //时
        shiTianGanWuXingIndex = shiGanIndex;
        shiTianGanWuXingString = wuXing[shiGanIndex];
        shiDiZhiWuXingIndex = shiZhiIndex;
        shiDiZhiWuXingString = wuXing[shiZhiIndex];

        //计算方位
        //年
        nianTianGanFangWeiIndex = nianGanIndex;
        nianTianGanFangWeiString = fangXiang[nianGanIndex];
        nianDiZhiFangWeiIndex = nianZhiIndex;
        nianDiZhiFangWeiString = fangXiang[nianZhiIndex];
        //月
        yueTianGanFangWeiIndex = yueGanIndex;
        yueTianGanFangWeiString = fangXiang[yueGanIndex];
        yueDiZhiFangWeiIndex = yueZhiIndex;
        yueDiZhiFangWeiString = fangXiang[yueZhiIndex];
        //日
        riTianGanFangWeiIndex = riGanIndex;
        riTianGanFangWeiString = fangXiang[riGanIndex];
        riDiZhiFangWeiIndex = riZhiIndex;
        riDiZhiFangWeiString = fangXiang[riZhiIndex];
        //时
        shiTianGanFangWeiIndex = shiGanIndex;
        shiTianGanFangWeiString = fangXiang[shiGanIndex];
        shiDiZhiFangWeiIndex = shiZhiIndex;
        shiDiZhiFangWeiString = fangXiang[shiZhiIndex];
    }
}
