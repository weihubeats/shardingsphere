/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.integration.data.pipline.util;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Execute util.
 */
@RequiredArgsConstructor
public final class ExecuteUtil {
    
    private final Executor executor;
    
    private final int retryCount;
    
    private final long waitMs;
    
    /**
     * Execute.
     *
     * @return execute result
     */
    public boolean execute() {
        int count = 0;
        while (!executor.execute() && retryCount > count) {
            try {
                TimeUnit.MILLISECONDS.sleep(waitMs);
            } catch (final InterruptedException ignored) {
            }
            count++;
        }
        return retryCount > count;
    }
}
