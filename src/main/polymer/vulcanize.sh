#!/bin/bash

if [ ! -f "vulcanize.sh" ]; 
then
	echo "This script needs to be run from within the polymer directory.";
	exit 1;
fi

vulcanize --inline-scripts --inline-css -p ./ index.html > ../resources/static/index.html
