#!/bin/bash

# Simple script to list last author and commit date, as well as number of
# unmerged commits, for each remote branch of a given remote.
#
# This is useful for analyzing which branches are obsolete and/or moldy.

remote="$@"
if [ "$remote" == "" ];
then
  echo Please specify one of the following remotes:
  git remote
  exit 1
fi

for ref in $(git for-each-ref refs/remotes/$remote --format='%(refname)')
do
  refname=$(echo $ref | sed -e "s-refs/remotes/$remote/--")
  unmerged_count=$(git cherry master $ref | grep '^+' | wc -l)
  info=$(git log -n 1 --format='%an - %ar' $ref)
  echo $refname - $info - $unmerged_count unmerged
done
