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

package com.liferay.jenkins.results.parser;

import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class GitBisectToolBuild extends TopLevelBuild {

	@Override
	public Element getJenkinsReportElement() {
		if (_workspaceGitRepository == null) {
			throw new IllegalStateException(
				"Please set the workspace git repository");
		}

		if (_downstreamBuildDataList == null) {
			throw new IllegalStateException(
				"Please set the downstream build data list");
		}

		return Dom4JUtil.getNewElement(
			"html", null, getJenkinsReportHeadElement(),
			getJenkinsReportBodyElement(
				_workspaceGitRepository, _downstreamBuildDataList));
	}

	public void setDownstreamBuildDataList(
		List<BuildData> downstreamBuildDataList) {

		_downstreamBuildDataList = downstreamBuildDataList;
	}

	public void setWorkspaceGitRepository(
		WorkspaceGitRepository workspaceGitRepository) {

		_workspaceGitRepository = workspaceGitRepository;
	}

	protected GitBisectToolBuild(String url) {
		this(url, null);
	}

	protected GitBisectToolBuild(String url, TopLevelBuild topLevelBuild) {
		super(url, topLevelBuild);
	}

	protected Element getJenkinsReportBodyElement(
		WorkspaceGitRepository workspaceGitRepository,
		List<BuildData> downstreamBuildDataList) {

		String buildURL = getBuildURL();

		Element headingElement = Dom4JUtil.getNewElement(
			"h1", null, "Jenkins report for ",
			Dom4JUtil.getNewAnchorElement(buildURL, buildURL));

		Element subheadingElement = null;

		JSONObject jobJSONObject = getBuildJSONObject();

		String description = jobJSONObject.optString("description");

		if (!description.isEmpty()) {
			subheadingElement = Dom4JUtil.getNewElement("h2");

			try {
				Dom4JUtil.addRawXMLToElement(subheadingElement, description);
			}
			catch (DocumentException de) {
				throw new RuntimeException(
					"Unable to parse description HTML " + description, de);
			}
		}

		return Dom4JUtil.getNewElement(
			"body", null, headingElement, subheadingElement,
			getJenkinsReportSummaryElement(), getJenkinsReportTimelineElement(),
			getJenkinsReportTableElement(
				workspaceGitRepository, downstreamBuildDataList),
			Dom4JUtil.getNewElement(
				"p", null,
				Dom4JUtil.getNewElement(
					"em", null, "Indicates HEAD Commit (*)")));
	}

	protected Element getJenkinsReportHeadElement() {
		Element headElement = Dom4JUtil.getNewElement("head");

		StringBuilder sb = new StringBuilder();

		sb.append("caption, td, th {\n");
		sb.append("padding: .5em;\n");
		sb.append("text-align: left;\n");
		sb.append("}\n\n");

		sb.append("td {\n");
		sb.append("max-width:500px;\n");
		sb.append("overflow: hidden;\n");
		sb.append("text-overflow:ellipsis;\n");
		sb.append("white-space: nowrap;\n");
		sb.append("}\n\n");

		sb.append("canvas {\n");
		sb.append("display: block;\n");
		sb.append("height: 300px;\n");
		sb.append("width: 1900px;\n");
		sb.append("}");

		Dom4JUtil.getNewElement("style", headElement, sb.toString());

		return headElement;
	}

	protected Element getJenkinsReportTableBodyElement(
		WorkspaceGitRepository workspaceGitRepository,
		List<BuildData> downstreamBuildDataList) {

		Element tableBodyElement = Dom4JUtil.getNewElement("tbody");

		List<Commit> historicalCommits =
			workspaceGitRepository.getHistoricalCommits();

		boolean first = true;

		for (Commit commit : historicalCommits) {
			String shaText = commit.getSHA();

			if (first) {
				shaText = "*" + shaText;

				first = false;
			}

			Element tableRowElement = Dom4JUtil.getNewElement(
				"tr", tableBodyElement,
				Dom4JUtil.getNewElement("td", null, shaText),
				Dom4JUtil.getNewElement("td", null, commit.getMessage()));

			BuildData buildData = _getBuildDataBySHA(
				commit.getSHA(), downstreamBuildDataList);

			if (buildData == null) {
				Dom4JUtil.getNewElement("td", tableRowElement, "");
				Dom4JUtil.getNewElement("td", tableRowElement, "");
				Dom4JUtil.getNewElement("td", tableRowElement, "");
				Dom4JUtil.getNewElement("td", tableRowElement, "");
				Dom4JUtil.getNewElement("td", tableRowElement, "");

				continue;
			}

			Dom4JUtil.getNewElement(
				"td", tableRowElement,
				Dom4JUtil.getNewAnchorElement(
					buildData.getBuildURL(), "build"));

			Dom4JUtil.getNewElement(
				"td", tableRowElement, buildData.getStartTimeString());

			Dom4JUtil.getNewElement(
				"td", tableRowElement, buildData.getBuildDurationString());

			Dom4JUtil.getNewElement(
				"td", tableRowElement, buildData.getBuildStatus());

			Dom4JUtil.getNewElement(
				"td", tableRowElement, buildData.getBuildResult());
		}

		return tableBodyElement;
	}

	@Override
	protected Element getJenkinsReportTableColumnHeadersElement() {
		Element commitElement = Dom4JUtil.getNewElement("th", null, "Commit");

		Element shaElement = Dom4JUtil.getNewElement("th", null, "SHA");

		Element buildElement = Dom4JUtil.getNewElement("th", null, "Build");

		Element startTimeElement = Dom4JUtil.getNewElement(
			"th", null, "Start Time");

		Element buildTimeElement = Dom4JUtil.getNewElement(
			"th", null, "Build Time");

		Element statusElement = Dom4JUtil.getNewElement("th", null, "Status");

		Element resultElement = Dom4JUtil.getNewElement("th", null, "Result");

		Element tableColumnHeaderElement = Dom4JUtil.getNewElement("tr");

		Dom4JUtil.addToElement(
			tableColumnHeaderElement, commitElement, shaElement, buildElement,
			startTimeElement, buildTimeElement, statusElement, resultElement);

		return tableColumnHeaderElement;
	}

	protected Element getJenkinsReportTableElement(
		WorkspaceGitRepository workspaceGitRepository,
		List<BuildData> downstreamBuildDataList) {

		Element topLevelTableElement = Dom4JUtil.getNewElement("table");

		String gitHubCommitsURL = workspaceGitRepository.getGitHubURL();

		gitHubCommitsURL = gitHubCommitsURL.replace("/tree/", "/commits/");

		Element captionElement = Dom4JUtil.getNewElement(
			"caption", topLevelTableElement);

		Dom4JUtil.getNewElement(
			"h2", captionElement, "Commit history of ",
			Dom4JUtil.getNewAnchorElement(gitHubCommitsURL, gitHubCommitsURL));

		Dom4JUtil.addToElement(
			topLevelTableElement, getJenkinsReportTableColumnHeadersElement(),
			getJenkinsReportTableBodyElement(
				workspaceGitRepository, downstreamBuildDataList));

		return topLevelTableElement;
	}

	private BuildData _getBuildDataBySHA(
		String sha, List<BuildData> downstreamBuildDataList) {

		for (BuildData buildData : downstreamBuildDataList) {
			PortalBuildData portalBuildData = (PortalBuildData)buildData;

			if (sha.equals(portalBuildData.getPortalBranchSHA())) {
				return portalBuildData;
			}
		}

		return null;
	}

	private List<BuildData> _downstreamBuildDataList;
	private WorkspaceGitRepository _workspaceGitRepository;

}