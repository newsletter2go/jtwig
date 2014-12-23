/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.functions.builtin;

import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;
import org.jtwig.functions.exceptions.FunctionException;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

public class DateFunctions {

	private static final String MODIFY_PATTERN = "^([\\\\+\\\\-])([0-9]+) ([a-zA-Z]+)$";

	@JtwigFunction(name = "date_modify")
	public Date modifyDate(@Parameter Date input, @Parameter String modifyString)
			throws FunctionException {
		return modify(modifyString, LocalDateTime.fromDateFields(input));
	}

	@JtwigFunction(name = "date_modify")
	public Date modifyDate(@Parameter String input,
			@Parameter String modifyString) throws FunctionException {
		return modify(modifyString, LocalDateTime.parse(input));
	}

	private Date modify(String modifyString, LocalDateTime localDateTime)
			throws FunctionException {

		LocalDateTime result;

		Matcher matcher = compile(MODIFY_PATTERN).matcher(modifyString);
		matcher.find();

		int signal = 1;

		if (matcher.group(1).equals("-"))
			signal = -1;

		int val = Integer.valueOf(matcher.group(2)) * signal;
		String type = matcher.group(3).toLowerCase();

		if (type.startsWith("day"))
			result = localDateTime.plusDays(val);
		else if (type.startsWith("month"))
			result = localDateTime.plusMonths(val);
		else if (type.startsWith("year"))
			result = localDateTime.plusYears(val);
		else if (type.startsWith("second"))
			result = localDateTime.plusSeconds(val);
		else if (type.startsWith("hour"))
			result = localDateTime.plusHours(val);
		else if (type.startsWith("minute"))
			result = localDateTime.plusMinutes(val);
		else
			throw new FunctionException("Unknown type " + matcher.group(3));

		return result.toDate();
	}

	@JtwigFunction(name = "date")
	public String format(@Parameter Date input, @Parameter String format) {
		if (input == null)
			return "";

		String jodaFormat = PHPDateFormatConverter.convertDateFormat(format);

		DateTimeFormatter formatter = DateTimeFormat.forPattern(jodaFormat);
		return LocalDateTime.fromDateFields(input).toString(formatter);
	}

	@JtwigFunction(name = "date")
	public String format(@Parameter Date input) {
		if (input == null)
			return "";

		return LocalDateTime.fromDateFields(input).toString(
				ISODateTimeFormat.dateHourMinuteSecond());
	}

	@JtwigFunction(name = "date")
	public String format(@Parameter String input) {
		if (input == null)
			return "";
		return format(LocalDateTime.parse(input).toDate());
	}

	@JtwigFunction(name = "date")
	public String format(@Parameter String input, @Parameter String format) {
		if (input == null)
			return "";
		return format(LocalDateTime.parse(input).toDate(), format);
	}

	private static class PHPDateFormatConverter {
		/**
		 * Mapping Table to convert the PHP date format
		 *   http://php.net/manual/en/function.date.php 
		 * into Joda's DateTimeFormat
		 *   http://joda-time.sourceforge.net/apidocs/org/joda/time/format/
		 * DateTimeFormat.html
		 * 
		 * At least as best as possible.
		 * 
		 * see https://code.google.com/p/gwt-ext/issues/detail?id=502
		 */
		private static final String[][] conversionTable = new String[][] {
				{ "y", "yy" }, 
				{ "Y", "yyyy" }, 
				{ "F", "MMMMM" },
				{ "M", "MMM" }, 
				{ "m", "MM" }, 
				{ "l", "EEEEEE" },
				{ "D", "EEE" }, 
				{ "d", "dd" }, 
				{ "H", "HH" }, 
				{ "i", "mm" },
				{ "s", "ss" }, 
				{ "h", "hh" }, 
				{ "a", "A" }, 
				{ "u", "S" },
				{ "z", "D" } 
			};

		/**
		 * Converts php date format to Joda's date format
		 *
		 * @param phpFormat
		 *            e.g. d-m-y
		 * @return e.g. dd-MM-yyyy
		 */
		public static String convertDateFormat(String phpFormat) {
			String result = phpFormat;
			for (int i = 0; i < conversionTable.length; i++) {
				result = result.replaceAll(conversionTable[i][0],
						conversionTable[i][1]);
			}
			return result;
		}
	}
}
