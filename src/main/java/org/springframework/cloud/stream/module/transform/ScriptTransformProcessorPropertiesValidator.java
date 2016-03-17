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

import org.springframework.core.io.Resource;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * 
 * @author Andy Clement
 */
public class ScriptTransformProcessorPropertiesValidator implements Validator {

	@Override
	public boolean supports(Class<?> type) {
		return type == ScriptTransformProcessorProperties.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ScriptTransformProcessorProperties properties = (ScriptTransformProcessorProperties)target;
		String script = properties.getScript();
		Resource scriptfile = properties.getScriptfile();
		String language = properties.getLanguage();
		if (script != null && scriptfile != null) {
			errors.rejectValue(null, "The script and scriptfile property should not both be specified together");
		}
		if (script == null && scriptfile == null) {
			throw new IllegalArgumentException(
					"Either the script property or the scriptfile property should be specified");
		}
		if (language == null) {
			if (scriptfile == null) {
				throw new IllegalArgumentException(
						"When specifying the script property, the language must be specified.");
			} else {
				String scriptFilename = scriptfile.getFilename();
				String suffix = scriptFilename.substring(scriptFilename.lastIndexOf("."));
				switch (suffix) {
				case ".js":
				case ".javascript":
					language = "javascript";
					break;
				case ".groovy":
					language = "groovy";
					break;
				case ".rb":
					language = "ruby";
					break;
				case ".py":
					language = "python";
					break;
				}
				if (language == null) {
					throw new IllegalArgumentException(
							"Unable to determine language from scriptfile name: " + scriptFilename);
				}
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "host", "host.empty");
//		ValidationUtils.rejectIfEmpty(errors, "port", "port.empty");
//		SampleProperties properties = (SampleProperties) o;
//		if (properties.getHost() != null
//				&& !this.pattern.matcher(properties.getHost()).matches()) {
//			errors.rejectValue("host", "Invalid host");
//		}
	}

}
