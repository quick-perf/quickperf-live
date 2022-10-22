/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2021-2022 the original author or authors.
 */
package org.quickperf.web.spring.jvm;

public class ByteWatcherSingleThreadRegistry {

    public static final ByteWatcherSingleThreadRegistry INSTANCE = new ByteWatcherSingleThreadRegistry();

    private ByteWatcherSingleThreadRegistry() { }

    private static final ThreadLocal<ByteWatcherSingleThread> BYTE_WATCHER = new ThreadLocal<>();

    public void register(ByteWatcherSingleThread byteWatcherSingleThread) {
        BYTE_WATCHER.set(byteWatcherSingleThread);
    }

    public void unregister() {
        BYTE_WATCHER.remove();
    }

    public ByteWatcherSingleThread get() {
        return BYTE_WATCHER.get();
    }

}
