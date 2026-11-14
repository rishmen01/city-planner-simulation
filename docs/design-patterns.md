# Design pattern notes

- **Strategy:** `Grid.BuildStrategy` is the strategy contract. `UniformBuild`, `RandomBuild`, and `CentralBuild` implement alternative structure-selection behaviour.
- **Template Method:** `Grid.AbstractBuild.buildCity()` defines the city-generation algorithm and calls the abstract `decideStructure()` hook implemented by concrete strategies.
- **Composable zoning rules:** `GridSquare` stores `ZoningRule` instances and applies each rule to build permission and cost. This is decorator-inspired composition, but it is not a canonical Decorator chain because rule objects do not wrap and delegate to another rule object.

See [`uml-diagram.png`](uml-diagram.png) for the submitted class-relationship diagram.
