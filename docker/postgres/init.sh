#!/bin/bash
set -e

psql -U docker -h 172.18.0.4 -p 5432 -d postgres -c "CREATE DATABASE authenticate;"
psql -U docker -h 172.18.0.4 -p 5432 -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE authenticate TO docker;"
psql -U docker -h 172.18.0.4 -p 5432 -d postgres -c "CREATE SCHEMA app;"
psql -U docker -h 172.18.0.4 -p 5432 -d postgres -c  "\dn"
