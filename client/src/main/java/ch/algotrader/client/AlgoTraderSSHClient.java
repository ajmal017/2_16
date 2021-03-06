/***********************************************************************************
 * AlgoTrader Enterprise Trading Framework
 *
 * Copyright (C) 2015 AlgoTrader GmbH - All rights reserved
 *
 * All information contained herein is, and remains the property of AlgoTrader GmbH.
 * The intellectual and technical concepts contained herein are proprietary to
 * AlgoTrader GmbH. Modification, translation, reverse engineering, decompilation,
 * disassembly or reproduction of this material is strictly forbidden unless prior
 * written permission is obtained from AlgoTrader GmbH
 *
 * Fur detailed terms and conditions consult the file LICENSE.txt or contact
 *
 * AlgoTrader GmbH
 * Aeschstrasse 6
 * 8834 Schindellegi
 ***********************************************************************************/
package ch.algotrader.client;

// CHECKSTYLE:OFF
import sun.tools.jconsole.JConsole;

// CHECKSTYLE:ON
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * @author <a href="mailto:aflury@algotrader.ch">Andy Flury</a>
 */
public class AlgoTraderSSHClient {

    public static void main(String[] args) throws Exception {

        // initialize JSch
        String user = args[0].substring(0, args[0].indexOf('@'));
        String host = args[0].substring(args[0].indexOf('@') + 1);

        JSch jsch = new JSch();

        jsch.addIdentity(args[1], args[2]);

        Session session = jsch.getSession(user, host, 22);

        UserInfo ui = new MyUserInfo();
        session.setUserInfo(ui);

        session.connect();

        // set up the tunnels
        for (String arg : args) {
            if (arg.startsWith("localhost")) {
                int port = Integer.parseInt(arg.substring(10));
                session.setPortForwardingL(port, "localhost", port);
                session.setPortForwardingL(port - 1, "localhost", port - 1);
            }
        }

        // remove the first 3 arguments
        String[] jConsoleArgs = new String[args.length - 3];

        System.arraycopy(args, 3, jConsoleArgs, 0, args.length - 3);

        // invoke JConsole
        JConsole.main(jConsoleArgs);
    }

    public static class MyUserInfo implements UserInfo {

        @Override
        // respond yes to unknown hosts
        public boolean promptYesNo(String str) {
            if (str.contains("The authenticity of host") && str.contains("can't be established")) {
                return true;
            } else {
                System.out.println(str);
                return false;
            }
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
            System.out.println(message);
        }
    }
}
