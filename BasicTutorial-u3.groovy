g.V('PublicCaCert').addE('CONTRIBUTES_SECURITY').to(g.V('AMI'))
g.V('PublicCaCert').addE('NEXT').to(g.V('PublicCaCert-1'))
g.V('PublicCaCert-1').addE('NEXT').to(g.V('PublicCaCert-2'))
g.V('PublicCaCert-2').addE('NEXT').to(g.V('PublicCaCert-3'))
g.V('PublicCaCert-3').addE('NEXT').to(g.V('PublicCaCert-4'))