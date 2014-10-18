package com.jfinal.ext.render.dwz;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
@TableBind(tableName = "user",configName = "another")
public class User extends Model<User> {

}
