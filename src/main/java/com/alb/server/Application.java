package com.alb.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.alb.server.model.Arguments;

public class Application {
    private static final String ARG_SERVER_OTHER = "server-other";
    private static final String ARG_SERVER_CHROME = "server-chrome";
    private static final String ARG_PORT = "port";

    private static int DEFAULT_HTTPS_SERVER_PORT = 8443;

    public static void main(String... args) throws Exception {
	try {
	    Arguments arguments = treatArguments(args);
	    
	    if (arguments != null) {
		BalancerServer.initServer(arguments.getPort(), arguments.getServerChrome(), arguments.getServerOther());
	    }
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    private static Arguments treatArguments(String[] args) throws ParseException {
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

	    Arguments arguments = new Arguments();
	    
	    if (line.hasOption(ARG_PORT))
		arguments.setPort(Integer.parseInt(line.getOptionValue(ARG_PORT)));
	    else
		arguments.setPort(DEFAULT_HTTPS_SERVER_PORT);

	    if (line.hasOption(ARG_SERVER_CHROME))
		arguments.setServerChrome(line.getOptionValue(ARG_SERVER_CHROME));

	    if (line.hasOption(ARG_SERVER_OTHER))
		arguments.setServerOther(line.getOptionValue(ARG_SERVER_CHROME));

	    return arguments;
	    
	} catch (ParseException exp) {
	    formatter.printHelp("help", options);
	    throw exp;
	}
    }
}
