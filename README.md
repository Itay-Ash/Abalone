A graphical implementation of the classic board game **Abalone**, developed in Java using JavaFX.

---

## 🧠 Game Overview

**Abalone** is a two-player abstract strategy board game. Players take turns pushing marbles on a hexagonal board in an attempt to push six of the opponent's marbles off the edge.

This project replicates the game with:

- Interactive GUI using JavaFX
- Full support for Player vs Player and Player vs Environment (AI)
- Custom game logic and marble movement rules

---
## 🎮 Gameplay

### 🏠 Main Menu

Upon launching the application, the main menu is displayed. It allows players to:

- Choose game mode (PvP or PvE)
- Choose each player's marble color

The player can click on the Abalone icon to open a game mode type menu and start the game!

![Main Menu Screenshot](https://github.com/user-attachments/assets/994f70b7-8482-4cce-b3e2-9346372c71d4)

---

### 🧩 In-Game Experience

Once the game starts:

- Players alternate turns, selecting and moving marbles according to official Abalone rules
- The GUI provides feedback on invalid moves and game status
- The game ends when one player pushes six of the opponent’s marbles off the board

Action-packed gameplay!

![Gameplay Screenshot](https://github.com/user-attachments/assets/83996c20-9f00-45c0-9f36-9ece43651e02)

### 🧭 Components UML Diagrm 

```plaintext
        +------------------+
        |     MainMenu     |
        +------------------+
                 |
                 v
        +------------------+
        |   gController    |
        +------------------+
           ^           ^
           |           |
   +-------+           +--------+
   |                            |
   v                            v
+-----------+         +----------------+
|  gViewer  |         |    gManager    |
+-----------+         +----------------+
                               ^
                               |
                               v
                         +------------+
                         |  AiPlayer  |
                         +------------+
```
### 🧩 Component Overview
This Model is based on the [MVC](https://www.geeksforgeeks.org/mvc-design-pattern/) UML diagrm type.

#### 🏠 Main Menu – `MainMenu`
- **Role:** Entry point for the user interface.
- **Details:** Sends player selections and setup choices to the controller to initialize the game.


#### 🎮 Controller – `gController`
- **Role:** Acts as the mediator between `gViewer` (View) and `gManager` (Model).
- **Details:** Deals with situations that requires all components, updates the model, and reflects changes back to the GUI.

#### 📺 View – `gViewer`
- **Role:** Handles all GUI elements and display logic.
- **Details:** Renders the board, player marbles, menus, and messages.
- **Communication:** Talks **only** with `gController` (no direct access to model).

#### 🧠 Model – `gManager`
- **Role:** Manages core game logic, rule enforcement, internal state, and any back-end calculations.
- **Details:** Processes moves, tracks player turns, determines win conditions.
- **Communication:** Talks **only** with `gController`, keeps track of game board.

#### 🤖 AI Player – `AiPlayer`
- **Role:** Interacts with the game logic (via `gManager`) to perform moves automatically.
- **Details:** Fetches board state from `gManager`, calculates optimal move, and applies it.

## 🤖 AI Strategy

The AI uses advanced decision-making algorithms to choose its moves intelligently. Two common approaches are **Minimax** and **Alpha-Beta Pruning**. 

- **Minimax** explores all possible moves and their outcomes to find the best strategy, but it can be very slow because it checks every possible path.
- **Alpha-Beta Pruning** improves on Minimax by cutting off paths that don’t need to be explored, which makes the search much faster without sacrificing the quality of the decision.

I chose **Alpha-Beta Pruning** for the AI because it finds strong moves much more efficiently, allowing the AI to think deeper and respond faster during gameplay.

The evaluation function used by the AI considers multiple strategic factors:
- The **total number of opponent marbles ejected**.
- The **difference in ejected marbles** between the AI and the opponent.
- The **potential to push an opponent's marble** without risking one's own.
- The number of **3-marble sequences** on the board (which cannot be pushed frontally or from behind).
- A score based on the **number of adjacent friendly marbles** (indicating formation strength).

To improve performance, each evaluation is localized to only relevant board areas, ensuring optimal decision-making with minimal computational overhead.

## 🧩 Technologies Used

This project is developed with:

- **Java** – The primary language powering all game logic and functionality.  
- **JavaFX** – Used for the graphical user interface, including board rendering, menus, and animations.  
- No additional external libraries are required beyond the standard Java and JavaFX libraries.

---

## ⚙️ Installation & Setup

Follow these steps to run the Abalone Java Game on your local machine:

1. **Prerequisites:**
   - Install [Java JDK 17 or later](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).  
   - Download and install the JavaFX SDK version compatible with your JDK from [Gluon](https://gluonhq.com/products/javafx/).

2. **Clone the repository:**
   ```bash
   git clone https://github.com/Itay-Ash/Abalone.git
   cd abalone```
 3. Build and run the project:
    - If you use an IDE (IntelliJ IDEA, Eclipse, NetBeans), import the project and configure the JavaFX SDK in your project settings.
    - Or, run from the command line (adjust paths accordingly):
    ```bash
    javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml src/**/*.java -d out
    java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out helpers.MainMenu
    ```
---

### Enjoy playing and mastering the strategic challenges of Abalone — may the best player win!
