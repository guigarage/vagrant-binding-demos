Vagrant-Binding-Demos
=====================

This projects offers some demos and tutorials for the use of [Vagrant-Binding](https://github.com/guigarage/vagrant-binding). You can use this as a base for your own integration of Vagrant-Binding in your projects and UnitTests.

DbHandlerTest
-------------
This [UnitTest](https://github.com/guigarage/vagrant-binding-demos/blob/master/src/test/java/com/guigarage/vagrant/dbhandler/DbHandlerTest.java) checks the functionality of the [DbHandler](https://github.com/guigarage/vagrant-binding-demos/blob/master/src/main/java/com/guigarage/vagrant/dbhandler/DbHandler.java) class. This class offers some simple methods for the work with a mysql database. You can connect to a existing mysql server by jdbc and create some dummy data in it. The UnitTests checks this functions. Normally a UnitTest like the given on has some problems:
- The mySQL server must be available
- The tables must be empty at the start of the test
- you can not run the test parallel to other UnitTests that use the database or the same tables

For this type of UnitTests people often use a in-memory database like Derby or H2. But sometimes you have to use a specific database. With the Vagrant-Binding you can exclude all the mentioned problems.
Vagrant-Bindings offers a JUnit Rule for you that works like a wrapper around your UnitTests. With the [VagrantTestRule](https://github.com/guigarage/vagrant-binding/blob/master/src/main/java/com/guigarage/vagrant/junit/VagrantTestRule.java) you can sync all your tests with the lifecycle of virtual machines.
In the DbHandler example a VagrantTestRule is defined with the configuration of a vm that runs MySQL server on it. The vm will be created and started before every test. When the test finishes the vm will be destroyed. So you can run your tests in parallel because every on uses its own virtual machine. The vm is created on startup with a new and empty MySQL Server on it. So you will never have a problem with old data again.
The vm for this test is created by a Vagrant / Puppet config that is created by Java code.