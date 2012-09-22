/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyncode.jtwig.tree;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.render.JtwigRender;
import com.lyncode.jtwig.render.JtwigVariableRender;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigVariable extends JtwigValue {
	private static Logger log = LogManager.getLogger(JtwigVariable.class);
	private String variable;

	public JtwigVariable(String variable) {
		super();
		this.variable = variable;
		log.debug("Variable: "+ variable);
	}

	public String getVariable() {
		return variable;
	}

	@Override
	public JtwigRender<? extends JtwigElement> renderer(Map<String, Object> map) {
		return new JtwigVariableRender(map, this);
	}
	
}
