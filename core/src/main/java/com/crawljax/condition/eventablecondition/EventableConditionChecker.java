package com.crawljax.condition.eventablecondition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.crawljax.core.CrawljaxException;
import com.crawljax.util.XPathHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Check whether the conditions of an eventable are satisfied.
 */
public class EventableConditionChecker {

	private static final Logger LOG = LoggerFactory.getLogger(EventableConditionChecker.class);
	private List<EventableCondition> eventableConditions;

	/**
	 * Construct the eventableconditionchecker with its eventable conditions.
	 * 
	 * @param eventableConditions
	 *            The eventable conditions.
	 */
	public EventableConditionChecker(List<EventableCondition> eventableConditions) {
		Preconditions.checkNotNull(eventableConditions);
		this.eventableConditions = eventableConditions;
	}

	/**
	 * @param id
	 *            Identifier of the {@link EventableCondition}.
	 * @return EventableCondition or <code>null</code>
	 */
	public EventableCondition getEventableCondition(String id) {
		if (!Strings.isNullOrEmpty(id)) {
			for (EventableCondition eventableCondition : eventableConditions) {
				if (eventableCondition.getId().equalsIgnoreCase(id)) {
					return eventableCondition;
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether an XPath expression starts with an XPath eventable condition.
	 * 
	 * @param dom
	 *            The DOM String.
	 * @param eventableCondition
	 *            The eventable condition.
	 * @param xpath
	 *            The XPath.
	 * @return boolean whether xpath starts with xpath location of eventable condition xpath
	 *         condition
	 * @throws CrawljaxException
	 *             when not can be determined whether xpath contains needed xpath locaton
	 */
	public boolean checkXpathStartsWithXpathEventableCondition(Document dom,
	        EventableCondition eventableCondition, String xpath) {
		if (eventableCondition == null || eventableCondition.getInXPath() == null
		        || eventableCondition.getInXPath().equals("")) {
			throw new CrawljaxException("Eventable has no XPath condition");
		}
		List<String> expressions =
		        XPathHelper.getXpathForXPathExpressions(dom, eventableCondition.getInXPath());

		return checkXPathUnderXPaths(xpath, expressions);
	}

	/**
	 * @param xpath
	 *            the xpath to check if its under a certain set of full-xPaths.
	 * @param xpathsList
	 *            the set of full-length-xPaths
	 * @return true if the xpath is under one of the full-length-xpaths.
	 */
	public boolean checkXPathUnderXPaths(String xpath, List<String> xpathsList) {
		/* check all expressions */
		for (String fullXpath : xpathsList) {
			if (xpath.startsWith(fullXpath)) {
				LOG.trace("{} IS under xpath {}", xpath, fullXpath);
				return true;
			}
			LOG.trace("{} is not under xpath {}", xpath, fullXpath);
		}

		return false;
	}

}
