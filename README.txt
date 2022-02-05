<<<<<<< HEAD
-------------------------------------------
=======
<<<<<<< HEAD
-------------------------------------------
=======

>>>>>>> 243d4f3 (Started porting the mod to 1.17)
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)
Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

<<<<<<< HEAD
=======
=======
Note also that the patches are built against "un-renamed" MCP source code (aka
SRG Names) - this means that you will not be able to read them directly against
normal code.

Setup Process:
==============================

>>>>>>> 243d4f3 (Started porting the mod to 1.17)
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)
Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.
<<<<<<< HEAD
=======
=======
1. Run the following command: `gradlew genEclipseRuns` (`./gradlew genEclipseRuns` if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run `gradlew eclipse` to generate the project.
>>>>>>> 243d4f3 (Started porting the mod to 1.17)
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
<<<<<<< HEAD
=======
=======
3. Run the following command: `gradlew genIntellijRuns` (`./gradlew genIntellijRuns` if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can 
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything 
{this does not affect your code} and then start the process again.

Mapping Names:
=============================
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license, if you do not agree with it you can change your mapping names to other crowdsourced names in your 
build.gradle. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/MinecraftForge/MCPConfig/blob/master/Mojang.md

Additional Resources: 
=========================
Community Documentation: http://mcforge.readthedocs.io/en/latest/gettingstarted/  
LexManos' Install Video: https://www.youtube.com/watch?v=8VEdtQLuLO0  
Forge Forum: https://forums.minecraftforge.net/  
Forge Discord: https://discord.gg/UvedJ9m  
>>>>>>> 243d4f3 (Started porting the mod to 1.17)
>>>>>>> 3d9c0b1 (Started porting the mod to 1.17)
