/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataComponent;
import org.vast.data.DataValue;


/**
 * <p>
 * Parser for data streams written using SWE Common XML encoding
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @since Feb, 28 2008
 * @version 1.0
 */
public class XmlDataParser extends AbstractDataParser
{

	public void setInput(InputStream inputStream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}
	
	
	public void parse(InputStream inputStream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void processAtom(DataValue scalarInfo) throws IOException
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	protected boolean processBlock(DataComponent blockInfo) throws IOException
	{
	    // TODO Auto-generated method stub
	    return true;
	}


    @Override
    protected boolean moreData() throws IOException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public void close() throws IOException
    {
        // TODO Auto-generated method stub
        
    }
}
