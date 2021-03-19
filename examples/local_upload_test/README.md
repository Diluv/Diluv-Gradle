# Local Upload Test
This test project is used to debug locally built versions of DiluvGradle. This is done by uploading a file to a local instance of the Diluv API that is supplied with our mock database.

## Usage Guide
1. Make sure the local DiluvGradle is built. This is done by running `./gradlew build` in the root directory of this project.
2. Make sure the local API is running and that the `gradle.properties` file reflects your local API and database.
3. Run `./gradlew clean build --debug` to test the upload.

## Security Note
This test example is provided for those who are developing the Diluv Gradle plugin and the Diluv API. It should not be treated as an example for implementing the plugin in your project. Running unsigned plugins, or publishing your auth token in the publicly visable `gradle.properties` file are both examples of horrible security practices.