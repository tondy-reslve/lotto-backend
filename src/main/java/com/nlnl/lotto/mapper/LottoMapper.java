package com.nlnl.lotto.mapper;

import com.nlnl.lotto.domain.LottoInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LottoMapper {

    /**
     * 保存双色球投注信息
     */
    void saveUnionLotto(LottoInfo info);

    /**
     * 根据id查询双色球信息
     */
    LottoInfo getUnionLottoById(String id);

    /**
     * 修改双色球中奖信息
     */
    void updateUnionLotto(LottoInfo info);


    /**
     * 查询双色球往期的购买
     */
    List<LottoInfo> getHistoryUnionLotto();

    /**
     * 保存大乐透投注信息
     */
    void saveSuperLotto(LottoInfo info);

    /**
     * 根据id查询大乐透信息
     */
    LottoInfo getSuperLottoById(String id);

    /**
     * 修改大乐透中奖信息
     */
    void updateSuperLotto(LottoInfo info);

    /**
     * 查询大乐透往期的购买
     */
    List<LottoInfo> getHistorySuperLotto();

    /**
     * 根据id查询六合彩信息
     */
    LottoInfo getMarkSixById(String id);

    /**
     * 查询六合彩往期的购买
     */
    List<LottoInfo> getHistoryMarkSix();

    /**
     * 保存六合彩投注信息
     */
    void saveMarkSix(LottoInfo info);

    /**
     * 修改六合彩中奖信息
     */
    void updateMarkSix(LottoInfo lottoInfo);

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
