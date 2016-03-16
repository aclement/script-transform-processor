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

import org.junit.Test;
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

	@WebIntegrationTest({"script=function add(a,b) { return a+b;};add(1,3)", "language=js", "variables=limit=5\\n foo=\\\\\40WORLD"})
	public static class UsingScriptIntegrationTests extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testJavaScriptFunctions() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is(4L)));
		}

	}
	
	@WebIntegrationTest({"script=payload.substring(0, limit as int) + foo", "variables=limit=5\\n foo=\\\\\40WORLD"})
	public static class UsingScriptIntegrationTests4 extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testGroovyBasic() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello WORLD")));
		}

	}

	@WebIntegrationTest({"script=payload+foo", "variables=limit=5\\n foo=\\\\\40WORLD"})
	public static class UsingScriptIntegrationTests2 extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testGroovyAlternative() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world WORLD")));
		}

	}
	

	@WebIntegrationTest({"script=payload+foo", "language=js", "variables=limit=5\\n foo=\\\\\40WORLD"})
	public static class UsingScriptIntegrationTests3 extends ScriptTransformProcessorIntegrationTests {

		@Test
		public void testJavaScript() {
			channels.input().send(new GenericMessage<Object>("hello world"));
			assertThat(collector.forChannel(channels.output()), receivesPayloadThat(is("hello world WORLD")));
		}

	}
	



}
