package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.guigarage.vagrant.Vagrant;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.configuration.builder.VagrantEnvironmentConfigBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantVmConfigBuilder;
import com.guigarage.vagrant.model.VagrantEnvironment;

public class VagrantTutorial3 {

	public static void main(String[] args) throws IOException {
		VagrantVmConfig vmConfig = VagrantVmConfigBuilder.create().withName("demoVm")
				.withLucid32Box().build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfigBuilder
				.create().withVagrantVmConfig(vmConfig).build();
		VagrantEnvironment vagrantEnvironmet = new Vagrant(true).createEnvironment(
				new File(FileUtils.getTempDirectory(),"myVagrantPath" + System.currentTimeMillis()), environmentConfig);
		vagrantEnvironmet.up();
		vagrantEnvironmet.getVm(0).createConnection().execute("touch /tmp/newFile.tmp", false);
		vagrantEnvironmet.getVm(0).createConnection().upload("path/to/local/file", "path/on/vm");
		vagrantEnvironmet.destroy();
	}
}
