
Inventory and Order Management System
=====================================


This is an inventory and order management system SDK which can be used by any businesses which take online orders and deliver to the customers. Based on the order input, it creates consignments from multiple warehouses based on optimized algorithm considering the distance and order quantity. It automatically performs consignment creation, stock level updation, sending notifications via email to the end user etc. It can be used as an independent application or it can be directly integrated to any e-commerce website.

## Description

This is an implementation of inventory and order management system. It automates the entire process of warehouse management, stock management, order management. It also has an algorithm which creates consignments for different warehouses based on stock availability and the distance of the warehouses from customer's address. The algorithm also provides a flexibility to the businesses to modify the strategy based on a certain threshold in the number of items in an order. This solution has been implemented using Java-Spring Boot with microservices architecture. It will be very useful for small scale to medium size businesses to manage the entire order flow. It can also be integrated with e-commerce systems.

## Microservices

* User Service - It captures the customer data, employee data, roles of the employees and departments.
* Order Service - It creates and maintains the orders & order items. It has APIs of creating orders, updating orders, cancelling orders, returning some item(s) of an order, replacing some item(s) of an order etc.
* Inventory Service - It maintains the entire inventory data. It has the warehouses data, location data, stock level data and logistics tie-up data. Order and Consignment services use this inventory service to get real time stock updates.
* Consignment Service - This service creates consignments against each order. An order can have one or more number of consignments based on the number of warehouses selected for the fulfillment. It also provides an API that can be used to push real time updates on the consignments.
* Product Service - This service has the whole products and categories data.
* Notification Service - It involves OTP validation, sending regular update emails to the customers.
* API Gateway Service - This is the service which acts as the API gateway to route the requests.
* Service Registry - Implemented using Eureka, which holds the services' data.


## Technical specifications

* Authentication - Spring Security, JWT Authentication using Spring webflux (Reactive)
* Cache - Redis Cache as a distributed caching mechanism
* Synchronous communication between microservices - Feign Client 
* Messaging platform - Apache Kafka
* API Requests routing - Spring cloud gateway & Eureka service Registry
* Logging framework - ELK logging (Elastic, LogStash, Kibana)
* Email - Email service for alert notifications and OTP implementation
* Database - MySQL (separate databases for each microservice)
* Jobs - Scheduled cronjobs for cleanup and time bound activities
* API framework - REST


## APIs covered:
* Signup : It creates an employee entry, sends a verification email with OTP to the employee's official email ID.
* Verify OTP : It validates the email ID against the input OTP and provides a response, makes a flag in user entry as true.
* Resend OTP : It makes any existing active OTP as expired and generates a new OTP & sends it.
* Login : Username and password will be the input, generates JWT token if the credentials are right AND user has been validated with the OTP.
* Create Order: It checks with stock levels of every warehouse, checks if at least one warehouse has enough stock to fulfill for each order item & creates a draft order if stocks are enough.
* Place Order: It checks with stock levels, runs an algorithm which assigns warehouses for each order item, deducts the respective quantity of each SKU from those warehouses. A background cronjob runs and creates consignments against these fulfillments for each warehouse.
* Cancel Order by Order Number: It cancels the order, their order items, consignments and consignment items.
* Return order items: It initiates return process for the order items specified. A background cronjob runs that creates new consignments for pickup.
* Replace order items: It initiates replacement process for the order items specified. It creates new order & order items for the replaced items, also a background cronjob runs which creates consignment for picking up the delivered items.
* Create Transaction: It creates transactions for consignment updates. Every consignment that gets a new update like PACKED, SHIPPED, DELIVERED etc needs to be fed into the system via this API. It links with consignment number, order number, previous status & present status. It updates all the relevant entities accordingly.
* Create, Update Stock Movement: It adds or deducts stock values from a particular warehouse. This API is majorly used to record any stock movement UP/DOWN for a particular warehouse.

    Data Setup APIs:
* Create, Update, Get logistics : 3 APIs for creating, updating and fetching logistics partner details respectively.
* Update Pincodes in bulk : Create or update pincode details in bulk
* Update states in bulk : Create or update state details in bulk
* Get state by pincode : Fetches the state, its 3 nearest warehouses by taking the customer's shipping pincode as input.
* Update stock in bulk : It updates stock levels in bulk.
* Create, Update, Get warehouse : 3 APIs for creating, updating and fetching warehouse details respectively.
* Create, Update, Get products : 3 APIs for creating, updating and fetching product details respectively.
* Create, Update, Get categories : 3 APIs for creating, updating and fetching category details respectively.
* Create, Update, Get customers : 3 APIs for creating, updating and fetching customer details respectively.
* Create, Update, Get employees : 3 APIs for creating, updating and fetching employee details respectively.
* Create, Update, Get roles : 3 APIs for creating, updating and fetching role details respectively.
* Create, Update, Get departments : 3 APIs for creating, updating and fetching department details respectively.

## API Documentation

