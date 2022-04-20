# freight-stats-api

# Introduction

API calls generally follow the following format, where `...` is the address the API is hosted at and `<table name>` is,
as you can guess, the name of the table you want to query. All requests return JSON if they were successful, but a
simple string if there was an error. This string tells you what went wrong during the request. Unfortunately for you,
this means you have to manually check whether the string returned by the request can be decoded into JSON.
```
.../api/<table name>
```

The tables, for your convenience:

- shipment
- commodity
- transportMethod
- state
- country
- covidCase

Keep in mind that the API uses camel casing, unlike the snake casing used in our MySQL table names. There's also an API
call named `monthly_value` that is not a table, but rather a call that makes use of an aggregator function.
  
So far, `shipment` is the only table that accepts parameters in its requests:

- `id` : Corresponds to `shipment_id`.
- `tradeType` : Corresponds to `trade_type`, must be 1 or 2.
- `commodityId` : Corresponds to `commodity_type`.
- `transportMethod` : Corresponds to `transport_method`.
- `source` : Corresponds to `source_state`.
- `destination` : Corresponds to `destination_state`.
- `value` : Corresponds to table column of the same name.
- `weight` : Corresponds to table column of the same name.
- `freightCharges` : Corresponds to `freight_charges`.
- `df` : Corresponds to table column of the same name, must be 0, 1, or 2.
- `containerized` : Corresponds to table column of the same name.
- `date` : Corresponds to `ship_date`, must be in the format `YYYY-mm-dd`.
- `start_date` : Corresponds to `ship_date`, but the value given acts as a start of a date range (inclusive).
- `end_date` : Same as above, but acts as the end of a date range instead.
- `orderBy` : Corresponds to no column, but any column names you provide will be passed as `ORDER BY` parameters.
- `order` : Corresponds to no column, but must be either `asc` or `desc`.

Providing no (valid) parameters will give you all rows. However, regardless of your query, the amount of rows will be
limited to only 500.

# Setup
- Clone the repo
- Install the dependencies using Maven.
- Set the following environment variables:
  - `FREIGHT_STATS_HOSTNAME`
  - `FREIGHT_STATS_SCHEMA`
  - `FREIGHT_STATS_USERNAME`
  - `FREIGHT_STATS_PASSWORD`
- Run FreightStatsApplication.Main.

I don't usually like using IDEs, but IntelliJ has built-in Maven support. Using it will make your life significantly
easier.

# Example
I'll give some examples so you can have an idea of everything from basic requests to advanced ones.

Requesting all shipments is as simple as:
```
.../api/shipment
```

To request all shipments with a tradeType of 2:
```
.../api/shipment?tradeType=2
```

To request all shipments within the date range \[6/1/2018 to 9/1/2018\]:
```
.../api/shipment?startDate=2018-06-01&endDate=2018-09-01
```

To order all shipments by weight and value (be sure to separate column names with commas), descending:
```
.../api/shiment?orderBy=weight,value&order=desc
```

Lastly, you can even combine all those examples into this horrid mess:
```
.../api/shipment?tradeType=2&orderBy=weight,value&startDate=2018-06-01&order=desc&endDate=2018-09-01
```
Order doesn't --- or at least, shouldn't --- matter. Feel free to mix and match parameter orders as you see fit.