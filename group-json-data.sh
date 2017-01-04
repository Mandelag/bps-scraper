#!/bin/sh
# Join all of the map data into one JSON array for easier processing.

first="TRUE"

echo [
for i in $(ls region-data-4/*.json); do
	if [ $first = "TRUE" ]; then
		first="FALSE"
	else
		echo ,
	fi
	cat $i
done

echo ]
