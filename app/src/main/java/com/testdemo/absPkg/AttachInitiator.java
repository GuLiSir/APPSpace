package com.testdemo.absPkg;

/**
 * 连接发起者
 */
public interface AttachInitiator {
    /**
     * 请求绑定指定的item
     * @param recipient
     *
     */
    void requestAttach(AttachInitiator recipient);

    /**
     * 请求解绑指定的item
     * @param recipient
     */
    void requestDetach(AttachInitiator recipient);

    /**
     * 收到绑定请求
     * @param initiator
     */
    void onAttachRequest(AttachInitiator initiator,int distance);

    /**
     * 收到断开绑定请求
     * @param initiator
     */
    void onDetachRequest(AttachInitiator initiator);

}
