# Minecraft Forge Example
This example project is used to debug and demonstrate integrating DiluvGradle with a Minecraft Forge project.

## Usage Guide
1. Make sure the `diluv_token` and `diluv_project` properties in the `gradle.properties` file have been changed to the correct project ID. The default values are used for testing on the mock database.
2. Build the project using Gradle, and invoke the new publishDiluv task. `./gradlew build publishDiluv`.

## ⚠️Security Note⚠️
Remember to never publish your auth token in a public environment such as Github. Auth tokens are private information and should be treated with the same level of security as an account password. When using with a build server like Jenkins or GitHub actions the auth token should be injected as a build secret.

## Verifying Integration
To verify that ForgeGradle integration was successful run the `./gradlew build publishDiluv -d` command. Then look for the following things in your log file. 

1. The build was successful.
2. The log contains `[DiluvGradle] Applying loader forge because pluin net.minecraftforge.gradle was found.`
3. The log contains `[DiluvGradle] Diluv Response Code: 200`.
4. The file was uploaded to the expected Diluv project.