/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import com.coverity.ws.v6.CovRemoteServiceException_Exception;
import com.coverity.ws.v6.MergedDefectDataObj;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.model.User;
import hudson.tasks.Mailer;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Sends mails to culprits when Coverity defects are found. Inspired by hudson.tasks.MailSender.
 * <p/>
 * Not used at this point.
 */
public class CoverityMailSender {
    /**
     * The charset to use for the text and subject.
     */
    private String charset = "UTF-8";

    private final String recipients;

    @DataBoundConstructor
    public CoverityMailSender(String recipients) {
        this.recipients = Util.fixEmptyAndTrim(recipients);
    }

    public String getRecipients() {
        return recipients;
    }

    public boolean execute(CoverityBuildAction action, BuildListener listener) throws InterruptedException {
        try {
            MimeMessage mail = getMail(action, listener);
            if(mail != null) {
                Address[] allRecipients = mail.getAllRecipients();
                if(allRecipients != null) {
                    StringBuilder buf = new StringBuilder("Sending e-mails to:");
                    for(Address a : allRecipients)
                        buf.append(' ').append(a);
                    listener.getLogger().println(buf);
                    Transport.send(mail);
                }
            }
        } catch(MessagingException e) {
            e.printStackTrace(listener.error(e.getMessage()));
        } catch(CovRemoteServiceException_Exception e) {
            e.printStackTrace(listener.error(e.getMessage()));
        } catch(IOException e) {
            e.printStackTrace(listener.error(e.getMessage()));
        }

        return true;
    }

    protected MimeMessage getMail(CoverityBuildAction action, BuildListener listener) throws MessagingException, InterruptedException, IOException, CovRemoteServiceException_Exception {
        MimeMessage msg = createEmptyMail(action, listener);

        List<MergedDefectDataObj> defects = action.getDefects();
        msg.setSubject(String.format("Build failed due to %d Coverity defects: ", defects.size(), action.getBuild().getFullDisplayName()), charset);

        StringBuilder buf = new StringBuilder();

        buf.append("<html>");

        buf.append("<body>");

        buf.append(String.format(
                "<p style=\"font-family: Verdana, Helvetica, sans serif; font-size: 12px;\">" +
                        "<a href=\"%s%s\">%s</a> failed because " + (defects.size() > 1 ? "%d Coverity defects were" : "%d Coverity defect was") + " found.</p>",
                Mailer.descriptor().getUrl(),
                action.getBuild().getUrl(),
                action.getBuild().getFullDisplayName(),
                defects.size()));

        buf.append("<table style='width: 100%; border-collapse: collapse; border: 1px #BBB solid; font-family: Verdana, Helvetica, sans serif; font-size: 12px;'>\n");
        buf.append("<tr style='border: 1px #BBB solid; border-right: none; border-left: none; background-color: #F0F0F0; font-weight: bold;'>").append("<td colspan=\"3\" class=\"pane-header\">Coverity Defects</td></tr>");
        buf.append("<tr style='text-align: left;'><th>CID</th><th>Checker</th><th>Function</th></tr>\n");
        for(MergedDefectDataObj defect : defects) {
            buf.append("<tr>\n");
            buf.append("<td align='left'><a href=\"").append(action.getURL(defect)).append("\">").append(Long.toString(defect.getCid())).append("</a></td>\n");
            buf.append("<td align='left'>").append(Util.escape(defect.getCheckerName())).append("</td>\n");
            buf.append("<td align='left'>").append(Util.escape(defect.getFunctionDisplayName())).append("</td>\n");
            buf.append("</tr>\n");
        }
        buf.append("</table>");
        buf.append("</body>");
        buf.append("</html>");

        msg.setText(buf.toString(), charset, "html");

        return msg;
    }


    private MimeMessage createEmptyMail(CoverityBuildAction action, BuildListener listener) throws MessagingException {
        MimeMessage msg = new MimeMessage(Mailer.descriptor().createSession());
        msg.setContent("", "text/html");
        msg.setFrom(new InternetAddress(Mailer.descriptor().getAdminAddress()));
        msg.setSentDate(new Date());

        Set<InternetAddress> rcp = new LinkedHashSet<InternetAddress>();

        if(recipients != null) {
            StringTokenizer tokens = new StringTokenizer(recipients);
            while(tokens.hasMoreTokens()) {
                String address = tokens.nextToken();
                try {
                    rcp.add(new InternetAddress(address));
                } catch(AddressException e) {
                    // report bad address, but try to send to other addresses
                    e.printStackTrace(listener.error(e.getMessage()));
                }
            }
        }

        Set<User> culprits = action.getBuild().getCulprits();

        rcp.addAll(buildCulpritList(listener, culprits));
        msg.setRecipients(Message.RecipientType.TO, rcp.toArray(new InternetAddress[rcp.size()]));

        return msg;
    }

    private Set<InternetAddress> buildCulpritList(BuildListener listener, Set<User> culprits) throws AddressException {
        Set<InternetAddress> r = new HashSet<InternetAddress>();
        for(User a : culprits) {
            String adrs = Util.fixEmpty(a.getProperty(Mailer.UserProperty.class).getAddress());
            if(adrs != null)
                r.add(new InternetAddress(adrs));
        }
        return r;
    }

}
