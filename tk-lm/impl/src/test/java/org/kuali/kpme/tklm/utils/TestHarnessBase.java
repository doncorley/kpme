/**
 * Copyright 2004-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kpme.tklm.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.kuali.rice.test.TransactionalLifecycle;
 @Ignore
public class TestHarnessBase extends Assert {

    
    private static final Logger LOG = Logger.getLogger(TestHarnessBase.class);
    protected List<String> reports = new ArrayList<String>();
    @Rule public TestName testName = new TestName();
    protected TransactionalLifecycle transactionalLifecycle;

    @Before
    public void setUp() throws Exception {
	logBeforeRun();
	transactionalLifecycle = new TransactionalLifecycle();
	transactionalLifecycle.start();
    }

    @After
    public void tearDown() throws Exception {
	logAfterRun();
	try {
	    if (transactionalLifecycle != null) {
		transactionalLifecycle.stop();
	    }
	} finally {
	    transactionalLifecycle = null;
	}
    }

    protected void logBeforeRun() {
	LOG.info("##############################################################");
	LOG.info("# Starting test " + getFullTestName() + "...");
	LOG.info("# " + dumpMemory());
	LOG.info("##############################################################");
    }

    protected void logAfterRun() {
	LOG.info("##############################################################");
	LOG.info("# ...finished test " + getFullTestName());
	LOG.info("# " + dumpMemory());
	for (final String report : this.reports) {
	    LOG.info("# " + report);
	}
	LOG.info("##############################################################\n\n\n");
    }

    protected void report(final String report) {
	this.reports.add(report);
    }

    protected String getFullTestName() {
	return getClass().getSimpleName() + "." + getTestName();
    }
    
    public String getTestName() {
	return (testName != null ? testName.getMethodName() : "null");
    }

    protected String dumpMemory() {
	final long total = Runtime.getRuntime().totalMemory();
	final long free = Runtime.getRuntime().freeMemory();
	final long max = Runtime.getRuntime().maxMemory();
	return "[Memory] max: " + max + ", total: " + total + ", free: " + free;
    }

}
