# SW_Eng_II_IdleGame

<h2>Description</h2>
This is the Repo of the DHBW Embedded Systems TES23 Software Engineering II Project.

Assignees are:

- Tim Schäfer
- Daniel Fellner

The Idle Game is written in Java and Thread-based. The goal is to make as much money as possible in a defined time format.
To do that the player has to produce ___ by using production machines. the producion machines produce one dedicated product in every time frame
To make money off these products, they have to be sold, which is done by a seller. in every time frame the seller sells one product and cashes its profits and the 
money is added to the player balance. with this balance the player is able to upgrade the machines or sellers or buy entire new machines or sellers.

The player controls this by console commands e.g.:
account --> the current account balance is shown
machines --> all of the current machines and their levels are shown
upgrade machine "yxz" --> if the player has the currency to afford the upgrade the corresponding machine is upgraded.

<h2>Windows Compatibility steps</h2>



+ Use lanterna-3.2.0-alpha1.jar

+ include jna.jar and jna-platforms.jar as libs

+ Command to successfull execution:

    ``java -cp "out/production/SW_Eng_II_IdleGame;IdleGame_Projekt/lanterna-3.2.0-alpha1.jar;IdleGame_Projekt/jna-jpms-5.18.1.jar;IdleGame_Projekt/jna-platform-jpms-5.18.1.jar" Main``

+ Execution or .jar built by intellij not working so far -> jna seems to be not part of compile/run process

+ to execute code with changes built project in intellij first