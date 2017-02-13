package com.iyuile.caelum.utils.plate;

import com.iyuile.caelum.utils.TimeUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * 八卦
 * <p/>
 * Created by WangYao on 2017/1/6.
 */
public class BaGuaUtils {

    // --------------------------------------------第二步--------------------------------------------

    /**
     * 计算阴阳遁和局数
     *
     * @param date  阳历时间 '2012-03-17 09:06:00'
     * @param model 万年历
     * @return yang是否是阳局 juShu局数
     */
    public YinYangAndJuShuModel getYinYangAndJuShu(Date date, PerpetualCalendarModel model) {
        int year = Integer.parseInt(TimeUtil.dateToString(date, "yyyy"));
        //计算阴阳局 冬至后夏至前为阳
        Date dongZhiDate = TimeUtil.stringToDate((year - 1) + "-12-22", TimeUtil.FORMAT_DATE);
        Date xiaZhiDate = TimeUtil.stringToDate(year + "-06-22", TimeUtil.FORMAT_DATE);
        boolean isYang = false;
        if (date.getTime() > (dongZhiDate.getTime()) && xiaZhiDate.getTime() > (date.getTime())) {
            //阳局
            isYang = true;
        }
        //计算局数
        int juShu = (model.getNianDiZhiIndex() + 1 + model.getYueIndex() + 1 + model.getRiIndex() + 1 + model.getShiDiZhiIndex() + 1) % 9;
        if (juShu == 0)
            juShu = 9;

        return new YinYangAndJuShuModel(isYang, juShu);
    }

    /**
     * 阴阳遁和局数模型
     */
    public class YinYangAndJuShuModel {
        private boolean isYang;
        private int number;//juShu

        public YinYangAndJuShuModel(boolean isYang, int number) {
            this.isYang = isYang;
            this.number = number;
        }

        public boolean isYang() {
            return isYang;
        }

        public void setYang(boolean yang) {
            isYang = yang;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "YinYangAndJuShuModel{" +
                    "isYang=" + isYang +
                    ", number=" + number +
                    '}';
        }
    }

    // --------------------------------------------第三步--------------------------------------------

    /**
     * 创建八卦空局
     */
    public GongModel[] createBaGua(GongModel[] gongModels) {
        for (int i = 1; i < 10; i++) {
            GongModel gongModel = new GongModel();
            gongModel.setNum(i);
            gongModels[i - 1] = gongModel;
        }
        return gongModels;
    }

    /**
     * 宫模型
     */
    public class GongModel {
        private int num;
        public int diPanSanQiLiuYiIndex;
        public String diPanSanQiLiuYiString;
        public int tianPanSanQiLiuYiIndex = -1;
        public String tianPanSanQiLiuYiString;
        public int baShenIndex;
        public String baShenString;
        public int jiuXingIndex;
        public String jiuXingString;
        public int baMenIndex;
        public String baMenString;
        public int yinGanIndex;
        public String yinGanString;

