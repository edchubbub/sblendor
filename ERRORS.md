
- Error: WARN akka.remote.artery.InboundHandshake$$anon$2 - Dropping Handshake Request from [akka://Sblendor@127.0.0.1:25251#-8494672189043404144] addressed to unknown local address [akka://ClusterSystem@127.0.0.1:25252]. Local address is [akka://Sblendor@127.0.0.1:25252]. Check that the sending system uses the same address to contact recipient system as defined in the 'akka.remote.artery.canonical.hostname' of the recipient system. The name of the ActorSystem must also match.
- Cause: Different name for ActorSystem in akka.cluster.seed-nodes and what is in initialisation of actor system

- Error: ERROR com.sblendor.BoardGame$ - Supervisor StopSupervisor saw failure: Default journal plugin is not configured, see 'reference.conf'
         java.lang.IllegalArgumentException: Default journal plugin is not configured, see 'reference.conf'
- Cause: When creating an ESB archi app and no "journal" settings was set 
        