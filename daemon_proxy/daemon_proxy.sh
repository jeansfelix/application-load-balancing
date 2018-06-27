#!/bin/bash
#
# description: Startup script for the application-load-balancing

# Source function library.
. /etc/rc.d/init.d/functions

PROGRAM_ARGUMENTS="-p 8443 -sc 'localhost:8080' -so 'localhost:8081'"
ALB_HOME="../target/"

start() {
    if [ -z `/usr/bin/pgrep -f "java -jar alb.jar"` ]; then
        echo "Starting alb..."
        cd ${ALB_HOME}

        java -jar alb.jar ${PROGRAM_ARGUMENTS} &
    else
        echo "alb is already running"
    fi
}

stop() {
    if [ ! -z `/usr/bin/pgrep -f "java -jar alb.jar"` ]; then
        echo "Stopping jetty..."
        kill -9 `/usr/bin/pgrep -f "java -jar alb.jar"`
        echo "alb stopped."
    else
        echo "alb is not running"
    fi
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
esac