        @Override
        public String toString() {
            return "GongModel{" +
                    "num=" + num +
                    ", diPanSanQiLiuYiIndex=" + diPanSanQiLiuYiIndex +
                    ", diPanSanQiLiuYiString='" + diPanSanQiLiuYiString + '\'' +
                    ", tianPanSanQiLiuYiIndex=" + tianPanSanQiLiuYiIndex +
                    ", tianPanSanQiLiuYiString='" + tianPanSanQiLiuYiString + '\'' +
                    ", baShenIndex=" + baShenIndex +
                    ", baShenString='" + baShenString + '\'' +
                    ", jiuXingIndex=" + jiuXingIndex +
                    ", jiuXingString='" + jiuXingString + '\'' +
                    ", baMenIndex=" + baMenIndex +
                    ", baMenString='" + baMenString + '\'' +
                    ", yinGanIndex=" + yinGanIndex +
                    ", yinGanString='" + yinGanString + '\'' +
                    '}';
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getDiPanSanQiLiuYiIndex() {
            return diPanSanQiLiuYiIndex;
        }

        public void setDiPanSanQiLiuYiIndex(int diPanSanQiLiuYiIndex) {
            this.diPanSanQiLiuYiIndex = diPanSanQiLiuYiIndex;
        }

        public String getDiPanSanQiLiuYiString() {
            return diPanSanQiLiuYiString;
        }

        public void setDiPanSanQiLiuYiString(String diPanSanQiLiuYiString) {
            this.diPanSanQiLiuYiString = diPanSanQiLiuYiString;
        }

        public int getTianPanSanQiLiuYiIndex() {
            return tianPanSanQiLiuYiIndex;
        }

        public void setTianPanSanQiLiuYiIndex(int tianPanSanQiLiuYiIndex) {
            this.tianPanSanQiLiuYiIndex = tianPanSanQiLiuYiIndex;
        }

        public String getTianPanSanQiLiuYiString() {
            return tianPanSanQiLiuYiString;
        }

        public void setTianPanSanQiLiuYiString(String tianPanSanQiLiuYiString) {
            this.tianPanSanQiLiuYiString = tianPanSanQiLiuYiString;
        }

        public int getBaShenIndex() {
            return baShenIndex;
        }

        public void setBaShenIndex(int baShenIndex) {
            this.baShenIndex = baShenIndex;
        }

        public String getBaShenString() {
            return baShenString;
        }

        public void setBaShenString(String baShenString) {
            this.baShenString = baShenString;
        }

        public int getJiuXingIndex() {
            return jiuXingIndex;
        }

        public void setJiuXingIndex(int jiuXingIndex) {
            this.jiuXingIndex = jiuXingIndex;
        }

        public String getJiuXingString() {
            return jiuXingString;
        }

        public void setJiuXingString(String jiuXingString) {
            this.jiuXingString = jiuXingString;
        }

        public int getBaMenIndex() {
            return baMenIndex;
        }

        public void setBaMenIndex(int baMenIndex) {
            this.baMenIndex = baMenIndex;
        }

        public String getBaMenString() {
            return baMenString;
        }

        public void setBaMenString(String baMenString) {
            this.baMenString = baMenString;
        }

        public int getYinGanIndex() {
            return yinGanIndex;
        }

        public void setYinGanIndex(int yinGanIndex) {
            this.yinGanIndex = yinGanIndex;
        }

        public String getYinGanString() {
            return yinGanString;
        }

        public void setYinGanString(String yinGanString) {
            this.yinGanString = yinGanString;
        }
    }

    // --------------------------------------------第四步--------------------------------------------

    private String[] sanQiLiuYi = new String[]{"戊", "己", "庚", "辛", "壬", "癸", "丁", "丙", "乙"};

    /**
     * 步地盘三奇六议
     *
     * @param gongModels
     * @param model
     */
    public GongModel[] drawDiPanSanQiLiuYi(GongModel[] gongModels, YinYangAndJuShuModel model) {

        int sanQiLiuYiIndex = 0;

        if (model.isYang) {
            //阳局顺时针
            for (int i = (model.number - 1); i < 9; i++) {
                sanQiLiuYiIndex = drawDiPanSanQiLiuYiForOperation(gongModels[i], sanQiLiuYiIndex);
            }
            //剩下的布局
            for (int i = 0; i < (model.number - 1); i++) {
                sanQiLiuYiIndex = drawDiPanSanQiLiuYiForOperation(gongModels[i], sanQiLiuYiIndex);
            }
        } else {
            //阴局逆时针
            for (int i = (model.number - 1); i >= 0; i--) {
                sanQiLiuYiIndex = drawDiPanSanQiLiuYiForOperation(gongModels[i], sanQiLiuYiIndex);
            }
            for (int i = 8; i > (model.number - 1); i--) {
                sanQiLiuYiIndex = drawDiPanSanQiLiuYiForOperation(gongModels[i], sanQiLiuYiIndex);
            }
        }

        //第五宫的放入第四宫
        //TODO :::test
//        gongModels[3].setDiPanSanQiLiuYiString(gongModels[3].getDiPanSanQiLiuYiString() + gongModels[4].getDiPanSanQiLiuYiString());
//        gongModels[4].setDiPanSanQiLiuYiString(null);

        return gongModels;
    }

    private int drawDiPanSanQiLiuYiForOperation(GongModel gongModel1, int sanQiLiuYiIndex) {
        GongModel gongModel = gongModel1;
        gongModel.setDiPanSanQiLiuYiIndex(sanQiLiuYiIndex);
        gongModel.setDiPanSanQiLiuYiString(sanQiLiuYi[sanQiLiuYiIndex]);
        sanQiLiuYiIndex++;
        return sanQiLiuYiIndex;
    }

