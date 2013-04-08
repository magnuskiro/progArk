#!/bin/sh

: <<'END'

# Description
This script compiles latex documents.

# Where it works
Linux: with pdlatex and texlive-extra
win7: with git bash and mikitex 

# Use
It takes one, or two arguments. 
The first being the document and the second being the log output file. 

TODO: add functionality for opening the pdf document in evince. 
"evince $doc &" 

compiling twice because once might not do the trick. 

END

if [ -n "$1" ] && [ -n "$2" ];
then
	pdflatex -interaction nonstopmode $1 > $2
	pdflatex -interaction nonstopmode $1 > $2
elif [ "$1" != "" ];
then
	pdflatex -interaction nonstopmode $1 > log
	pdflatex -interaction nonstopmode $1 > log
else
	echo "usage: ./compile file.tex file.log"
	exit 1
fi

# removes garbage after compile. 
rm -f log *.toc *.aux *.out  *.snm *.nav *.dvi 

exit 1
