# sblendor
Practicing Akka Cluster and Event Source Behavior

### 1. Running the server
    sbt -Dconfig.resource=local.conf run
### 2. Configure Cassandra Plugin
https://doc.akka.io/docs/akka-persistence-cassandra/current/configuration.html
#### 2.1 Minimal configuration for Cassandra plugin 
based on akka-persistence-cassandra example project

    akka {
        loglevel = DEBUG
        actor {
            provider = "cluster"
            serialization-bindings {
                "com.sblendor.domain.CborSerializable" = jackson-cbor
            }
        }
        
        cluster {
            seed-nodes = [
                "akka://Sblendor@127.0.0.1:25251",
                "akka://Sblendor@127.0.0.1:25252"
            ]
            downing-provider-class = "akka.cluster.sbr.SplitBrainResolverProvider"
        }
        
        persistence {
            cassandra {
                journal {
                  class = "akka.persistence.cassandra.journal.CassandraJournal"
                  keyspace-autocreate = true
                  tables-autocreate = true
                }
                snapshot {
                  class = "akka.persistence.cassandra.journal.CassandraJournal"
                  keyspace-autocreate = true
                  tables-autocreate = true
                }
                query {
                    refresh-interval = 2s
                }
                events-by-tag {
                    eventual-consistency-delay = 25ms
                    flush-interval = 25ms
                    pubsub-notification = on
                }
            }
            journal {
                plugin = "akka.persistence.cassandra.journal"
                auto-start-journals = ["akka.persistence.cassandra.journal"]
            }
            snapshot-store.plugin = "akka.persistence.cassandra.snapshot"
        }
    }

##### 2.1.1 For local setup
    akka {
        management {
            http {
                hostname = ${akka.remote.artery.canonical.hostname}
                port = 8551
            }
        }    
    
        remote {
            artery {
                canonical.hostname = "127.0.0.1"
                canonical.port = 0
            }
        }
    }

#### 2.2 Cassandra driver configuration
    datastax-java-driver {
        advanced.reconnect-on-init = true
        
        profiles {
            akka-persistence-cassandra-profile {
            basic.request {
                consistency = QUORUM
                default-idempotence = true
            }
        }
        akka-persistence-cassandra-snapshot-profile {
            basic.request {
                consistency = ONE
                default-idempotence = true
                }
            }
        }
    }

#### 2.3 Contact points configuration
    datastax-java-driver {
        basic.contact-points = ["cassandra:9042"]
        basic.load-balancing-policy.local-datacenter = "datacenter1"
    }

#### 2.4 Add this Akka Persistence Cassandra dependency for using the Cassandra plugin
https://doc.akka.io/docs/akka-persistence-cassandra/current/overview.html
    
    val akkaPersistenceCassandraDependencies = Seq(
        "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.0.4"
    )

###Glossary:
| Name  | Description |
| ----- | ----------- |
| Apache Cassandra | a NoSQL db |
| DataStax | a company that uses Apache Cassandra |
| reference.conf | contains defaults; included in core driver JAR |
