package com.tomo.dao.impl;

import com.tomo.dao.IconDao;
import com.tomo.dao.impl.common.BaseDaoImpl;
import com.tomo.entity.Icon;

public class IconDaoImpl extends BaseDaoImpl<Icon> implements IconDao {
    @Override
    protected String getPKName() {
        return "iconid";
    }
}
