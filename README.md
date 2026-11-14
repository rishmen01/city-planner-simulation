# City Planner – Urban Structure Simulation

This is a Java console application. The program loads a two-dimensional terrain grid, evaluates building placement against terrain and zoning restrictions, calculates construction costs, and generates city layouts using interchangeable building strategies.

## Features

- Loads a terrain grid from a text file.
- Displays terrain types and zoning-rule summaries.
- Evaluates whether a structure can be placed at a selected grid coordinate.
- Calculates base construction costs and terrain/zoning cost adjustments.
- Generates city layouts using uniform, random, and centre-oriented building strategies.
- Applies multiple zoning rules to each grid square.

## Technologies

- Java
- Gradle
- Java standard library (`java.util`, `java.io`, and `java.util.logging`)
- PMD with a project-specific rule set

## Design patterns and design approach

### Strategy Pattern

`Grid.BuildStrategy` defines the building-strategy interface. `UniformBuild`, `RandomBuild`, and `CentralBuild` provide alternative ways to choose a structure for each grid cell. The application can switch between these strategies at runtime.

### Template Method Pattern

`Grid.AbstractBuild.buildCity()` defines the shared city-generation workflow: iterate through the grid, choose a structure, evaluate the zoning constraints, accumulate the cost, and produce a simple visualisation. Concrete strategies customise the `decideStructure()` step.

### Composable zoning rules (Decorator-inspired)

`ZoningRule` defines a common contract for zoning constraints and cost adjustments. `HeritageRule`, `HeightLimitRule`, `FloodRiskRule`, and `ContaminationRule` implement individual rule behaviours. `GridSquare` stores a collection of rules and applies them when checking a proposed structure and calculating its cost. This is a composable, decorator-inspired rule design; the current implementation uses a list of rule objects rather than nested wrapper objects.

## Project structure

```text
city-planner-github/
├── docs/
│   └── uml-diagram.png
├── examples/
│   └── grid.txt
├── gradle/
│   └── wrapper/
├── src/
│   ├── main/java/edu/curtin/app/
│   │   ├── App.java
│   │   ├── Constants.java
│   │   ├── Grid.java
│   │   ├── GridSquare.java
│   │   ├── Structure.java
│   │   └── ZoningRule.java
│   └── test/resources/faulty-inputs/
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── logging.properties
├── oose-pmd-rules.xml
└── settings.gradle
```

## Requirements

- JDK 17 or later
- Internet connection on the first Gradle Wrapper run, so Gradle 8.9 can be downloaded if it is not already cached

## Run the application

On Linux or macOS:

```bash
./gradlew run --args="examples/grid.txt"
```

On Windows:

```bat
gradlew.bat run --args="examples/grid.txt"
```

The application presents a menu to:

1. Evaluate a structure at a chosen grid coordinate.
2. Generate a city using the currently selected strategy.
3. Select a building strategy (uniform, random, or central).
4. Display the terrain grid and zoning-rule summaries.
5. Exit.

## Input format

The first line contains the grid dimensions as `rows,columns`. It is followed by one line per grid cell, in row-major order. Each cell line starts with a terrain type and may contain comma-separated zoning rules.

Example:

```text
2,2
flat
swampy,heritage=wood
rocky,height-limit=4
flat,flood-risk=35,contamination
```

The implementation recognises the terrain constants `flat`, `swampy`, and `rocky`. Supported zoning-rule forms include:

- `heritage=wood` (or another material value)
- `height-limit=4`
- `flood-risk=35`
- `contamination`

See `examples/grid.txt` for the supplied sample grid.

## Quality checks

The Gradle build is configured with PMD and a project-specific rules file. Run:

```bash
./gradlew check
```

The `src/test/resources/faulty-inputs/` directory contains malformed input samples for manual checking. They are not automated JUnit tests; automated unit tests should be added for grid parsing, zoning-rule validation, cost calculations, and strategy behaviour.

## Current limitations and possible improvements

- Grid parsing needs stricter validation for empty files, non-positive dimensions, invalid terrain names, malformed zoning rules, and out-of-range rule values. Some malformed inputs can currently be accepted silently or raise an uncaught parsing exception.
- The zoning rules are composed as a list of rule objects; they are not implemented as a chain of nested Decorator wrappers.
- `buildStructure()` evaluates placement and prints the cost; it does not persist a built structure in the grid.
- Automated JUnit tests are not included in the submitted source archive.


