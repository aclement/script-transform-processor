/*
 * Copyright 2015-2016 the original author or authors.
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration Tests for the Script Transform Processor.
 *
 * @author Eric Bottard
 * @author Marius Bogoevici
 * @author Artem Bilan
 * @author Andy Clement
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScriptTransformProcessorApplication.class)
@WebIntegrationTest(randomPort = true)
@DirtiesContext
public abstract class ScriptTransformProcessorIntegrationTests {

	@Autowired
	@Bindings(ScriptTransformProcessor.class)
	protected Processor channels;

	@Autowired
	protected MessageCollector collector;

	@WebIntegrationTest({ "script=payload+foo", "language=js", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class JavascriptScriptProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testJavascriptSimple() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world WORLD")));
		}

	}

	@WebIntegrationTest({ "script=function add(a,b) { return a+b;};add(1,3)", "language=js",
			"variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class JavascriptScriptProperty1Tests extends ScriptTransformProcessorIntegrationTests {
		@Test
		public void testJavascriptFunctions() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(4L)));
		}
	}

	@WebIntegrationTest({ "script=payload+foo", "language=groovy", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class GroovyScriptProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testGroovyBasic() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world WORLD")));
		}

	}

	@WebIntegrationTest({ "script=payload.substring(0, limit as int) + foo", "language=groovy",
			"variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class GroovyScriptProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testGroovyComplex() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello WORLD")));
		}

	}

	@WebIntegrationTest({ "script=return \"\"#{payload.upcase}\"\"", "language=ruby",
			"variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class RubyScriptProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testRubyScript() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("HELLO WORLD")));
		}

	}

	@WebIntegrationTest({ "script=\"def foo(x)\\n  return x+5\\nend\\nfoo(payload)\\n\"", "language=ruby",
			"variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class RubyScriptProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testRuby() {
			channels.input().send(new GenericMessage<Object>(9));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(14L)));
		}

	}

	@WebIntegrationTest({ "script=\"def multiply(x,y):\\n  return x*y\\nanswer = multiply(payload,5)\\n\"",
			"language=python", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class PythonScriptProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testPython() {
			channels.input().send(new GenericMessage<Object>(6));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(30)));
		}

	}

	@WebIntegrationTest({ "scriptfile=basic.py", "language=python", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class PythonScriptFileProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testPython() {
			channels.input().send(new GenericMessage<Object>(6));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(48)));
		}

	}

	@WebIntegrationTest({ "scriptFile=basic.py", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class PythonScriptFileProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testPython() {
			channels.input().send(new GenericMessage<Object>(6));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(48)));
		}

	}

	@WebIntegrationTest({ "scriptfile=script.groovy", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class GroovyScriptfileProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testGroovyBasic() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world WORLD")));
		}

	}

	@WebIntegrationTest({ "scriptfile=script.groovy", "language=groovy", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class GroovyScriptfileProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testGroovyBasic() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world WORLD")));
		}

	}

	@WebIntegrationTest({ "scriptfile=basic.rb", "language=ruby", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class RubyScriptfileProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testRubyBasic() {
			channels.input().send(new GenericMessage<Object>(32));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(47L)));
		}

	}

	@WebIntegrationTest({ "scriptfile=basic.rb", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class RubyScriptfileProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testRubyBasic() {
			channels.input().send(new GenericMessage<Object>(32));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(47L)));
		}

	}

	@WebIntegrationTest({ "scriptfile=basic.js", "language=js", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class JavascriptScriptfileProperty1Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testJavascriptSimple() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world3")));
		}

	}

	@WebIntegrationTest({ "scriptfile=basic.js", "variables=limit=5\\n foo=\\\\\40WORLD" })
	public static class JavascriptScriptfileProperty2Tests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testJavascriptSimple() {
			channels.input().send(new GenericMessage<Object>(4));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(7.0)));
		}

	}

}
