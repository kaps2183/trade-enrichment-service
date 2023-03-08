# trade-enrichment-service

Instructions - 
1. Download repo in your preferred IDE
2. This is a spring boot service, please run TradeEnrichmentServiceApplication that should start the service.
3. To test -> 
use below in http client  

POST http://localhost:8080/api/v1/enrich
Accept: text/csv
Content-Type: text/csv

< <YOUR_PATH_To_PROEJCT>/trade-enrichment-service/src/test/resources/trade.csv

or 

# curl --request POST --data @/<YOUR_PATH_To_PROEJCT>/trade-enrichment-service/src/test/resources/trade.csv --header 'Content-Type: text/csv' --header 'Accept: text/csv' http://localhost:8080/api/v1/enrich
