/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import org.vast.util.ReaderException;
import org.w3c.dom.Node;


/**
 * <p>
 * Exception generated by implementations of XML reading interfaces
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Nov 18, 2012
 * @version 1.0
 */
public class XMLReaderException extends ReaderException
{
    private static final long serialVersionUID = 3623156292080498179L;
    protected Node nodeWithError;
    

    public XMLReaderException(Exception e)
    {
        super(e);
    }
    
    
    public XMLReaderException(String message)
    {
        super(message);
    }
       
    
    public XMLReaderException(String message, Exception e)
    {
        super(message, e);
    }
    
    
    public XMLReaderException(String message, Node nodeWithError)
    {
        super(message);
        this.nodeWithError = nodeWithError;
    }
    
    
    public XMLReaderException(String message, Node nodeWithError, Exception e)
    {
        super(message, e);
        this.nodeWithError = nodeWithError;
    }
    
    
    public XMLReaderException(Node nodeWithError)
    {
        super("");
        this.nodeWithError = nodeWithError;
    }
    
    
    public XMLReaderException(Node nodeWithError, Exception e)
    {
        super(e);
        this.nodeWithError = nodeWithError;
    }


    @Override
    public String getMessage()
    {
        String msg = super.getMessage();
        
        StringBuilder nodePath = new StringBuilder();
        if (nodeWithError != null)
        {
            Node currentNode = nodeWithError;
            while (currentNode != null)
            {
                nodePath.append('/');
                nodePath.append(currentNode.getNodeName());
                currentNode = currentNode.getParentNode();
            }
        }
        
        if (nodeWithError != null)
        {
            if (msg == null)
                return "Error while reading node " + nodePath.toString();
            else
                return msg + " (" + nodePath.toString() + ")";
        }
        
        return msg;
    }
}
