package com.shsxt.crm.dao.provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shsxt.crm.dto.SaleChanceQuery;

public class SaleChanceProvider {
	
	private static Logger logger = LoggerFactory.getLogger(SaleChanceProvider.class);
	private static final String COLUMNS = "t.id, t.customer_id, t.customer_name, t.overview, t.link_man, t.link_phone, "
			+ " t.create_man, t.create_date, t.assign_man,t.assign_time,t.state, t.cgjl, t.description, t.chance_source, t.dev_result";
	
	public String selectForPage(final SaleChanceQuery query) {
		
		String sql = new SQL(){{
			
			SELECT(COLUMNS);
			FROM("t_sale_chance t");
			WHERE("is_valid = 1");
			if (StringUtils.isNoneBlank(query.getCustomerName())) {
				AND().WHERE("customer_name like '%"+ query.getCustomerName() +"%'");
			}
			if (StringUtils.isNoneBlank(query.getOverview())) {
				AND().WHERE("overview like '%"+ query.getOverview() +"%'");
			}
			if (StringUtils.isNoneBlank(query.getCreateMan())) {
				AND().WHERE("create_man like '%"+ query.getCreateMan() +"%'");
			}
			if (query.getState() != null) {
				AND().WHERE("state = #{state}");
			}
			if (query.getDevResult() != null) {
				AND().WHERE("dev_result = #{devResult}");
			}


		}}.toString();
		
		logger.debug("打印的sql是：{}", sql);
		return sql;
	}
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public String findById(final Integer id) {
		
		String sql = new SQL(){{
			
			SELECT(COLUMNS);
			FROM("t_sale_chance t");
			WHERE("is_valid = 1 and id = #{id}");
			
		}}.toString();
		logger.debug("打印的sql是：{}", sql);
		return sql;
	}
	
}
