# trade-enrichment-service

Instructions - 
1. Please download repo in your preferred IDE
2. This is a spring boot service, please run **TradeEnrichmentServiceApplication** to start the service.
3. To test -> 
use below in http client  

POST http://localhost:8080/api/v1/enrich
Accept: text/csv
Content-Type: text/csv

< <YOUR_PATH_To_PROEJCT>/trade-enrichment-service/src/test/resources/trade.csv

or 

curl --request POST --data @/<YOUR_PATH_To_PROEJCT>/trade-enrichment-service/src/test/resources/trade.csv --header 'Content-Type: text/csv' --header 'Accept: text/csv' http://localhost:8080/api/v1/enrich

Use cases handled - 
1. Missing Product Name - if mapping is missing for a product Id
2. Date Validation - basic validation where date format is imposed using jackson, we skip the row and continue

TODO - 
1. Testing - As I started with service class, so unit tests exist for to cover above mentioned use cases. Given more time I would have liked to develop tests first for other components of the service and some integration tests for controller too. 
2. product enrichment logic can be extracted in a separate class if we have more fields to enrich going forwards using some pattern (e.g.- strategy pattern).
3. Date validation logic can more sofesticated given some more time to work with. 
