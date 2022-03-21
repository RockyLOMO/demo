package com.cowell.service.biz;

//todo
//client ws -> server
//server ack -> client
public class WebSocketKeepalive implements Keepalive {
    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean sendAck() {
        return false;
    }
}
