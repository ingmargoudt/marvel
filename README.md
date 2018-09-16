# marvel
A simple Test Automation toolkit for Selenium

# What is marvel?
Marvel is a library, created by best practices I applied the last couple of years as a Test Automation Engineer. Marvel is 
primarily designed to support testing of end-to-end (business) processes. 

# What can marvel do for me?
Marvel 
* automatically logs of your teststeps
* manages your webdriver
* provides ways to keep your code well-structured and clean 

# Datadriven, rather than Gherkin
The marvel toolkit is designed to help you create your tests datadriven. To my belief the quality and specifcation 
of testdata is the 'engine' behind every test. Marvel brings business logic right where it belongs: not in the test, but in the business-layer. 
This means that your test does not define how a feature is to be tested, but your testdata defines which paths through your business-flows are taken.
Be aware, BDD is a collaboration methodology, it is not a testing tool. 

Will a gherkin language as Cucumber or JBehave be supported? 
Perhaps in a later stage if there is a requirement to. 

# Alright, I am interested. Details please?!
As mentioned, marvel assists testing end-to-end business processes. A business process often is static and defined by higher level business analysts.
For example, let's say we are testing a webshop: one of the business processes could be : Purchasing.

So, once we have a process, we can think which high level activities belong to it. In the case of Purchasing we could think of : logging in, searching for the item and add it, confirming your basket , confirming payment.
Those are user activities, but we can offcourse think of system-activities such as backoffice checks. These highlevel activities are called a 'Flow' in marvel.
In this example, we might have LoginFlow, LookupItemFlow, BasketFlow, PaymentFlow and a PaymentAcceptedByBackofficeFlow.

Both the Process and the Flow determine what we want to achieve, now we need to know how to achieve it. A Flow is a collection of Steps that have to be taken on the application. 
Think for example : enterUserName(), enterPassword(), searchItem(), selectResult(), etc. 
( By the way, did you see that we are designing our tests, without touching Selenium at all? )
Each Step is directly linked to a particular page of your application, and this is where the Page Object pattern comes in. Marvel provides
an API to safely interact with webelements, using explicit waits, and supports both the Page Object pattern and the PageFactory.

Oh, and you don't need to use any constructors: all your steps and programs can be injected by an annotation. Marvel will also assist  you with logging
as it will log every step and arguments used, to help you tracing down your tests.

# Is marvel applicable for my project?
That is entirely up to you. There is no tool, no library, no framework that can do it all for you. A tool is just one thing contributing to successful automation. Based on your requirements, technical, functional or communication, select what is best for you. 

# When will there be a first RC?
Updates to the basics of marvel are still being pushed, you will read updates on this github. Releases will be pushed to the Maven repositories.
