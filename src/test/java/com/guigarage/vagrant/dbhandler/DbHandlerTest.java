package com.guigarage.vagrant.dbhandler;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import com.guigarage.vagrant.configuration.PuppetProvisionerConfig;
import com.guigarage.vagrant.configuration.VagrantConfiguration;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantFileTemplateConfiguration;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.configuration.builder.PuppetProvisionerConfigBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantConfigurationBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantEnvironmentConfigBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantFileTemplateConfigurationBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantVmConfigBuilder;
import com.guigarage.vagrant.junit.VagrantTestRule;
import com.guigarage.vagrant.util.VagrantUtils;

public class DbHandlerTest {

	private static final String VM_IP = "192.168.50.4";
	
	@Rule
	public VagrantTestRule testRule = new VagrantTestRule(createConfig());

	public static VagrantConfiguration createConfig() {
		try {
		PuppetProvisionerConfig puppetConfig = PuppetProvisionerConfigBuilder
				.create()
				.withManifestPath("puppet/manifests")
				.withManifestFile("dbserver.pp")
				.withDebug(true)
				.build();
		
		VagrantVmConfig vmConfig = VagrantVmConfigBuilder
				.create()
				.withName("mysqlvm")
				.withHostOnlyIp(VM_IP)
				.withLucid32Box()
				.withPuppetProvisionerConfig(puppetConfig)
				.build();
		
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfigBuilder
				.create()
				.withVagrantVmConfig(vmConfig)
				.build();
		
		VagrantFileTemplateConfiguration fileTemplateConfiguration1 = VagrantFileTemplateConfigurationBuilder
				.create()
				.withUrlTemplate(VagrantUtils.getInstance().load("com/guigarage/vagrant/dbhandler/my.cnf"))
				.withPathInVagrantFolder("files/my.cnf")
				.build();
		
		VagrantFileTemplateConfiguration fileTemplateConfiguration2 = VagrantFileTemplateConfigurationBuilder
				.create()
				.withUrlTemplate(VagrantUtils.getInstance().load("com/guigarage/vagrant/dbhandler/dbserver.pp"))
				.withPathInVagrantFolder("puppet/manifests/dbserver.pp")
				.build();
		
		VagrantConfiguration configuration = VagrantConfigurationBuilder
				.create()
				.withVagrantEnvironmentConfig(environmentConfig)
				.withVagrantFileTemplateConfiguration(fileTemplateConfiguration1)
				.withVagrantFileTemplateConfiguration(fileTemplateConfiguration2)
				.build();
		
		return configuration;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testJdbc() {
		try {
			DbHandler dbHandler = new DbHandler(VM_IP, "testapp", "dbuser", "dbuser");
			dbHandler.connect();
			dbHandler.createMyTable();
			for(int i=0; i < 100; i++) {
				dbHandler.insertRow();
			}
			Assert.assertEquals(100, dbHandler.getRowCount());
			dbHandler.close();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
