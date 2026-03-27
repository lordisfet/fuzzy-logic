Feature: Core Fuzzy Logic Mathematical Engine
  Scenario: Exact match on the peak of a triangular function
    Given an empty linguistic variable "Speed" with range 0.0 to 100.0
    And it has a triangular term "Medium" with points 20.0, 50.0, 80.0
    When I fuzzify the crisp input 50.0
    Then the membership degree for "Medium" should be 1.0