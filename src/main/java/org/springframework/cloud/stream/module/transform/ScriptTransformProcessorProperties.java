/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.stream.module.transform;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * Configuration properties for the Script Transform Processor module.
 *
 * @author Andy Clement
 */
@ConfigurationProperties
public class ScriptTransformProcessorProperties {

	/**
	 * Language of the text in the script property or the
	 * contents of file referenced by the script file property.
	 * This is used to call the {@link ScriptExecutorFactory.getScriptExecutor(language)} method.
	 * Currently supported are 'groovy' (the default), 'javascript', 'ruby' and 'python'.
	 */
	private String language;

	/**
	 * Reference to a script used to process messages. Specify
	 * the script file property <b>or</b> the script property but not both.
	 */
	private Resource scriptfile;

	/**
	 * The actual text of a script. Specify the script file property
	 * <b>or</b> the script property but not both.
	 */
	private String script;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Resource getScriptfile() {
		return scriptfile;
	}

	public void setScriptfile(Resource scriptfile) {
		this.scriptfile = scriptfile;
	}
}
