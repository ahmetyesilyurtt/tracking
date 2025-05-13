package com.migros.tracking.common.config.async;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadExecutorProperties {

    private String name;

    private int corePoolSize;

    private int maxPoolSize;

    private int queueCapacity;
}
