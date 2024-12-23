# River Rage Game

Welcome to the River Rage game! This game is developed using Java and OpenGL on NetBeans. Follow the instructions below to set up and run the game.

## Features

- **Main Menu**: Navigate through the game options.
- **High Scores Dashboard**: Displays the top 8 highest scores.
- **Volume Adjustor**: Adjust the game volume to your preference.
- **Single Player Mode**: 
  - 3 levels of increasing difficulty.
  - Level 1 is the easiest.
  - Pause functionality.
  - Timer and score counters.
  - Lives counter.
- **Multiplayer Mode**: 
  - Player 1 starts the game.
  - After Player 1 loses, Player 2 begins.
  - At the end, the game calculates the scores for each player and determines the winner.

## Installation

### Prerequisites

- NetBeans IDE
- JOGL (Java Binding for the OpenGL API)
- Absolute Layout Library (included by default in NetBeans)

### Setting Up JOGL in NetBeans

1. **Download JOGL**:
   - Download the JOGL 1.1 "windows amd64 and i586" zip file from [here](https://mgayar.blogspot.com/2014/03/how-to-install-jogl11-into-netbeans.html).

2. **Extract JOGL**:
   - Extract the zip file to `C:\` so you should have `C:\JOGLwin\lib32` and `C:\JOGLwin\lib64`.

3. **Clone the Repository**:
   - Open NetBeans and clone the River Rage repository:
     ```bash
     git clone https://github.com/MohammedRagab00/River-Rage.git
     ```

4. **Add JOGL Libraries**:
   - Right-click the project and choose "Properties".
   - Select "Run" from the left, then add this Java VM option: `-Djava.library.path=C:\JOGLwin\lib64`, and press OK.
   - Right-click on "Libraries" and select "Add Library", or choose "Libraries" from project properties on the left.
   - Press "Add Library" -> "Create" -> Type the library name "Jogl" and press OK.
   - Click "Add JAR/Folder", browse to `C:\JOGLwin\lib32`, select all files, and press "Add JAR/Folder". Repeat for `C:\JOGLwin\lib64`.

5. **Run the Project**:
   - Click OK -> OK, run your project/file, and it should work.

## Running the Game

1. **Open the Project**:
   - Open the River Rage project in NetBeans.

2. **Build and Run**:
   - Click on "Build" to compile the project.
   - Click on "Run" to start the game.

3. **Enjoy the Game**:
   - Navigate through the main menu to start playing.
   - Adjust the volume using the volume adjustor.
   - Check the high scores dashboard to see the top 8 scores.
   - Play in single-player mode or challenge a friend in multiplayer mode.

## Contributing

If you would like to contribute to the development of this game, please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the GNU General Public License (GPL) Version 3.
