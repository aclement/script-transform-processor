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

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
	@NotNull
	private String language;

	/**
	 * Text of the script.  Newlines should be escaped (\\n) and double quotes
	 * should be in pairs if the DSL parser is to let them through. If surrounded with a
	 * pair of double quotes (to pass white space through), they will be stripped off
	 * before processing.  Example: "return ""hello""+payload"
	 */
	@NotNull
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

}
