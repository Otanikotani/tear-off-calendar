/**
 * Theme management
 *
 */
package com.tearoffcalendar.themes;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * @author Servy
 * 
 *         Theme manager that loads themes from raw resources.
 * 
 */
public class ResourcesThemeManager implements BasicThemeManager {

	private Map<String, BasicTheme> themes;
	private static final String THEMES_FOLDER = "themes";
	
	public ResourcesThemeManager(Context context) throws ThemeException {
		themes = new HashMap<String, BasicTheme>();
		String[] themeFileNames;
		try {
			themeFileNames = context.getResources().getAssets().list(THEMES_FOLDER);
		} catch (IOException e) {
			throw new ThemeException("IOException when retrieving list of themes from " + THEMES_FOLDER, e);
		}
		for (String filename: themeFileNames) {
			BasicTheme theme = new ResourceTheme(THEMES_FOLDER + filename, context); 
			themes.put(theme.getName(), theme);			
		}		
	}
	
	public Collection<BasicTheme> getAvailableThemes() {
		return themes.values();
	}

	public BasicTheme getThemeByName(String name) throws ThemeException {
		BasicTheme r = themes.get(name);
		if (r == null)
			throw new ThemeException(String.format("Theme %s was not found", name));
		return r;
	}
}
