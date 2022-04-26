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

package org.apache.shardingsphere.sharding.rewrite.token.generator.impl;

import lombok.Setter;
import org.apache.shardingsphere.infra.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.infra.binder.type.IndexAvailable;
import org.apache.shardingsphere.infra.metadata.schema.ShardingSphereSchema;
import org.apache.shardingsphere.infra.rewrite.sql.token.generator.CollectionSQLTokenGenerator;
import org.apache.shardingsphere.infra.rewrite.sql.token.generator.aware.SchemaMetaDataAware;
import org.apache.shardingsphere.sharding.rewrite.token.pojo.IndexToken;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sharding.rule.aware.ShardingRuleAware;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.index.IndexSegment;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Index token generator.
 */
@Setter
public final class IndexTokenGenerator implements CollectionSQLTokenGenerator<SQLStatementContext<?>>, ShardingRuleAware, SchemaMetaDataAware {
    
    private ShardingRule shardingRule;
    
    private Map<String, ShardingSphereSchema> schemas;
    
    private String databaseName;
    
    @Override
    public boolean isGenerateSQLToken(final SQLStatementContext<?> sqlStatementContext) {
        return sqlStatementContext instanceof IndexAvailable && !((IndexAvailable) sqlStatementContext).getIndexes().isEmpty();
    }
    
    @Override
    public Collection<IndexToken> generateSQLTokens(final SQLStatementContext<?> sqlStatementContext) {
        Collection<IndexToken> result = new LinkedList<>();
        String defaultSchema = sqlStatementContext.getDatabaseType().getDefaultSchema(databaseName);
        ShardingSphereSchema schema = sqlStatementContext.getTablesContext().getSchemaName().map(schemas::get).orElse(schemas.get(defaultSchema));
        if (sqlStatementContext instanceof IndexAvailable) {
            for (IndexSegment each : ((IndexAvailable) sqlStatementContext).getIndexes()) {
                result.add(new IndexToken(each.getStartIndex(), each.getStopIndex(), each.getIdentifier(), sqlStatementContext, shardingRule, schema));
            }
        }
        return result;
    }
}
