#!/bin/bash

echo "start init create queue"

awslocal sqs create-queue --queue-name dao-local-queue.fifo --attributes FifoQueue=true,ContentBasedDeduplication=true
