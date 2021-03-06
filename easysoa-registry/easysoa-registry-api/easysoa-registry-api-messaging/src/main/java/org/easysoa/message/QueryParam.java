/**
 * EasySOA Proxy
 * Copyright 2011 Open Wide
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact : easysoa-dev@googlegroups.com
 */

package org.easysoa.message;

/**
 * Query Param
 * @author jguillemotte
 *
 */
public class QueryParam extends CustomField {

	private String comment;

	public QueryParam(){
		super();
		comment = "";
	}
	
	/**
	 * Creates a new <code>QueryParam</code> object
	 * @param name Name of the query parameter
	 * @param value Value of the query parameter
	 * @param comment User comment
	 */
	public QueryParam(String name, String value, String comment) {
		super(name, value);
		this.comment = comment;
	}

	/**
	 * Creates a new <code>QueryParam</code> object with empty comment
	 * @param name Name of the query parameter
	 * @param value Value of the query parameter
	 */
	public QueryParam(String name, String value) {
		this(name, value, "");
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
