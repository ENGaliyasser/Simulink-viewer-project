# Simulink Viewer

Simulink Viewer is (Advanced Computer Programming - CSE231s) project that allows users to read and visualize Simulink files using a JavaFX-based application. The project consists of four main classes: App, MDLFileExtractor, Block and Lines.

## How to Run the Simulink Viewer Project
To run the Simulink Viewer project, follow these steps:

1-Launch the Simulink Viewer application.

2-Load the selected MDL file into the application.

3-The application will parse the file and display the blocks on the screen.

## Class Descriptions
The Simulink Viewer project comprises the following classes:

### 1. App Class

It extends the Application class from JavaFX and provides a GUI for viewing Simulink model files. It allows users to select a file, extract Simulink blocks, and display them as a graphical diagram.

### 2. MDLFileExtractor Class

Responsible for extracting Simulink blocks from an MDL file and creating corresponding Block objects. It provides methods to extract block strings, parse block information, and draw the blocks on a JavaFX Pane. The class utilizes regular expressions and string manipulation to extract relevant block data. Overall, it facilitates the processing and visualization of Simulink models in the Simulink Viewer application.

### 3. Block Class

Represents a Simulink block within the Simulink Viewer application. It encapsulates properties such as ID, type, number of ports, position, and graphical elements like rectangles, text, and icons. The class provides methods to set and retrieve block properties, calculate port positions, and create graphical representations of the block. It plays a crucial role in visualizing Simulink blocks and their associated information on the viewer's interface.

### 4. Lines class

draws lines/arrows between blocks. It reads line info, determines start/end points, and draws the lines. It supports straight arrows and branched lines using "Arrow" and "Branch" classes.

## Main Method
The main method of the Simulink Viewer project carries out the following operations:

Initializes the SimulinkParser class to parse the relevant data from the Simulink file.
Creates arrays to store block data based on their Z-order.
Renders lines connecting blocks or using intermediate points.
If a line has branches, sets the branch's source as the line's destination.
Utilizes JavaFX to draw the graphical user interface, including line shading and arrow rendering.
## Team Members
The Simulink Viewer project was developed by the following team members:

* Ali Yasser Ali Abdallah 2000311
* Kareem Khaled Abdelazim Mohamed 2001091
* Ahmed Hossam eldin Mohamed Ahmed Mobark 2000075
* Ahmed Hesham Mahmoud Arafa 2000079

Please note that this is a high-level description of the Simulink Viewer project, and more detailed implementation information can be found in the source code.
