#!bash.sh
git fetch upstream
git merge upstream/master
git commit -a -m "merged upstream."
git push

