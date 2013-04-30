package com.jfinal.ext.plugin.sqlinxml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
class SqlGroup {
    @XmlAttribute
    String name;
    @XmlElement(name = "sql")
    List<SqlItem> sqlItems = Lists.newArrayList();

    void addSqlgroup(SqlItem sqlGroup) {
        sqlItems.add(sqlGroup);
    }

}
