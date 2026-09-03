package com.nlnl.lotto.common;

public enum LottoType {

    // 定義規則：名稱, 紅球總數, 紅球選幾個, 藍球總數, 藍球選幾個
    UNION_LOTTO("union_lotto", 33, 6, 16, 1) {
        @Override
        public int calculatePrize(int red, int blue) {
            // 你的 calculateUnionLottoPrize 邏輯搬過來
            if (red == 6 && blue == 1) return 5000000;
            if (red == 6) return 1500000;
            if (red == 5 && blue == 1) return 3000;
            if (red == 5 || (red == 4 && blue == 1)) return 200;
            if (red == 4 || (red == 3 && blue == 1)) return 10;
            if (blue == 1) return 5;
            return 0;
        }
    },
    SUPER_LOTTO("super_lotto", 35, 5, 12, 2) {
        @Override
        public int calculatePrize(int red, int blue) {
            if (red == 5 && blue == 2) return 5000000;
            if (red == 5 && blue == 1) return 1100000;
            if (red == 5) return 10000;
            if (red == 4 && blue == 2) return 3000;
            if (red == 4 && blue == 1) return 300;
            if (red == 3 && blue == 2) return 200;
            if (red == 4) return 100;
            if ((red == 3 && blue == 1) || (red == 2 && blue == 2)) return 15;
            if (red == 3 || (red == 2 && blue == 1) || blue == 2) return 5;
            return 0;
        }
    },
    MARK_SIX("mark_six", 49, 6, 0, 0) {
        @Override
        public int calculatePrize(int red, int blue) {
            if (red == 6) return 8000000;
            if (red == 5 && blue == 1) return 800000;
            if (red == 5) return 80000;
            if (red == 4 && blue == 1) return 9600;
            if (red == 4) return 640;
            if (red == 3 && blue == 1) return 320;
            if (red == 3) return 40;
            return 0;
        }
    };

    // 字段定義
    public final String code;
    public final int redTotal;
    public final int redCount;
    public final int blueTotal;
    public final int blueCount;

    LottoType(String code, int redTotal, int redCount, int blueTotal, int blueCount) {
        this.code = code;
        this.redTotal = redTotal;
        this.redCount = redCount;
        this.blueTotal = blueTotal;
        this.blueCount = blueCount;
    }

    // 抽象方法：算錢邏輯
    public abstract int calculatePrize(int redHit, int blueHit);
}
