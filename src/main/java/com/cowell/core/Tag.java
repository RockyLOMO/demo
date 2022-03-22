package com.cowell.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.rx.core.Extends;

@Data
@AllArgsConstructor
public class Tag implements Extends {
    String name;
    String value;
}
