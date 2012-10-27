/**
 * 
 */
package com.tearoffcalendar.themes;

import java.util.Collection;

/**
 * @author Servy
 *
 * Basic theme manager interface, which allows to list all themes
 */
public interface BasicThemeManager {
	/**
	 * @return the list of themes available
	 */
	Collection<BasicTheme> getAvailableThemes();
	
	/**
	 * @param name A name to look for 
	 * @return A theme with a given name
	 * @throws ThemeException if theme was not found
	 */
	BasicTheme getThemeByName(String name) throws ThemeException;
}
