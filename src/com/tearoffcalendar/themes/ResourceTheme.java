/**
 * 
 */
package com.tearoffcalendar.themes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import android.content.Context;

/**
 * @author Servy
 * 
 *         Theme from the resource file *
 * 
 */
public class ResourceTheme implements BasicTheme {

	private Date startDate;
	private Date endDate;
	private String name;
	private String description;
	private String filename;
	private Context context;
	/**
	 * Cashed input stream
	 */
	private InputStream stream;

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	public ResourceTheme(String filename, Context context)
			throws ThemeException {
		this.filename = filename;
		this.context = context;
		this.stream = null;
		readFields();
	}

	/**
	 * @throws ThemeException
	 * 
	 *             Sets field values (such as name, description, etc.) based on
	 *             data read from associated file (filename member)
	 */
	private void readFields() throws ThemeException {
		InputStream assetStream;
		try {
			assetStream = context.getAssets().open(filename);
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

				XPath selector = XPathFactory.newInstance().newXPath();
				name = selector.evaluate("theme/name", assetStream);
				startDate = formatter.parse(selector.evaluate(
						"theme/startdate", assetStream));
				endDate = formatter.parse(selector.evaluate("theme/enddate",
						assetStream));
				description = selector.evaluate("theme/description",
						assetStream);
			} catch (XPathExpressionException e) {
				throw new ThemeException(
						"XPath evaluation failed while parsing " + filename, e);
			} catch (ParseException e) {
				throw new ThemeException("Date parse exception " + filename, e);
			} finally {
				assetStream.close();
			}
		} catch (IOException ex) {
			throw new ThemeException("IO exception when working with "
					+ filename, ex);
		}
	}

	/*
	 * @see com.tearoffcalendar.themes.BasicTheme#getStartDate()
	 */
	public Date getStartDate() {
		return startDate;
	}

	/*
	 * @see com.tearoffcalendar.themes.BasicTheme#getEndDate()
	 */
	public Date getEndDate() {
		return endDate;
	}

	/*
	 * @see com.tearoffcalendar.themes.BasicTheme#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * @see com.tearoffcalendar.themes.BasicTheme#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * @see com.tearoffcalendar.themes.BasicTheme#getTextCard(java.util.Date)
	 */
	public String getTextCard(Date day) throws ThemeException {
		// truncate accidental time info
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date trunkedDate = cal.getTime();

		// check if date is within bounds
		if (trunkedDate.before(startDate) || (trunkedDate.after(endDate)))
			throw new ThemeException(
					String.format(
							"Selected text card date is out of theme bounds: %s [%s %s]",
							trunkedDate, startDate, endDate));

		return readTextCard(trunkedDate);
	}

	/**
	 * Reads card text from an xml.
	 * 
	 * @param date
	 *            The date for which text card's text should be retrieved
	 * @return The text of the textcard
	 * @throws ThemeException
	 */
	private String readTextCard(Date date) throws ThemeException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

		// use xpath to retrieve the text card
		XPath selectText = XPathFactory.newInstance().newXPath();
		String expression = String.format(
				"/theme/textcards/textcard[date=\"%s\"]/text",
				formatter.format(date));
		InputStream stream = getInputStream();
		try {
			return selectText.evaluate(expression, stream);
		} catch (XPathExpressionException e) {
			throw new ThemeException("XPath error while parsing " + filename, e);
		}
	}

	private static final int IO_BUFFER_SIZE = 4 * 1024;

	/**
	 * @return A stream to read xml data from.
	 * @throws ThemeException
	 */
	private InputStream getInputStream() throws ThemeException {
		if (stream == null) {
			try {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				try {
					InputStream assetStream = context.getAssets()
							.open(filename);
					try {
						byte[] b = new byte[IO_BUFFER_SIZE];
						int read;
						while ((read = assetStream.read(b)) != -1) {
							outStream.write(b, 0, read);
						}
						stream = new ByteArrayInputStream(
								outStream.toByteArray());
					} finally {
						assetStream.close();
					}
				} finally {
					outStream.close();
				}
			} catch (IOException e) {
				throw new ThemeException("IOError while dealing with "
						+ filename, e);
			}
		}
		return stream;
	}
}
