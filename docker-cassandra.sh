#!/usr/bin/env bash
    docker run -d --name cassandra-server \
    --network app-tier \
    bitnami/cassandra:latest

docker container start cassandra-server