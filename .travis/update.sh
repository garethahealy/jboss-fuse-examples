#!/usr/bin/env bash

sudo -E apt-get -o Dpkg::Options::="--force-confnew" -yq --force-yes install oracle-java8-installer

sudo apt-get purge maven maven2 maven3
sudo add-apt-repository -y ppa:andrei-pozolotin/maven3
sudo apt-get update
sudo -E apt-get -o Dpkg::Options::="--force-confnew" -yq --force-yes install maven3

sudo ln -sfn /usr/local/maven /usr/share/maven3
