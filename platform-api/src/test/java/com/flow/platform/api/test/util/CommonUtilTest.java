/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.platform.api.test.util;

import com.flow.platform.api.test.TestBase;
import com.flow.platform.api.util.CommonUtil;
import com.flow.platform.cmd.Log;
import com.flow.platform.util.Logger;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * @author yh@firim
 */
public class CommonUtilTest extends TestBase {

    @Test
    public void should_generate_success() throws Throwable {
        int numOfThread = 10;
        int numOfIdPerThread = 1000;

        CountDownLatch latch = new CountDownLatch(numOfThread);
        Executor pool = Executors.newFixedThreadPool(numOfThread);
        Map<BigInteger, String> hashMap = new ConcurrentHashMap<>(numOfThread * numOfIdPerThread);

        for (int i = 0; i < numOfThread; i++) {
            pool.execute(() -> {
                for (int j = 0; j < numOfIdPerThread; j++) {
                    BigInteger id = CommonUtil.randomId();
                    hashMap.put(id, String.valueOf(j));
                }
                latch.countDown();
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        Assert.assertEquals(numOfIdPerThread * numOfThread, hashMap.size());
    }


    @Test
    public void should_show_jfiglet_success() {
        String message = CommonUtil.showJfigletMessage("Hello World");
        Logger logger = new Logger(CommonUtilTest.class);
        logger.trace(message);

        Assert.assertNotNull(message);
    }
}
