/**
 * 
 */
package com.tearoffcalendar.themes;

/**
 * @author Servy
 *
 * Exception thrown by some Theme methods
 */
@SuppressWarnings("serial")
public class ThemeException extends Exception {
    public ThemeException(String message) {
        super(message);
    }

    public ThemeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
