maven=$1

find . -name pom.xml | xargs  sed -i -b "0,/<version>.*<\/version>/{s/<version>[0-9]\.[0-9]\.[0-9].*<\/version>/<version>${maven}<\/version>/}"

sed -i -b "s/<version>[0-9]\.[0-9]\.[0-9].*<\/version>/<version>${maven}<\/version>/" archetype/src/main/resources/archetype-resources/pom.xml

