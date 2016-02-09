<div align="center"><h1>RecycleRush STEMly Competition</h1></div>
![FRC Image](http://team4619.com/wp-content/uploads/2015/06/936381_orig.png)
<h3>Steps for Integration:<h3>
===
***
<h4>Requirements:</h4>
(see FRC requirements for version details)
* Eclipse
* Windows Mac or Linux
* Intellij Idea
* Git SCM

Setup

1.  If you haven't created a project in eclipse for FRC development follow the instructions posted at https://wpilib.screenstepslive.com/s/4485/m/13809
2. Launch Intellij Idea
3. If Already in a project File --> new --> Project from Version Control --> Github, otherwise just click  Project from Version Control --> Github
4. Use you Github login. Don't have one? go to https://github.com/ and create and account
5. The Following should look as follows before clicking clone

 Git Repository URL: https://github.com/frc4976/RecycleRush-MAIN.git<br>
 Parent Directory: //the default is will work fine<br>
 Directory Name: RecycleRush-MAIN
6. Click clone
7. If prompted to open in new window or this window click select this window
8. Double Click RecycleRush-MAIN
9. Right click src then Mark Directory As --> Sources Root
10. Right click src then Add as Ant Build File
11. Navigate to File --> Project Structure / Modules / Dependencies
12. Click the + then JARs or directories...
13. Navigate to your wpilib library files it should look something like this assuming you on

 Windows: C:\Users\\(your username)\wpilib\java\current\lib - Windows <br>
 Linux: /home/(your username)/wpilib/java/current/lib - linux <br>
 Mac: ? Possibly similar to linux
14. After selecting the directory click OK

Now your ready to develop to your hearts content remember to commit regularly.