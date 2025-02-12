/*
 * Copyright (c) 2016-2022 Michael Zhang <yidongnan@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.devh.boot.grpc.client.inject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;

class GrpcClientConstructorInjection {

    public static final String BEAN_NAME = "grpcClientConstructorInjection";

    private final List<Registry> injections = new ArrayList<>();

    static class Registry {

        private final Class<?> stubClazz;
        private final GrpcClient client;
        private final Class<?> targetClazz;
        private final BeanDefinition targetBeanDefinition;
        private final int constructorArgumentIndex;

        public Registry(final Class<?> stubClazz, final GrpcClient client, final Class<?> targetClazz,
                final BeanDefinition targetBeanDefinition, final int constructorArgumentIndex) {
            this.stubClazz = stubClazz;
            this.client = client;
            this.targetClazz = targetClazz;
            this.targetBeanDefinition = targetBeanDefinition;
            this.constructorArgumentIndex = constructorArgumentIndex;
        }

        public Class<?> getStubClass() {
            return this.stubClazz;
        }

        public GrpcClient getClient() {
            return this.client;
        }

        public Class<?> getTargetClazz() {
            return this.targetClazz;
        }

        public BeanDefinition getTargetBeanDefinition() {
            return this.targetBeanDefinition;
        }

        public int getConstructorArgumentIndex() {
            return this.constructorArgumentIndex;
        }
    }

    public List<Registry> getRegistries() {
        return this.injections;
    }

    public GrpcClientConstructorInjection add(final Registry injection) {
        this.injections.add(injection);
        return this;
    }

    public boolean isEmpty() {
        return this.injections.isEmpty();
    }

}
