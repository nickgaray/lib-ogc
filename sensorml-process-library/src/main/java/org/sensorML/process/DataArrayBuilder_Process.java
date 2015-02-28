/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML AbstractProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.sensorML.process;

import net.opengis.swe.v20.DataComponent;
import org.vast.data.*;
import org.vast.process.*;
import org.vast.sensorML.ExecutableProcessImpl;


/**
 * <p>
 * This process synchronizes two asynchronous streams by interpolating
 * the slave data stream at times given by the master time stream
 * </p>
 *
 * @author Alexandre Robin
 * @date May 2, 2007
 */
public class DataArrayBuilder_Process extends ExecutableProcessImpl
{
	int ArrayDataIndex, ValueDataIndex, ArrayElementCount, i=0;
	DataComponent ArrayData, ValueData;    
	double value;
	
    @Override
    public void init() throws SMLProcessException
    {
        try
        {
        	ArrayDataIndex = outputData.getComponentIndex("array");
        	ArrayData = outputData.getComponent(ArrayDataIndex);
        	ValueDataIndex = inputData.getComponentIndex("value");
        	ValueData = (DataValue)inputData.getComponent(ValueDataIndex);
            ArrayElementCount = ArrayData.getComponentCount();

        }
        catch (Exception e)
        {
            throw new SMLProcessException(ioError, e);
        }
    }
    
    @Override
    public void execute() throws SMLProcessException
    {
    	value = ValueData.getData().getDoubleValue();
    	ArrayData.getData().setDoubleValue(i, value);
    	i++;
    	
    	nextInputNeeded();
    	
    	if(i==ArrayElementCount){
    		nextOutput();
    		i = 0;
    	}
    	
     } 	
    	
    protected void nextInputNeeded()
    {
    	inputConnections.get(ValueDataIndex).setNeeded(true);
        outputConnections.get(ArrayDataIndex).setNeeded(false);
    }
    
    
    protected void nextOutput()
    {
        inputConnections.get(ValueDataIndex).setNeeded(false);
        outputConnections.get(ArrayDataIndex).setNeeded(true);
    }
}