    // --------------------------------------------第五步--------------------------------------------

    /**
     * 查找寻首与大将
     */
    public XunShouAndDaJjiangModel findXunShouAndDaJjiang(PerpetualCalendarModel perpetualCalendarModel) throws Exception {

        int shiTianGan = perpetualCalendarModel.getShiTianGanIndex();
        int shiDiZhi = perpetualCalendarModel.getShiDiZhiIndex();

//        int colum = shiTianGan;
        int sum = (shiTianGan - shiDiZhi);
        if (sum < 0)
            sum += 12;
        int row = sum / 2;

        int xunShouTianGanIndex = 0;
        String xunShouTianGanString = perpetualCalendarModel.tianGan[xunShouTianGanIndex];
        int xunShouDiZhiIndex = (row * 10) % 12;
        String xunShouDiZhiString = perpetualCalendarModel.diZhi[xunShouDiZhiIndex];

        int daJiangIndex = row;
        String daJiangString = sanQiLiuYi[daJiangIndex];

        return new XunShouAndDaJjiangModel(xunShouTianGanIndex, xunShouTianGanString, xunShouDiZhiIndex, xunShouDiZhiString, daJiangIndex, daJiangString);
    }

    /**
     * 查找寻首与大将模型
     */
    public class XunShouAndDaJjiangModel {

        private int xunShouTianGanIndex;
        private String xunShouTianGanString;
        private int xunShouDiZhiIndex;
        private String xunShouDiZhiString;
        private int daJiangIndex;
        private String daJiangString;

        public XunShouAndDaJjiangModel(int xunShouTianGanIndex, String xunShouTianGanString, int xunShouDiZhiIndex, String xunShouDiZhiString, int daJiangIndex, String daJiangString) {
            this.xunShouTianGanIndex = xunShouTianGanIndex;
            this.xunShouTianGanString = xunShouTianGanString;
            this.xunShouDiZhiIndex = xunShouDiZhiIndex;
            this.xunShouDiZhiString = xunShouDiZhiString;
            this.daJiangIndex = daJiangIndex;
            this.daJiangString = daJiangString;
        }

        public int getXunShouTianGanIndex() {
            return xunShouTianGanIndex;
        }

        public void setXunShouTianGanIndex(int xunShouTianGanIndex) {
            this.xunShouTianGanIndex = xunShouTianGanIndex;
        }

        public String getXunShouTianGanString() {
            return xunShouTianGanString;
        }

        public void setXunShouTianGanString(String xunShouTianGanString) {
            this.xunShouTianGanString = xunShouTianGanString;
        }

        public int getXunShouDiZhiIndex() {
            return xunShouDiZhiIndex;
        }

        public void setXunShouDiZhiIndex(int xunShouDiZhiIndex) {
            this.xunShouDiZhiIndex = xunShouDiZhiIndex;
        }

        public String getXunShouDiZhiString() {
            return xunShouDiZhiString;
        }

        public void setXunShouDiZhiString(String xunShouDiZhiString) {
            this.xunShouDiZhiString = xunShouDiZhiString;
        }

        public int getDaJiangIndex() {
            return daJiangIndex;
        }

        public void setDaJiangIndex(int daJiangIndex) {
            this.daJiangIndex = daJiangIndex;
        }

        public String getDaJiangString() {
            return daJiangString;
        }

        public void setDaJiangString(String daJiangString) {
            this.daJiangString = daJiangString;
        }

        @Override
        public String toString() {
            return "XunShouAndDaJjiangModel{" +
                    "xunShouTianGanIndex=" + xunShouTianGanIndex +
                    ", xunShouTianGanString='" + xunShouTianGanString + '\'' +
                    ", xunShouDiZhiIndex=" + xunShouDiZhiIndex +
                    ", xunShouDiZhiString='" + xunShouDiZhiString + '\'' +
                    ", daJiangIndex=" + daJiangIndex +
                    ", daJiangString='" + daJiangString + '\'' +
                    '}';
        }
    }

    // --------------------------------------------第六步--------------------------------------------

