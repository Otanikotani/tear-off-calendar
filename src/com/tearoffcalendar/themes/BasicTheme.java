package com.tearoffcalendar.themes;

import java.util.Date;

/**
 * @author Servy
 * 
 *         Basic theme interface. Theme contains a set of text card associated
 *         with a given time period. Check out
 *         https://github.com/Otanikotani/tear-off-calendar/issues/4 for
 *         details.
 */
public interface BasicTheme {

	/**
	 * @return The start date of the period covered by this theme (inclusive).
	 */
	Date getStartDate();

	/**
	 * @return The end date of the period covered by this theme (inclusive).
	 */
	Date getEndDate();

	/**
	 * @return The name of this theme. Name should be unique.
	 */
	String getName();

	/**
	 * @return The human readable description of this theme. It may include
	 *         simple html formatting.
	 */
	String getDescription();

	/**
	 * Returns a text card for a given day. The text card returned is supposed
	 * to be valid html document with no external resources.
	 * 
	 * @param day The day for which the text card should be returned.
	 * @return The text card of the theme that corresponds to a given day.
	 */
	String getTextCard(Date day) throws ThemeException;
}
