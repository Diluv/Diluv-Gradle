# Minecraft Forge Example
This example is used to debug Diluv's integration with Fabric's Loom Gradle plugin. This example has been configured to connect to a locally hosted instance of the Diluv API that has been supplied with our mock database.

## Usage Guide
1. Make sure the local DiluvGradle plugin has been built. This is done by running `./gradlew clean build` in the root directory of this project repo.
2. Make sure the local API is running and that the `gradle.properties` file reflects your local API and database.
3. Run `./gradlew clean build publishDiluv` in the example directory. 

## ⚠️Security Note⚠️
Remember to never publish your auth token in a public environment such as Github. Auth tokens are private information and should be treated with the same level of security as an account password. When using with a build server like Jenkins or GitHub actions the auth token should be injected as a build secret.

## Verifying Integration
To verify that integration with Loom was successful run the `./gradlew build publishDiluv -d` command. then look for the following things in your log.

1. The build was successful.
2. The log contains `[DiluvGradle] Applying loader fabric because pluing fabric-loom was found.`
3. The log contains `[DiluvGradle] Diluv Response Code: 200`.
4. The file was uploaded to the expected Diluv project.