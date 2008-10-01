/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.cdm.common.DataComponent;
import org.vast.ows.OWSException;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * GetStatus Response Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Used to read a version 2.0 SPS GetStatus Response into
 * a GetStatusResponse object
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 29 2008
 * @version 1.0
 */
public class GetStatusResponseReaderV20 extends ParameterizedResponseReader<GetStatusResponse>
{
	protected SPSCommonReaderV20 commonReader = new SPSCommonReaderV20();
	protected DataComponent paramStructure;
	
	
	public void setParamStructure(DataComponent paramStructure)
	{
		this.paramStructure = paramStructure;
	}
	
	
	public GetStatusResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		try
		{
			assert(paramStructure != null);
			
			GetStatusResponse response = new GetStatusResponse();
			response.setVersion("2.0");
			
			// progress report
			Element reportElt = dom.getElement(responseElt, "ProgressReport");
			if (reportElt != null)
			{
				ProgressReport report = commonReader.readProgressReport(dom, reportElt, paramStructure);
				response.setProgressReport(report);
			}
			
			return response;
		}
		catch (Exception e)
		{
			throw new SPSException(e);
		}
	}
	
}
