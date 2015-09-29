#!/bin/bash

downloadJobs=("weps-3/task-1/test/")

for baseDir in "${downloadJobs[@]}"
do
    echo "${baseDir}"
    wget --input-file="${baseDir}web_pages.lnk" --output-document="${baseDir}web_pages.tar.gz"
    tar xzvf "${baseDir}web_pages.tar.gz" -C ${baseDir}
    rm "${baseDir}web_pages.tar.gz"
done
