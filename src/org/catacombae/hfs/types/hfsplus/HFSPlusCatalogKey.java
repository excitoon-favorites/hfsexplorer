/*-
 * Copyright (C) 2006-2007 Erik Larsson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.hfs.types.hfsplus;

import org.catacombae.csjc.structelements.Dictionary;
import org.catacombae.util.Util;
import org.catacombae.hfs.FastUnicodeCompare;
import java.io.PrintStream;
import org.catacombae.csjc.DynamicStruct;
import org.catacombae.csjc.StructElements;

/** This class was generated by CStructToJavaClass. */
public class HFSPlusCatalogKey extends BTKey implements DynamicStruct, StructElements {
    /*
     * struct HFSPlusCatalogKey
     * size: 518 bytes
     * description: 
     * 
     * BP  Size  Type              Identifier  Description
     * ---------------------------------------------------
     * 0   2     UInt16            keyLength              
     * 2   4     HFSCatalogNodeID  parentID               
     * 6   ~512  HFSUniStr255      nodeName    Size of string is max 512 bytes. Actual size: keyLength-4.
     */
    private static final int MAX_STRUCTSIZE = 518;
    
    private final byte[] keyLength = new byte[2];
    private final HFSCatalogNodeID parentID;
    private final HFSUniStr255 nodeName;
    
    public HFSPlusCatalogKey(byte[] data, int offset) {
	System.arraycopy(data, offset+0, keyLength, 0, 2);
	parentID = new HFSCatalogNodeID(data, offset+2);
	nodeName = new HFSUniStr255(data, offset+6);
    }
    
    public HFSPlusCatalogKey(HFSCatalogNodeID parentID, HFSUniStr255 nodeName) {
	this.parentID = parentID;
	this.nodeName = nodeName;
	System.arraycopy(Util.toByteArrayBE((short)(4+nodeName.length())), 0, keyLength, 0, 2);
    }

    public HFSPlusCatalogKey(int parentIDInt, String nodeNameString) {
	parentID = new HFSCatalogNodeID(parentIDInt);
	nodeName = new HFSUniStr255(nodeNameString);
	System.arraycopy(Util.toByteArrayBE((short)(4+nodeName.length())), 0, keyLength, 0, 2);
    }
    
    @Override
    public short getKeyLength() { return Util.readShortBE(keyLength); }
    public HFSCatalogNodeID getParentID() { return parentID; }
    public HFSUniStr255 getNodeName() { return nodeName; }

    @Override
    public byte[] getBytes() {
	byte[] result = new byte[length()];
	System.arraycopy(keyLength, 0, result, 0, 2);
	System.arraycopy(Util.toByteArrayBE(parentID.toInt()), 0, result, 2, 4);
	System.arraycopy(nodeName.getBytes(), 0, result, 6, nodeName.length());
	return result;
    }

    @Override
    public int compareTo(BTKey btk) {
	if(btk instanceof HFSPlusCatalogKey) {
	    HFSPlusCatalogKey catKey = (HFSPlusCatalogKey) btk;
	    if(Util.unsign(getParentID().toInt()) == Util.unsign(catKey.getParentID().toInt()))
		return FastUnicodeCompare.compare(nodeName.getUnicode(), catKey.getNodeName().getUnicode());
	    else if(Util.unsign(getParentID().toInt()) < Util.unsign(catKey.getParentID().toInt()))
		return -1;
	    else
		return 1;
	}
	else {
	    return super.compareTo(btk);
	}
    }
    
    public void printFields(PrintStream ps, String prefix) {
	ps.println(prefix + " keyLength: " + Util.unsign(getKeyLength()));
	ps.println(prefix + " parentID: ");
	getParentID().print(ps, prefix+"  ");
	ps.println(prefix + " nodeName: ");
	getNodeName().print(ps, prefix+"  ");
    }
    
    public void print(PrintStream ps, String prefix) {
	ps.println(prefix + "HFSPlusCatalogKey:");
	printFields(ps, prefix);
    }
    
    @Override
    public int length() { return occupiedSize(); }
    
    /* @Override */
    public int occupiedSize() { return 2+Util.unsign(getKeyLength()); }
    
    /* @Override */
    public int maxSize() { return MAX_STRUCTSIZE; }

    /* @Override */
    public Dictionary getStructElements() {
        DictionaryBuilder db = new DictionaryBuilder(HFSPlusCatalogKey.class.getSimpleName());
        
        db.addUIntBE("keyLength", keyLength);
        db.add("parentID", parentID.getStructElements());
        db.add("nodeName", nodeName.getStructElements());
        
        return db.getResult();
    }
}
