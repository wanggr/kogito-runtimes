/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.grafana.dmn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.kie.kogito.grafana.model.panel.common.YAxis;

public class LocalDateType extends AbstractDmnType {

    public LocalDateType() {
        super(LocalDate.class, "date");
        addFunctions(new TreeMap<>());
        List<YAxis> yaxes = new ArrayList<>();
        yaxes.add(new YAxis("dateTimeAsIso", true));
        yaxes.add(new YAxis("ms", false));
        setYAxes(yaxes);
    }
}