[Click here to view the API Documentation](https://documenter.getpostman.com/view/18403833/2sA3dygWA1)

## Data modeling - Entity Relationship Diagram

* User Service - [User Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=User%20Service.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22H0oItFghLZOZoXgkCqya%22%3E7Vpdc5s6EP01fkwHG%2BOkj4a4N07T3kxpJ23fFFgbTYTkK4Q%2F8uu7AmFMcKZcA810xjOeBB3tLutd7dmFZGB78fYfSVbRJxECG4yscDuwrwej0eX7Mf7UwC4HxsNRDiwlDXNoWAI%2BfQYDWgZNaQhJRVAJwRRdVcFAcA6BqmBESrGpii0Eq951RZZQA%2FyAsDr6QEMV5eiVY5X4DdBlVNx5aJmdmBTCBkgiEorNAWTPBrYnhVD5Vbz1gOnYFXHJ9T68srt3TAJXTRTg%2ByL%2Brj7ebdgzvUl%2FfLyB29sLY2VNWGq%2B8GA0YWjPXQg0i16rnQnF5L9UFBsXSZaoKQqgC5hrt9zHq6X%2B%2FS0Bifs%2ByDUNoDCL%2FuWWcyETmv1NRlKkPATtsoXbm4gq8Fck0LsbPGCIRSpmuBrutdcgFWxfjcpwH2s8oyBiUHKHIkZhnx5zPovlpkz2uEhpdJDoicGIOV%2FLveUyBXhhsvA%2FMjKqZcRLE4XGZVILVrKhMSMcTFZ8s6MjF0SUhXdkJ1LtcKJI8FSs3EhI%2BozypIgjbktlas%2B2KhK%2B1jQ2Jei03xexHb6APpFtRfCOJKrwRjBGVgl9zPzTijGRS8pdofCrNUv2vn66ynw18UO7nnn78kjmR1d9pd5%2BtRhDuj5ai4xyuCicy6pR86ZztB4rtnJojqasXNFXkvLlQZUeiB3R%2FExiOFX3PhL8ZOVZTCg7VXkahng0k5MdpzzA%2Fnaq%2BjyZBoquS30XGxkQ3tiAJ4EoKvg1UaWRnxjMUCNfaQyNTenaxF5NFzRXPsXcS%2FQFOWFlqoxbpHgCTzAhEeciZyvK2AuIMLrkuGSw0Gq6tCm24amBYxqG2rKbIEFg1O8ysetxiXwxJaohgeoLlrFFhIqAFtyVoFxlJeu4%2BME68ax3zsBBXz1cD8s1frS4VJ7g6D6hGaEAhmwDmtJcKRRR5HFPtl20qdebQZ3BDGU1ZiynL8Ya1xhrFq%2BY2AGcm1WnzWpcG1Ma5t7prVs5f0%2B3Kg7l5zR%2BzIbSP97zviDTt9G%2FBjwrKsbT8mad1wd8xgqJ3LWyco9zrOCEterj%2Fy4WNKAdGcm%2BTruT0bqzv%2B1g8oA0FIk0aRmFW2ywKF8ZJ%2FJF0xNGGMlIz%2BiKVFNw4xID3bJBtipTY6MbwuhwXpsn93hAsPzZ7kHIJ%2FRizr9GNPFEvCJ8d%2FLB62IOPAI9yt8Ni%2BcR8g%2BNkONORkintxFyUhsjyl53HiL7fOMxbvz8YPWV%2FMu%2FZ4Zs01ZazwfzxF%2BBHlamQYBzwoxr5gjPLxLOXaBhF5h00gWGl30RwVWNCPQT05n%2F%2B%2BT%2FidOU%2F%2B2%2B0v7%2BzP9vwf%2FoTxhTfu4f5%2F7RsH9cdfMi%2BoRBEpfl38izvYN%2FNLBnvwA%3D%3C%2Fdiagram%3E%3C%2Fmxfile%3E)

* Order Service - [Order Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Order%20Service.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22fkR9zFDDgrfsMQm2Oscb%22%3E7VpRb9o6FP41SLsPnQIB2j1CSFdUSrMENu2%2BIDcxxKoTcx2nlP36HSdOIAU2mgQ26SKhEh%2F7fMc%2B37H9paKhG8HrZ46W%2FgPzMG20NO%2B1oQ8ardb1pzb8lYZ1amg3W6lhwYmXmpobg0N%2BYGXUlDUmHo4KAwVjVJBl0eiyMMSuKNgQ52xVHDZntBh1iRZ4x%2BC4iO5avxFP%2BKn1pqNt7HeYLPwsclNTPQHKBitD5COPrbZMutnQDc6YSJ%2BCVwNTmbssL6nf7YHefGIch%2BIYh7vvVs9cYmOwat4g8vnram7hK4XygmisFtxodSng9ecMYGHWYq1S0f0vZlnHVZQQ1YMBMAXgur%2Fph6eF%2FH7kHuYwwMH8hbg4w4UJptDpKJWbPEqLszj0sJyzBt0rnwjsLJEre1dQYWDzRUCh1cy9XzAX%2BPVgWpp5sqFIMQuw4GsYohxyflSBZs3Vhu12xqm%2FxXRX2ZAqsEWOvOEAHhQN76CktUNJksloJ1PRigQUhVhx4qgemTbXJ9QboTWL5WwjgdznrNX3GSc%2FYDzKkgjdXKidp2uFEY70VJgcS9KtLLHNN6YH9FoYOEKRyGbDKEXLiDwl85OOAeILEvaZECw4jul899RFe5H1pr5Lu369j3b9VLzrB7eiR1727kRKQnyVTS7Zi%2FLU7OzdjQWsg6YhoGspliM4CRdbu%2FbXntluH8fBU%2FJQCgXukNADtwmT5ZmBDFgsS%2BdYkAlUYknXAYlcOH9EWX%2FHJ8tlsmDtFuOyKBZao2R8tTwYcQTbKyGjPK99Qmm6oJ7nwc6OygJtpaY%2BJIuELiiOskhbGTIDRGgNOJbPwjrmM0ZBaRg4tUW8Sa8ZxgF8fUi2aNonTf%2B8H2%2FEFmXnBK4kEsSVMEZKWZUlbsNNOFxSaT2Ur3ODYyQIC%2BU2Q2Izr3%2BBTk9aJiQ4fusll19LAzVM5gR79YAOMCVw563rQbOI%2Bxwv68EapqyGURwki02x%2BqDTMQp%2FjXKc9Y3ygWtfJMKFs2dsMMo42EOWSiE4sN6YECWLEJoUz6Wb1A0EFH5PmQPieRK5H4H6gJoZJcMG7Y3FVve%2FNDFwn9NEivjgiAGhv2QkFIke6PThA5ewoX3sNDowVwPazU0bPnI4FzJbgiOSqBUM5bLCUi%2F1OZz4Aj3lSq4OAXxYZu7KI6WHjpZD2qnkUHu%2FDJabXODgooZrVcPtZlk5fDL%2BO%2BeWw9W1ryzMyhLY4syLXVHpMnPup2VdpyGRsS2evLKXVMGI4qgqyN8hxSu8S3yJUSiIWOf%2BI%2FYOHjLlXymHGUgthFxUZQlVCbc8iIwAJ3Vcy%2FlQhKyGVc9L%2Bze4ZnwWR7j62v5SEX6RoyeXo%2B1a5Gjn%2BlRypHtQjubnWHoi%2Fp9laR3%2Fl70pKtHrT7vU5%2BWwTX0uYWun%2FroGJaodp0QvWL%2FFGti928kRd0DFMIZt9ibm4PSBrJ5xf444zt3Qss4R6HE6md0%2B2rOBORp%2BNe3vp4%2BoIp1jcbY5mdrjmW1%2BmZrOZHa2KrFNa9QzzAdzPPkTwYuLfhzfDu2HP7bs84XPStkaGvdT6wxnwVCeBbNzhEo5PSOHZ4hk9MaGORr9LlT91sv7QO3vA933vg%2FsFYX6zftFITQ3P8pJ%2BrZ%2B2aSbPwE%3D%3C%2Fdiagram%3E%3C%2Fmxfile%3E)

* Inventory Service - [Inventory Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Inventory%20Service.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22YDoVRBxFZmCFP5W51xK6%22%3E7Vxbb%2BI6EP41SOc89CghQOkjhNBFmwIidKs9L5WbGGJtEnMcB9r99TvOhXA9DSShVI1UUTK2xxPP%2BPt8mbamqO7rPUML%2B4Fa2KnVJeu1pvRq9frtXQM%2BheAtEjTkeiSYM2JFIjkVGOQ3joVSLA2Ihf2tipxSh5PFttCknodNviVDjNHVdrUZdbZ7XaA53hMYJnL2pU%2FE4nYkbTelVP4Nk7md9CxLcYmLksqxwLeRRVcbIkWrKSqjlEff3FcVO2LsknGJ2vWPlK4NY9jjWRrYk%2B7Ppxmf%2FJCpTHod9eXGHdzEWpbICeIXrtVbDujrziioBav5WzwUrf8CmhTc%2BKGjOlABTABfd9Ny%2BDYXvwfeEiyj7A0qGZgtiYkT3WBkpD6qGY%2FPuqc6o4FnYWG3BMUrm3BsLJApSlcQZSCzuevAk7xuvcSM49ejQyOvBxwCFVMX89CuuMHaR3GQJo%2Br1OONxK%2F2hrdbsQzFQTZfa079AF9iV5zglvqeW3Q6Jz4npr83WP6KuA7ycOwaIy4RI2faxLF09EYDYbDPkfkreeralJHfUB8l4wjFjMcTUJG2ahiiZayTYeH7cTK28o7oAb1uVdSRzxNrqOOghU9eQvtEQxexOfG6lHPqZnP2ehIV5fltx8vKvueV2wOer7fLcr1ydEZaZHlwQjrEwzeJceGUFODZPDgpt3QdFQ1AuxTpMjgj3nxj4v5%2FyyFy8bltVeAOaJVHxZiSELXoDD5UEbsmL0fl2KZeaG3gvmBWsHLNRcQ5V%2BcAIELqmJws03fuAmVi5GV3BcOIEwoNpB7iqZ5%2F4aUtIZkSF2fWFmJAXYK1AZkRgPXzlWaT7gAkoAMP8Y3RX1ilDmUg92iEmMRxdkTIIXMPHh08E80EvBBYD3RisUssS2ju%2BgBS4Bc9rNZrpJJJDBNCRKH5zAkRy4aGGDR0F8LpIWw0u%2FADc1WV%2FmnWmmCrCs9y%2Bgw%2FojrjEBpgPiIhqGEYzhUWsNpllCOOXtaAXwRVHiekfRSNYTMzajbLQs3GHmqOiWdSsXSs%2BLJAvmxcH2E2r4AwC%2BXQOHJzk4sOEM4D62zm06k3z6WgBytWRkJGO6s9TKOQJs4fypALxeYjZJ2vyIUVQ16IIRtXzpCtPZgMp1fFj2XuJxuZ%2Fa6U5ffbS9Pjx2we%2B4SFwPoErrJp4OenTwOb1LMKVTm1CStW42chp4pwCiecVjGEI5UFPO0DhEPNXxXhlLohuwLGufsajFMg03x%2FPH%2BLBFOqJs4OAy%2FdZ%2FVoIGK0IpGKRN4hkfaVk0hyDbzLImGALbGLxYhXhFLeDqbVzhgDDbm0GDh%2BSX11hFI0KaBwB5KE%2FHmbDoY8H5nFAbxK3YWDi9M3wSYm0QldVwSiBurpG84%2FhAlCQMvp2yI1U%2FMCF379tYskaT1R4%2B8TXgD54WC8q35d87QOBE0AS8VKcp4Kp4kEUgE3rpvqetgBP7LQtlJ05nrxQfTGnh%2B4OU6DxaFZ4GfxdVTzHSdXS5zSlziydJjvTlzjKKVdYCUZcRv8tqaRanFT6m458%2BqmWd7q5uIJP599u1xEgsuYeCn%2FnWVFx7Ig5P1zm1eXs93RbEZMgsQMKSabakNhkkGV4xb%2Bk6WSra8rxiO1IB3V1US15DpvyVVMulijvCXXfr7Ye7u1r70QK2Dt1d5eesmyvB8G69DYDAP5trQwKCJ3TMq29sqpqz%2FRjG%2FPxnSkfs%2BAmTk7C7t5nk46Q6OvTcrvr6fpgx%2Fa5Odz%2F1HvD3T9QRtOy%2B91ok0fJ8PL9vnUMaade61iqI9mqJPTtQ5DU6M0aDqUr3XkjKmipoKpSZE%2B3v9F5G1dhpqK1DUYDqaDzlTrlQ%2FFg%2BHzeDK6B141yu9MHT2Mde0i76V2hqqm6%2B91VVHPR1HPyYlbh6GnvBPJI5lbtWM3ZRX%2F5D2WvtshoHrWvdFdaUFQRBbXZTjjcXyBHcroaVgh50cj58nZSgfnzBl%2FtQ%2BP6T9qCMs2%2FtuFov0B%3C%2Fdiagram%3E%3C%2Fmxfile%3E)

* Consignment Service - [Consignment Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Consignment%20Service.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22aTM2vdfj-_rym8DIIym2%22%3E7Vttc6o4FP41zux%2B6A6IaO9HpfSuU1pdsXun94uTSpRMgbghaL2%2Ffk8gCFS81xdwuq4zzmhOzjk5b0meQGxohv%2F%2BlaGF%2B0gd7DWaivPe0O4azaaq397Cl6CsE8pts5UQ5ow4kikj2OQHlkRFUiPi4LDAyCn1OFkUiVMaBHjKCzTEGF0V2WbUK466QHO8RbCnyNumfiMOd6UXupLR%2F8Rk7qYjq4rs8VHKLAmhixy6ypE0s6EZjFKe%2FPLfDeyJ4KVxSeTud%2FRuDGM44PsItLVhs22vtMn3TveFs%2FBt3IpupJYl8iLpcKPZ9kBfb0ZBLVjN1zIU7X8imnbchHGiusAAJkCye1k%2F%2FJqLb4MGIZkHPo712JgtyRSn2sHMZICEV0ZoM1aT0ShwsLBcge6VSzi2F2gqeldQaEBzue9BS91ILzHj%2BH1ncNRNyKFWMfUxZ2tgkQKbLMkyTZurLOetNLNuLt9tSUOyzOYbzVkm4IdMxgGJaW4lJhfPcCte4Yr4HgqwzI8te0Twpi7xHAutaSRsDjmavqWtnksZ%2BQH8KA0ldDMuZ6GmFDhsISl1MiwKYJiGV%2F1AekTvBUYLhTy1hnoeWoTkNbZPCPqIzUnQo5xTf798b2ZSVckv5l7VtpOvdUqTX1f29Z3T0iHL0lnpkQDfpMbF81KsoHrpzCzoSkh9UKUkgjZnJJjnJmqOrUSyOM2fIv8Vs2N1DZgTC%2Fc59sNTlZxmStGtkwz6BoXo0ijEJxsFU5BHmR1mEPnw9VvO1oRDdPx%2BuFaLzo%2B1rEc8L%2BZXuo4Dk%2F7oYNkuWSyq1jQkgQHI5OhaiEJYm5KiQv7RaoYuDY4WNn1EvGOFIbEk5GQqYikDcYojeXVjBrtCEuUT1hCGESc0AMk7xDO7vkPAHEEZEx%2Fvb57YbZoKYFEyI4AhKlF6H3kzqHG5HhjUX3i4aHKirsJBxutFprBkrue4Beuhk57Cbi6itMTpIndY%2Fn45REp6Zb8S%2FQBmYCfnMRZh9A0b1KMM6AFN0A14%2FIGEPAgIND08E2ICChAA8F1J9onjCM29EAAFeGXFbHetjDKSW7ogQTjYzIvRhQuCGDT0FpQIzAW26T34wL5qKH%2FoDR1sNaCtZm34CHbGRZo4QyQGIBgKcoUFBOoxCostet2AsyqQ7W78uI14JMTZF%2BHonboQTvtn%2BDbbca8wtzqY22odiXO127qqoHNunFsj9BUVWykCrhjDVmPYaVo%2BOxQ%2B4wb8H8A89sPzsen5K0IBJ3y9kbfoGfHEFWWcCWW0K0EZml7X%2FqL%2B9DEadJRO%2BGSq%2F5%2BhRwVoQ1eLaONLe7saNhWSr4YvtRWDtlUMd6bV%2F9scvVwXhxoWB%2FXgM0hpPWi11UNrqx6GfePheXithvNXw76rQ33VUH4ijfyFRGkbxHndHmrYHvTWvgVQ2ys39UsFp1Fl52n0aTCxx93xs70H4D1xqFQXLCdBqbI4vzdhkmChLKDMFzVY9h53ZHbH5l3O6kTt3uj9Mzkz7BoPl%2BKLNfjat8d9w56IbevyvJLg7FIcS5I0EfDiEtwZPI8n94PRJIPQl%2BRVCgQvwSdj8Di0zAtawkfjfteyXiZG98kwLetSHLswd55tc3RpKQKn%2Bk%2BmbR%2Ft1%2FVgWf3B8uCHkOXnitpOlun1sty5YsxQEKKpOFleX3LWeZev09lOf%2Fkz6Nru8jV337E930vOEtLnuvJ3mpYhw0tC46cz9bwY%2FOw3qCAAobxoXYv%2FuQWrUeWNpyruHtb90vYU6nW%2Fr%2F5mk1K%2BARz40rF5xNUmaGb%2F14j7cv960cx%2FAQ%3D%3D%3C%2Fdiagram%3E%3C%2Fmxfile%3E)

* Product Service - [Product Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Product%20Service.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22hpgmYD3lzxTVLDo37Bfv%22%3E7VhZc5swEP41fnSHw3bSx4BzTZPUU6dN2zcF1qCJkKiQr%2Fz6rkCA8dEQ4vQpM4yNPu0uyx7fyu65frK6lCSNb0UIrOdY4arnjnuOc%2FJ5gJ8aWBfAwHYKIJI0LCC7Bqb0GQxoGXROQ8gagkoIpmjaBAPBOQSqgREpxbIpNhOs%2BdSURLADTAPCdtEHGqq4QE%2BHVo1fAY3i8sm2ZXYSUgobIItJKJYbkHvec30phCrukpUPTMeujEuhd3Fgt3JMAldtFC6zH3fe%2FeRqlMyc%2FtdfcPHz6aFvrCwIm5sXnkgRzgOVGafVuoxEtqQJIxxX3kxwNTU7Fq6DmLLwhqzFXHuSKRI8lSsvFpI%2BozxhuGUjgNtSmUS7VkNiqjWNTQkZykzK17O3oFuyagjekEyV3gjGSJrRx9w%2FrZgQGVHuCaVEYoSWMVUwTUmgZZZYutoRlVROlsnSCxMjkApWB4NvVynFVgCRgJJrFDEKZRGYLrBds17WNeWeGCzeqCfn1IDE1HFUma5TjTcm26%2FIvLOT%2BZ4zYjpjIV00Mj%2F6M9c16jHKoV86d4YieZMO8wCVMngXme%2Fa1kHoGq1bha2pkpRHpQy%2B0b8170gCXXWnX753VZ1IGtTPHYu5rrG2ytfYU9ZZoOiiNuEhlwHhrW2MIQskTRUVvOs7%2BERBJPLi7B5%2FXwIxTozRXmXmt%2BAQauSeJu0jkzevY%2BHwoDMK4RuMtkO3mA3bWuXEJMUT%2BIIJiTgXBdVRxrYgwmjEcclgptU0L1AcGGcGTmgYastehuyCMb3JxcaDGvlm%2BltDAtVnLKeaGBUBLXipoFzl%2FT708MIm861Pw94QffVxbddrvLS4VL7g6D6hORsBhnMJmg89KRRR5LFi6hdorxXTHR4ku%2FRn%2BK413Q3fi%2B7cg3Snh9levtMb%2FSwfVDnbWelqL9eZiambCOQiJ4iq7ArjB%2BpOijkPITxial4YQltTyNnNysDak5XReyVlsJMUQ04UPs4fRz1%2FDOytA8hJ24603iv5w%2F99AMFp1%2Fmo0UVxc1Z30d88L3Q6LGwP6eNP6LeO549BfPRBPDjKIK744RVtj8v612y%2Bt%2FGXgHv%2BFw%3D%3D%3C%2Fdiagram%3E%3C%2Fmxfile%3E)

* Notification Service - [Notification Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Notification%20Service.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22NUZaX0TjFb9taekaHMm7%22%3E7Vltc%2BI2EP41zLQfcmNsIMlHbJQLUwLUNu1dvzCKLbB6skVlEeB%2B%2Fa1s2eAAPcLbTXuZYYL17Kt3V7tSqFlOvPwo8Cx64iFhNdMIlzWrUzPN2%2FsG%2FFXAKgcadTMHpoKGOVRfAx79SjRoaHROQ5JWGCXnTNJZFQx4kpBAVjAsBF9U2SacVa3O8JRsAV6A2Tb6Jw1llKN3TWONPxI6jQrLdUNTYlwwayCNcMgXG5CFapYjOJf5U7x0CFOxK%2BKSyz3soZaOCZLIQwRQ%2B77hfzLEJ79dR5Yb3j36jzdaywtmc%2F3CNbPFQJ894aAWvJYrHYrWP3NeEG7SLFFtYAAXINf2mg5PU%2FXd55JOaIAl5QnweUS80IAU6sHP3ELOrENUGjMFnychUa4bQF5EVBJvhgNFXUChARbJmMGqXkq%2FECHJcm906mXMoVYJj4kUK2DRAmWadJ0Wy8U66Y0itdFGwlsaw7rOpqXmdSrgQWfjDZkxtzKDYkyVWI9Pt8KVLmjMcEJ0fjxNUbELIsrCHl7xuXI5lTj4UqzsiAv6FfhxEUkgC6l3oWVUODwlqXUKogpgWES3%2Fgp6wssKYw%2BnsvCGM4ZnKX3O%2FFOCMRZTmthcSh4flu5yJ50r99XU163t3Fu3O3JvmZdKvrV3W4b0ZeeuZDQhN4Vz2b5UHbS5c2dWdOVQF1QZuaAnBU2mG%2Ft0g22HZFGVJwn7qxkpdaBkHsPXLxlRURTw68E6nXkKlUQEqDj%2BrTaU9HFMjlbDk5ROk5hkvbQ%2Fj58zlUfpGogw9%2BckLb7ASYoD3ZSPD5A3f%2F5bzdsjxW0erk4tGpcEdEbz0HawVEny6Uau%2FuIJCRUhR%2F9N%2B2v0VXeF1iKz5ij4F%2BJwxgXgCc%2FbLWXsFYQZ5ByWjEyUmOpNMAdZW8MxDUOl2U6hw8GL9zK2TmONuLrHKIiD%2BIRl7S4CQQIa7Bmnicx6TtOGD2x0x%2FjQrDXBVwfW9fUaPopdSFWJUmCadUQC%2FXhBVE%2B2BZdY4udyWpxj0u6fZ9stWPfcQ1uueX%2BpltvYarkDf%2Fg%2BZ885ZxvHDtqyPM6e9eZ%2FZ9Bm5fgDZnQ3BZlRCmfxQoENFzCCk8OnoCDFLUD36Tc36A0oK3LTgJsmXC8yr86gFC1nNCvVY3W9T5CLT5DGeSaIcale0tp7Y9PH2%2Fxg%2BzNPlHMMkfvqELHuthNfFsN1bmu3ZxgixmFD5H%2Bq6w%2Fkdh8%2Bj7MR992OfaIt77E7HKLO5Q0NRv74YeCOO6jXhRf8fHmL2tI1X27YdX4bXSFryg7qjK9hauB2kDt2Bv2HrvvU9ruD%2FtVstvsO6vWuZNNF%2Fsjtj130%2Bwh5%2Fthtd71rVI6Lhr22g55Q37%2B67bxaS7M63t%2BzfAr6fg48%2Bzmw9dZz4M7jwDH%2FSYDl%2BveajLbxo5eFvgE%3D%3C%2Fdiagram%3E%3C%2Fmxfile%3E)

* API Gateway Service [API Gateway Service ER Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=API%20Gateway.drawio#R%3Cmxfile%3E%3Cdiagram%20name%3D%22Page-1%22%20id%3D%22SqPOIW5PzVN-hXpgZnab%22%3E7VfbbqMwEP2aPHbFJUnbx0Ky3UhpVTXay9vKBQe8NR7WdkLSr98xGAih0abqJS%2BVIoLPzHiGOcfYDPww21xLkqc3EFM%2B8Jx4M%2FAnA887vxzi1QDbChi6XgUkksUV5LbAgj1RCzoWXbGYqo6jBuCa5V0wAiFopDsYkRKKrtsSeDdrThLaAxYR4X30J4t1WqEXI6fFv1GWpHVm17GWjNTOFlApiaHYgfzpwA8lgK7usk1Iueld3Zcq7usBa1OYpEIfEzChv3L%2Fz2TkFvpiBqn6fb8Kzuwsa8JX9oEH3pjjfMEScFqsWm9tK8Z%2FV1AbzlRJ1BU6YAnIddDa8S4x%2F1d3MzRfE00LssW7BZVrFtF6fiy0SlF52x412TwJKxFTU7uD5iJlmi5yEhlrgUpDLNUZx5HbRK%2Bp1HRzsD1u03QUK4WMamnqqgMuLU9WqDXDRcv6sOY23WF8bDFihZY0M7dc4I2l4wXUeD1qvisqVa9RqmAZJ4JaahbWYroWpYzHc7KFlSlWaRI91qMgBcme0J%2FUPUSz1HYB%2Bk7HY2Ei7ZySGu7v6r66e9AN2XQc50TpuhrgnOSKPZT1mcCMyISJALSG7Diim0X0Rqx7513W3Wdobyjepd1%2FN979Hu8zvDjValtoyUTS0wA2QJcUSnikIXCQiAuoRME434MIZ4nAIadLE2Y6yPCVd2XhjMWxmTlQyAOmm5duk2GL3NtGGAgwfMlLUlIMpDhDkAMTumzMKMAftip0vowGI6w1xLHbjvFn3KUOQWD5hJW8UVRNQY1yAgmaaPLQaPot3gSH11tfKFYY%2FrG6eC9ZDHuyuCUZ%2FRTGqYUxPrUwRj1hTDPCeE8Z1bYbs3W75VbQg9xHsI5n%2FHagO6JUATI%2BkOS%2F8a9IbTZB8Zzy3z%2F1DDdf5weukRgPNe2zB3gepUR8RAVTYQR%2FktRXK20OBJpR1aSfM2UOcCQza60MwI8Agb2pnbeN9WXJXlNnFOHJ0RxdbwEb40w3OZMnatleKXOIHk9TSYgdwJccI1ydoDGfe9IH70mXR%2B5Jzen3BZsSDttv1tK28%2BHvT%2F8B%3C%2Fdiagram%3E%3C%2Fmxfile%3E)


## Usage Instructions

    1. Download the given postman collection - Inventory & Order Management System.postman_collection.json and import it in the postman.

    2. Clone this repository.

    3. Pre-requisites to run this project: Java 17, Eclipse IDE, Postman, MySQL workbench, ElasticSearch, LogStash, Kibana, Apache Kafka, Filebeats, Ubuntu, Redis server

    4. Windows subsystem for Linux should be enabled.

    5. MySQL server, ElasticSearch server, LogStash server, Kibana server, Apache Kafka server, Zookeeper server, Ubuntu WSL2, Redis server should be up and running.

    6. Import this project to Eclipse IDE & update the project in order to download all maven dependencies mentioned in pom.xml of all the microservices.

    7. In Eclipse, go to help->Install new software-> Put the following URL - https://projectlombok.org/p2 and download lombok. This is required to auto-generate getters and setters.

    8. Configure the MySQL database and create a databases as mentioned in application.properties of all the microservices.

    9. If pom.xml gives an error on the external resources link, you will have to download them in order to mitigate it.

    10. Start Service Registry server first, then start all other servers.

    11. Open service registry's dashboard & make sure all the services are running fine.

    12. Run the APIs in the postman.

## Order Management
Order management is one of the major applications of this implementation. It allows users to create an order, update an order, cancel an order, return order items and replace order items. When the order is created, it runs with an algorithm that effectively selects the warehouses for the fulfillment and creates consignments accordingly. One consignment is linked to one warehouse and one order. It can have multiple consignment items. If there is one order which needs to be fulfilled by 2 warehouses, with one warehouse fulfilling 4 order items and another one fulfilling 3 order items, then one consignment linked to 4 consignment items will be created for that one warehouse and another consignment for the rest of the 3 consignment items will be created. Each consignment item will be linked to one order item. Stock level will be updated accordingly in the respective warehouses. By using the create transaction API, each consignment's regular update can be pushed like PACKED, SHIPPED, OUT FOR DELIVERY etc.This update will reflect in the respective Consignment, Consignment Items, Order and Order items status. It also sends an alert email to the customer with each of these updates for each consignment. It also allows users to return or replace items. An order item can be returned, but a consignment item cannot be. Whenever return or replace APIs are hit, new consignments will be created. A consignment/consignment item can be for delivery or pickup, not both at the same time.

## Inventory Management
Inventory management is a major factor in order to keep the stock levels of each product in each warehouse updated so that, the business doesn't take orders even if there is no stock. Before placing an order, there is create order API which can be used during checkout or before making the payment where a draft order will be created. At this stage, it makes a call to inventory system to check if the stock levels in every warehouse is in such a way that every order item can be fulfilled. If not, order won't be placed and those order items with insufficient stock will be returned. When an order will be placed, again stock levels will be checked. An algorithm determines the warehouse to fulfill for each order item & stocks will be deducted from those warehouses for those products accordingly. A stock movement entry will be recorded for each order fulfillment (Stock DOWN), order item return (Stock UP). Employees can manually create stock movement entries for restock, stock transfers, stock wastage and for many other purposes. These stock movement entries will be running in the background which updates stock levels accordingly. In this way, inventory management has been implemented efficiently.

## Algorithm that is implemented to select warehouses for order fulfillment
This is the major factor of this management system which automates the fulfillment process and eliminates human intervention. This algorithm lets the businesses decide between cost effectiveness and faster delivery. As per the research, the following points have been noted down.

* Lesser the distance between the warehouse and delivery address, less will be the cost of shipping and can enable faster delivery.
* If the number of order items is quite high, then fulfilling the entire order from one warehouse which could be not the nearby warehouse seems to be cost effective than creating multiple consignments, but this cannot do faster delivery.
* If the number of order items is less, preferring nearby warehouses is better than a single consignment with a warehouse which is very far from delivery address.
* Most of the small to medium scale businesses will have 1 to 10 warehouses in total. 

Based on this research, an algorithm has been developed that is explained below.
* This algorithm lets the business decide whether they want cost effectiveness or faster delivery or a mix of both.
* A threshold count is set on the number of order items.
* This algorithm implements cost effective methodology if the number of order items of an order is more than that threshold count.
* This algorithm implements quick delivery methodology if the number of order items of an order is less than that threshold count.
* In this way, businesses can decide based on their preferences.
    1. If the business wants the algorithm to be entirely cost effective, then set the threshold count to 0.
    2. If the business wants the algorithm to be entirely optimized for faster delivery, then set the threshold count to a very large number (say 1 lakh) so that every order's order items count will always be less than that number.
    3. If the business wants to implement cost effective model for orders with a higher number of order items and implement faster delivery model for orders with lower number of order items, then set the threshold count accordingly. So, it behaves based on that.
* When an order is received, it first checks if at least one warehouse can fulfill the stocks for each order item. It doesn't matter if one warehouse can fulfill only one order item & likewise, it leads to 5 warehouses. Ultimately, it should check all the warehouses & their stocks such that we have enough stock for each order item somewhere. If one or more order item doesn't have enough stock in any of the warehouses, then order placement will be rejected.
* After confirming that every order item has enough stock somewhere in different/same warehouses, the algorithm begins. 
* Every state will have an entry in State table which will have top 3 nearest warehouse numbers. FirstWarehouseNumber, SecondWarehouseNumber,ThirdWarehouseNumber - like this 3 different columns are there in state table. Any entry in the state table captures FirstWarehouseNumber to be the nearest warehouse for that state (it could be a warehouse situated in that state itself), second being the 2nd nearest, third being the 3rd nearest.
* State data will be fetched by inputting user's pincode. So, it gets to know which is the nearest wareshouse, 2nd nearest & 3rd nearest for the customer's shipping pincode.
* It first checks if the number of order items is less than the threshold count or more.
* Case 1: Order items are less than the threshold count: This is optimized for faster delivery. It checks if the first nearest warehouse has enough stock for each order item. If yes, it creates a single consignment linking with all the order items and that nearest warehouse. If not, then it considers only those order items which can be fulfilled from that nearest warehouse and creates one consignment for that. Then, it checks for 2nd nearest, checks if all the remaining items can be fulfilled from 2nd one. If yes, creates one consignment for all the remaining items. Or else, it creates consignment for those which can be fulfilled from 2nd. Similarly for 3rd. If still few order items are remaining to be fulfilled, then it looks for all the remaining warehouses together and creates consignments accordingly.
* Case 2: Order items are more than the threshold count: This is optimized for cost effectiveness. It first checks if first nearest warehouse can fulfill all the order items or not. If yes, creates a single consignment. If not, it doesn't create any consignment with first nearest warehouse, looks for 2nd nearest warehouse. If yes, creates a single consignment for 2nd nearest warehouse. If not, it looks for third warehouse. If yes, creates a single consignment with 3rd nearest warehouse. If not, then it looks for any other available warehouse that can fulfill all the order items. If yes, then it creates a single consignment with that wareshouse. If not, then it switches to faster delivery algorithm itself such that it creates multiple consignments with part of order items with first nearest, second nearest, third nearest, any remaining warehouse etc. 
* If the business wants only case 2 for all the orders, then set the threshold count to 0.
* If the business wants only case 1 for all the orders, then set the threshold count to some very large number (say 1 lakh).

## Functionality

* Signup - Employee user : 

        1. User hits SignUp API, which takes user's data input and creates a user entry in API gateway service. 
        2. While entering the password, it encrypts the password using BCryptEncoder. 
        3. Then it makes a call to User service where Employee's entry will be created.
        4. Then, it makes a call to Notification service which generates an OTP entry & embeds the OTP in the email & triggers the email. 
        5. A response will be sent to the user.


* Verify OTP :

        1. User puts the email ID and the received OTP.
        2. A call will be made to Notification Service which validates this OTP by checking the entry created against this OTP.
        3. It checks if email ID is correct, it is not yet expired & this OTP is not consumed already.
        4. If it is correct, then the user's is Validated flag will be marked as true.

* Resend OTP :

        1. It takes email ID as the input.
        2. It makes a call to Notification service which makes the existing OTP expire and generates a new OTP.
        3. A new email will be sent with this new OTP.

* Login :

        1. User puts the email ID and password.
        2. Authentication mechanism checks if an entry exists in the database for this email ID AND is already OTP validated, then fetches the entry.
        3. User given password will be encoded by BCryptEncoder and matched against the encoded password saved in the database.
        4. If it matches, a JWT token will be generated with a fixed expiry time.
        5. Same token will be returned as response. This token needs to be entered as Bearer Token in every other API from now on in order to authenticate itself.
        6. JWT authentication is applicable for all the endpoints belonging to all the microservices except Signup, VerifyOTP, ResendOTP and Login.

* Create Stock Movement:

        1. Employees can hit this API when they want to add any stock movements like transfer from one warehouse to another, restock, disposing wastages etc. Do not hit this API to mention stock IN and OUT for order fulfillment or pickups. Because, other APIs will hit this API internally & make sure that stocks are updated.
        2. Every stock movement entry will be linked to that warehouse. Also, it takes the list of SKU and its count in a field in the format SKU1_Count1;SKU2_Count2;.....
        3. A cronjob runs once in 5 minutes which consumes these stock movement entries and updates the stock entries accordingly.
        
* Create Order:

        1. It involves 2 APIs. CreateOrder API which creates a draft order if the stock levels in the warehouses are enough such that every order item can be fulfilled even with different warehouses. If not, it returns back with the list of SKUs whose stock is less for the given quantity.
        2. After this API, OrderPlaced API comes into picture. It needs just the order number. It makes the order & order items status as CREATED. Runs the algorithm to select the warehouse for each order item and updates it in order items. Also deducts the stock from the stock level of those respective warehouses by making an internal call.
        3. It uses Apache Kafka and pushes a message to notification service which sends an order confirmation email.
        4. A cronjob runs once in 5 minutes which actively looks for these type of orders & creates consignments accordingly. If a single warehouse is fulfilling all the order items, then a single consignment will be created. In this way, consignments, consignment items, stock movement entries for each consignment, transaction entry for each consignment.
        
* Cancel the order

        1. This API marks the order, order items, consignment and consignment items status to CANCELLED. Creates a transaction accordingly. Updates the stock movement entry accordingly.
        2. It also sends a kafka message to notification service, which triggers an email of order cancellation.
        
* Create transaction (for consignment updates)

        1. Each transaction is linked to a consignment and an order. Whenever a consignment will be packed, picked by logistics, out for delivery, delivered etc., a fresh entry needs to be created in this table with present and previous status of the consignment.
        2. It creates an entry, also updates the consignment and its items with the latest status.
        3. It also sends a kafka message to order service, which consumes it, updates the order and order items status accordingly. Also, for some specific updates like SHIPPED, OUT_FOR_DELIVERY etc., order service sends a kafka message to notification service to trigger emails accordingly.
        
* Return an item:

        1. When user hits this API, that order item's status will be updated as RETURN_REQUEST_INITIATED. An email will be triggered.
        2. A cronjob runs once in 30 minutes which consumes all such return and replacement requests and creates consignments for pickup. The reason of waiting for 30 minutes is that, user usually raises return or replacement requests one after the other for each order item. So, if a time window is given, then it consumes it and creates consignments by clubbing order items if there are multiple order items which were fulfilled from same warehouse. This is a cost cutting optimization. It also creates transaction entry, stock movement entry etc.
        3. If an order item is returned, then it should go back to the same warehouse from which it was fulfilled.
        
* Replace an item:

        1. When user hits this API, that order item's status will be updated as REPLCEMENT_REQUEST_INITIATED. An email will be triggered.
        2. A new order will be created with this order item for the replacement. New order, new consignment, new transaction etc. And this order item need not be necessarily fulfilled from the same warehouse which had fulfilled the previous item. But, the pickup that happens as part of this replacement should go to the warehouse from which it was sent.
        2. A cronjob runs once in 30 minutes which consumes all such return and replacement requests and creates consignments for pickup (Consignment for delivery fulfillment of replaced item was created earlier only. This is purely for the pickup). The reason of waiting for 30 minutes is that, user usually raises return or replacement requests one after the other for each order item. So, if a time window is given, then it consumes it and creates consignments by clubbing order items if there are multiple order items which were fulfilled from same warehouse. This is a cost cutting optimization. It also creates transaction entry, stock movement entry etc.
        3. If an order item is returned, then it should go back to the same warehouse from which it was fulfilled.

* Scheduled jobs :

        1. consumeStockMovements : It runs once in every 15 minutes in inventory service to fetch all unconsumed stock movement entries. It reads the stock level data & updates the stock of respective SKUs accordingly by adding or subtracting the values.
        2. createDeliveryConsignments : It runs once in every 5 minutes in order service. It consumes all non consumed orders & creates consignments for each order. Order item will have the warehouse number data. So, based on that input, one or more consignments will be created for each order. Also, transaction entries, stock movement entries will be created accordingly. But, the stock movement entries which will be created here will not be consumed by the previous cronjob. Because, the stock level would have been updated during the order placement itself. This stock movement entry creation is only for the record purpose.
        3. createPickupConsignments : It runs once in every 30 minutes in order service. It consumes all non consumed order items which need to be returned or replaced & creates consignments for each order. Order item will have the warehouse number data. So, based on that input, one or more consignments will be created for each order. Also, transaction entries, stock movement entries will be created accordingly. But, the stock movement entries which will be created here will not be consumed by the previous cronjob. Because, the stock level would have been updated during the order placement itself. This stock movement entry creation is only for the record purpose.


* JWT Authentication :

        1. It is implemented using Spring Security and Spring Webflux in reactive way. Except signup, login & few initial APIs, all other APIs need to be authenticated. 
        2. Once authentication is successful, JWT token will be generated which will have a signature, expiry time, username and claims.

* Redis Cache :

        1. It is implemented as a distributed caching system.
        2. @Cacheable is used for all GET operations.
        3. @CachePut is used for all DML operations.
        4. @CacheEvict is used for all delete operations.
        5. A unique key will be used to store it.
        6. Redis CLI can be used to test the stored data.

## Highlights

1. This project includes 55 APIs in total, including 8 microservices.
2. API Gateway, Service Registry, JWT Authentication in gateway is implemented.
3. Cache implementation is done considering the consistency of data & speed. Not every method is cached. Decision is based on consistency of data.
4. This project is best suited for businesses having 1 to 10 warehouses in total.
5. ELK logging has been implemented.
6. Data modelling ER diagrams, pin code details for all the pincodes in India (zip file), Postman collection have been attached.


## Developer

* Nishal Beegamudre

</br></br><a  href="https://www.linkedin.com/in/nishal-beegamudre/" target="_blank"><img alt="LinkedIn" src="https://img.shields.io/badge/linkedin%20-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" /></a>
