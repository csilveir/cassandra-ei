#!/usr/bin/env bash
    docker run -d --name cassandra-server \
    --network app-tier \
    bitnami/cassandra:latest

docker container start cassandra-server

#Cliente

docker run -it --rm \
    --network app-tier \
    bitnami/cassandra:latest cqlsh --username cassandra --password cassandra cassandra-server