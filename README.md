# OAuth 2 + Open ID Connect using Spring Boot and Azure AD

##### Table of Contents

[Overview](#overview)

[Azure AD Setup](#ad-setup)

[Application Registration Notes](#app-registration)

[Application Configuration](#app-configuration)

[Running and Testing the STUB](#running-testing)

[Implementing Azure AD with your own application](#implementation)

<a name="overview"/>

## Overview
This simple Spring Boot application authenticates with Azure AD via OAuth2 using Open ID Connect.  This can be used to test your
Azure AD configuration prior to integrating with your application.  

The project uses the following key dependencies:
 - SpringBoot v2
 - Spring Security v5
 - Azure AD Spring Security Starter v2

<a name="ad-setup"/>

## Azure AD Setup
You need an Azure AD Account in order to run this project - see [here](https://azure.microsoft.com/en-gb/) for
details on how to create your free account. 

Once created, the online documentation [here][Azure-AD-SpringBoot.md] is the best guide to explain the registration of an application within AAD

<a name="app-registration"/>

### Application Registration Notes
1) Make a note of the following during the application registration process as you will need them later to configure your application
    - Client ID - This is the unique id of your application registered in AAD
    - Client Secret - This is the unique secret key for your application, it is only displayed at creation so make sure you note it or you will have to create a new secret
    - Tenant ID - This is the unique id of the AAD instance
 
2) Change the application manifest and set the following to true
    - availableToOtherTenants
    - oauth2AllowImplicitFlow

3) Set the Home Page URL of your application to **http://{host}:{port}/{application-context}/**

4) Set the Reply URL for your application to **http://{host}:{port}/{application-context}/login/oauth2/code/azure**

5) Set the following Delegated Permissions
    - Access the directory as the signed-in user
    - Sign in and read user profile
    
6) Save + Grant Permissions (grant permissions requires an admin user)

7) It will also be necessary to setup the AD Groups that your application requires and invite users to these (from within the AD Portal). 
Invited users will receive an email that will direct them to the AD service where they will approve the delegated permissions that 
have been setup during application registration.  For this stub application, create the following groups
 - OAUTH2_SB_1
 - OAUTH2_SB_2

<a name="app-configuration"/>

## Application Configuration
There is a base application.yml supplied in this project.  You will notice some placeholders in there as we don't want to check any
sensitive AD configuration into GIT.  You should have made note of the required items during the Application Registration step.  You will
need to edit the application.yml file and replace the following with the values you noted down.
 - {tenant-id}
 - {client-id}
 - {client-secret}

**Note** that the {tenant-id} property appears multiple times in the application.yml file.  

You will also notice a property called **active-directory-groups** This needs to be populated with the full list of groups that you wish
to search for in order to authenticate your user.  You can leave as is for the stub application and just ensure you add users to these groups
within the Azure AD Portal.

The above properties should never be checked into GIT and instead should be sourced from the VAULT.   

**Please Note** that the current version of Spring Security (at time of writing this README) does not support the **On Behalf Of** protocol.
This is anticipated to be available in an upcoming release. 

<a name="running-testing"/>

## Running and Testing the STUB
The project is a simple Spring Boot Application and can either be run from within and IDE or straight from the command line.

In order to run it from the command line simply type the following from the root project directory

```./mvnw spring-boot:run```

Once the project is up and running point your browser at **http://localhost:8080**

1) You should be presented with a Microsoft Login Page asking for your email address.  Enter 
your AD registered email address and hit enter.  

2) You will then be presented with a page asking you if you wish to remain logged in, select either 
option.

4) You should then be presented with the index.html page from this project that will
confirm that you have been authenticated, who you are and what groups you are part of.

<a name="implementation"/>

## Implementing Azure AD with your own application
Once you've got the stub application up and running and are comfortable with how it works and
hangs together you can start to implement the solution into your own application.
 
You will need the **spring-security** and **azure** configuration from the application.yml file as
well as the ```AADOAuth2LoginSecurityConfig``` Java class file from the config package.

<!-- Links -->
[Azure-AD-SpringBoot.md]:https://docs.microsoft.com/en-gb/azure/active-directory/develop/quickstart-v1-integrate-apps-with-azure-ad
