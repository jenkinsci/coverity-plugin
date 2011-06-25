// (c) 2009 Coverity, Inc. All rights reserved worldwide.

package jenkins.plugins.coverity;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.coverity.ws.v3.*;

/**
 * Example application that uses Web Services to work with the
 * Coverity Integrity Manager server.  This application gets all the 
 * users and prints information about them.
 * 
 * Most of the complexity is in arranging WSS authentication for the
 * messages sent to the user.
 */
public class ListDefects {
    /**
     * <code>main</code> entry point.  
     */
	public static void main (String[] args) {
		try {
			ListDefects listDefects = new ListDefects();
			listDefects.displayList("localhost:18080", "password");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
    /**
     * Gets a list of assignable users from the target server address and writes
     * the list to standard output.
     *
     * @param serverAddr
     *          The address of the server, including the port number
     *          in <code>address:port</code> format.
     *
     * @param password
     *          The password for the "admin" user.
     */
	public void displayList(String serverAddr, String password) 
        throws Exception {
        try {
            // Create a Web Services port to the server
            DefectServiceService defectServiceService = new DefectServiceService(
                    new URL("http://" + serverAddr + "/ws/v3/defectservice?wsdl"),
                    new QName("http://ws.coverity.com/v3", "DefectServiceService"));
            DefectService defectService = defectServiceService.getDefectServicePort();

            ConfigurationService configurationService = new ConfigurationServiceService(
                    new URL("http://" + serverAddr + "/ws/v3/configurationservice?wsdl"),
                    new QName("http://ws.coverity.com/v3", "ConfigurationServiceService")).
                    getConfigurationServicePort();
            
            // Attach an authentication handler to it
            BindingProvider bindingProvider = (BindingProvider)defectService;
            bindingProvider.getBinding().setHandlerChain(
                new ArrayList<Handler>(Arrays.asList(
                    new ClientAuthenticationHandlerWSS("admin", password))));
            bindingProvider = (BindingProvider)configurationService;
            bindingProvider.getBinding().setHandlerChain(
                new ArrayList<Handler>(Arrays.asList(
                    new ClientAuthenticationHandlerWSS("admin", password))));


            SnapshotIdDataObj snapshotId = new SnapshotIdDataObj();
            snapshotId.setId(10001);
            List<Long> cids = defectService.getCIDsForSnapshot(snapshotId);
            System.out.println(cids);

            StreamIdDataObj streamId = new StreamIdDataObj();
            streamId.setName("claim");
            streamId.setType("STATIC");
            cids = defectService.getCIDsForStreams(Arrays.asList(streamId), new MergedDefectFilterSpecDataObj());
            for (Long cid: cids) {
                System.out.println("\tCID " + cid);
            }

            StreamDefectFilterSpecDataObj filterSpec = new StreamDefectFilterSpecDataObj();
            filterSpec.setIncludeDefectInstances(true);
            filterSpec.setIncludeHistory(true);
            List<StreamDefectDataObj> defects = defectService.getStreamDefects(cids, filterSpec);
            for (StreamDefectDataObj defect: defects) {
                System.out.println(defect.getAction());
                System.out.println(defect.getOwner());
                System.out.println(defect.getChecker());
                System.out.println("Defect Instances");
                for (DefectInstanceDataObj i: defect.getDefectInstances()) {
                    System.out.println("\t" + i);
                }
                System.out.println("History");
                for (DefectStateDataObj i: defect.getHistory()) {
                    System.out.println("\t" + i);
                }
            }

            System.out.println("getMergedDefectsForStreams");
            MergedDefectFilterSpecDataObj filterSpec1 = new MergedDefectFilterSpecDataObj();
            //filterSpec1.getStreamIdIncludeList().add(streamId);
            filterSpec1.getCidList().addAll(cids);
            PageSpecDataObj pageSpec = new PageSpecDataObj();
            pageSpec.setPageSize(100);
            MergedDefectsPageDataObj mergedDefectsForStreams = defectService.getMergedDefectsForStreams(Arrays.asList(streamId), filterSpec1, pageSpec);
            for (MergedDefectDataObj defect: mergedDefectsForStreams.getMergedDefects()) {
                System.out.println(defect);
            }

            System.out.println("getStreams");
            StreamFilterSpecDataObj filterSpec2 = new StreamFilterSpecDataObj();
            filterSpec2.setNamePattern("claim");
            List<StreamDataObj> streams =  configurationService.getStreams(filterSpec2);
            for (StreamDataObj stream: streams) {
                System.out.println(stream.getId().getName());
                System.out.println(stream.getDescription());
                System.out.println(stream.getLanguage());
            }

        }
        catch (CovRemoteServiceException_Exception x){
            System.err.println(x);
        }
        catch (SOAPFaultException x){
            System.err.println(x);
        }
        catch (WebServiceException x){
            System.err.println(x);
        }
    }
}
