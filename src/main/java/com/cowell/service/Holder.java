package com.cowell.service;

import org.rx.io.ShardingEntityDatabase;

public interface Holder {
    long DEFAULT_MISS_DURATION = 15000 * 3;
    ShardingEntityDatabase EDB = null;
}
