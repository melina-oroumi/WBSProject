# WBSProject

1. Task class - done
2. WBS tree - done
3. Find tasks - done
4. Display tree - done
5. Load WBS file - done
6. Save WBS file - done
7. Calculate total effort - done
8. Calculate unknown tasks - done
9. Basic GUI/menu
10. Configure N
11. Configure reconciliation
12. Estimate effort
13. Test

1. Rubric

Map - Map<String, WBSComponent> for task lookup
Clear responsibilities - Separate classes for WBS, tasks, file handling, estimation, GUI/menu
Methods - keep them very small
Error handling - custom exceptions + controlled external handling
Logging - SLF4J
Strategy pattern - different effort reconciliation strats
Composite pattern - WBS task hierarchy
GUI/menu - Separate presentations from WBS logic


2. Composite pattern

Game design
- Core Concepts
- Storyline
- Game World

3. Polymorphism

WBSComponent task; could refer to either new LeafTask() or new CompositeTask()