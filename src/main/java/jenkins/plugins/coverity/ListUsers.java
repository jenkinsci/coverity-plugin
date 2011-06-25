// (c) 2009 Coverity, Inc. All rights reserved worldwide.

package jenkins.plugins.coverity;

import javax.xml.ws.WebServiceRef;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.ws.handler.Handler;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.coverity.ws.v3.*;

/**
 * Example application that uses Web Services to work with the
 * Coverity Integrity Manager server.  This application gets all the 
 * users and prints information about them.
 * 
 * Most of the complexity is in arranging WSS authentication for the
 * messages sent to the user.
 */
public class ListUsers {
    /**
     * <code>main</code> entry point.  
     */
	public static void main (String[] args) {
		try {
			if(args.length != 2){
				System.err.println(
                    "Usage:\n <this-command> <server-address>:<port> <admin-password>");
				System.exit(1);
			}
			ListUsers listUsers = new ListUsers();
			listUsers.displayList(args[0], args[1]);
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
            AdministrationServiceService administrationServiceService = 
                new AdministrationServiceService(
                    new URL("http://" + serverAddr + "/ws/v3/administrationservice?wsdl"),
                    new QName("http://ws.coverity.com/v3", "AdministrationServiceService"));
            AdministrationService administrationService = 
                administrationServiceService.getAdministrationServicePort();
            
            // Attach an authentication handler to it
            BindingProvider bindingProvider = (BindingProvider)administrationService;
            bindingProvider.getBinding().setHandlerChain(
                new ArrayList<Handler>(Arrays.asList(
                    new ClientAuthenticationHandlerWSS("admin", password))));
            
            // Describe the pages of data to return.
            PageSpecDataObj pageSpec = new PageSpecDataObj();
            pageSpec.setPageSize(100);

            // Get the list of assignable users, a page at a time.
            int userCount = 0;
            while(true) {
                pageSpec.setStartIndex(userCount);
                UsersPageDataObj usersPage = administrationService.getAssignableUsers(pageSpec);
                
                for(UserDataObj aUser : usersPage.getUsers()){
                    // Print the username, actual name, email address 
                    // and creation date of each user
                    System.out.println(aUser.getUsername()
                            + ": " + aUser.getGivenName()
                            + " " + aUser.getFamilyName()
                            + " (" + aUser.getEmail()
                            + ") " + aUser.getDateCreated());
                            
                }

                userCount += usersPage.getUsers().size();
                if(userCount >= usersPage.getTotalNumberOfRecords())
                    break;
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
