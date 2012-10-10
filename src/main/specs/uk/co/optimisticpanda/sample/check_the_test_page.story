We will allow users to search for interesting topics

Meta:
@sections 
Test Page > Search 
Test Page > User Functions

@Link 
JIRAID-01 : User Search [JIRAID-01] 

Narrative: 
In order to find all of the information that I want to
As a user of the page
I want to be able to search for any term

Scenario: Users are able to search for what they want

Given I have opened the test page
When I search for "test"
Then I "should" be able to see "test" in the searchbox

Scenario: Users will not view things they did not search for

Given I have opened the test page
When I search for "test"
Then I "should not" be able to see "dragons" in the searchbox