    /**
     * 查找值符星与值使们
     *
     * @return array|bool
     */
    public ZhiFuXingAndZhiShiMenModel findZhiFuXingAndZhiShiMen(GongModel[] diPangongModel, int daJiangIndex) {
        //地盘中 阳二局 则 八卦中 2->戊 3->己 4->庚 5->辛 6->壬 7->癸 8->丁 9->丙 1->乙
        //大将如果为 壬 则 值符星就是6宫中的 天任星 值使们就是6宫中的 生门
        for (GongModel gong : diPangongModel) {
            if (gong.getDiPanSanQiLiuYiIndex() == daJiangIndex) {
                //落在中宫
                if (gong.getNum() == 5)
                    gong = diPangongModel[3];
                BaGuaGongModel baGuaGongModel = new BaGuaGongModel(gong.getNum());
                return new ZhiFuXingAndZhiShiMenModel(
                        baGuaGongModel.getNum(),
                        baGuaGongModel.getNum() - 1,
                        baGuaGongModel.getMen(),
                        baGuaGongModel.getNum() - 1,
                        baGuaGongModel.getXing()
                );
            }
        }
        return null;
    }

    /**
     * 值符星与值使们模型
     */
    public class ZhiFuXingAndZhiShiMenModel {
        private int zhiFuGongNum;
        private int zhiShiMenIndex;
        private String zhiShiMenString;
        private int zhiFuXingIndex;
        private String zhiFuXingString;

        public ZhiFuXingAndZhiShiMenModel(int zhiFuGongNum, int zhiShiMenIndex, String zhiShiMenString, int zhiFuXingIndex, String zhiFuXingString) {
            this.zhiFuGongNum = zhiFuGongNum;
            this.zhiShiMenIndex = zhiShiMenIndex;
            this.zhiShiMenString = zhiShiMenString;
            this.zhiFuXingIndex = zhiFuXingIndex;
            this.zhiFuXingString = zhiFuXingString;
        }

        @Override
        public String toString() {
            return "ZhiFuXingAndZhiShiMenModel{" +
                    "zhiFuGongNum=" + zhiFuGongNum +
                    ", zhiShiMenIndex=" + zhiShiMenIndex +
                    ", zhiShiMenString='" + zhiShiMenString + '\'' +
                    ", zhiFuXingIndex=" + zhiFuXingIndex +
                    ", zhiFuXingString='" + zhiFuXingString + '\'' +
                    '}';
        }

        public int getZhiFuGongNum() {
            return zhiFuGongNum;
        }

        public void setZhiFuGongNum(int zhiFuGongNum) {
            this.zhiFuGongNum = zhiFuGongNum;
        }

        public int getZhiShiMenIndex() {
            return zhiShiMenIndex;
        }

        public void setZhiShiMenIndex(int zhiShiMenIndex) {
            this.zhiShiMenIndex = zhiShiMenIndex;
        }

        public String getZhiShiMenString() {
            return zhiShiMenString;
        }

        public void setZhiShiMenString(String zhiShiMenString) {
            this.zhiShiMenString = zhiShiMenString;
        }

        public int getZhiFuXingIndex() {
            return zhiFuXingIndex;
        }

        public void setZhiFuXingIndex(int zhiFuXingIndex) {
            this.zhiFuXingIndex = zhiFuXingIndex;
        }

        public String getZhiFuXingString() {
            return zhiFuXingString;
        }

        public void setZhiFuXingString(String zhiFuXingString) {
            this.zhiFuXingString = zhiFuXingString;
        }
    }

    /**
     * 八卦宫模型
     */
    public class BaGuaGongModel {

        private int num;
        private String[] names = new String[]{"坎", "震", "兑", "坤", "中宫", "艮", "乾", "巽", "离"};
        //        private String[] xings = new String[]{"天蓬星", "天辅星", "天柱星", "天芮星", "中宫", "天任星", "天冲星", "天心星", "天英星"};
        private String[] xings = new String[]{"蓬", "辅", "柱", "芮", "中宫", "任", "冲", "心", "英"};
        //        private String[] mens = new String[]{"休门", "杜门", "惊门", "死门", "中宫", "生门", "伤门", "开门", "景门"};
        private String[] mens = new String[]{"休", "杜", "惊", "死", "中宫", "生", "伤", "开", "景"};
        private String[] shiChens = new String[]{"子", "巳辰", "酉", "申未", "中宫", "寅", "卯", "戌亥", "午"};
        private String[] wuXings = new String[]{"水", "木", "金", "土", "中宫", "土", "木", "金", "火"};

