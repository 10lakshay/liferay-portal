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

package com.liferay.change.tracking.service.impl;

import com.liferay.change.tracking.exception.CTCollectionNameException;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.change.tracking.service.base.CTCollectionLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Daniel Kocsis
 */
public class CTCollectionLocalServiceImpl
	extends CTCollectionLocalServiceBaseImpl {

	@Override
	public CTCollection addCTCollection(
			long companyId, long userId, String name, String description,
			ServiceContext serviceContext)
		throws PortalException {

		_validate(name);

		long id = counterLocalService.increment();

		CTCollection ctCollection = ctCollectionPersistence.create(id);

		User user = userLocalService.getUser(userId);
		Date now = new Date();

		ctCollection.setCompanyId(companyId);
		ctCollection.setUserId(user.getUserId());
		ctCollection.setUserName(user.getFullName());
		ctCollection.setCreateDate(serviceContext.getCreateDate(now));
		ctCollection.setModifiedDate(serviceContext.getModifiedDate(now));
		ctCollection.setName(name);
		ctCollection.setDescription(description);
		ctCollection.setStatus(WorkflowConstants.STATUS_APPROVED);

		return ctCollectionPersistence.update(ctCollection);
	}

	@Override
	public void deleteCompanyCTCollections(long companyId) {
		List<CTCollection> ctCollections =
			ctCollectionPersistence.findByCompanyId(companyId);

		for (CTCollection ctCollection : ctCollections) {
			ctCollectionLocalService.deleteCTCollection(ctCollection);
		}
	}

	@Override
	public CTCollection deleteCTCollection(CTCollection ctCollection) {
		List<CTEntry> ctEntries = ctCollectionPersistence.getCTEntries(
			ctCollection.getCtCollectionId());

		for (CTEntry ctEntry : ctEntries) {
			int collectionsSize = ctEntryPersistence.getCTCollectionsSize(
				ctCollection.getCtCollectionId());

			if (collectionsSize > 1) {
				continue;
			}

			ctEntryLocalService.deleteCTEntry(ctEntry);
		}

		ctCollectionPersistence.remove(ctCollection);
		ctCollectionPersistence.clearCTEntries(
			ctCollection.getCtCollectionId());

		return ctCollection;
	}

	@Override
	public CTCollection fetchCTCollection(long companyId, String name) {
		return ctCollectionPersistence.fetchByC_N(companyId, name);
	}

	@Override
	public List<CTCollection> getCTCollections(long companyId) {
		return ctCollectionPersistence.findByCompanyId(companyId);
	}

	@Override
	public CTCollection updateStatus(
			long userId, CTCollection collection, int status,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		Date now = new Date();

		Date modifiedDate = serviceContext.getModifiedDate(now);

		collection.setModifiedDate(modifiedDate);

		collection.setStatus(status);
		collection.setStatusByUserId(user.getUserId());
		collection.setStatusByUserName(user.getFullName());
		collection.setStatusDate(modifiedDate);

		return ctCollectionPersistence.update(collection);
	}

	private void _validate(String name) throws CTCollectionNameException {
		if (Validator.isNull(name)) {
			throw new CTCollectionNameException();
		}
	}

}