#!/bin/bash

EXT_HOME=$JAVA_HOME/jre/lib/ext
START_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
OS=""

if [[ $OSTYPE = linux* ]] || [[ $OSTYPE = unix* ]]
then
	OS="linux"
fi

if [[ $OSTYPE = darwin* ]]
then
	OS="osx"
fi

if [[ $OSTYPE = msys* ]]
then
	OS="windows"
fi

echo "OS type:"$OSTYPE
echo "OS:"$OS
echo "start dir:"$START_DIR
echo "java ext home:"$EXT_HOME
echo "input file:"$1
echo "output file:"$2


if [[ $OS = linux ]]
then
	echo "Running linux start script..."
	java -Dfile.encoding=UTF-8 -DinputFile=$1 -DoutputFile=$2 -classpath "$START_DIR/jar/zzMavenTest.jar:$EXT_HOME/access-bridge-64.jar:$EXT_HOME/cldrdata.jar:$EXT_HOME/dnsns.jar:$EXT_HOME/jaccess.jar:$EXT_HOME/jfxrt.jar:$EXT_HOME/localedata.jar:$EXT_HOME/nashorn.jar:$EXT_HOME/sunec.jar:$EXT_HOME/sunjce_provider.jar:$EXT_HOME/sunmscapi.jar:$EXT_HOME/sunpkcs11.jar:$EXT_HOME/zipfs.jar" -Djava.ext.dirs="$START_DIR/jar" zz.maven.test.AiPackExcelTest
elif [[ $OS = windows ]]
then
	echo "Running windows start script..."
	START_DIR="$(echo "$START_DIR" | sed -e 's/^\///' -e 's/\//\\/g' -e 's/^./\0:/')"
	echo "start dir:"$START_DIR
	java -Dfile.encoding=UTF-8 -DinputFile=$1 -DoutputFile=$2 -classpath "$START_DIR/jar/zzMavenTest.jar;$EXT_HOME/access-bridge-64.jar;$EXT_HOME/cldrdata.jar;$EXT_HOME/dnsns.jar;$EXT_HOME/jaccess.jar;$EXT_HOME/jfxrt.jar;$EXT_HOME/localedata.jar;$EXT_HOME/nashorn.jar;$EXT_HOME/sunec.jar;$EXT_HOME/sunjce_provider.jar;$EXT_HOME/sunmscapi.jar;$EXT_HOME/sunpkcs11.jar;$EXT_HOME/zipfs.jar" -Djava.ext.dirs="$START_DIR/jar" zz.maven.test.AiPackExcelTest
elif [[ $OS = osx ]]
then
	echo "Running osx start script..."
	java -Dfile.encoding=UTF-8 -DinputFile=$1 -DoutputFile=$2 -classpath "$START_DIR/jar/zzMavenTest.jar:$EXT_HOME/access-bridge-64.jar:$EXT_HOME/cldrdata.jar:$EXT_HOME/dnsns.jar:$EXT_HOME/jaccess.jar:$EXT_HOME/jfxrt.jar:$EXT_HOME/localedata.jar:$EXT_HOME/nashorn.jar:$EXT_HOME/sunec.jar:$EXT_HOME/sunjce_provider.jar:$EXT_HOME/sunmscapi.jar:$EXT_HOME/sunpkcs11.jar:$EXT_HOME/zipfs.jar" -Djava.ext.dirs="$START_DIR/jar" zz.maven.test.AiPackExcelTest
else
	echo "Running default start script..."
	java -Dfile.encoding=UTF-8 -DinputFile=$1 -DoutputFile=$2 -classpath "$START_DIR/jar/zzMavenTest.jar:$EXT_HOME/access-bridge-64.jar:$EXT_HOME/cldrdata.jar:$EXT_HOME/dnsns.jar:$EXT_HOME/jaccess.jar:$EXT_HOME/jfxrt.jar:$EXT_HOME/localedata.jar:$EXT_HOME/nashorn.jar:$EXT_HOME/sunec.jar:$EXT_HOME/sunjce_provider.jar:$EXT_HOME/sunmscapi.jar:$EXT_HOME/sunpkcs11.jar:$EXT_HOME/zipfs.jar" -Djava.ext.dirs="$START_DIR/jar" zz.maven.test.AiPackExcelTest
fi
