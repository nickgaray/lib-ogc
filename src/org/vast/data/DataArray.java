/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import org.ogc.cdm.common.*;


/**
 * <p><b>Title:</b><br/> DataArray</p>
 *
 * <p><b>Description:</b><br/>
 * Array of identical DataContainers. Can be of variable size.
 * In the case of a variable size array, size is actually given
 * by another component: sizeData which should be a DataValue
 * carrying an Integer value.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataArray extends AbstractDataComponent
{
    protected AbstractDataComponent component = null;
    protected int arraySize = -1;
    protected boolean variableSize;
    protected DataValue sizeData = null;
    protected String sizeDataName = null;
    protected int stepsToSizeData = -1;

    
    public DataArray()
    {
    }  
    
    
    public DataArray(int arraySize)
    {
        this.arraySize = arraySize;
        this.variableSize = false;
    }
    
    
    public DataArray(DataValue sizeData)
    {
        this.sizeData = sizeData;
        this.arraySize = 1;
        this.variableSize = true;
    }


    @Override
    public DataArray copy()
    {
    	DataArray newArray = new DataArray();
    	newArray.name = this.name;
    	newArray.properties = this.properties;    	
    	newArray.arraySize = this.arraySize;
    	newArray.variableSize = this.variableSize;
        newArray.addComponent(this.component.copy());
        
        // also keep track of sizeData component
        if (this.variableSize && this.getSizeData() != null)
        {
            // find where sizeData component is
            int step = 0;
            AbstractDataComponent dataComponent = this;
            while (dataComponent.getComponent(sizeData.name) != sizeData)
            {
                dataComponent = dataComponent.parent;
                step++;
            }
            
            newArray.sizeDataName = sizeData.name;
            newArray.stepsToSizeData = step;
        }
        
    	return newArray;
    }
    
    
    @Override
    protected void updateStartIndex(int startIndex)
    {
        dataBlock.startIndex = startIndex;
        
        if (dataBlock instanceof DataBlockMixed)
        {
            // TODO updateStartIndex for array with DataBlockMixed
        }
        else if (dataBlock instanceof DataBlockParallel)
        {
            component.updateStartIndex(startIndex);
        }
        else // case of primitive array
        {
            component.updateStartIndex(startIndex);
        }
    }
    
    
    @Override
    public void addComponent(DataComponent component)
    {
        String componentName = component.getName();
        if (componentName != null)
            this.names.put(componentName, 0);
        
    	this.component = (AbstractDataComponent)component;
        ((AbstractDataComponent)component).parent = this;
    }


    @Override
    public AbstractDataComponent getComponent(int index)
    {
        checkIndex(index);
        
        if (dataBlock != null)
        {
            int startIndex = dataBlock.startIndex;
            
            if (dataBlock instanceof DataBlockMixed)
            {
                // TODO getComponent for array with DataBlockMixed
                throw new IllegalStateException("Error: DataArrays should never contain a DataBlockMixed");
            }            
            else if (dataBlock instanceof DataBlockParallel)
            {
                if (component instanceof DataArray)
                    startIndex += index * ((DataArray)component).arraySize;
                else
                    startIndex += index;
            }
            else // primitive block
            {
                startIndex += index * component.scalarCount;
            }
            
            // update child start index
            component.updateStartIndex(startIndex);
        }
        
        return component;
    }
    
    
    @Override
    public void setData(DataBlock dataBlock)
    {
    	this.dataBlock = (AbstractDataBlock)dataBlock;

		// also assign dataBlock to child
    	AbstractDataBlock childBlock = ((AbstractDataBlock)dataBlock).copy();
		childBlock.atomCount = component.scalarCount;
		component.setData(childBlock);
    }
    
    
    /**
     * Create the right data block to carry this array data
     * It can be either a scalar array (DataBlockDouble, etc...)
     * or a group of mixed types parallel arrays (DataBlockMixed)
     */
    @Override
    protected AbstractDataBlock createDataBlock()
    {
    	AbstractDataBlock childBlock = component.createDataBlock();
    	AbstractDataBlock newBlock = childBlock.copy();
    	int newSize = 0;
        
    	if (arraySize > 0)
    	{
	    	// create bigger parallel block
	        if (childBlock instanceof DataBlockParallel)
	        {
	        	newBlock = childBlock.copy();
                newSize = childBlock.atomCount * arraySize;
	        }
            
            // create parallel block
            else if (childBlock instanceof DataBlockTuple)
            {
                DataBlockParallel parallelBlock = new DataBlockParallel();
                parallelBlock.blockArray = ((DataBlockTuple)childBlock).blockArray;
                newSize = childBlock.atomCount * arraySize;
                newBlock = parallelBlock;
            }
            
            // create tuple block
            else if (childBlock instanceof DataBlockMixed)
            {
                // TODO arrays of DataBlockMixed -> How to keep track of things ??
            }
	        
	        // create bigger primitive block
	        else
	        {
	        	newBlock = childBlock.copy();
                newSize = childBlock.atomCount * arraySize;   	
	        }
    	}
        
        newBlock.resize(newSize);
        
        scalarCount = newBlock.atomCount;
        return newBlock;
    }


    /**
     * Check that the integer index given is in range: 0 to size of array - 1
     * @param index int
     * @throws DataException
     */
    protected void checkIndex(int index)
    {
        // error if index is out of range
        if ((index >= arraySize) || (index < 0))
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }
    
    
    /**
     * Dynamically update size of a VARIABLE SIZE array
     * Note that sizeData must carry the right value at this time
     */
    public void updateSize()
    {
    	if (variableSize)
    	{
    		int oldSize = arraySize;
    	
            // assign variable size value to arraySize
	    	if (this.getSizeData() != null)
	    	{
	    		DataBlock data = sizeData.getData();
	    		
	    		if (data != null)
	    			arraySize = data.getIntValue();
	    	}
            
            // take care of variable size child array
            if (component instanceof DataArray)
            {
                if (((DataArray)component).isVariableSize())
                    ((DataArray)component).updateSize();
            }
            
            // update scalar count
            this.scalarCount = component.scalarCount * arraySize;
	    	
            // stop here if parent also has variable size
            if (parent instanceof DataArray)
            {
                if (((DataArray)parent).isVariableSize())
                    return;
            }
            
            // resize underlying datablock
            if (dataBlock != null)
			{
				dataBlock.resize(scalarCount);
				setData(dataBlock);
			}
	    	
            // also update parent components??
	    	if (parent != null)
	    	{               
                parent.dataBlock.atomCount += component.scalarCount * (arraySize - oldSize);
	    		// TODO parent of parent should also be updated and so on...
	    	}
    	}
    }
    
    
    /**
     * Set the size of this VARIABLE SIZE array to a new value
     * Automatically updates the sizeData component value.
     * @param newSize
     */
    public void updateSize(int newSize)
    {
        if (variableSize && newSize > 0)
        {
            int oldSize = arraySize;
            arraySize = newSize;
        
            if (this.getSizeData() != null)
            {
                DataBlock data = sizeData.getData();
                
                if (data != null)
                    data.setIntValue(newSize);
            }
            
            if (dataBlock != null)
            {
                dataBlock.resize(component.scalarCount * arraySize);
                scalarCount = dataBlock.atomCount;
                setData(dataBlock);
            }
            
            if (parent != null)
            {
                parent.dataBlock.atomCount += component.scalarCount * (arraySize - oldSize);
                // TODO parent of parent should also be updated and so on...
            }
        }
    }
    
    
    /**
     * Set the size of this FIXED SIZE array to a new value
     * @param newSize
     */
    public void setSize(int newSize)
    {
        if (newSize > 0)
        {
        	this.arraySize = newSize;
            this.variableSize = false;
            this.sizeData = null;
        }
    }
    
    
    public boolean isVariableSize()
    {
        return this.variableSize;
    }
    

    @Override
    public int getComponentCount()
    {
    	if (variableSize)
    	{
    		if (this.getSizeData() != null)
            {   
                DataBlock data = sizeData.getData();   		
        		if (data != null)
        			return data.getIntValue();
            }
    	}
    	
    	return this.arraySize;
    }


    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("DataArray[");
        if (variableSize)
        	text.append("?=");
        text.append(getComponentCount());
        text.append("]: " + name + "\n");
        text.append(indent + "  ");
        text.append(component.toString(indent + "  "));

        return text.toString();
    }
    
    
    @Override
    public void removeAllComponents()
    {
        this.component = null;
        this.dataBlock = null;
    }
    
    
    @Override
    public void removeComponent(int index)
    {
        removeAllComponents();
    }


    public DataValue getSizeData()
    {
        if (sizeData != null)
            return sizeData;
        
        // try to find sizeData in the parent hierarchy
        AbstractDataComponent dataComponent = this;
        for (int i=0; i<stepsToSizeData; i++)
            dataComponent = dataComponent.parent;
        sizeData = (DataValue)dataComponent.getComponent(sizeDataName);        
        return sizeData;
    }


    public void setSizeData(DataValue sizeData)
    {
        this.sizeData = sizeData;
    }
}
