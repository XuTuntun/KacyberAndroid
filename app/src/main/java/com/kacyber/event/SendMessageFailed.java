package com.kacyber.event;

/**
 * Created by guofuming on 6/11/2016.
 */

public class SendMessageFailed {
    public long messageId;

    public SendMessageFailed(long messageIdInput) {
        messageId = messageIdInput;
    }
}
