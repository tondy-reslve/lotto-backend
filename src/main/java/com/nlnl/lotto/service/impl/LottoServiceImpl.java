package com.nlnl.lotto.service.impl;

import com.nlnl.lotto.common.Constant;
import com.nlnl.lotto.common.LottoType;
import com.nlnl.lotto.domain.LottoInfo;
import com.nlnl.lotto.mapper.LottoMapper;
import com.nlnl.lotto.service.LottoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

/**
 * @author Tondy (Refactored by AI)
 * JDK 17+ Optimized Version with Time Travel Ritual
 */
@Slf4j
@Service
public class LottoServiceImpl implements LottoService {

    @Resource
    private LottoMapper lottoMapper;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final RandomGenerator rng = RandomGenerator.of("L128X256MixRandom");

    private static final int[] DAYS_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @Override
    public LottoInfo getUnionLotto(List<Integer> winNos) {
        return processGenerate(LottoType.UNION_LOTTO, winNos);
    }

    @Override
    public LottoInfo getSuperLotto(List<Integer> winNos) {
        return processGenerate(LottoType.SUPER_LOTTO, winNos);
    }

    @Override
    public LottoInfo getMarkSix(List<Integer> winNos) {
        return processGenerate(LottoType.MARK_SIX, winNos);
    }


    @Override
    public String updateUnionLotto(LottoInfo info) {
        return processUpdate(LottoType.UNION_LOTTO, info, lottoMapper.getUnionLottoById(info.getId()));
    }

    @Override
    public String updateSuperLotto(LottoInfo info) {
        return processUpdate(LottoType.SUPER_LOTTO, info, lottoMapper.getSuperLottoById(info.getId()));
    }

    @Override
    public String updateMarkSix(LottoInfo info) {
        return processUpdate(LottoType.MARK_SIX, info, lottoMapper.getMarkSixById(info.getId()));
    }

    // ================= 3. 歷史查詢 =================

    @Override public List<LottoInfo> getHistoryUnionLotto() { return lottoMapper.getHistoryUnionLotto(); }
    @Override public List<LottoInfo> getHistorySuperLotto() { return lottoMapper.getHistorySuperLotto(); }
    @Override public List<LottoInfo> getHistoryMarkSix() { return lottoMapper.getHistoryMarkSix(); }

    // ================= 4. 頭獎查詢 =================

    @Override public LottoInfo getUnionLottoFirstPrize() { return getFirstPrize(LottoType.UNION_LOTTO, lottoMapper.getUnionLottoFirstPrize()); }
    @Override public LottoInfo getSuperLottoFirstPrize() { return getFirstPrize(LottoType.SUPER_LOTTO, lottoMapper.getSuperLottoFirstPrize()); }
    @Override public LottoInfo getMarkSixFirstPrize() { return getFirstPrize(LottoType.MARK_SIX, lottoMapper.getMarkSixFirstPrize()); }


    // =======================================================
    //                     核心業務邏輯區
    // =======================================================

