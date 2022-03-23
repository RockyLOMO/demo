package com.cowell.service.keepalive;

import lombok.Data;

import java.io.Serializable;

@Data
public class KAEntity implements Serializable {
    long id;
    long maxMissDuration;
    long lastAckTime;
}
