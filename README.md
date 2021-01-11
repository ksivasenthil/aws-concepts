# aws-concepts

Concepts of AWS (from their documentation) visualized as graph. Curated manually.

# pre-requisites

 * Create a container in cosmos db
 * Install apache-tinkerpop-gremlin-console only
 * Configure the console to securely connect with cosmos db
 
# connecting to remote server

 * Issue the command `:remote connect tinkerpop.server conf/remote-secure.yaml`
 * Followed by the command `:remote console`

# executing the script

To excute the script issue the command `. <Path to the groovy file>`

For example
. .\RootVolume-u0.groovy

In case you run into errors resolve to complete path.

# !!! caution !!!

The console groovy files are not complete; as-in I had created few vertexes and edges without a corresponding representation here.

I will work backwards and upate them here over time. Keep a watch for commit messages.

Such commits will have messages - `# Adding back-dated files`

# order for script execution

For the time being due to absence of other better alternative order in which the script files have to executed are listed here - 

  1. RegionsAndZones-u0.groovy
  2. RootVolume-u0.groovy
  3. LaunchingEc2-u0.groovy
  4. QuickStartEc2-u0.groovy