    /**
     * 【時光穿梭儀式】核心生成方法
     * 邏輯：找出輸入的最大日期，從公元1年1月1日開始遍歷。
     * 只有經過真實存在的歷史日期，隨機引擎才轉動。
     */
    private LottoInfo processGenerate(LottoType type, List<Integer> winNos) {
        log.info("{} 時光穿梭投注儀式啟動...", type.code);
        LottoInfo info = new LottoInfo();

        // 1. 鎖定時間終點 (Max Date)
        int maxDate = 0;
        Set<Integer> targetDates = new HashSet<>();

        for (Integer num : winNos) {
            if (num != null) {
                targetDates.add(num);
                if (num > maxDate) maxDate = num;
            }
        }

        log.info("鎖定時間終點: {}，準備從 00010101 開始重啟時間線...", maxDate);

        // 存放 "日期 -> 號碼" 的結果
        Map<Integer, String> resultMap = new HashMap<>();
        long totalValidDays = 0;

        // 2. 如果最大日期是有效範圍 (大於 10101)，則啟動時光穿梭
        if (maxDate >= 10101) {
            for (int currentNum = 1; currentNum <= maxDate; currentNum++) {

                // 極速校驗：如果是真實日期 (包含閏年判斷)
                if (isFastValidDate(currentNum)) {

                    rng.nextInt(); // 🔥 命運轉動：消耗一次隨機數
                    totalValidDays++;

                    // 命運交匯：如果今天是用戶輸入的日子，生成號碼
                    if (targetDates.contains(currentNum)) {
                        String bet = generateSingleBet(type);
                        resultMap.put(currentNum, bet);
                        log.info(">>> [歷史第 {} 天] 抵達紀念日 {} -> 生成號碼: {}", totalValidDays, currentNum, bet);
                    }
                }
            }
        } else {
            log.info("輸入日期過小或非日期格式，跳過時光儀式，直接隨機...");
        }

        // 3. 組裝結果 (按用戶輸入順序)
        List<String> orderedResults = new ArrayList<>();
        for (int i = 0; i < winNos.size(); i++) {
            Integer inputNum = winNos.get(i);
            String bet;

            if (resultMap.containsKey(inputNum)) {
                bet = resultMap.get(inputNum);
            } else {
                // 如果 Map 裡沒有 (說明輸入的不是有效日期，例如 888 或 20260230)
                // 進行普通隨機生成
                bet = generateSingleBet(type);
                log.info("第 {} 注 (編號 {}): 非有效歷史日期，普通隨機 -> {}", (i + 1), inputNum, bet);
            }
            orderedResults.add(bet);
        }

        // 4. 保存與返回
        info.setBettingNumber(String.join(Constant.SEMICOLON, orderedResults));
        info.setId(LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        info.setMessage(Constant.GET_MORE_MONEY);

        saveToDb(type, info);

        log.info("儀式完成，共歷經 {} 個歷史日夜。", totalValidDays);
        log.info(Constant.GET_MORE_MONEY);
        return info;
    }

    /**
     * 【單注生成器】根據 LottoType 規則生成號碼
     */
    private String generateSingleBet(LottoType type) {
        String red = pickBalls(type.redTotal, type.redCount);
        if (type.blueCount > 0) {
            String blue = pickBalls(type.blueTotal, type.blueCount);
            // 注意：你的原代碼雙色球/大樂透都是用 + 號連接，這裡保持一致
            // 如果是六合彩 (blueCount=0)，這裡不會執行
            return red + " + " + blue;
        }
        return red;
    }

    /**
     * 【選球機】隨機選出 count 個球，自動排序、補零
     */
    private String pickBalls(int total, int count) {
        Set<Integer> balls = new TreeSet<>(); // TreeSet 自動排序
        while (balls.size() < count) {
            balls.add(rng.nextInt(total) + 1);
        }
        return balls.stream()
                .map(n -> String.format("%02d", n))
                .collect(Collectors.joining(" "));
    }

    /**
     * 【終極版日期校驗】
     * 純數學運算，支持閏年 (Leap Year) 判斷，性能極高
     */
    private boolean isFastValidDate(int dateNum) {
        if (dateNum < 10101) return false;

        int day = dateNum % 100;
        if (day < 1 || day > 31) return false;

        int month = (dateNum / 100) % 100;
        if (month < 1 || month > 12) return false;

        int year = dateNum / 10000;

        if (month == 2) {
            // 使用 Java 標準庫判斷閏年 (底層是高效位運算)
            boolean isLeap = Year.isLeap(year);
            return day <= (isLeap ? 29 : 28);
        } else {
            return day <= DAYS_IN_MONTH[month];
        }
    }

    /**
     * 【統一更新邏輯】計算中獎金額
     */
    private String processUpdate(LottoType type, LottoInfo inputInfo, LottoInfo dbInfo) {
        if (dbInfo == null) return null;

        dbInfo.setNumberOfPeriods(inputInfo.getNumberOfPeriods());
        dbInfo.setWinningNumbers(inputInfo.getWinningNumbers());

        // 使用 Record 解析開獎號碼 (Java 16+)
        ParsedResult winRes = parseNumbers(inputInfo.getWinningNumbers());

        long totalPrize = 0;
        String[] bets = dbInfo.getBettingNumber().split(Constant.SEMICOLON);

        for (String bet : bets) {
            ParsedResult betRes = parseNumbers(bet);

            // 紅球命中數
            long redHit = betRes.reds.stream().filter(winRes.reds::contains).count();

            // 藍球命中數 (六合彩特別處理：特別號算藍球)
            long blueHit;
            if (type == LottoType.MARK_SIX) {
                blueHit = betRes.reds.stream().filter(winRes.blues::contains).count();
            } else {
                blueHit = betRes.blues.stream().filter(winRes.blues::contains).count();
            }

            // 調用 Enum 計算
            totalPrize += type.calculatePrize((int) redHit, (int) blueHit);
        }

        String msg = (totalPrize == 0) ? Constant.NOT_WIN : "中獎" + String.format("%,d", totalPrize) + "元,爽!";
        dbInfo.setMessage(msg);

        switch (type) {
            case UNION_LOTTO -> lottoMapper.updateUnionLotto(dbInfo);
            case SUPER_LOTTO -> lottoMapper.updateSuperLotto(dbInfo);
            case MARK_SIX -> lottoMapper.updateMarkSix(dbInfo);
        }
        return msg;
    }

    // 保存邏輯封裝
    private void saveToDb(LottoType type, LottoInfo info) {
        switch (type) {
            case UNION_LOTTO -> lottoMapper.saveUnionLotto(info);
            case SUPER_LOTTO -> lottoMapper.saveSuperLotto(info);
            case MARK_SIX -> lottoMapper.saveMarkSix(info);
        }
        kafkaTemplate.send("lotto-topic", "New Ticket Generated: " + info.getId());
        log.info("✅ 已推送 Kafka 隊列: {}", info.getId());
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "lotto-topic", groupId = "lotto-group")
    public void consumeLottoEvent(String message) {
        log.info("📥 成功從 Kafka 隊列消費訊息: {}", message);
    }

    // 頭獎邏輯封裝
    private LottoInfo getFirstPrize(LottoType type, LottoInfo info) {
        if (info == null) return null;
        info.setId(LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
        info.setMessage(Constant.GET_MORE_MONEY);
        saveToDb(type, info);
        log.info("{} 頭獎號碼: {}", type.code, info.getBettingNumber());
        return info;
    }

    // 簡單的數據結構 (Record)
    record ParsedResult(List<String> reds, List<String> blues) {}

    private ParsedResult parseNumbers(String numStr) {
        if (StringUtils.isBlank(numStr)) return new ParsedResult(Collections.emptyList(), Collections.emptyList());
        String[] parts = numStr.split("\\+");
        List<String> r = Arrays.asList(parts[0].trim().split("\\s+"));
        List<String> b = (parts.length > 1) ? Arrays.asList(parts[1].trim().split("\\s+")) : Collections.emptyList();
        return new ParsedResult(r, b);
    }
}