        public BaGuaGongModel(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String[] getNames() {
            return names;
        }

        public void setNames(String[] names) {
            this.names = names;
        }

        public String[] getXings() {
            return xings;
        }

        public void setXings(String[] xings) {
            this.xings = xings;
        }

        public String[] getMens() {
            return mens;
        }

        public void setMens(String[] mens) {
            this.mens = mens;
        }

        public String[] getShiChens() {
            return shiChens;
        }

        public void setShiChens(String[] shiChens) {
            this.shiChens = shiChens;
        }

        public String[] getWuXings() {
            return wuXings;
        }

        public String getMen() {
            return mens[num - 1];
        }

        public String getXing() {
            return xings[num - 1];
        }

        public String getShiChen() {
            return shiChens[num - 1];
        }

        public String getWuXing() {
            return wuXings[num - 1];
        }
    }

    // --------------------------------------------第七步--------------------------------------------

    private String[] baGuaTurn = new String[]{"8", "1", "6", "7", "2", "9", "4", "3"};

    /**
     * 第七步，布天盘三奇六仪
     *
     * @param perpetualCalendarModel
     * @param gongModels
     * @param xunShouAndDaJjiangModel
     * @return
     */
    public GongModel[] drawTianPanSanQiLiuYi(PerpetualCalendarModel perpetualCalendarModel, GongModel[] gongModels, XunShouAndDaJjiangModel xunShouAndDaJjiangModel, YinYangAndJuShuModel yinYangAndJuShuModel) {
        //获取当前预测时间时干
        String thiTianGanString = perpetualCalendarModel.getShiTianGanString();

        int shiTianGanSanQiLiuYiIndex = Arrays.asList(sanQiLiuYi).indexOf(thiTianGanString);

        //如果为甲时，则天地盘一样
        if (shiTianGanSanQiLiuYiIndex == -1) {
            for (GongModel gongModel : gongModels) {
                gongModel.setTianPanSanQiLiuYiIndex(gongModel.getDiPanSanQiLiuYiIndex());
                gongModel.setTianPanSanQiLiuYiString(gongModel.getDiPanSanQiLiuYiString());
            }
        } else {
            //如果不是甲
            for (GongModel gongModel : gongModels) {
                if (gongModel.getDiPanSanQiLiuYiIndex() == shiTianGanSanQiLiuYiIndex) {
                    //把大将放入该宫
                    gongModel.setTianPanSanQiLiuYiIndex(xunShouAndDaJjiangModel.getDaJiangIndex());
                    gongModel.setTianPanSanQiLiuYiString(xunShouAndDaJjiangModel.getDaJiangString());

                    //生成其他宫的天盘,逆时针
                    //TODO 中宫信息丢失，原因在baGuaTurn中缺少5
                    String[] reverseBaGua = baGuaTurn.clone();
                    if (!yinYangAndJuShuModel.isYang()) {
                        Collections.reverse(Arrays.asList(reverseBaGua));
                    }
                    int index = Arrays.asList(reverseBaGua).indexOf(gongModel.getNum() + "");
                    //重新排序
                    int[] nowBaGuas = new int[reverseBaGua.length - 1];

                    //填写index之后的
                    int j = 0;
                    for (int i = index + 1; i < reverseBaGua.length; i++) {
                        //获取宫数
                        int gongNum = Integer.parseInt(reverseBaGua[i]);
                        nowBaGuas[j] = gongNum;
                        j++;
                    }

                    //填写index之前的
                    for (int i = 0; i < index; i++) {
                        int gongNum = Integer.parseInt(reverseBaGua[i]);
                        nowBaGuas[j] = gongNum;
                        j++;
                    }

                    //赋值天盘信息
                    int prevIndex = gongModel.getDiPanSanQiLiuYiIndex();
                    String prevString = gongModel.getDiPanSanQiLiuYiString();

                    for (int i = 0; i < nowBaGuas.length; i++) {
                        int gongNum = nowBaGuas[i];
                        gongModels[gongNum - 1].setTianPanSanQiLiuYiIndex(prevIndex);
                        gongModels[gongNum - 1].setTianPanSanQiLiuYiString(prevString);

                        prevIndex = gongModels[gongNum - 1].getDiPanSanQiLiuYiIndex();
                        prevString = gongModels[gongNum - 1].getDiPanSanQiLiuYiString();
                    }
                } else {
                    //TODO 错误处理，理论上没有错误
                }
            }
        }

        return gongModels;
    }

