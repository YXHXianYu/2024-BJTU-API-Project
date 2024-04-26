#!/bin/bash

URL="http://192.168.1.100:8080/students?limit=10"

QPS=$1
DURATION=$2
TOTAL_REQUESTS=$((QPS * DURATION))
SLEEP_INTERVAL=$((1 / QPS))

echo "URL: $URL"
echo "QPS: $QPS, Duration: $DURATION sec, Interval: $SLEEP_INTERVAL sec."

for ((i=1; i<=TOTAL_REQUESTS; i++))
do
  curl -s "$URL" > /dev/null
  echo "Sent #$i"
  sleep $SLEEP_INTERVAL
done

echo "Done."
