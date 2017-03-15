package com.overseer.service.impl;

import com.overseer.dao.RequestDao;
import com.overseer.dao.UserDao;
import com.overseer.model.*;
import com.overseer.service.RequestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/*@Transactional*/
public class RequestServiceImplTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RequestDao requestDao;

    private Request request;
    private User assignee;
    private User reporter;
    private User lastChanger;
    private ProgressStatus progress;
    private PriorityStatus priority;
    private List<Long> requestsGroupIds;

    @Autowired
    private RequestService requestService;

    @Before
    public void setUp() throws Exception {
        requestsGroupIds = Arrays.asList(113L, 114L, 115L);

        Role reporterRole = new Role("employee");
        reporterRole.setId(12L);
        reporter = new User();
        reporter.setFirstName("Tom");
        reporter.setLastName("Hardy");
        reporter.setPassword("gunner12");
        reporter.setEmail( "some@email.com");
        reporter.setRole(reporterRole);
        reporter = this.userDao.save(reporter);


        Role changerRole = new Role("admin");
        changerRole.setId(10L);
        lastChanger = new User();
        lastChanger.setFirstName("Bruce");
        lastChanger.setLastName("li");
        lastChanger.setPassword("qwerty123");
        lastChanger.setEmail("bruceli@email.com");
        lastChanger.setRole(changerRole);
        lastChanger = this.userDao.save(lastChanger);

        Role assigneeRole = new Role("office manager");
        assigneeRole.setId(11L);
        assignee = new User();
        assignee.setFirstName("Tom");
        assignee.setLastName("Cruz");
        assignee.setPassword("cruzXXX");
        assignee.setEmail("cruzooo@email.com");
        assignee.setRole(assigneeRole);
        assignee = this.userDao.save(assignee);

        priority = new PriorityStatus("Normal", 200);
        priority.setId(2L);

        progress = new ProgressStatus("Free", 200);
        progress.setId(5L);

        request = new Request();
        request.setTitle("Do something");
        request.setDescription("Do some great work");
        request.setParentId(null);
        request.setEstimateTimeInDays(3);
        request.setDateOfCreation(LocalDateTime.of(2015, 6, 21, 12, 30));
        request.setReporter(reporter);
        request.setAssignee(assignee);
        request.setLastChanger(lastChanger);
        request.setPriorityStatus(priority);
        request.setProgressStatus(progress);

        this.requestDao.save(request);
    }

    @Test
    public void joinRequestsIntoParent() throws Exception {
        Request parent = requestService.joinRequestsIntoParent(requestsGroupIds, request);

        Assert.assertEquals(parent.getId(), request.getId());

        Request firstChildRequest = requestService.findOne(requestsGroupIds.get(0));
        Assert.assertEquals(request.getId(), firstChildRequest.getParentId());

        Request secondChildRequest = requestService.findOne(requestsGroupIds.get(0));
        Assert.assertEquals(request.getId(), secondChildRequest.getParentId());

        Request thirdChildRequest = requestService.findOne(requestsGroupIds.get(0));
        Assert.assertEquals(request.getId(), thirdChildRequest.getParentId());
    }
}