#!/usr/bin/env bash

set -e

dub wait mosquitto 1883 20
dub wait elasticsearch 9200 20

exec "$@"