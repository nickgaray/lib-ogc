/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import java.io.*;

import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataHandler;
import org.vast.math.Vector3d;
import org.vast.ogc.gml.GMLGeometryReader;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
import org.vast.sweCommon.SWEFilter;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.sweCommon.URIStreamHandler;
import org.vast.xml.*;
import org.w3c.dom.*;


/**
 * <p><b>Title:</b>
 * WPSDescribeProcessStreamReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Streaming reader for WPS describeProcess responses with the process input and output expressed in SWE Common.
 * This won't deal with Observation Collections properly.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Gregoire Berthiau
 * @date Dec 3, 2008
 * @version 1.0
 */
public class DescribeProcessStreamReader
{
	protected SWEFilter streamFilter;       
    
    public DescribeProcessStreamReader()
    {
    }
    

	public Element getInputElement(InputStream inputStream) throws CDMException
	{
	    streamFilter = new SWEFilter(inputStream);
		try
		{
			// parse xml header using DOMReader
			DOMHelper dom = new DOMHelper(streamFilter, false);
			OGCExceptionReader.checkException(dom);
			
			// find DescribeProcess element
			Element rootElement = dom.getRootElement();
			if (rootElement == null)
				throw new CDMException("XML Response doesn't contain any process description");
			
			Element inputElt = dom.getElement(rootElement, "input");
            return inputElt;
		}
		catch (DOMHelperException e)
		{
			throw new CDMException("Error while parsing Observation XML", e);
		} catch (OGCException e) {
			// TODO Auto-generated catch block
			throw new CDMException(e.getMessage());
		}	

	}
}
