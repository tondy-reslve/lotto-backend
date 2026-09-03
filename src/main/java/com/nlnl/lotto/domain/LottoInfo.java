package com.nlnl.lotto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LottoInfo {

    //投注id
    private String id;
    //投注号码
    private String bettingNumber;
    //开奖期数
    private String numberOfPeriods;
    //开奖号码
    private String winningNumbers;
    //创建日期
    private Date createDate;
    //备注
    private String message;

}
