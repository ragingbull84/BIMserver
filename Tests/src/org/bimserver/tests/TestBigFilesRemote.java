package org.bimserver.tests;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.PublicInterfaceNotFoundException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.ServiceException;

public class TestBigFilesRemote {
	 public static void main(String[] args) {
		new TestBigFilesRemote().start(args);
	}

	private void start(String[] args) {
		JsonBimServerClientFactory factory = new JsonBimServerClientFactory(args[0]);
		try {
			BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo(args[1], args[2]));
			
			String basePath = "";
			
			String[] fileNames = new String[]{
				"4NC Whole Model.ifc",
				"1006 General withIFC_exportLayerCombos.ifc",
				"12001_17 MOS_AC17SpecialBigVersion.ifc",
				"12510_MASTER_Drofus_Test.ifc",
				"BondBryan10-134 (06) Proposed Site-1.ifc",
				"HLM_39090_12259 University of Sheffield NEB  [PR-BIM-01-bhelberg].ifc"
			};
			
			SDeserializerPluginConfiguration deserializer = client.getBimsie1ServiceInterface().getSuggestedDeserializerForExtension("ifc");
			
			for (String fileName : fileNames) {
				String projectName = fileName.substring(0, fileName.lastIndexOf(".ifc"));
				System.out.println("Creating project " + fileName);
				SProject project = client.getBimsie1ServiceInterface().addProject(projectName);
				client.getServiceInterface().checkinFromUrl(project.getOid(), fileName, deserializer.getOid(), fileName, basePath + fileName, false, true);
				System.out.println("Done checking in " + fileName);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (ChannelConnectionException e) {
			e.printStackTrace();
		} catch (PublicInterfaceNotFoundException e) {
			e.printStackTrace();
		}
	}
}