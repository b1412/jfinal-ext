/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.kit;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class RecordKit {

    protected final static Logger logger = Logger.getLogger(RecordKit.class);

    public static Model<?> toModel(Class<? extends Model<?>> clazz, Record record) {
        Model<?> model = null;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return model;
        }
        for (String columnName : record.getColumnNames()) {
            model.set(columnName, record.get("columnName"));
        }
        return model;
    }
}
