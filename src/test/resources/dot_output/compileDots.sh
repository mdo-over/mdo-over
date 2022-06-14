#!/bin/sh
FILES=$(find -mindepth 2 -type f -name "*.dot")
for f in $FILES
do
 DIRECTORY=$(dirname $f)
 dot $f -Tpdf -o${f%.*}".pdf" 
done