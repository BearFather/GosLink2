Welcome to goslink. This is a simple app to link upto 3 different realms gossip. If you have any problems email me at BearFather@gmail.com and put in the subject "goslink" so I see it faster.

Setup: 
1.Create an account on each board you want to link. Or if the same board 2 different accounts. 
2.Disable ansi on both accounts permently(in account settings) 
3.Enter the realm place the charaters in a safe place(sys supp) optional: To make to more seamless, I make the accounts invis, and hid them from topten lists. Also not required but I like to do is "setstatline none" keeps what the app sees cleaner and less noisy. 
4.Extract goslink.rar file. 
5.Copy config.properties-default to config.properties 
6.Edit config.properties with your info for servers. 
7.Launch goslink.exe/jar 
8.Sit back and enjoy.

INFO: If you want to run this under a non GUI OS use noguigoslink.jar. You can also run this in a GUI OS but you wont ever see a window, and will have to kill the process to exit the program.

Remember if you want, I do accept paypal donations at bearfather@gmail.com.


*****************************************************
Settings help...

server1name,server2name,server3name:
The display name of the Server.

server1,server2,server3:
Server ip address. Set to none to disable.

user1,user2,user3:
User to to login into the board.

muser1,muser2,muser3:
The mud user name in the game.

pass1,pass2,pass3:
Password to the board account.

game1,game2,game3:
Menu command to get to Mud.

puser:
Login prompt when logging into server.

ppass:
Password prompt when logging into board.

pmenu:
Main menu prompt of board.

pmud:
Major Mud prompt.(Rarely changed)

cleanup=Sorry to interrupt here, but the server will be shutting
Cleanup message sent when warning users shutdown is comming.

time:
Relog pause time. 0=none pause.

window:
Display output window.  False, will display no window you must kill the "javaw" process to close it.

deny:
Use the the deny.txt file to deny users access to alignment commands(for pvp).

webchat:
Enable webchat port, must be true for global chat.

webuser:
Webchat port user.

webpass:
Webchat password.

**Optional Settings**
timestamp:
Enable a time stamp in output window.

ansi:
If set to true will send =a to enable ansi.  For boards with no access to settings.

key:
Used to verify server with global chat.

debug:
Enable debug button, will allow full output from the bot.
