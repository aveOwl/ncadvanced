package com.overseer.dao.impl;

import com.overseer.dao.HistoryDAO;
import com.overseer.dao.RequestDao;
import com.overseer.dao.UserDao;
import com.overseer.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  Test for {@link HistoryDAO}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class HistoryDaoImplTest {

    @Autowired
    private HistoryDAO historyDAO;

    @Autowired
    private RequestDao requestDao;

    @Autowired
    private UserDao userDao;

    private Long oldAssigneeId;

    private static final String TEST_TITLE_1 = "title 1";
    private static final String TEST_TITLE_2 = "title 2";
    private static final String TEST_DESCRIPTION_1 = "desc 1";
    private static final String TEST_DESCRIPTION_2 = "desc 2";

    private Request request;
    private User assignee;

    private void updateRequest(){
        request.setTitle(TEST_TITLE_2);
        request.setDescription(TEST_DESCRIPTION_2);

        Role assigneeRole = new Role("office manager");
        assigneeRole.setId(11L);
        assignee = new User("office manager 2", "office manager 2", "office manager 2", "office manager 2@email.com", assigneeRole);
        assignee = this.userDao.save(assignee);
        request.setAssignee(assignee);
    }

    @Before
    public void createReporter() throws Exception{
        Role reporterRole = new Role("employee");
        reporterRole.setId(12L);
        User reporter = new User("EmployeeName", "EmployeeSurname", "EmployeePassword", "employeeEmail@email.com", reporterRole);
        reporter = this.userDao.save(reporter);

        Role assigneeRole = new Role("office manager");
        assigneeRole.setId(11L);
        assignee = new User("office manager 1", "office manager 1", "office manager 1", "office manager 1@email.com", assigneeRole);
        assignee = this.userDao.save(assignee);
        oldAssigneeId = assignee.getId();

        Role changerRole = new Role("admin");
        changerRole.setId(10L);
        User lastChanger = new User("Bruce", "li", "qwerty123", "bruceli@email.com", changerRole);
        lastChanger = this.userDao.save(lastChanger);

        PriorityStatus priority = new PriorityStatus("Normal", 200);
        priority.setId(2L);

        ProgressStatus progress = new ProgressStatus("Free", 200);
        progress.setId(5L);

        request = new Request();
        request.setTitle(TEST_TITLE_1);
        request.setDescription(TEST_DESCRIPTION_1);
        request.setParentId(null);
        request.setEstimateTimeInDays(3);
        request.setDateOfCreation(LocalDateTime.of(2017, 6, 21, 12, 30));
        request.setReporter(reporter);
        request.setAssignee(assignee);
        request.setLastChanger(lastChanger);
        request.setPriorityStatus(priority);
        request.setProgressStatus(progress);

        request = requestDao.save(request);
    }
    @Test
    public void findAllForEntityTestForNumberOfHistoryRecords() throws Exception {
        // given
        updateRequest();

        Role assigneeRole = new Role("office manager");
        assigneeRole.setId(11L);
        assignee = new User("office manager 2", "office manager 2", "office manager 2", "office manager 2@email.com", assigneeRole);
        assignee = this.userDao.save(assignee);
        request.setAssignee(assignee);

        // when
        requestDao.save(request);
        List<History> allHistoryForRequest = historyDAO.findAllForEntity(request.getId());

        // then
        assertThat(allHistoryForRequest.size(), is(3));
    }

    @Test
    public void findAllForEntityTestForCheckOldAndNewValuesInFirstHistoryRecord() throws Exception{
        // given
        updateRequest();

        // when
        requestDao.save(request);
        List<History> allHistoryForRequest = historyDAO.findAllForEntity(request.getId());

        // then
        assertThat(allHistoryForRequest.get(0).getColumnName(), is("title"));
        assertThat(allHistoryForRequest.get(0).getOldValue(), is(TEST_TITLE_1));
        assertThat(allHistoryForRequest.get(0).getNewValue(), is(TEST_TITLE_2));
        assertThat(allHistoryForRequest.get(0).getRecordId(), is(request.getId()));
    }

    @Test
    public void findAllForEntityTestForCheckOldAndNewValuesInSecondHistoryRecord() throws Exception{
        // given
        updateRequest();

        // when
        requestDao.save(request);
        List<History> allHistoryForRequest = historyDAO.findAllForEntity(request.getId());

        // then
        assertThat(allHistoryForRequest.get(1).getColumnName(), is("description"));
        assertThat(allHistoryForRequest.get(1).getOldValue(), is(TEST_DESCRIPTION_1));
        assertThat(allHistoryForRequest.get(1).getNewValue(), is(TEST_DESCRIPTION_2));
        assertThat(allHistoryForRequest.get(1).getRecordId(), is(request.getId()));
    }

    @Test
    public void findAllForEntityTestForCheckOldAndNewValuesInThirdHistoryRecord() throws Exception{
        // given
        updateRequest();

        // when
        requestDao.save(request);
        List<History> allHistoryForRequest = historyDAO.findAllForEntity(request.getId());

        // then
        assertThat(allHistoryForRequest.get(2).getColumnName(), is("assignee_id"));
        assertThat(new Long(allHistoryForRequest.get(2).getOldValue()), is(oldAssigneeId));
        assertThat(new Long(allHistoryForRequest.get(2).getNewValue()), is(assignee.getId()));
        assertThat(allHistoryForRequest.get(2).getRecordId(), is(request.getId()));
    }
}