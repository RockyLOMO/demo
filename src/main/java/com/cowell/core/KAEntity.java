package com.cowell.core;

import java.io.Serializable;

public interface KAEntity extends Serializable {
    long getId();

    long getTtl();

    void setTtl(long ttl);
}
