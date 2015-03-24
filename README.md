**OndertekenenClient** [![Build Status](https://travis-ci.org/YuriMB/OndertekenenDemo.svg?branch=master)](https://travis-ci.org/YuriMB/OndertekenenDemo) [![Maven Download](https://img.shields.io/github/tag/YuriMB/OndertekenenDemo.svg?label=maven)](https://jitpack.io/#YuriMB/OndertekenenDemo/v0.1.0)
---


What is OndertekenenClient?
---
OndertekenenClient is a Java client-library for the ondertekenen.nl service. It is written in Java 1.8, using the Jersey client as basis.


What is Ondertekenen.nl?
---
**This text is copied from https://www.signhost.com:**

Signhost.com is a service that makes it possible to digitally sign, seal or deliver your documents. (http://www.signhost.com) Documents can be signed directly from your webportal or by sending a signing request by email or other message to the end-user.

In some countries signhost is branded by a locale domain, like ondertekenen.nl in The Netherlands. In this document the signhost.com domain is replaceable by the local promoted domains like ondertekenen.nl.

During the signing process of the end-user, different identity verification methods can be used to include in the digital signature ie; email, SMS, scribble signature or authentication mechanisms like Google, LinkedIn, and national eID like DigiD(NL) or eHerkenning(NL).

The service is available via our GUI based online portal: https://login.signhost.com/ or via several gateways to connect directly from a third party system.

[This](https://api.signhost.com/) document describes the Signhost REST API implementation.

The REST API is the underlying interface for all of our official Signhost API methods. It's the most direct way to access the API.

This reference document is designed for those interested in developing for solutions using the SignHost.com REST Api Signing feature or for those interested in exploring Ondertekenen.nl integration features in detail.

Further details about the methods can be found [here](https://api.signhost.com/Help)


Integrating the client
---

 1. Download and unpack the Client from [GitHub](https://github.com/YuriMB/OndertekenenDemo/archive/master.zip) .
 2. Build and install the Client using [Maven](https://maven.apache.org/index.html).
 3. Add the following dependency to your own Maven project file:

        <dependency>
            <groupId>yurimeiburg.ondertekenen</groupId>
            <artifactId>client</artifactId>
            <version>0.1.0</version>
        </dependency>

 4. There is also a `Demo` project, to use as a starting point.

Examples
---

**Generic error handling**
All data objects inherit from the abstract class `ModelObject`. This class provides some basic error-handling. All calls should be checked if they executed properly, using the `isOk()` method. This function returns `true` if the call succeeded, `false` otherwise.

If an error occurred, the exact message can be obtained using the method `getErrorMessage()`.

In some cases the returning object is `null`. If this happens, there is a fatal error, such as no connectivity to the web service.

**Creating a transaction**
Transactions can be created using `new Transaction(FileInfo, Signer [])`, and then using a builder pattern can be modified with extra parameters, e.g.:

    Transaction t = new Transaction(fileInfo, signers)
            .seal(true)
            .reference("Contract #123")
            .sendEmailNotifications(true)
            .daysToExpire(90)
            .signRequestMode(1)
            .daysToReminder(7);

With this object, we can send a request to the server to generate a new transaction. This is done as follows:

	Transaction transaction = ondertekenenClient.createTransaction(t);

**Retrieving a transaction**
Transactions can be retrieved based in their ID. This is useful for polling the current state. The `Demo` program demonstrates this by blocking further execution until the Transaction is signed or cancelled.

	Transaction transaction = ondertekenenClient.getTransaction("my-id");

**Deleting a transaction**
Transactions are deleted based on their ID. If the transaction was successfully deleted, the transaction will be returned. You must specify whether or not you want to notify the user of this action, and the reason for the deletion:

	boolean shouldNotify = true;
	String message = "Sent the wrong contract.";
	ondertekenenClient.deleteTransaction(transaction, shouldNotify, message);

**Uploading a file**
To upload a file, you must specify a File object with the location of the PDF to upload. If uploading went successful the function will return `true`, `false` otherwise.

	if (!ondertekenenClient.uploadFile(transaction, new File("test.pdf"){
		LOGGER.error("Oops, something went wrong!);
	}

**Download proof**
After the user has signed or cancelled, you can download the signed document + a receipt. These files are binaries, and should be written to disk/database/etc. This is also demonstrated in the `Demo` module.

	/* Send an email notifying the users of this action */
	boolean sendSignRequest = true; 
	
	Document signedDocument = ondertekenenClient.getSignedDocument(transaction.getFile().getId(), sendSignRequest);
	
	Receipt receipt = ondertekenenClient.getReceipt(transaction.getId(), sendSignRequest);
