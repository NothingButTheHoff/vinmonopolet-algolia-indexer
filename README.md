# vinmonopolet-algolia-indexer

Application for indexing products from Vinmonopolet to Algolia.

## Prerequisites 

Before you run the indexer you need to set up an index at Algolia
  1. Create an account on https://www.algolia.com/users/sign_up
  2. Create a new index
  3. Replace `appId`, `apiKey` and `indexName` in AlgoliaService.kt from the newly created index ( use admin API key when working with the index)
  
  
## Build & Run

`$ ./mvnw clean install`

`$ ./mvnw spring-boot:run`



# Run indexer

Go to `http://localhost:8080/` to trig the rest enpoint performing the indexing job