    // --------------------------------------------第八步--------------------------------------------

    private String[] baShen = new String[]{"符", "蛇", "阴", "六", "白", "玄", "地", "天"};

    /**
     * 排八神
     *
     * @param yinYangAndJuShuModel->yang            是否是阳局
     * @param gongModels                            排好天盘底盘的宫
     * @param xunShouAndDaJjiangModel->daJiangIndex 大将的Index
     * @return 返回排完八神的宫
     */
    public GongModel[] drawBaShen(YinYangAndJuShuModel yinYangAndJuShuModel, GongModel[] gongModels, XunShouAndDaJjiangModel xunShouAndDaJjiangModel) {
        String[] gongsTurnArray = baGuaTurn.clone();
        if (!yinYangAndJuShuModel.isYang)
            Collections.reverse(Arrays.asList(gongsTurnArray));
        for (GongModel gongModel : gongModels) {
            if (xunShouAndDaJjiangModel.getDaJiangIndex() == gongModel.getTianPanSanQiLiuYiIndex() && gongModel.getTianPanSanQiLiuYiIndex() != -1) {
                //中宫情况
                if (gongModel.getNum() == 5) {
                    gongModel = gongModels[3];
                }

                //进行排序
                int[] nowBaGuas = new int[gongsTurnArray.length];
                int index = Arrays.asList(gongsTurnArray).indexOf(gongModel.getNum() + "");
                //先排序后面的
                int j = 0;
                for (int i = index; i < gongsTurnArray.length; i++) {
                    nowBaGuas[j] = Integer.parseInt(gongsTurnArray[i]);
                    j++;
                }
                //再拍前面的
                for (int i = 0; i < index; i++) {
                    nowBaGuas[j] = Integer.parseInt(gongsTurnArray[i]);
                    j++;
                }

                //赋值八神
                for (int i = 0; i < nowBaGuas.length; i++) {
                    GongModel gongModel1 = gongModels[nowBaGuas[i] - 1];
                    gongModel1.setBaShenIndex(i);
                    gongModel1.setBaShenString(baShen[i]);
                }
            }
        }
        return gongModels;
    }

    // --------------------------------------------第九步--------------------------------------------

    /**
     * 排九星
     *
     * @param gongModels                            拍完天盘的gongs
     * @param xunShouAndDaJjiangModel->daJiangIndex 地盘的地星Index
     * @return 返回排完的gongs
     */
    public GongModel[] drawJiuXing(GongModel[] gongModels, XunShouAndDaJjiangModel xunShouAndDaJjiangModel) {
        //九星是按照天盘的大将进行排，这时候大将已经从地盘的6转移到了天盘的1
        //获取天盘的大将
        for (GongModel gongModel : gongModels) {
            if (gongModel.getTianPanSanQiLiuYiIndex() == xunShouAndDaJjiangModel.getDaJiangIndex()) {
                //中宫情况
                if (gongModel.getNum() == 5) {
                    gongModel = gongModels[3];
                }
                int[] nowBaGuas = new int[baGuaTurn.length];
                //进行排序,顺时针
                int index = Arrays.asList(baGuaTurn).indexOf(gongModel.getNum() + "");
                //先排序后面的
                int j = 0;
                for (int i = index; i < baGuaTurn.length; i++) {
                    nowBaGuas[j] = Integer.parseInt(baGuaTurn[i]);
                    j++;
                }
                //再拍前面的
                for (int i = 0; i < index; i++) {
                    nowBaGuas[j] = Integer.parseInt(baGuaTurn[i]);
                    j++;
                }

                //获取地盘九星顺序
                BaGuaGongModel baGuaGong = new BaGuaGongModel(gongModel.getNum());
                int lastXingIndex = gongModel.getNum() - 1;
                String lastXingString = baGuaGong.getXing();

                //排九星
                for (int i = 0; i < nowBaGuas.length; i++) {
                    if (i == nowBaGuas.length - 1) {
                        GongModel gongModel1 = gongModels[nowBaGuas[i] - 1];
                        gongModel1.setJiuXingIndex(lastXingIndex);
                        gongModel1.setJiuXingString(lastXingString);
                    } else {
                        GongModel gongModel1 = gongModels[nowBaGuas[i] - 1];
                        GongModel nextGong = gongModels[nowBaGuas[i + 1] - 1];
                        //获取下一个的星
                        BaGuaGongModel nextBaGuaGong = new BaGuaGongModel(nextGong.getNum());
                        gongModel1.setJiuXingIndex(nextBaGuaGong.getNum() - 1);
                        gongModel1.setJiuXingString(nextBaGuaGong.getXing());
                    }
                }
            }
        }
        return gongModels;
    }

