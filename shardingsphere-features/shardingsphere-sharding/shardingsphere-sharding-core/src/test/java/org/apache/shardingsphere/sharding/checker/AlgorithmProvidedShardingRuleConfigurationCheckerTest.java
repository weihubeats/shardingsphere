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

package org.apache.shardingsphere.sharding.checker;

import org.apache.shardingsphere.infra.config.checker.RuleConfigurationChecker;
import org.apache.shardingsphere.infra.config.checker.RuleConfigurationCheckerFactory;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingAutoTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.ShardingStrategyConfiguration;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class AlgorithmProvidedShardingRuleConfigurationCheckerTest {
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void assertCheckPass() {
        AlgorithmProvidedShardingRuleConfiguration config = mock(AlgorithmProvidedShardingRuleConfiguration.class);
        when(config.getTables()).thenReturn(Collections.singleton(mock(ShardingTableRuleConfiguration.class)));
        when(config.getAutoTables()).thenReturn(Collections.singleton(mock(ShardingAutoTableRuleConfiguration.class)));
        when(config.getDefaultTableShardingStrategy()).thenReturn(mock(ShardingStrategyConfiguration.class));
        Optional<RuleConfigurationChecker> checker = RuleConfigurationCheckerFactory.newInstance(config);
        assertTrue(checker.isPresent());
        assertThat(checker.get(), instanceOf(AlgorithmProvidedShardingRuleConfigurationChecker.class));
        checker.get().check("test", config);
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test(expected = IllegalStateException.class)
    public void assertCheckNoPass() {
        AlgorithmProvidedShardingRuleConfiguration config = mock(AlgorithmProvidedShardingRuleConfiguration.class);
        when(config.getTables()).thenReturn(Collections.emptyList());
        when(config.getAutoTables()).thenReturn(Collections.emptyList());
        Optional<RuleConfigurationChecker> checker = RuleConfigurationCheckerFactory.newInstance(config);
        assertTrue(checker.isPresent());
        assertThat(checker.get(), instanceOf(AlgorithmProvidedShardingRuleConfigurationChecker.class));
        checker.get().check("test", config);
    }
}
