package com.nlnl.lotto.exception;

/**
 * 彩票业务异常（非受检异常，配合全局异常处理器使用）
 *
 * @author Tondy
 */
public class LottoException extends RuntimeException {

    public LottoException() {
        super();
    }

    public LottoException(String message) {
        super(message);
    }

    public LottoException(String message, Throwable cause) {
        super(message, cause);
    }
}