    // --------------------------------------------第十步--------------------------------------------

    /**
     * 排八门
     *
     * @param gongModels
     * @param perpetualCalendarModel
     * @param zhiFuXingAndZhiShiMenModel
     * @param yinYangAndJuShuModel
     * @return
     */
    public GongModel[] drawBaMen(GongModel[] gongModels, PerpetualCalendarModel perpetualCalendarModel, ZhiFuXingAndZhiShiMenModel zhiFuXingAndZhiShiMenModel, YinYangAndJuShuModel yinYangAndJuShuModel) {
        //获取指使宫
        GongModel zhiShiGong = gongModels[zhiFuXingAndZhiShiMenModel.getZhiShiMenIndex()];
        //获取当前时辰
        int shiTianGan = perpetualCalendarModel.getShiTianGanIndex();

        //计算生门移动到的宫
        String[] turnGongs = baGuaTurn.clone();
        if (!yinYangAndJuShuModel.isYang)
            Collections.reverse(Arrays.asList(turnGongs));

        //计算移动的步数
        int step = shiTianGan;

        //查找当前的指使宫所在index
        int index = Arrays.asList(turnGongs).indexOf(zhiShiGong.getNum() + "");

        //排序
        int[] nowBaGuas = new int[baGuaTurn.length];
        //先排序后面的
        int j = 0;
        for (int i = index; i < baGuaTurn.length; i++) {
            nowBaGuas[j] = Integer.parseInt(baGuaTurn[i]);
            j++;
        }
        //再拍前面的
        for (int i = 0; i < index; i++) {
            nowBaGuas[j] = Integer.parseInt(baGuaTurn[i]);
            j++;
        }

        //进行排八门
        for (int i = 0; i < nowBaGuas.length; i++) {
            BaGuaGongModel bgGua = new BaGuaGongModel(nowBaGuas[i]);
            index = i + step;
            if (index >= nowBaGuas.length) {
                index -= nowBaGuas.length;
            }
            int willGongNum = nowBaGuas[index];

            //获取到要赋值的宫
            GongModel gongModel = gongModels[willGongNum - 1];
            gongModel.setBaMenIndex(bgGua.getNum() - 1);
            gongModel.setBaMenString(bgGua.getMen());
        }
        return gongModels;
    }
    // --------------------------------------------第十一步--------------------------------------------

