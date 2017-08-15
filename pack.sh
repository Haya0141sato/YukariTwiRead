#!/bin/sh

outDir=yukaTwiRead

if [ -e ${outDir} ];then
	rm -rf ${outDir}
fi
if [ -e yukaTwiRead.zip ];then
	rm yukaTwiRead.zip
fi

mkdir ${outDir}

jar -cvfm ytr.jar MANIFEST.MF yukaritwiread/twitter/*.class yukaritwiread/twitter/*.png
mv ytr.jar ${outDir}
cp YukariTwitterReader.sh ${outDir}
cp -r bin ${outDir}
cp -r lib ${outDir}
cp -r LICENCE ${outDir}
zip -r yukaTwiRead.zip ${outDir}
#rm -r ${outDir}
