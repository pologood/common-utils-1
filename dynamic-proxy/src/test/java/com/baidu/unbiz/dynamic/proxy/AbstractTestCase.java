package com.baidu.unbiz.dynamic.proxy;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;

import com.baidu.unbiz.common.able.Buildable;
import com.baidu.unbiz.common.io.SerializationUtil;
import com.baidu.unbiz.dynamic.proxy.Invocation;
import com.baidu.unbiz.dynamic.proxy.util.ProxyUtil;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午11:31:29
 */
public abstract class AbstractTestCase {

    protected void assertSerializable(Object o) {
        assertTrue(o instanceof Serializable);
        try {
            SerializationUtil.copy((Serializable) o);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected MockInvocationBuilder mockInvocation(Class<?> type, String name, Class<?>...argumentTypes) {
        try {
            return new MockInvocationBuilder(type.getMethod(name, argumentTypes));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method not found.", e);
        }
    }

    protected static final class MockInvocationBuilder implements Buildable<Invocation> {
        private final Method method;
        private Object[] arguments = ProxyUtil.EMPTY_ARGUMENTS;
        private Object returnValue = null;

        public MockInvocationBuilder(Method method) {
            this.method = method;
        }

        public MockInvocationBuilder withArguments(Object...arguments) {
            this.arguments = arguments;
            return this;
        }

        public MockInvocationBuilder returning(Object value) {
            this.returnValue = value;
            return this;
        }

        @Override
        public Invocation build() {
            return new MockInvocation(method, returnValue, arguments);
        }
    }
}
