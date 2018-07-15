package com.alb.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.alb.server.model.ServerConfig;

public class Application {
    private static final String ARG_SERVER_OTHER = "server-other";
    private static final String ARG_SERVER_CHROME = "server-chrome";
    private static final String ARG_PORT = "port";

    private static Integer DEFAULT_HTTPS_SERVER_PORT = 8443;
    private static String DEFAULT_PROXYTO_CHROME = "http://localhost:8080";
    private static String DEFAULT_PROXYTO_OTHER = "http://localhost:8081";

    public static void main(String... args) throws Exception {
        try {
            ServerConfig serverConfig = treatArguments(args);

            if (serverConfig != null) {
                BalancerServer.initServer(serverConfig);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    private static ServerConfig treatArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("help", "help menu");
        options.addOption("p", ARG_PORT, true, "server proxy port. Default: 8443");
        options.addOption("sc", ARG_SERVER_CHROME, true, "server for chrome requests. Default: localhost:8080");
        options.addOption("so", ARG_SERVER_OTHER, true, "server for others requests. Default: localhost:8081");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("help")) {
                formatter.printHelp("help", options);
                return null;
            }

            ServerConfig serverConfig = new ServerConfig(DEFAULT_HTTPS_SERVER_PORT, DEFAULT_PROXYTO_CHROME,
                    DEFAULT_PROXYTO_OTHER);

            if (line.hasOption(ARG_PORT))
                serverConfig.setPort(Integer.parseInt(line.getOptionValue(ARG_PORT)));

            if (line.hasOption(ARG_SERVER_CHROME))
                serverConfig.setChromeBrowserTarget(line.getOptionValue(ARG_SERVER_CHROME));

            if (line.hasOption(ARG_SERVER_OTHER))
                serverConfig.setOtherBrowserTarget(line.getOptionValue(ARG_SERVER_OTHER));

            return serverConfig;

        } catch (ParseException exp) {
            formatter.printHelp("help", options);
            throw exp;
        }
    }
}
