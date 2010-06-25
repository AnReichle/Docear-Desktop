package org.freeplane.plugin.spreadsheet;

import org.freeplane.features.common.map.ITextTransformer;
import org.freeplane.features.common.map.NodeModel;
import org.freeplane.features.mindmapmode.MModeController;
import org.freeplane.plugin.script.ExecuteScriptException;
import org.freeplane.plugin.script.ScriptingEngine;

class SpreadsheetTextTransformer implements ITextTransformer {
	private MModeController modeController;

	SpreadsheetTextTransformer(MModeController modeController) {
		this.modeController = modeController;
	}

	public String transform(String text, NodeModel nodeModel) {
		// I don't think we need a null check here - NPE would be fine
		if (text.startsWith("="))
			return eval(text.substring(1), nodeModel);
		else
			return text;
	}

	private String eval(String script, NodeModel nodeModel) {
		final Object result = ScriptingEngine.executeScript(modeController, nodeModel, script);
		if (result == null) {
			throw new ExecuteScriptException("got null result from evaluating " + nodeModel.getID() + ", text='"
			        + script + "'");
		}
		return "eval: " + result;
	}
}
