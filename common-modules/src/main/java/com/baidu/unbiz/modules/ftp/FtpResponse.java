package com.baidu.unbiz.modules.ftp;

import com.baidu.unbiz.common.StringPool;
import com.baidu.unbiz.common.lang.StringableSupport;
import com.baidu.unbiz.modules.ftp.exception.InvalidReplyException;

/**
 * FtpResponse
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午5:53:08
 */
public class FtpResponse extends StringableSupport implements StringPool.Symbol {

    private String replyCode;

    private String replyText;

    private String rawReply;

    private String[] data;

    public FtpResponse(String replyCode, String replyText) throws InvalidReplyException {
        this.replyCode = replyCode;
        this.replyText = replyText;
        rawReply = replyCode + SPACE + replyText;
        validateCode(replyCode);
    }

    public FtpResponse(String replyCode, String replyText, String[] data) throws InvalidReplyException {
        this.replyCode = replyCode;
        this.replyText = replyText;
        this.data = data;
        validateCode(replyCode);
    }

    public FtpResponse(String reply) throws InvalidReplyException {
        this.rawReply = reply.trim();
        replyCode = rawReply.substring(0, 3);
        replyText = (rawReply.length() > 3) ? rawReply.substring(4) : EMPTY;

        validateCode(replyCode);
    }

    public String getRawReply() {
        return rawReply;
    }

    public String getReplyCode() {
        return replyCode;
    }

    public String getReplyText() {
        return replyText;
    }

    public String[] getReplyData() {
        return data;
    }

    private void validateCode(String code) throws InvalidReplyException {
        try {
            Integer.parseInt(code);
        } catch (NumberFormatException ex) {
            throw new InvalidReplyException("Invalid reply code '{}'", code);
        }
    }

}
