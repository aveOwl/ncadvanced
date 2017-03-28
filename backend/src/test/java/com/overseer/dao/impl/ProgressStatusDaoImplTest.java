package com.overseer.dao.impl;

import com.overseer.dao.ProgressStatusDao;
import com.overseer.model.ProgressStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 *  Test for {@link ProgressStatusDao}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProgressStatusDaoImplTest {

    @Autowired
    private ProgressStatusDao progressStatusDao;

    private static final String TEST_NAME = "ProgressStatus";
    private ProgressStatus savedProgressStatus;

    @Before
    public void setUp() throws Exception {
        savedProgressStatus = new ProgressStatus();
        savedProgressStatus.setName(TEST_NAME);
        savedProgressStatus.setValue(700);
        progressStatusDao.save(savedProgressStatus);
    }

    @After
    public void tearDown() throws Exception {
        savedProgressStatus = null;
    }

    @Test
    public void testAddProgressStatus() {
        // given

        // when
        ProgressStatus fromDbProgressStatus = progressStatusDao.findOne(savedProgressStatus.getId());

        // then
        assertThat(fromDbProgressStatus.getName(), is(TEST_NAME));
    }

    @Test
    public void testDeleteProgressStatus() {
        // given

        // when
        progressStatusDao.delete(savedProgressStatus);
        ProgressStatus fromDbProgressStatus = progressStatusDao.findOne(savedProgressStatus.getId());

        // then
        assertThat(fromDbProgressStatus, is(nullValue()));
    }

    @Test
    public void testFindByNameProgressStatus() {
        // given

        // when
        ProgressStatus fromDbProgressStatus = progressStatusDao.findByName(TEST_NAME);

        // then
        assertThat(fromDbProgressStatus, is(notNullValue()));
        assertThat(fromDbProgressStatus, is(savedProgressStatus));
    }


    @Test
    public void testUpdateProgressStatus() {
        // given
        String updatedName = "TestStatus";
        savedProgressStatus.setName(updatedName);

        // when
        ProgressStatus fromDbPriorityStatus = progressStatusDao.save(savedProgressStatus);

        // then
        assertThat(fromDbPriorityStatus.getName(), is(updatedName));
    }
}
