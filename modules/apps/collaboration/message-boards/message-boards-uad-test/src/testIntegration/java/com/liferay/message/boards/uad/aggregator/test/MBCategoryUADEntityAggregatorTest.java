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

package com.liferay.message.boards.uad.aggregator.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;

import com.liferay.message.boards.model.MBCategory;
import com.liferay.message.boards.uad.constants.MBUADConstants;
import com.liferay.message.boards.uad.test.MBCategoryUADEntityTestHelper;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import com.liferay.user.associated.data.aggregator.UADEntityAggregator;
import com.liferay.user.associated.data.test.util.BaseUADEntityAggregatorTestCase;
import com.liferay.user.associated.data.test.util.WhenHasStatusByUserIdField;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;

import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@RunWith(Arquillian.class)
public class MBCategoryUADEntityAggregatorTest
	extends BaseUADEntityAggregatorTestCase
	implements WhenHasStatusByUserIdField {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new LiferayIntegrationTestRule();

	@Override
	public BaseModel<?> addBaseModelWithStatusByUserId(long userId,
		long statusByUserId) throws Exception {
		MBCategory mbCategory = _mbCategoryUADEntityTestHelper.addMBCategoryWithStatusByUserId(userId,
				statusByUserId);

		_mbCategories.add(mbCategory);

		return mbCategory;
	}

	@Override
	protected BaseModel<?> addBaseModel(long userId) throws Exception {
		MBCategory mbCategory = _mbCategoryUADEntityTestHelper.addMBCategory(userId);

		_mbCategories.add(mbCategory);

		return mbCategory;
	}

	@Override
	protected UADEntityAggregator getUADEntityAggregator() {
		return _uadEntityAggregator;
	}

	@After
	public void tearDown() throws Exception {
		_mbCategoryUADEntityTestHelper.cleanUpDependencies(_mbCategories);
	}

	@DeleteAfterTestRun
	private final List<MBCategory> _mbCategories = new ArrayList<MBCategory>();
	@Inject
	private MBCategoryUADEntityTestHelper _mbCategoryUADEntityTestHelper;
	@Inject(filter = "model.class.name=" +
	MBUADConstants.CLASS_NAME_MB_CATEGORY)
	private UADEntityAggregator _uadEntityAggregator;
}