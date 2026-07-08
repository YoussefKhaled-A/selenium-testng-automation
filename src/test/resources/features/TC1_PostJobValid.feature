Feature: Post Job

  Scenario: TC1 Post Job Valid
    Given employer is logged in
    When employer posts a valid job
    Then job should be posted successfully
