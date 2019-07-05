package com.whty.eschoolbag.filemanager.sw.constants;


public enum DimenTypes {

    //适配Android 3.2以上   大部分手机的sw值集中在  300-460之间


    DP_sw__300(300),
    DP_sw__360(360),
    DP_sw__350(350),
    DP_sw__400(400),
    DP_sw__450(450),
    DP_sw__500(500),
    DP_sw__550(550),
    DP_sw__600(600),
    DP_sw__650(650),
    DP_sw__700(700),
    DP_sw__750(750),
    DP_sw__800(800),
    DP_sw__850(850),
    DP_sw__900(900);

    // 想生成多少自己以此类推


    /**
     * 屏幕最小宽度
     */
    private int swWidthDp;


    DimenTypes(int swWidthDp) {

        this.swWidthDp = swWidthDp;
    }

    public int getSwWidthDp() {
        return swWidthDp;
    }

    public void setSwWidthDp(int swWidthDp) {
        this.swWidthDp = swWidthDp;
    }

}
