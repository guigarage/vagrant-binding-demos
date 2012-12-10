package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.guigarage.vagrant.Vagrant;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.configuration.builder.VagrantEnvironmentConfigBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantPortForwardingBuilder;
import com.guigarage.vagrant.configuration.builder.VagrantVmConfigBuilder;
import com.guigarage.vagrant.model.VagrantEnvironment;
import com.guigarage.vagrant.model.VagrantVm;
import com.guigarage.vagrant.util.VagrantUtils;

public class VagrantTutorial2 {

	public static void main(String[] args) throws IOException {
		VagrantVmConfig vmConfig1 = VagrantVmConfigBuilder.create().withName("32BitVm")
				.withLucid32Box().build();
		VagrantVmConfig vmConfig2 = VagrantVmConfigBuilder
				.create()
				.withBoxName("lucid64").withName("64BitVm")
				.withBoxUrl(VagrantUtils.getInstance().getLucid64Url())
				.withHostOnlyIp("192.168.50.4")
				.withVagrantPortForwarding(
						VagrantPortForwardingBuilder.create()
								.withGuestPort(7411).withHostPort(8080).build())
				.build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfigBuilder
				.create().withVagrantVmConfig(vmConfig1).withVagrantVmConfig(vmConfig2).build();
		VagrantEnvironment vagrantEnvironmet = new Vagrant(true).createEnvironment(
				new File(FileUtils.getTempDirectory(),"myVagrantPath" + System.currentTimeMillis()), environmentConfig);
		for(VagrantVm vm : vagrantEnvironmet.getAllVms()) {
			vm.start();
		}
		for(VagrantVm vm : vagrantEnvironmet.getAllVms()) {
			if(vm.isRunning()) {
				System.out.println("VM " + vm.getName() + " is running");
			} else {
				System.out.println("VM " + vm.getName() + " is not running");
			}
		}
		for(VagrantVm vm : vagrantEnvironmet.getAllVms()) {
			vm.destroy();
		}
	}
}
