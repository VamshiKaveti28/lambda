//import libraries
import lib.JenkinsUtilities

// Perform unit test
def unitTest(){
    def utils = new JenkinsUtilities(this)
    try {
        utils.printBold("Performing Unit test")
        sh '''
        # $BUILD_FOLDER is where template app code stores while merging with Jenkins pipeline
        cd "$BUILD_FOLDER/DailyNewsFunction"
        # Generate surefire report
        mvn surefire-report:report
        '''
        junit testResults: "$BUILD_FOLDER/DailyNewsFunction/target/surefire-reports/*.xml", skipPublishingChecks: true
        //junit "$BUILD_FOLDER/DailyNewsFunction/target/surefire-reports/*.xml"

    } catch (Exception e) {
        env.CI_STATUS = "FAILURE"
        env.status_msg = "$SAM_OPS --Performing Unit Test Failed with error: ${e.message}"
        utils.printError "Error encountered during tests"
        echo 'Exception occured: ' + e.toString()
        throw e

    } finally {
        utils.printBold("export unit test results")
        publishHTML( target: [
        reportDir: "$BUILD_FOLDER/DailyNewsFunction/target/site",
        reportFiles: 'surefire-report.html',
        alwaysLinkToLastBuild: true,
        keepAll: true,
        reportName: 'surefire report',
        reportTitles: 'surefire report'
        ])

        utils.printSuccess("Successfully performed surefire-report tests")
        // Keep the below lines if you want build to fail when unit tests fail. Omitting these lines would mark the build Unstable and continue with next stages
        if (currentBuild.result == 'UNSTABLE' || currentBuild.result == 'FAILURE') {
            currentBuild.result = 'FAILURE'
            utils.errorOutBuild("Unit Tests failed")
        }            
        }
    }
return this
