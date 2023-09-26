package org.rx.demo.gjmq.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rx.core.Extends;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Extends {
    String name;
    String value;
}
