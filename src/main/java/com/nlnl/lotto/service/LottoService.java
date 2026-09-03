package com.nlnl.lotto.service;

import com.nlnl.lotto.domain.LottoInfo;

import java.util.List;

public interface LottoService {

    /**
     * 获取双色球投注号码
     */
    LottoInfo getUnionLotto(List<Integer> winNos);

    /**
     * 添加双色球中奖信息
     */
    String updateUnionLotto(LottoInfo info);

    /**
     * 查询所有双色球信息
     */
    List<LottoInfo> getHistoryUnionLotto();

    /**
     * 获取大乐透投注号码
     */
    LottoInfo getSuperLotto(List<Integer> winNos);

    /**
     * 添加大乐透中奖信息
     */
    String updateSuperLotto(LottoInfo info);

    /**
     * 查询所有大乐透信息
     */
    List<LottoInfo> getHistorySuperLotto();

    /**
     * 获取六合彩投注号码
     */
    LottoInfo getMarkSix(List<Integer> winNos);

    /**
     * 添加六合彩中奖信息
     */
    String updateMarkSix(LottoInfo info);

    /**
     * 查询所有六合彩信息
     */
    List<LottoInfo> getHistoryMarkSix();

    /**
     * 获取双色球头奖号码
     */
    LottoInfo getUnionLottoFirstPrize();

    /**
     * 获取大乐透头奖号码
     */
    LottoInfo getSuperLottoFirstPrize();

    /**
     * 获取六合彩头奖号码
     */
    LottoInfo getMarkSixFirstPrize();
}
