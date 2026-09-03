package com.nlnl.lotto.controller;

import com.nlnl.lotto.common.Constant;
import com.nlnl.lotto.common.Result;
import com.nlnl.lotto.domain.LottoInfo;
import com.nlnl.lotto.exception.LottoException;
import com.nlnl.lotto.service.LottoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author Tondy
 */
@Slf4j
@RestController
@RequestMapping("/lotto")
public class LottoController {

    @Resource
    private LottoService lottoService;

    // ==================== 双色球 ====================

    /**
     * 获取双色球投注号码
     */
    @PostMapping("/get_union_lotto")
    public Result<LottoInfo> getUnionLotto(@RequestBody List<Integer> winNos) throws LottoException {
        if (CollectionUtils.isEmpty(winNos)) {
            throw new LottoException(Constant.PARAM_NOT_NULL);
        }
        return Result.success(lottoService.getUnionLotto(winNos));
    }

    /**
     * 添加双色球中奖信息
     */
    @PostMapping("/update_union_lotto")
    public Result<String> updateUnionLotto(@RequestBody LottoInfo info) {
        String msg = lottoService.updateUnionLotto(info);
        if (StringUtils.isBlank(msg)) {
            return Result.fail(Constant.UPDATE_FAIL);
        }
        return Result.success(msg);
    }

    /**
     * 获取双色球历史信息
     */
    @GetMapping("/get_history_union_lotto")
    public Result<List<LottoInfo>> getHistoryUnionLotto() {
        return Result.success(lottoService.getHistoryUnionLotto());
    }

    /**
     * 获取双色球头奖号码
     */
    @GetMapping("/get_union_lotto_first_prize")
    public Result<LottoInfo> getUnionLottoFirstPrize() {
        return Result.success(lottoService.getUnionLottoFirstPrize());
    }

    // ==================== 大乐透 ====================

    /**
     * 获取大乐透投注号码
     */
    @PostMapping("/get_super_lotto")
    public Result<LottoInfo> getSuperLotto(@RequestBody List<Integer> winNos) throws LottoException {
        if (CollectionUtils.isEmpty(winNos)) {
            throw new LottoException(Constant.PARAM_NOT_NULL);
        }
        return Result.success(lottoService.getSuperLotto(winNos));
    }

    /**
     * 添加大乐透中奖信息
     */
    @PostMapping("/update_super_lotto")
    public Result<String> updateSuperLotto(@RequestBody LottoInfo info) {
        String msg = lottoService.updateSuperLotto(info);
        if (StringUtils.isBlank(msg)) {
            return Result.fail(Constant.UPDATE_FAIL);
        }
        return Result.success(msg);
    }

    /**
     * 获取大乐透历史信息
     */
    @GetMapping("/get_history_super_lotto")
    public Result<List<LottoInfo>> getHistorySuperLotto() {
        return Result.success(lottoService.getHistorySuperLotto());
    }

    /**
     * 获取大乐透头奖号码
     */
    @GetMapping("/get_super_lotto_first_prize")
    public Result<LottoInfo> getSuperLottoFirstPrize() {
        return Result.success(lottoService.getSuperLottoFirstPrize());
    }

    // ==================== 六合彩 ====================

    /**
     * 获取六合彩投注号码
     */
    @PostMapping("/get_mark_six")
    public Result<LottoInfo> getMarkSix(@RequestBody List<Integer> winNos) throws LottoException {
        if (CollectionUtils.isEmpty(winNos)) {
            throw new LottoException(Constant.PARAM_NOT_NULL);
        }
        return Result.success(lottoService.getMarkSix(winNos));
    }

    /**
     * 添加六合彩中奖信息
     */
    @PostMapping("/update_mark_six")
    public Result<String> updateMarkSix(@RequestBody LottoInfo info) {
        String msg = lottoService.updateMarkSix(info);
        if (StringUtils.isBlank(msg)) {
            return Result.fail(Constant.UPDATE_FAIL);
        }
        return Result.success(msg);
    }

    /**
     * 获取六合彩历史信息
     */
    @GetMapping("/get_history_mark_six")
    public Result<List<LottoInfo>> getHistoryMarkSix() {
        return Result.success(lottoService.getHistoryMarkSix());
    }

    /**
     * 获取六合彩头奖号码
     */
    @GetMapping("/get_mark_six_first_prize")
    public Result<LottoInfo> getMarkSixFirstPrize() {
        return Result.success(lottoService.getMarkSixFirstPrize());
    }
}
