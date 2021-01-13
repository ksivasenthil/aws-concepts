# Interesting questions by knowledge miners

## Assumption

It is assumed that you have run all the groovy files in the order mentioned in [ReadMe](../ReadMe.md)

Assuming you have no idea about the graph and its connectivity, every question will start with a basic discovery query.

The result of this query will be used to march further ahead towards a probable answer.

I am running these questions against the graph database form a [tinkerpop-gremlin-console](https://www.apache.org/dyn/closer.lua/tinkerpop/3.4.9/apache-tinkerpop-gremlin-console-3.4.9-bin.zip).
I assume you will also do the same, though it is not restrictive.
You could use any other approach, but then you might have use the outline of the query steps and re-build in that approach.

## Purpose

Knowledge miner is you!

Given, you have assimilated knowledge in the structure of a graph, you will be interested in how to use this knowledge.

Answer to that is simple, using questions !

Yes you need to ask question to the graph we have built. 

This file is a compilation of some interesting questions *[ from a single knowledge miner (myself) ]* that can be asked atop the graph structure we are building.

Intent is to put to use the valuable knowledge compiled so-far. Particularly when the knowledge is compiled manually.

**You have the power to remove the statement within square brackets couple of lines above by contributing**

## Questions

### What steps are involved in configuring a secure connection for Amazon Linux 2 instance?


First let us find if we have some vertex for Amazon Linux 2 instance - 

    g.V().
    has('name', 
      containing('Linux 2')).
    values('id', 'name')

This will yield the result

    ==>AmzLinux2Tls
    ==>Configure SSL/TLS on Amazon Linux 2

We now have both id and name. We will use the id to enquire if that contains connections to other vertexes.
We inspect this because any process will have steps. We hope the steps are modelled as seperate vertexes.
Also, the question is about determining the steps involved in a specific process i.e. configuring a secure connection.

We are thus interested in the edges from the above discovered vertex.

    g.V('AmzLinux2Tls').
    outE().
    values('label').
    dedup()

This yields the result 

    ==>CONTRIBUTES_SECURITY
    ==>NEXT

So, we have 2 kind of relationship flowing out of vertex `AmzLinux2Tls`.
We can inspect both but by the look of it we can guess `NEXT` is of more relevance than `CONTRIBUTES_SECURITY` to our context.

Let us now see what is/are connected to `NEXT`

    g.V('AmzLinux2Tls').
    outE('NEXT').
    inV().
    values('id', 'name', 'label')

This will yield a result like the following 

    ==> AmzLinux2Tls-1
    ==> Check Apache Web Server is running
    ==> process-step

This is a sequential print of id of the node and the name. We know the result resembles a step.
Let us take this to see how many further steps are involved. We also take a leap of faith on the modeller that knowledge of steps would have been modelled as linked list.

So, we are interested in finding what is the `NEXT` step after `Check Apache Web Server is running`

We can do that by

    g.V('AmzLinux2Tls-1').
    outE('NEXT').
    inV().
    values('id', 'name', 'label')

The above query yields  - 

    ==> AmzLinux2Tsl-2
    ==> Update all software packages
    ==> process-step

Now, let us complete this by fetching all steps with a combination of `until` and `repeat` step modifier in gremlin.

    g.V('AmzLinux2Tls').
    until(
      has('process-step')).
    repeat(
      out('NEXT')).
    emit().
    values('id')

This should yield the following - 

    ==>AmzLinux2Tls-1
    ==>AmzLinux2Tls-2
    ==>AmzLinux2Tls-3
    ==>AmzLinux2Tls-4
    ==>AmzLinux2Tls-5
    ==>AmzLinux2Tls-6
    ==>AmzLinux2Tls-7

So, we see that it is a process with 7 steps. Let us get the names of each step so that we could easily get a glimpse of what is involved.

    g.V('AmzLinux2Tls').
    until(
      has('process-step')).
    repeat(
      out('NEXT')).
    emit().
    values('name')

This should yield the following - 

    ==>Check Apache Web Server is running
    ==>Update all software packages
    ==>Install TLS module for Apache
    ==>Generate self-signed certificate
    ==>Disable a seperate key file directive
    ==>Restart Apache Web Server
    ==>Test the SSL configuration.

So, we have the answer now. However, let us be prudent and see if there are other branches from any of the vertex.

Branch could have been modelled for situation where a particular step fails, or alternative or just another way.

To inspect, that possibility we need to see what kind of edges flow from each step.

We accomplish that with

    g.V().
    has('process-step', 'id', 
      containing('AmzLinux2Tls')).
    bothE().
    as('a').
    project('label', 'outVId', 'inVId').
      by('label').
      by(select('a').
        outV().
        values('id')).
      by(select('a').
        inV().
        values('id')).
      dedup()

This is pretty long one which yields the following

    ==>[label:NEXT_OPTIONAL,outVId:AmzLinux2Tls-1,inVId:AmzLinux2Tls-1a]
    ==>[label:NEXT,outVId:AmzLinux2Tls-1,inVId:AmzLinux2Tls-2]
    ==>[label:NEXT,outVId:AmzLinux2Tls,inVId:AmzLinux2Tls-1]
    ==>[label:NEXT,outVId:AmzLinux2Tls-1a,inVId:AmzLinux2Tls-2]
    ==>[label:NEXT,outVId:AmzLinux2Tls-2,inVId:AmzLinux2Tls-3]
    ==>[label:NEXT,outVId:AmzLinux2Tls-3,inVId:AmzLinux2Tls-4]
    ==>[label:NEXT,outVId:AmzLinux2Tls-4,inVId:AmzLinux2Tls-5]
    ==>[label:NEXT,outVId:AmzLinux2Tls-5,inVId:AmzLinux2Tls-6]
    ==>[label:NEXT,outVId:AmzLinux2Tls-6,inVId:AmzLinux2Tls-7]
    ==>[label:REFERENCE,outVId:AmzLinux2CaCert-11,inVId:AmzLinux2Tls-7]

We just discovered we have two kind of branches `NEXT_OPTIONAL` and `REFERENCE`.

In fact `REFERENCE` branch is pointing to a completely different kind of vertex. However, a pure branch like an equivalent for an optional step to the current process is modelled as `NEXT_OPTIONAL`.

So, let us now discover all the steps involved with this branching as well !

    g.V('AmzLinux2Tls').
    until(
      has('process-step')).
    repeat(
      out('NEXT', 'NEXT_OPTIONAL')).
    emit().
    as('a').
    project('edgeLabel', 'name').
      by(select('a').
        inE().
        values('label')).
      by('name').dedup()

This gives us just this - 

    ==>[edgeLabel:NEXT,name:Check Apache Web Server is running]
    ==>[edgeLabel:NEXT_OPTIONAL,name:Auto start Apache Web Server on booting]
    ==>[edgeLabel:NEXT,name:Update all software packages]
    ==>[edgeLabel:NEXT,name:Install TLS module for Apache]
    ==>[edgeLabel:NEXT,name:Generate self-signed certificate]
    ==>[edgeLabel:NEXT,name:Disable a seperate key file directive]
    ==>[edgeLabel:NEXT,name:Restart Apache Web Server]
    ==>[edgeLabel:NEXT,name:Test the SSL configuration.]

This is our final answer.

**NOTE** *We could have executed just the last query to get the answer. However, in real life even if it was the individual who created the graph, it is difficult to remember all these relationship.
A time saver recommendation will be to use a pictorial graph model for the schema which will help discover much structure of what we discovered via multiple queries.*