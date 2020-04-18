Control Panel is separated as per MVC file structure.

**not everything is commented as of yet however most function should be understandable i hope**

So far implementation of of UserCreation and Management is completed
Session tokens and slated hash passwords are yet to be completed properly


**VIEW**
view is separated into cards where each card contains a north,center and south panel.
North panel as of now only contains a home button, Center panel is for content and south for buttons
+HOMECARD card containing panels for the home screen
+LOGINCARD card containing panels for the Login screen
+MANAGEUSERCARD card containing panels for the mange user screen

+MAINVIEW is the primary Card which is displayed, A card is called to be displayed on the Mainview within controller
+MASTERVIEW adult class containing some general functions for all cards

**CONTROLLER**
Controller handles all event handling
Each CARD has a corresponding controller handling all events for that card.
The controller accesses each element of the card through gets and sets(can be found towards the bottom of any class)
+HOMECONTROLLER
+LOGINCONTROLLER
+MANAGEUSERCONTROLLER

+MAINCONTROLLER calls and instantiates all controllers aswell as MAINVIEW


**MODEL**
Model contains all database related commands aswell as the database model itself
+DBConnection initiates a database connection instance and is called in DBInteract
+DBInteract manages all command and queries towards the database
+models.sql is the file used to create the database. U HAVE TO GIVE YOURSELF(ROOTUSER) ADMIN PRIVILEGES OR U WILL GET A FAILED CONNECTION ERROR. i did this through console
