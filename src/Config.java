/*
 * This is the Configuration object for storing
 * the setting to run our code.
 */

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Config {
    // store our mode as a character
    private char mode = 0; // null character
    // storing our output mode
    private char outputMode = 'm';

    //This is to keep track of checkpoint arguments
    private boolean checkpoint1 = false;
    private boolean checkpoint2 = false;

    //getOpt options
    public static LongOpt[] longOptions = {
            new LongOpt("help", LongOpt.NO_ARGUMENT, null,'h'),
            new LongOpt("stack", LongOpt.NO_ARGUMENT, null,'s'),
            new LongOpt("queue", LongOpt.NO_ARGUMENT, null,'q'),
            new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null,'o'),
            new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null,'x'),
            new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null,'y'),
    };


    /*
     * Construct our configuration object using GetOpt
     * @param args string of command line arguments
     */
    public Config(String[] args){
        // we will do all getOpt parcing

        // making a getOpt object
        Getopt g = new Getopt("mazeSolver", args, "hsqo:xy", longOptions);
        g.setOpterr(true);

        int choice;

        //proccess each argument from the command line in turn
        while((choice = g.getopt()) != -1) {
            // check which argument we are processing
            switch (choice) {
                case 'h':
                    printhelp();
                    break;
                case 's':
                case 'q':
                    // verify this was provided once
                    // if mode is not null character, this indicates an error.
                    if (mode != 0) {
                        System.err.println("Mode was already set.");
                        System.exit(1);
                    }
                    mode = (char) choice;
                    break;
                case 'o':
                    // check and designate the requested output
                    String requestedOutput = g.getOptarg();
                    if (requestedOutput.equals("map") || requestedOutput.equals("list"))
                        outputMode = requestedOutput.charAt(0);
                    else{
                        System.err.println("Unknown output mode -" + requestedOutput);
                        System.exit(1);
                    }
                    break;
                case 'x':
                    checkpoint1 = true;
                    break;
                case 'y':
                    checkpoint2 = true;
                    break;
                default:
                    System.err.println("Unknown command line option -" + (char )choice);
                    System.exit(1);


            } // switch()
        } // while()

        // we need to do some error checking here
        // check if we have stack or queue mode specified
        if (mode == 0){
            System.err.println("You must specify stack or queue mode in command line");
            System.exit(1);
        }


    }

    public boolean isQueuemode() {
        return mode == 'q';
    }

    public boolean isMapoutputMode() {
        return outputMode == 'm';
    }
    public boolean isListoutputMode() {
        return outputMode == 'l';
    }


    public boolean isCheckpoint1() {
        return checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }
    private void printhelp() {
        System.out.println("The help argument has been triggered in the command line. Figure it out.");
    }

    public boolean isStackmode() {
        return mode == 's';
    }
}
