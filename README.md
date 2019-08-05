# SAP Marketing Function Import Demo

This is a sample java springboot maven project to demo how to call an SAP Marketing function import through a java application.

In this application, I call the /RebuildTargetGroup function import of the API_MKT_TARGET_GROUP_SRV OData API.
Go through the following document for details regarding the same.
https://help.sap.com/viewer/0f9408e4921e4ba3bb4a7a1f75f837a7/1905.500/en-US/97ed2d7497c94d8589082a56b10ac9d1.html

You can clone the project and import it into any IDE and run the application on local tomcat.
Or you can also run a maven clean install to generate the jar for the same and can deploy it anywhere to test it.

In order to test the application, make a post call to the /rebuildTargetGroup endpoint with the following details:  

URL:  
{baseURL}/rebuildTargetGroup
 
Request Type:  
POST
 
Header:  
Basic Auth with a valid communication user’s username and password.
 
Body:  
{
                "systemURL":"MarketingSystemURL",  
                "targetGroupUUID":"ValidTargetGroupUUIDPresentInMKTSystem"  
}
