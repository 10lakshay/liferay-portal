/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.messageboards.service.impl;

import com.liferay.message.boards.kernel.model.MBThread;
import com.liferay.message.boards.kernel.model.MBThreadFlag;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portlet.messageboards.service.base.MBThreadFlagLocalServiceBaseImpl;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @deprecated As of 7.0.0, replaced by {@link
 *             com.liferay.message.boards.service.impl.MBThreadFlagLocalServiceImpl}
 */
@Deprecated
public class MBThreadFlagLocalServiceImpl
	extends MBThreadFlagLocalServiceBaseImpl {

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MBThreadFlag addThreadFlag(
			long userId, MBThread thread, ServiceContext serviceContext)
		throws PortalException {

		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

	@Override
	public void deleteThreadFlag(long threadFlagId) throws PortalException {
		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public void deleteThreadFlag(MBThreadFlag threadFlag) {
		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

	@Override
	public void deleteThreadFlagsByThreadId(long threadId) {
		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

	@Override
	public void deleteThreadFlagsByUserId(long userId) {
		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

	@Override
	public MBThreadFlag getThreadFlag(long userId, MBThread thread)
		throws PortalException {

		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

	@Override
	public boolean hasThreadFlag(long userId, MBThread thread)
		throws PortalException {

		throw new UnsupportedOperationException(
			"This class is deprecated and replaced by " +
				"com.liferay.message.boards.service.impl." +
					"MBThreadFlagLocalServiceImpl");
	}

}