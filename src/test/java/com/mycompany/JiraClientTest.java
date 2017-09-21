package com.mycompany;

import static org.junit.Assert.assertNotNull;
import java.net.URI;
import java.net.URISyntaxException;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import org.junit.Before;
import org.junit.Test;

public class JiraClientTest {

    JiraClient jiraClient;
    JiraRestClient jiraRestClient;

    private static final String PROJECT_KEY = "IDP";

    private static final long TASK = 10002L;
    private static final String TASK_S = "Task";

    private static final String JIRA_URL = "https://kkk.atlassian.net/";
    private static final String INACCESSIBLE_URL = "https://XXX.atlassian.net/XYZ";
    private static final String USER_NAME = "email.com";
    private static final String INVALID_USER_NAME = "xyz@abc.net";
    private static final String PASSWORD = "password";
    private static final String INVALID_PASSWORD = "XYZ";
    private static final String DESCRIPTION = "Description";
    private static final String TITLE = "Title";

    @Before
    public void setUp() throws Exception {
        instantiateJiraObjects(USER_NAME, PASSWORD, JIRA_URL);
    }

    private void instantiateJiraObjects(String userName, String password, String url) {
        try {
            jiraClient = new JiraClient(userName, password, new URI(url));
        } catch (URISyntaxException e) {
            System.out.println("Invalid URL");
        }
        jiraRestClient = jiraClient.getJiraRestClient();
    }

    @Test
    public void shouldReturnValidBasicIssueWhenHaveValidParamsWithTaskInIdFormat() {
        BasicIssue newIssue = jiraClient.createIssue(jiraRestClient, PROJECT_KEY, TASK, TITLE, DESCRIPTION);
        assertNotNull(newIssue);
    }
    @Test(expected = RestClientException.class)
    public void shouldRaiseExceptionWhenHaveInvalidTaskId() {
        jiraClient.createIssue(jiraRestClient, PROJECT_KEY, 123L, TITLE, DESCRIPTION);
    }

    @Test
    public void shouldReturnValidBasicIssueWhenHaveValidParamsWithTaskInStringFormat() {
        BasicIssue newIssue = jiraClient.createIssue(jiraRestClient, PROJECT_KEY, TASK_S, TITLE, DESCRIPTION);
        assertNotNull(newIssue);
    }

    @Test(expected = RestClientException.class)
    public void shouldRaiseExceptionWhenHaveInvalidTaskName() {
        jiraClient.createIssue(jiraRestClient, PROJECT_KEY, "task", TITLE, DESCRIPTION);
    }

    @Test(expected = RestClientException.class)
    public void shouldRaiseExceptionWhenHaveInaccessibleUrl() {
        instantiateJiraObjects(USER_NAME, PASSWORD, INACCESSIBLE_URL);
        jiraClient.createIssue(jiraRestClient, PROJECT_KEY, TASK, TITLE, DESCRIPTION);
    }

    @Test(expected = RestClientException.class)
    public void shouldRaiseExceptionWhenHaveInvalidPassword() {
        instantiateJiraObjects(USER_NAME, INVALID_PASSWORD, JIRA_URL);
        jiraClient.createIssue(jiraRestClient, PROJECT_KEY, TASK, TITLE, DESCRIPTION);
    }

    @Test(expected = RestClientException.class)
    public void shouldRaiseExceptionWhenHaveInvalidUserName() {
        instantiateJiraObjects(INVALID_USER_NAME, PASSWORD, JIRA_URL);
        jiraClient.createIssue(jiraRestClient, PROJECT_KEY, TASK, TITLE, DESCRIPTION);
    }

}