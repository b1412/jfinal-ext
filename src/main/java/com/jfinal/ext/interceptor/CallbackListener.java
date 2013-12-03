package com.jfinal.ext.interceptor;

import com.jfinal.plugin.activerecord.Model;

public interface CallbackListener {

    void beforeSave(Model<?> m);

    void afterSave(Model<?> m);

    void beforeUpdate(Model<?> m);

    void afterUpdate(Model<?> m);

    void beforeDelete(Model<?> m);

    void afterDelete(Model<?> m);
}
