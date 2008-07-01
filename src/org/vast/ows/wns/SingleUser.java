/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code part of the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wns;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * <p><b>Title:</b><br/>
 * SingleUser
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Jan 16 2008
 * @version 1.0
 */
public class SingleUser extends WNSUser
{
	protected Hashtable<String, List<String>> protocolTable;

	
	public SingleUser()
	{
		protocolTable = new Hashtable<String, List<String>>();
	}
	
	
	public Hashtable<String, List<String>> getProtocolTable()
	{
		return protocolTable;
	}
	
	
	public void addProtocol(String protocol, String address)
	{
		// add to list if already there, create one otherwise
		List<String> addressList = protocolTable.get(protocol);
		if (addressList == null)
		{
			addressList = new ArrayList<String>(1);
			protocolTable.put(protocol, addressList);
		}
		
		addressList.add(address);
	}
}
