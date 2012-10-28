/**
 * 
 */
package com.tearoffcalendar.themes;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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
	 * Cashed xml
	 */
	private Node xml;

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	public ResourceTheme(String filename, Context context)
			throws ThemeException {
		this.filename = filename;
		this.context = context;
		this.xml = null;
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
				InputSource source = new InputSource(assetStream);
				SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
				
				XPath selector = XPathFactory.newInstance().newXPath();
				
				// parse the whole xml first to avoid reparsing it for each expression
				Node root = (Node) selector.evaluate("/", source, XPathConstants.NODE);
				
				// read fields
				name = selector.evaluate("/theme/name/text()", root);
				startDate = formatter.parse(selector.evaluate("/theme/startdate/text()", root));
				endDate = formatter.parse(selector.evaluate("/theme/enddate/text()",
						root));
				description = selector.evaluate("/theme/description/text()",
						root);
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
				"/theme/textcards/textcard[date=\"%s\"]/text/text()",
				formatter.format(date));
		try {
			return selectText.evaluate(expression, getXml());
		} catch (XPathExpressionException e) {
			throw new ThemeException("XPath error while parsing " + filename, e);
		}
	}

	/**
	 * @return A root node of the parsed xml
	 * @throws ThemeException
	 */
	private Node getXml() throws ThemeException {		
		if (xml == null) {
			try {
				InputStream assetStream = context.getAssets()
							.open(filename);
				try {
					XPath parser = XPathFactory.newInstance().newXPath();
					xml = (Node)parser.evaluate("/", new InputSource(assetStream), XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					throw new ThemeException(
							"XPath evaluation failed while parsing " + filename, e);
				} finally {
					assetStream.close();
				}
			} catch (IOException e) {
				throw new ThemeException("IO exception when working with "
						+ filename, e);
			}
		}
		return xml;
	}
}
