# Complex

> Why make it simple when it can be *complex*
> ... 10x dev

This project creates a distributed key-value store using various akka clustering technologies. An akka http server is used as the interface to the key-store which is stored on the cluster.

Currently, key-value stores have been created with

- [cluster sharding]()
- [distributed data]()

## Running the application

All implementations can be started in the same way:

- start the seed nodes
- start the http server


### Starting the seed nodes

#### Cluster Sharding

``` bash 

sbt clusterSharding/runMain com.idarlington.clusterSharding.NodeApp 2551 2552

```
#### Distributed Data

``` bash
sbt distributedData/runMain com.idarlington.distributedData.NodeApp 2551 2552
```



### Start the http server

#### Cluster Sharding

``` bash
sbt clusterSharding/runMain com.idarlington.clusterSharding.ComplexApp
```

#### Distributed Data

``` bash
sbt distributedData/runMain
com.idarlington.istributedData.ComplexApp
```

###
