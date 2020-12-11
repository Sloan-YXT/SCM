/**
 * 
 */
package com.tomo.dao;

import com.tomo.dao.common.BaseDao;
import com.tomo.entity.Look;
import com.tomo.entity.common.PageModel;

public interface LookDao extends BaseDao<Look> {
	public void delete(String username, int shopid);

	public PageModel<Look> findByPager(String string1, String string2,
			int pageSize, int pageNo);
}
