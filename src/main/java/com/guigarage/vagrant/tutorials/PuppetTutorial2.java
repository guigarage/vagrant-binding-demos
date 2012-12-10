package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

import com.guigarage.puppet.forge.PuppetForgeClient;
import com.guigarage.vagrant.Vagrant;
import com.guigarage.vagrant.configuration.PuppetProvisionerConfig;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantFileTemplateConfiguration;
import com.guigarage.vagrant.configuration.VagrantFolderTemplateConfiguration;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.configuration.builder.PuppetProvisionerConfigBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantEnvironmentConfigBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantFileTemplateConfigurationBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantFolderTemplateConfigurationBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantVmConfigBuilder;
import com.guigarage.vagrant.model.VagrantEnvironment;
import com.guigarage.vagrant.util.VagrantUtils;

public class PuppetTutorial2 {

	public static void main(String[] args) throws IOException {
		PuppetProvisionerConfig puppetConfig = PuppetProvisionerConfigBuilder.create().withDebug(true).withManifestFile("myPuppetManifest.pp").withManifestPath("puppet/manifests").withModulesPath("puppet/modules").build();

		File tempFolder = new File(FileUtils.getTempDirectoryPath(), "puppet-modules");
		tempFolder.mkdirs();
		PuppetForgeClient forgeClient = new PuppetForgeClient();
		forgeClient.installToModulesDir(tempFolder, forgeClient.findModule("puppetlabs", "mongodb"));
		VagrantFolderTemplateConfiguration folderConfig = VagrantFolderTemplateConfigurationBuilder.create().withLocalFolder(tempFolder).withPathInVagrantFolder("puppet/modules").build();
		VagrantFileTemplateConfiguration fileConfig = VagrantFileTemplateConfigurationBuilder.create().withUrlTemplate(VagrantUtils.getInstance().load("/com/guigarage/vagrant/tutorials/myPuppetMongoManifest.pp")).withPathInVagrantFolder("manifests/myPuppetManifest.pp").build();

		VagrantVmConfig vmConfig = VagrantVmConfigBuilder.create().withName("demoVm")
				.withLucid32Box().withPuppetProvisionerConfig(puppetConfig).build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfigBuilder
				.create().withVagrantVmConfig(vmConfig).build();
		VagrantEnvironment vagrantEnvironmet = new Vagrant(true).createEnvironment(
				new File(FileUtils.getTempDirectory(),"myVagrantPath" + System.currentTimeMillis()), environmentConfig, Collections.singleton(fileConfig), Collections.singleton(folderConfig));
		vagrantEnvironmet.up();
		vagrantEnvironmet.destroy();
	}
}
