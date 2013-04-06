package com.jfinal.ext.plugin.sqlinxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class SqlGroup {
    @XmlAttribute
    String name;
    @XmlElement(name = "sql")
    List<SqlItem> sqlItems = new ArrayList<SqlItem>();

    void addSqlgroup(SqlItem sqlGroup) {
        sqlItems.add(sqlGroup);
    }

}
