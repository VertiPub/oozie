/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.oozie.executor.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.oozie.CoordinatorActionBean;
import org.apache.oozie.ErrorCode;
import org.apache.oozie.util.ParamChecker;

/**
 * Load coordinator actions by dates.
 */
public class CoordJobGetActionsForDatesJPAExecutor implements JPAExecutor<List<CoordinatorActionBean>> {

    private String jobId = null;
    private Date startDate, endDate;

    public CoordJobGetActionsForDatesJPAExecutor(String jobId, Date startDate, Date endDate) {
        ParamChecker.notNull(jobId, "jobId");
        this.jobId = jobId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String getName() {
        return "CoordJobGetActionsForDatesJPAExecutor";
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CoordinatorActionBean> execute(EntityManager em) throws JPAExecutorException {
        List<CoordinatorActionBean> actions;
        try {
            Query q = em.createNamedQuery("GET_ACTIONS_FOR_DATES");
            q.setParameter("jobId", jobId);
            q.setParameter("startTime", new Timestamp(startDate.getTime()));
            q.setParameter("endTime", new Timestamp(endDate.getTime()));
            actions = q.getResultList();
            return actions;
        }
        catch (Exception e) {
            throw new JPAExecutorException(ErrorCode.E0603, e.getMessage(), e);
        }
    }

}
