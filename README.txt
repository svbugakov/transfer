1)git clone https://github.com/svbugakov/client.git
folder 'client' is created
2)git clone https://github.com/svbugakov/transfer.git
folder 'transfer' is created
3)go to transfer\server and run 'run.bat'
(to start again, you must first stop it 'stop.bat')
4)go to client\client\ and...

example:
1)
client getaccs                                                                                                                                                                                                                                    
Output from Server ....                                                                                                                                
                                                                                                                                                  
{"account":[{"acc":"40817000000000000001","balance":"198.00"},{"acc":"40817000000000000003","balance":"100.00"}]} 

2)client transfer 40817000000000000003 40817000000000000001 90                                                                                          
Output from Server ....                                                                                                                                
                                                                                                                                                      
Money transferred from the account 40817000000000000003 to 40817000000000000001 successfully

3)client getacc 40817000000000000001                                                                                                                                                                                            
Output from Server ....                                                                                                                                
                                                                                                                                                       
{"acc":"40817000000000000001","balance":"288.00"}  

etc....
------------------------------------------------------------------------
GUIDE ON COMMANDS
client getaccs - get all accounts and its balance
client getacc [ACC] - get the account [ACC] and it balance
client transfer [ACC_FROM] [ACC_TO] [SUM] - transfer [SUM] money between accounts [ACC_FROM] and [ACC_TO]
(I did not create the creation functional, and in theory
account closure (since the account can not be deleted), sorry I did not have enough time)
---------------------------------------------------------------------------------------------
GIIDE DEPLOY
--CLIENT
1) go to client and run gradle build
--SERVER
1) go to transfer and run gradle build
2) gradle buildProduct
3) find app in build/output/transfer


