# Mobimeo Backend Engineering Challenge

## Problem

In the fictional city of Verspaetung, public transport is notoriously unreliable. To tackle the problem, the city council has decided to make the public transport timetable and delay information public, opening up opportunities for innovative use cases.

You are given the task of writing a web API to expose the Verspaetung public transport information.

As a side note, the city of Verspaetung has been built on a strict grid - all location information can be assumed to be from a cartesian coordinate system.

## Data

The Verspaetung public transport information is comprised of 4 CSV files:

- `lines.csv` - the public transport lines.
- `stops.csv` - the stops along each line.
- `times.csv` - the time vehicles arrive & depart at each stop. The timetimestamp is in the format of `HH:MM:SS`.
- `delays.csv` - the delays for each line. This data is static and assumed to be valid for any time of day.

## Challenge

Build a web API which provides the following features:

- Find a vehicle for a given time and X & Y coordinates `Implemented`
- Return the vehicle arriving next at a given stop `Not implemented`
- Indicate if a given line is currently delayed `Not implemented`

Endpoints should be available via port `8081`

## Implementation

Please complete the challenge in either Kotlin or Scala. You're free to use frameworks or libraries of your choice.

Make sure to follow general software development best practices and best practices of the respective language.

## Solution
Current solution contains only one part of the three, in particular the endpoint for fetching a vihecle for a given time and X & Y coordinates.

`/vehicle?time=<HH:MM:SS>&x=<int>&y=<int>`

## How to run
Simply run `sbt run` under the project folder, the server will be running on `localhost:8081`

### Request Examples
Getting vehicle for the given time and stop
`curl -X GET "localhost:8081/vehicle?time=10:05:00&x=1&y=7"` will give `M4`

Getting list of vehicles for the given time and stop 
`curl -X GET "localhost:8081/vehicle?time=10:09:00&x=2&y=9"` will give `200,S75`