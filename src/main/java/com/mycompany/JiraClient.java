package com.mycompany;

import static com.atlassian.jira.rest.client.api.domain.IssueFieldId.*;
import java.net.URI;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.IssueFieldId;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

public class JiraClient {

    private String userName;
    private String password;
    private URI jiraUrl;

    public JiraClient(String userName, String password, URI jiraUrl) {
        this.userName = userName;
        this.password = password;
        this.jiraUrl = jiraUrl;
    }

    public JiraRestClient getJiraRestClient() {
        JiraRestClient jiraRestClient = null;
        JiraRestClientFactory restClientFactory = new AsynchronousJiraRestClientFactory();
        jiraRestClient = restClientFactory.createWithBasicHttpAuthentication(jiraUrl, userName, password);
        return jiraRestClient;
    }

    public BasicIssue createIssue(JiraRestClient jiraRestClient, String projectKey, String issueName, String title, String description) {
        final IssueInput newIssue = IssueInput.createWithFields(
                new FieldInput(DESCRIPTION_FIELD, description),
                new FieldInput(IssueFieldId.PROJECT_FIELD, ComplexIssueInputFieldValue.with("key", projectKey)),
                new FieldInput(ISSUE_TYPE_FIELD, ComplexIssueInputFieldValue.with("name", issueName)),
                new FieldInput(SUMMARY_FIELD, title)
        );

        return createIssue(jiraRestClient, newIssue);
    }

    public BasicIssue createIssue(JiraRestClient jiraRestClient, String projectKey, long issueId, String title, String description) {
        final IssueInput newIssue = IssueInput.createWithFields(
                new FieldInput(DESCRIPTION_FIELD, description),
                new FieldInput(IssueFieldId.PROJECT_FIELD, ComplexIssueInputFieldValue.with("key", projectKey)),
                new FieldInput(ISSUE_TYPE_FIELD, ComplexIssueInputFieldValue.with("id", issueId)),
                new FieldInput(SUMMARY_FIELD, title)
        );

        return createIssue(jiraRestClient, newIssue);
    }

    private BasicIssue createIssue(JiraRestClient jiraRestClient, IssueInput issueInput) throws RestClientException {
        final Promise<BasicIssue> promise;
        final IssueRestClient issueClient = jiraRestClient.getIssueClient();

        promise = issueClient.createIssue(issueInput);

        return promise.claim();
    }
}
