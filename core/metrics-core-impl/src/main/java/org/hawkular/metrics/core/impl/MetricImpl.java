/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hawkular.metrics.core.api.DataPoint;
import org.hawkular.metrics.core.api.Metric;
import org.hawkular.metrics.core.api.MetricId;
import org.hawkular.metrics.core.api.MetricType;

/**
 * @author jsanda
 */
public class MetricImpl<T extends DataPoint> implements Metric<T> {

    private String tenantId;
    private MetricType type;
    private MetricId id;
    private Map<String, String> tags = Collections.emptyMap();
    private Integer dataRetention;
    private List<T> dataPoints = new ArrayList<>();

    public MetricImpl(String tenantId, MetricType type, MetricId id) {
        this.tenantId = tenantId;
        this.type = type;
        this.id = id;
    }

    public MetricImpl(String tenantId, MetricType type, MetricId id, Map<String, String> tags, Integer dataRetention) {
        this.tenantId = tenantId;
        this.type = type;
        this.id = id;
        this.tags = tags;
        // If the data_retention column is not set, the driver returns zero instead of null.
        // We are (at least for now) using null to indicate that the metric does not have
        // the data retention set.
        if (dataRetention == null || dataRetention == 0) {
            this.dataRetention = null;
        } else {
            this.dataRetention = dataRetention;
        }
    }

    public MetricImpl(String tenantId, MetricType type, MetricId id, List<T> dataPoints) {
        this.tenantId = tenantId;
        this.type = type;
        this.id = id;
        this.dataPoints = dataPoints;
    }

    @Override
    public MetricType getType() {
        return type;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public MetricId getId() {
        return id;
    }

    @Override
    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public Integer getDataRetention() {
        return dataRetention;
    }

    @Override
    public List<T> getDataPoints() {
        return dataPoints;
    }

    public MetricImpl<T> addDataPoint(T dataPoint) {
        dataPoints.add(dataPoint);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricImpl<?> metric = (MetricImpl<?>) o;
        return Objects.equals(tenantId, metric.tenantId) &&
                Objects.equals(type, metric.type) &&
                Objects.equals(id, metric.id) &&
                Objects.equals(tags, metric.tags) &&
                Objects.equals(dataRetention, metric.dataRetention);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, type, id, tags, dataRetention);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper("MetricImpl")
                .add("tenantId", tenantId)
                .add("id", id)
                .add("tags", tags)
                .add("dataRetention", dataRetention)
                .toString();
    }
}
