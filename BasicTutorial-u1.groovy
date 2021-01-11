g.addV('network').property('id','Route53').property('name', 'Amazon Route53').property(list, 'key-phrases', 'Route 53 is highly available and scalable Domain Name System (DNS) web service.')
g.V('Route53').property(list, 'key-phrases', 'Performs 3 main function in any combination - DNS routing, Domain registration, Health checking')
g.V('Instance').addE('USES').to(g.V('EIP')).property('notes', 'For a stable public IP.')
g.V('EIP').addE('NEEDS').to(g.V('Route53')).property('notes', 'To map a IP address to a domain registered with Route53.')