    /**
     * 获取隐干
     *
     * @param gongModels
     * @param zhiFuXingAndZhiShiMenModel
     * @param perpetualCalendarModel
     * @param yinYangAndJuShuModel
     * @return
     */
    public GongModel[] drawYinGan(GongModel[] gongModels, ZhiFuXingAndZhiShiMenModel zhiFuXingAndZhiShiMenModel, PerpetualCalendarModel perpetualCalendarModel, YinYangAndJuShuModel yinYangAndJuShuModel) {
        //获取当前时辰
        int shiTianGan = perpetualCalendarModel.getShiTianGanIndex();
        String shiTianGanString = perpetualCalendarModel.getShiTianGanString();
        GongModel zhiShiGong = null;
        //当前天盘值付宫是
        for (GongModel gongModel : gongModels) {
            if (gongModel.getBaMenIndex() == zhiFuXingAndZhiShiMenModel.getZhiShiMenIndex())
                zhiShiGong = gongModel;
        }

        //时干为甲的时候,为伏吟局
        if (shiTianGan == 0) {
            int[] turnGongs = new int[]{5, 6, 7, 8, 9, 1, 2, 3, 4};
            if (!yinYangAndJuShuModel.isYang)
                turnGongs = new int[]{5, 4, 3, 2, 1, 9, 8, 7, 6};

            //赋值给排序好的宫，
            for (int i = 0; i < turnGongs.length; i++) {
                GongModel gongModel = gongModels[turnGongs[i] - 1];
                gongModel.setYinGanIndex(i);
                gongModel.setYinGanString(sanQiLiuYi[i]);
            }
        } else {
            //先给天盘排序
            //找到时干在天盘中的位置//获取当前实干对应的宫
            int[] tianPanTurns = new int[baGuaTurn.length];
            int tianGanIndex = Arrays.asList(sanQiLiuYi).indexOf(shiTianGanString);

            //排之后的
            for (GongModel gongModel : gongModels) {
                if (gongModel.getTianPanSanQiLiuYiIndex() == tianGanIndex && tianGanIndex != -1) {
                    //排序
                    int index = Arrays.asList(baGuaTurn).indexOf(gongModel.getNum() + "");
                    //排后面的
                    for (int i = index; i < baGuaTurn.length; i++) {
                        tianPanTurns[i] = Integer.parseInt(baGuaTurn[i]);
                    }
                    //排前面的
                    for (int i = 0; i < index; i++) {
                        tianPanTurns[i] = Integer.parseInt(baGuaTurn[i]);
                    }
                }
            }

            //排序
            int index = Arrays.asList(baGuaTurn).indexOf(zhiShiGong.getNum() + "");
            //排序
            int[] turnGongs = new int[baGuaTurn.length];
            //先排序后面的
            for (int i = index; i < baGuaTurn.length; i++) {
                turnGongs[i] = Integer.parseInt(baGuaTurn[i]);
            }
            //再拍前面的
            for (int i = 0; i < index; i++) {
                turnGongs[i] = Integer.parseInt(baGuaTurn[i]);
            }

            //赋值给排序好的宫，
            for (int i = 0; i < turnGongs.length; i++) {
                GongModel valueGong = gongModels[tianPanTurns[i] - 1];

                GongModel gong = gongModels[turnGongs[i] - 1];
                gong.setYinGanIndex(valueGong.getTianPanSanQiLiuYiIndex());
                gong.setYinGanString(valueGong.getTianPanSanQiLiuYiString());
            }
        }

        return gongModels;
    }

    // --------------------------------------------第十二步--------------------------------------------

    /**
     * 空亡and马星
     *
     * @param xunShouAndDaJjiangModel
     * @param perpetualCalendarModel
     * @return
     */
    public KongWangAndMaXingModel drawKongWangAndMaXing(XunShouAndDaJjiangModel xunShouAndDaJjiangModel, PerpetualCalendarModel perpetualCalendarModel) {
        int[][] kongWangArray = new int[][]{{11, 0}, {9, 10}, {7, 8}, {5, 6}, {3, 4}, {1, 2}};

        String[] maXingArray = new String[]{"1", "5", "9", "0", "8", "4", "3", "7", "11", "2", "6", "10"};

        int index = Arrays.asList(maXingArray).indexOf(perpetualCalendarModel.getShiDiZhiIndex() + "");
        int maXingIndex = 0;
        switch (index) {
            case 0:
            case 1:
            case 2:
                maXingIndex = 0;
                break;
            case 3:
            case 4:
            case 5:
                maXingIndex = 1;
                break;
            case 6:
            case 7:
            case 8:
                maXingIndex = 2;
                break;
            case 9:
            case 10:
            case 11:
                maXingIndex = 3;
                break;
        }

        return new KongWangAndMaXingModel(kongWangArray[xunShouAndDaJjiangModel.getDaJiangIndex()], maXingIndex);
    }

    /**
     * 空亡and马星
     */
    public class KongWangAndMaXingModel {
        private int[] kongWangIndex;
        private int maXingIndex;

        public KongWangAndMaXingModel(int[] kongWangIndex, int maXingIndex) {
            this.kongWangIndex = kongWangIndex;
            this.maXingIndex = maXingIndex;
        }

        public int[] getKongWangIndex() {
            return kongWangIndex;
        }

        public int getMaXingIndex() {
            return maXingIndex;
        }

        @Override
        public String toString() {
            return "KongWangAndMaXingModel{" +
                    "kongWangIndex=" + Arrays.toString(kongWangIndex) +
                    ", maXingIndex=" + maXingIndex +
                    '}';
        }
    }


}
