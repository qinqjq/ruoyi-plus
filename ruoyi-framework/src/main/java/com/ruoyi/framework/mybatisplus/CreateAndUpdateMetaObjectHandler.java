package com.ruoyi.framework.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * MP注入处理器
 * @author Lion Li
 * @date 2021/4/25
 */
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		//根据属性名字设置要填充的值
		if (metaObject.hasGetter("createTime")) {
			Class<?> createTime = metaObject.getGetterType("createTime");
			String typeName = createTime.getTypeName();
			if (typeName.endsWith("LocalDateTime")) {
				this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
			} else if (typeName.endsWith("Date")) {
				this.setFieldValByName("createTime", new Date(), metaObject);
			}
		}
		if (metaObject.hasGetter("createBy")) {
			if (metaObject.getValue("createBy") == null) {
				this.setFieldValByName("createBy", SecurityUtils.getUsername(), metaObject);
			}
		}
		updateFill(metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		if (metaObject.hasGetter("updateBy")) {
			if (metaObject.getValue("updateBy") == null) {
				this.setFieldValByName("updateBy", SecurityUtils.getUsername(), metaObject);
			}
		}
		if (metaObject.hasGetter("updateTime")) {
			Class<?> updateTime = metaObject.getGetterType("updateTime");
			String typeName = updateTime.getTypeName();
			if (typeName.endsWith("LocalDateTime")) {
				this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
			} else if (typeName.endsWith("Date")) {
				this.setFieldValByName("updateTime", new Date(), metaObject);
			}
		}
	}

}
