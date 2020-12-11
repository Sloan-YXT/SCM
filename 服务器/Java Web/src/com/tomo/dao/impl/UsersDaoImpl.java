/**
 * 
 */
package com.tomo.dao.impl;

import com.tomo.dao.UsersDao;
import com.tomo.dao.impl.common.BaseDaoImpl;
import com.tomo.entity.Users;

public class UsersDaoImpl extends BaseDaoImpl<Users> implements UsersDao {

	@Override
	protected String getPKName() {
		return "userid";
	}
}
