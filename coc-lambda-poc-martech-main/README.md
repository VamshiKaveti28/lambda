SAM Lambda Java App CI/CD Pipeline
==================================

This is a sample Lambda Java application using SAM Test, Build and publish packages to Artifactory(S3 Bucket) and finally Deploy to AWS Cloud (CD)

Use below document to get detailed instructions on how to use this pipeline
https://massmutual.atlassian.net/wiki/spaces/ALP/pages/4532797441/Getting+started+with+Jenkins+for+SAM+Lambda+deployment+to+AWS+Cloud+CI+CD

You can create your own repo using this template. 
Ensure samconfig.toml has been updated correctly with AWS infrastructure details
https://github.com/massmutual/swift-sam-java-lambda-cicd/blob/main/src-java/samconfig.toml

Ensure template.yaml updates with Application build details
https://github.com/massmutual/swift-sam-java-lambda-cicd/blob/main/src-java/template.yaml

# daily-news-java
Read the full tutorial at https://towardsdatascience.com/how-to-build-a-serverless-application-using-aws-sam-b4d595fe689f
