/**
 * author: Marcel Genzmehr
 * 21.07.2011
 */
package org.freeplane.plugin.workspace.io.node;

import java.io.File;
import java.io.IOException;

import org.freeplane.core.util.LogUtils;
import org.freeplane.features.mode.Controller;
import org.freeplane.plugin.workspace.config.node.WorkspaceNode;
import org.freeplane.plugin.workspace.controller.IWorkspaceNodeEventListener;
import org.freeplane.plugin.workspace.controller.WorkspaceNodeEvent;

/**
 * 
 */
public class DefaultFileNode extends WorkspaceNode implements IWorkspaceNodeEventListener{
	
	private File file;
	
	/***********************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************/
	
	/**
	 * @param name
	 */
	public DefaultFileNode(final String name, final File file) {
		super(file.getName());
		this.setName(name);
		this.file = file;
		
	}

	/***********************************************************************************
	 * METHODS
	 **********************************************************************************/
	
	public File getFile() {
		return this.file;
	}
	
	public boolean rename(final String name) {
		File newFile = new File(getFile().getParentFile() + File.separator + name);
		if(getFile().renameTo(newFile)) {
			this.file = newFile;
			return true;
		}
		return false;
	}
	
	public void relocateFile(final File parentFolder) {
		File newFile = new File(parentFolder.getPath() + File.separator + getName());
		if(newFile.exists()) {
			this.file = newFile;
		}
	}
	
	/***********************************************************************************
	 * REQUIRED METHODS FOR INTERFACES
	 **********************************************************************************/
	
	public void handleEvent(WorkspaceNodeEvent event) {		
		if(event.getType() == WorkspaceNodeEvent.WSNODE_CHANGED) {
			if(rename(event.getBaggage().toString())) {
				setName(event.getBaggage().toString());
			}
			else {
				LogUtils.warn("Could not rename File("+getName()+") to File("+event.getBaggage()+")");
			}
			
		}
		else if(event.getType() == WorkspaceNodeEvent.WSNODE_OPEN_DOCUMENT) {
			try {
				Controller.getCurrentController().getViewController().openDocument(getFile().toURI());
			}
			catch (IOException e) {
				LogUtils.warn("could not open document ("+getFile()+")", e);
			}
		}
	}
	
	public String getTagName() {
		return null;
	}
}